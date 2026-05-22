package io.github.devapro.droid.core.security

internal object Sha1 {

    fun hash(data: ByteArray): ByteArray {
        val ml = data.size.toLong() * 8
        val withOne = data + byteArrayOf(0x80.toByte())
        val padLen = (56 - withOne.size % 64 + 64) % 64
        val padded = withOne + ByteArray(padLen) + longToBigEndianBytes(ml)

        var h0 = 0x67452301
        var h1 = 0xEFCDAB89.toInt()
        var h2 = 0x98BADCFE.toInt()
        var h3 = 0x10325476
        var h4 = 0xC3D2E1F0.toInt()

        val w = IntArray(80)
        var chunk = 0
        while (chunk < padded.size) {
            for (i in 0 until 16) {
                val base = chunk + i * 4
                w[i] = ((padded[base].toInt() and 0xFF) shl 24) or
                    ((padded[base + 1].toInt() and 0xFF) shl 16) or
                    ((padded[base + 2].toInt() and 0xFF) shl 8) or
                    (padded[base + 3].toInt() and 0xFF)
            }
            for (i in 16 until 80) {
                val v = w[i - 3] xor w[i - 8] xor w[i - 14] xor w[i - 16]
                w[i] = (v shl 1) or (v ushr 31)
            }

            var a = h0
            var b = h1
            var c = h2
            var d = h3
            var e = h4

            for (i in 0 until 80) {
                val f: Int
                val k: Int
                when {
                    i < 20 -> {
                        f = (b and c) or (b.inv() and d)
                        k = 0x5A827999
                    }
                    i < 40 -> {
                        f = b xor c xor d
                        k = 0x6ED9EBA1
                    }
                    i < 60 -> {
                        f = (b and c) or (b and d) or (c and d)
                        k = 0x8F1BBCDC.toInt()
                    }
                    else -> {
                        f = b xor c xor d
                        k = 0xCA62C1D6.toInt()
                    }
                }
                val temp = ((a shl 5) or (a ushr 27)) + f + e + k + w[i]
                e = d
                d = c
                c = (b shl 30) or (b ushr 2)
                b = a
                a = temp
            }

            h0 += a
            h1 += b
            h2 += c
            h3 += d
            h4 += e
            chunk += 64
        }

        return intToBigEndianBytes(h0) + intToBigEndianBytes(h1) +
            intToBigEndianBytes(h2) + intToBigEndianBytes(h3) + intToBigEndianBytes(h4)
    }

    private fun intToBigEndianBytes(value: Int): ByteArray = byteArrayOf(
        (value ushr 24).toByte(),
        (value ushr 16).toByte(),
        (value ushr 8).toByte(),
        value.toByte()
    )

    private fun longToBigEndianBytes(value: Long): ByteArray = byteArrayOf(
        (value ushr 56).toByte(),
        (value ushr 48).toByte(),
        (value ushr 40).toByte(),
        (value ushr 32).toByte(),
        (value ushr 24).toByte(),
        (value ushr 16).toByte(),
        (value ushr 8).toByte(),
        value.toByte()
    )
}

internal object HmacSha1 {
    private const val BLOCK_SIZE = 64

    fun mac(key: ByteArray, message: ByteArray): ByteArray {
        val normalisedKey = when {
            key.size > BLOCK_SIZE -> Sha1.hash(key) + ByteArray(BLOCK_SIZE - 20)
            key.size < BLOCK_SIZE -> key + ByteArray(BLOCK_SIZE - key.size)
            else -> key
        }
        val outerKey = ByteArray(BLOCK_SIZE) { (normalisedKey[it].toInt() xor 0x5C).toByte() }
        val innerKey = ByteArray(BLOCK_SIZE) { (normalisedKey[it].toInt() xor 0x36).toByte() }
        val innerHash = Sha1.hash(innerKey + message)
        return Sha1.hash(outerKey + innerHash)
    }
}
