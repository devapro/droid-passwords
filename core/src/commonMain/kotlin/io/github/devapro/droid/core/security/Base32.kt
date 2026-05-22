package io.github.devapro.droid.core.security

object Base32 {
    private const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"

    fun decode(encoded: String): ByteArray? {
        val cleaned = encoded.uppercase().replace(" ", "").replace("-", "").trimEnd('=')
        if (cleaned.isEmpty()) return ByteArray(0)
        if (cleaned.any { it !in ALPHABET }) return null

        val out = ArrayList<Byte>(cleaned.length * 5 / 8)
        var buffer = 0
        var bitsLeft = 0
        for (ch in cleaned) {
            val value = ALPHABET.indexOf(ch)
            buffer = (buffer shl 5) or value
            bitsLeft += 5
            if (bitsLeft >= 8) {
                bitsLeft -= 8
                out.add(((buffer shr bitsLeft) and 0xFF).toByte())
            }
        }
        return out.toByteArray()
    }
}
