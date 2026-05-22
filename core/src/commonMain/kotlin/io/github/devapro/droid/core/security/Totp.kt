package io.github.devapro.droid.core.security

import kotlin.math.pow

object Totp {

    const val DEFAULT_PERIOD_SECONDS = 30L
    const val DEFAULT_DIGITS = 6

    /**
     * Returns true if the given input looks like a valid Base32 TOTP secret
     * (non-empty, decodes successfully, has at least 10 bytes of entropy).
     */
    fun isValidSecret(secret: String): Boolean {
        val bytes = Base32.decode(secret) ?: return false
        return bytes.size >= 10
    }

    /**
     * Generate the TOTP code for the given Base32-encoded [secret] at [epochSeconds].
     * Returns null if the secret cannot be decoded.
     */
    fun generate(
        secret: String,
        epochSeconds: Long,
        periodSeconds: Long = DEFAULT_PERIOD_SECONDS,
        digits: Int = DEFAULT_DIGITS
    ): String? {
        val key = Base32.decode(secret) ?: return null
        if (key.isEmpty()) return null
        val counter = epochSeconds / periodSeconds
        val message = ByteArray(8) { i -> (counter ushr ((7 - i) * 8)).toByte() }
        val hmac = HmacSha1.mac(key, message)
        val offset = hmac[hmac.size - 1].toInt() and 0x0F
        val binary = ((hmac[offset].toInt() and 0x7F) shl 24) or
            ((hmac[offset + 1].toInt() and 0xFF) shl 16) or
            ((hmac[offset + 2].toInt() and 0xFF) shl 8) or
            (hmac[offset + 3].toInt() and 0xFF)
        val modulo = 10.0.pow(digits).toInt()
        val code = binary % modulo
        return code.toString().padStart(digits, '0')
    }

    /**
     * Seconds remaining in the current rolling window.
     */
    fun secondsRemaining(epochSeconds: Long, periodSeconds: Long = DEFAULT_PERIOD_SECONDS): Long {
        return periodSeconds - (epochSeconds % periodSeconds)
    }
}
