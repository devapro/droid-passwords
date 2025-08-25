package io.github.devapro.droid.data.vault

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES

class CryptoMapper {

    suspend fun encode(
        password: String,
        data: String
    ): ByteArray {

        val provider = CryptographyProvider.Default
        val aesGcm = provider.get(AES.GCM)

        val encodedKey: ByteArray = password.to32ByteArray()
        val decodedKey: AES.GCM.Key =
            aesGcm.keyDecoder().decodeFromByteArray(AES.Key.Format.RAW, encodedKey)

        val cipher = decodedKey.cipher()
        return cipher.encrypt(plaintext = data.encodeToByteArray())
    }

    suspend fun decode(
        password: String,
        data: ByteArray
    ): String {
        val provider = CryptographyProvider.Default
        val aesGcm = provider.get(AES.GCM)

        val encodedKey = password.to32ByteArray()
        val decodedKey = aesGcm.keyDecoder().decodeFromByteArray(AES.Key.Format.RAW, encodedKey)

        val cipher = decodedKey.cipher()
        return cipher.decrypt(ciphertext = data).decodeToString()
    }

    private fun String.to32ByteArray(): ByteArray {
        if (this.isEmpty()) {
            return ByteArray(32) // Return 32 zero bytes for an empty string
        }

        val sourceBytes = this.encodeToByteArray()
        val targetSize = 32

        return when {
            sourceBytes.size == targetSize -> {
                // If already 32 bytes, return as is
                sourceBytes
            }

            sourceBytes.size > targetSize -> {
                // If longer, truncate
                sourceBytes.copyOfRange(0, targetSize)
            }

            else -> {
                // If shorter, repeat to fill
                val result = ByteArray(targetSize)
                var currentIndex = 0
                while (currentIndex < targetSize) {
                    for (byteValue in sourceBytes) {
                        if (currentIndex < targetSize) {
                            result[currentIndex] = byteValue
                            currentIndex++
                        } else {
                            break // Stop if targetSize is reached
                        }
                    }
                }
                result
            }
        }
    }
}