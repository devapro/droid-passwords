#!/usr/bin/env bash
#
# Generates a self-signed TLS keystore (JKS) for the sync server.
# The server also auto-generates one on first start if none exists, but this
# script lets you bake in extra Subject Alternative Names (e.g. your LAN IP)
# so phones/other devices can validate the certificate.
#
# Usage:
#   ./gen-cert.sh [output_keystore] [password] [alias] "dns1,dns2,ip1,..."
#
set -euo pipefail

OUT="${1:-certs/keystore.jks}"
PASS="${2:-changeit}"
ALIAS="${3:-droidpasswords}"
SANS="${4:-localhost,127.0.0.1}"

mkdir -p "$(dirname "$OUT")"

# Build the SAN extension string for keytool.
EXT="SAN="
IFS=',' read -ra PARTS <<< "$SANS"
for part in "${PARTS[@]}"; do
  part="$(echo -e "${part}" | tr -d '[:space:]')"
  if [[ "$part" =~ ^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    EXT+="IP:${part},"
  else
    EXT+="DNS:${part},"
  fi
done
EXT="${EXT%,}"

if [[ -f "$OUT" ]]; then
  echo "Keystore already exists at $OUT — remove it first to regenerate."
  exit 0
fi

keytool -genkeypair \
  -alias "$ALIAS" \
  -keyalg RSA \
  -keysize 2048 \
  -validity 3650 \
  -dname "CN=DroidPasswordsSync, OU=Dev, O=DroidPasswords, L=, ST=, C=US" \
  -ext "$EXT" \
  -keystore "$OUT" \
  -storepass "$PASS" \
  -keypass "$PASS" \
  -storetype JKS

echo "Created self-signed keystore: $OUT (alias=$ALIAS)"
echo "SANs: $SANS"
echo
echo "To extract the public certificate (to trust it on a client):"
echo "  keytool -exportcert -rfc -alias $ALIAS -keystore $OUT -storepass $PASS -file certs/server.pem"
