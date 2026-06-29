package io.github.devapro.droid.sync

import java.sql.Connection
import java.sql.DriverManager

data class User(
    val id: Long,
    val username: String,
    val salt: String,
    val passwordHash: String
)

/**
 * SQLite-backed storage. Items are stored as opaque end-to-end encrypted
 * payloads keyed by (userId, itemId). Each write bumps a per-user monotonically
 * increasing [seq] so clients can pull only the changes newer than the last seq
 * they have already seen (incremental sync).
 *
 * Access is guarded by a single lock because we use one shared JDBC connection.
 */
class Storage(dbPath: String) {

    private val connection: Connection
    private val lock = Any()

    init {
        Class.forName("org.sqlite.JDBC")
        connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
        connection.autoCommit = true
        createSchema()
    }

    private fun createSchema() = synchronized(lock) {
        connection.createStatement().use { st ->
            st.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    salt TEXT NOT NULL,
                    password_hash TEXT NOT NULL,
                    created_at INTEGER NOT NULL
                )
                """.trimIndent()
            )
            st.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS tokens (
                    token TEXT PRIMARY KEY,
                    user_id INTEGER NOT NULL,
                    created_at INTEGER NOT NULL
                )
                """.trimIndent()
            )
            st.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS items (
                    user_id INTEGER NOT NULL,
                    item_id TEXT NOT NULL,
                    seq INTEGER NOT NULL,
                    updated_at INTEGER NOT NULL,
                    deleted INTEGER NOT NULL,
                    payload TEXT,
                    PRIMARY KEY (user_id, item_id)
                )
                """.trimIndent()
            )
            st.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS seq_counters (
                    user_id INTEGER PRIMARY KEY,
                    value INTEGER NOT NULL
                )
                """.trimIndent()
            )
            st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_items_user_seq ON items(user_id, seq)")
        }
    }

    // --- Users -------------------------------------------------------------

    fun createUser(username: String, salt: String, passwordHash: String): User? = synchronized(lock) {
        if (findUser(username) != null) return null
        connection.prepareStatement(
            "INSERT INTO users(username, salt, password_hash, created_at) VALUES(?, ?, ?, ?)"
        ).use { ps ->
            ps.setString(1, username)
            ps.setString(2, salt)
            ps.setString(3, passwordHash)
            ps.setLong(4, System.currentTimeMillis())
            ps.executeUpdate()
        }
        findUser(username)
    }

    fun findUser(username: String): User? = synchronized(lock) {
        connection.prepareStatement(
            "SELECT id, username, salt, password_hash FROM users WHERE username = ?"
        ).use { ps ->
            ps.setString(1, username)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    User(
                        id = rs.getLong("id"),
                        username = rs.getString("username"),
                        salt = rs.getString("salt"),
                        passwordHash = rs.getString("password_hash")
                    )
                } else {
                    null
                }
            }
        }
    }

    // --- Tokens ------------------------------------------------------------

    fun createToken(userId: Long, token: String) = synchronized(lock) {
        connection.prepareStatement(
            "INSERT INTO tokens(token, user_id, created_at) VALUES(?, ?, ?)"
        ).use { ps ->
            ps.setString(1, token)
            ps.setLong(2, userId)
            ps.setLong(3, System.currentTimeMillis())
            ps.executeUpdate()
        }
    }

    fun userIdForToken(token: String): Long? = synchronized(lock) {
        connection.prepareStatement(
            "SELECT user_id FROM tokens WHERE token = ?"
        ).use { ps ->
            ps.setString(1, token)
            ps.executeQuery().use { rs ->
                if (rs.next()) rs.getLong("user_id") else null
            }
        }
    }

    // --- Sync items --------------------------------------------------------

    private fun nextSeq(userId: Long): Long {
        // caller must hold lock
        val current = connection.prepareStatement(
            "SELECT value FROM seq_counters WHERE user_id = ?"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.executeQuery().use { rs -> if (rs.next()) rs.getLong("value") else 0L }
        }
        val next = current + 1
        connection.prepareStatement(
            "INSERT INTO seq_counters(user_id, value) VALUES(?, ?) " +
                "ON CONFLICT(user_id) DO UPDATE SET value = excluded.value"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.setLong(2, next)
            ps.executeUpdate()
        }
        return next
    }

    fun latestSeq(userId: Long): Long = synchronized(lock) {
        connection.prepareStatement(
            "SELECT value FROM seq_counters WHERE user_id = ?"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.executeQuery().use { rs -> if (rs.next()) rs.getLong("value") else 0L }
        }
    }

    private fun findItem(userId: Long, itemId: String): ChangeItem? {
        // caller must hold lock
        return connection.prepareStatement(
            "SELECT item_id, seq, updated_at, deleted, payload FROM items WHERE user_id = ? AND item_id = ?"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.setString(2, itemId)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    ChangeItem(
                        id = rs.getString("item_id"),
                        seq = rs.getLong("seq"),
                        updatedAt = rs.getLong("updated_at"),
                        deleted = rs.getInt("deleted") == 1,
                        payload = rs.getString("payload")
                    )
                } else {
                    null
                }
            }
        }
    }

    /**
     * Applies a single incoming item using last-write-wins by [PushItem.updatedAt].
     * Returns the resulting state so the client can reconcile if the server kept a
     * newer version.
     */
    fun applyPush(userId: Long, item: PushItem): PushResultItem = synchronized(lock) {
        val existing = findItem(userId, item.id)
        if (existing != null && existing.updatedAt >= item.updatedAt) {
            // Server already has an equal or newer version; keep it.
            return PushResultItem(
                id = item.id,
                seq = existing.seq,
                stored = false,
                serverUpdatedAt = existing.updatedAt
            )
        }
        val seq = nextSeq(userId)
        connection.prepareStatement(
            "INSERT INTO items(user_id, item_id, seq, updated_at, deleted, payload) VALUES(?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(user_id, item_id) DO UPDATE SET " +
                "seq = excluded.seq, updated_at = excluded.updated_at, deleted = excluded.deleted, payload = excluded.payload"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.setString(2, item.id)
            ps.setLong(3, seq)
            ps.setLong(4, item.updatedAt)
            ps.setInt(5, if (item.deleted) 1 else 0)
            if (item.deleted) ps.setString(6, null) else ps.setString(6, item.payload)
            ps.executeUpdate()
        }
        PushResultItem(
            id = item.id,
            seq = seq,
            stored = true,
            serverUpdatedAt = item.updatedAt
        )
    }

    fun changesSince(userId: Long, sinceSeq: Long, limit: Int): List<ChangeItem> = synchronized(lock) {
        connection.prepareStatement(
            "SELECT item_id, seq, updated_at, deleted, payload FROM items " +
                "WHERE user_id = ? AND seq > ? ORDER BY seq ASC LIMIT ?"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.setLong(2, sinceSeq)
            ps.setInt(3, limit)
            ps.executeQuery().use { rs ->
                val result = mutableListOf<ChangeItem>()
                while (rs.next()) {
                    result.add(
                        ChangeItem(
                            id = rs.getString("item_id"),
                            seq = rs.getLong("seq"),
                            updatedAt = rs.getLong("updated_at"),
                            deleted = rs.getInt("deleted") == 1,
                            payload = rs.getString("payload")
                        )
                    )
                }
                result
            }
        }
    }
}
