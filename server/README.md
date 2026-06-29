# DroidPasswords Sync Server

A small, **zero-knowledge** sync backend for the DroidPasswords app. It stores only
opaque, end-to-end encrypted password blobs. The server never sees your master
password or any plaintext — items are encrypted on the device before upload and
decrypted only on the device after download.

## How sync works (incremental)

- Every account has a per-user, monotonically increasing `seq` counter.
- Each stored item keeps `{ id, seq, updatedAt, deleted, payload }` where `payload`
  is the AES-GCM ciphertext produced by the app.
- **Push**: the client uploads only its locally changed items. The server applies
  last-write-wins by `updatedAt` and assigns a new `seq` to anything it stores.
- **Pull**: the client asks for `changes?since=<lastSeq>` and receives only the items
  whose `seq` is greater than the last one it has seen.
- **Deletes** are propagated as tombstones (`deleted = true`).

This means each periodic sync only transfers the deltas, not the whole vault.

## API

| Method | Path             | Auth   | Body / Query                              |
|--------|------------------|--------|-------------------------------------------|
| GET    | `/health`        | no     | —                                         |
| POST   | `/auth/register` | no     | `{ "username", "password" }` → `{token}`  |
| POST   | `/auth/login`    | no     | `{ "username", "password" }` → `{token}`  |
| GET    | `/sync/changes`  | bearer | `?since=<seq>&limit=<n>`                   |
| POST   | `/sync/push`     | bearer | `{ "items": [{id, updatedAt, deleted, payload}] }` |

Authenticated requests must send `Authorization: Bearer <token>`.

## Running with Docker (recommended)

```bash
cd server
# Optional: bake your LAN IP / hostname into the cert so devices can validate it.
#   edit CERT_DOMAINS in docker-compose.yml, e.g. "localhost,127.0.0.1,192.168.1.50"
docker compose up --build
```

- HTTP:  `http://localhost:8080`
- HTTPS: `https://localhost:8443` (self-signed)

The SQLite database is persisted in `./data` and the TLS keystore in `./certs`.
On first start the server auto-generates a self-signed keystore if none is present.

## Self-signed certificate

A keystore is generated automatically on first run. To create one yourself with
specific Subject Alternative Names (needed so other devices accept it):

```bash
./gen-cert.sh certs/keystore.jks changeit droidpasswords "localhost,127.0.0.1,192.168.1.50"
```

Export the public certificate (to import/trust on a client if desired):

```bash
keytool -exportcert -rfc -alias droidpasswords \
  -keystore certs/keystore.jks -storepass changeit -file certs/server.pem
```

> The Android/desktop app is configured to trust the self-signed certificate of
> the server URL you enter in Settings, so importing the cert system-wide is
> optional for normal use.

## Running locally without Docker

```bash
cd server
./gen-cert.sh                 # or let the server auto-generate it
gradle run                    # uses application.conf (HTTP 8080, HTTPS 8443)
```

## Configuration (environment variables)

| Variable            | Default               | Description                           |
|---------------------|-----------------------|---------------------------------------|
| `PORT`              | `8080`                | HTTP port                             |
| `SSL_PORT`          | `8443`                | HTTPS port                            |
| `DB_PATH`           | `data/sync.db`        | SQLite database file                  |
| `KEYSTORE_PATH`     | `certs/keystore.jks`  | TLS keystore                          |
| `KEY_ALIAS`         | `droidpasswords`      | Key alias in the keystore             |
| `KEYSTORE_PASSWORD` | `changeit`            | Keystore password                     |
| `KEY_PASSWORD`      | `changeit`            | Private key password                  |
| `CERT_DOMAINS`      | `localhost`           | SANs for the auto-generated cert      |
