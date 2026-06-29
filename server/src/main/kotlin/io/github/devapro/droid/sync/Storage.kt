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
 * Vault id assigned to rows migrated from the pre-multi-vault schema, where items
 * were keyed only by (userId, itemId). Such rows are folded into a single legacy
 * vault namespace so existing data keeps syncing.
 */
private const val LEGACY_VAULT_ID = "legacy"

/**
 * SQLite-backed storage. Items are stored as opaque end-to-end encrypted payloads
 * keyed by (userId, vaultId, itemId) so a single account can hold several
 * independent vaults. Each write bumps a per-(userId, vaultId) monotonically
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
        // Must run before createIndexes(): on a pre-multi-vault DB the legacy `items`
        // table has no `vault_id` column, so the index below would fail until the table
        // has been rebuilt by the migration.
        migrateLegacySchema()
        createIndexes()
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
                    vault_id TEXT NOT NULL,
                    item_id TEXT NOT NULL,
                    seq INTEGER NOT NULL,
                    updated_at INTEGER NOT NULL,
                    deleted INTEGER NOT NULL,
                    payload TEXT,
                    PRIMARY KEY (user_id, vault_id, item_id)
                )
                """.trimIndent()
            )
            st.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS seq_counters (
                    user_id INTEGER NOT NULL,
                    vault_id TEXT NOT NULL,
                    value INTEGER NOT NULL,
                    PRIMARY KEY (user_id, vault_id)
                )
                """.trimIndent()
            )
            st.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS vaults (
                    user_id INTEGER NOT NULL,
                    vault_id TEXT NOT NULL,
                    updated_at INTEGER NOT NULL,
                    PRIMARY KEY (user_id, vault_id)
                )
                """.trimIndent()
            )
        }
    }

    private fun createIndexes() = synchronized(lock) {
        connection.createStatement().use { st ->
            st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_items_user_vault_seq ON items(user_id, vault_id, seq)")
        }
    }

    /**
     * Folds rows from the pre-multi-vault schema (a `items`/`seq_counters` shape
     * without a `vault_id` column) into the [LEGACY_VAULT_ID] namespace. Detection
     * is by column presence; the migration is a no-op once tables already carry it.
     */
    private fun migrateLegacySchema(): Unit = synchronized(lock) {
        if (hasColumn("items", "vault_id") && hasColumn("seq_counters", "vault_id")) return@synchronized
        connection.createStatement().use { st ->
            // SQLite cannot redefine a primary key in place, so rebuild each table.
            st.executeUpdate("ALTER TABLE items RENAME TO items_legacy")
            st.executeUpdate("ALTER TABLE seq_counters RENAME TO seq_counters_legacy")
        }
        createSchema()
        connection.createStatement().use { st ->
            st.executeUpdate(
                "INSERT INTO items(user_id, vault_id, item_id, seq, updated_at, deleted, payload) " +
                    "SELECT user_id, '$LEGACY_VAULT_ID', item_id, seq, updated_at, deleted, payload FROM items_legacy"
            )
            st.executeUpdate(
                "INSERT INTO seq_counters(user_id, vault_id, value) " +
                    "SELECT user_id, '$LEGACY_VAULT_ID', value FROM seq_counters_legacy"
            )
            st.executeUpdate(
                "INSERT INTO vaults(user_id, vault_id, updated_at) " +
                    "SELECT DISTINCT user_id, '$LEGACY_VAULT_ID', 0 FROM items_legacy"
            )
            st.executeUpdate("DROP TABLE items_legacy")
            st.executeUpdate("DROP TABLE seq_counters_legacy")
        }
    }

    private fun hasColumn(table: String, column: String): Boolean {
        connection.prepareStatement("PRAGMA table_info($table)").use { ps ->
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    if (rs.getString("name") == column) return true
                }
            }
        }
        return false
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

    // --- Vaults ------------------------------------------------------------

    fun listVaults(userId: Long): List<VaultSummary> = synchronized(lock) {
        connection.prepareStatement(
            "SELECT v.vault_id AS vault_id, v.updated_at AS updated_at, " +
                "COALESCE(c.value, 0) AS latest_seq " +
                "FROM vaults v LEFT JOIN seq_counters c " +
                "ON v.user_id = c.user_id AND v.vault_id = c.vault_id " +
                "WHERE v.user_id = ? ORDER BY v.vault_id ASC"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.executeQuery().use { rs ->
                val result = mutableListOf<VaultSummary>()
                while (rs.next()) {
                    result.add(
                        VaultSummary(
                            vaultId = rs.getString("vault_id"),
                            latestSeq = rs.getLong("latest_seq"),
                            updatedAt = rs.getLong("updated_at")
                        )
                    )
                }
                result
            }
        }
    }

    private fun touchVault(userId: Long, vaultId: String, updatedAt: Long) {
        // caller must hold lock
        connection.prepareStatement(
            "INSERT INTO vaults(user_id, vault_id, updated_at) VALUES(?, ?, ?) " +
                "ON CONFLICT(user_id, vault_id) DO UPDATE SET updated_at = MAX(vaults.updated_at, excluded.updated_at)"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.setString(2, vaultId)
            ps.setLong(3, updatedAt)
            ps.executeUpdate()
        }
    }

    // --- Sync items --------------------------------------------------------

    private fun nextSeq(userId: Long, vaultId: String): Long {
        // caller must hold lock
        val current = connection.prepareStatement(
            "SELECT value FROM seq_counters WHERE user_id = ? AND vault_id = ?"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.setString(2, vaultId)
            ps.executeQuery().use { rs -> if (rs.next()) rs.getLong("value") else 0L }
        }
        val next = current + 1
        connection.prepareStatement(
            "INSERT INTO seq_counters(user_id, vault_id, value) VALUES(?, ?, ?) " +
                "ON CONFLICT(user_id, vault_id) DO UPDATE SET value = excluded.value"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.setString(2, vaultId)
            ps.setLong(3, next)
            ps.executeUpdate()
        }
        return next
    }

    fun latestSeq(userId: Long, vaultId: String): Long = synchronized(lock) {
        connection.prepareStatement(
            "SELECT value FROM seq_counters WHERE user_id = ? AND vault_id = ?"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.setString(2, vaultId)
            ps.executeQuery().use { rs -> if (rs.next()) rs.getLong("value") else 0L }
        }
    }

    private fun findItem(userId: Long, vaultId: String, itemId: String): ChangeItem? {
        // caller must hold lock
        return connection.prepareStatement(
            "SELECT item_id, seq, updated_at, deleted, payload FROM items " +
                "WHERE user_id = ? AND vault_id = ? AND item_id = ?"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.setString(2, vaultId)
            ps.setString(3, itemId)
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
    fun applyPush(userId: Long, vaultId: String, item: PushItem): PushResultItem = synchronized(lock) {
        touchVault(userId, vaultId, item.updatedAt)
        val existing = findItem(userId, vaultId, item.id)
        if (existing != null && existing.updatedAt >= item.updatedAt) {
            // Server already has an equal or newer version; keep it.
            return PushResultItem(
                id = item.id,
                seq = existing.seq,
                stored = false,
                serverUpdatedAt = existing.updatedAt
            )
        }
        val seq = nextSeq(userId, vaultId)
        connection.prepareStatement(
            "INSERT INTO items(user_id, vault_id, item_id, seq, updated_at, deleted, payload) VALUES(?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(user_id, vault_id, item_id) DO UPDATE SET " +
                "seq = excluded.seq, updated_at = excluded.updated_at, deleted = excluded.deleted, payload = excluded.payload"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.setString(2, vaultId)
            ps.setString(3, item.id)
            ps.setLong(4, seq)
            ps.setLong(5, item.updatedAt)
            ps.setInt(6, if (item.deleted) 1 else 0)
            if (item.deleted) ps.setString(7, null) else ps.setString(7, item.payload)
            ps.executeUpdate()
        }
        PushResultItem(
            id = item.id,
            seq = seq,
            stored = true,
            serverUpdatedAt = item.updatedAt
        )
    }

    fun changesSince(userId: Long, vaultId: String, sinceSeq: Long, limit: Int): List<ChangeItem> = synchronized(lock) {
        connection.prepareStatement(
            "SELECT item_id, seq, updated_at, deleted, payload FROM items " +
                "WHERE user_id = ? AND vault_id = ? AND seq > ? ORDER BY seq ASC LIMIT ?"
        ).use { ps ->
            ps.setLong(1, userId)
            ps.setString(2, vaultId)
            ps.setLong(3, sinceSeq)
            ps.setInt(4, limit)
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
