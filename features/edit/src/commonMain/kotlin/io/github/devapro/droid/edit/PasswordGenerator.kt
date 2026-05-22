package io.github.devapro.droid.edit

import io.github.devapro.droid.edit.model.PasswordGeneratorOptions
import kotlin.random.Random

object PasswordGenerator {

    private const val UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWERCASE = "abcdefghijklmnopqrstuvwxyz"
    private const val DIGITS = "0123456789"
    private const val SYMBOLS = "!@#\$%^&*()-_=+[]{};:,.?/"
    private const val AMBIGUOUS = "Il1O0o"

    fun generate(options: PasswordGeneratorOptions): String {
        val length = options.length.coerceIn(
            PasswordGeneratorOptions.MIN_LENGTH,
            PasswordGeneratorOptions.MAX_LENGTH
        )

        val pools = mutableListOf<String>()
        if (options.includeUppercase) pools += UPPERCASE.filterAmbiguous(options.excludeAmbiguous)
        if (options.includeLowercase) pools += LOWERCASE.filterAmbiguous(options.excludeAmbiguous)
        if (options.includeDigits) pools += DIGITS.filterAmbiguous(options.excludeAmbiguous)
        if (options.includeSymbols) pools += SYMBOLS

        val nonEmptyPools = pools.filter { it.isNotEmpty() }
        if (nonEmptyPools.isEmpty()) return ""

        val fullPool = nonEmptyPools.joinToString("")
        val chars = CharArray(length)

        // Ensure at least one char from each selected pool
        nonEmptyPools.forEachIndexed { index, pool ->
            if (index < length) {
                chars[index] = pool[Random.nextInt(pool.length)]
            }
        }

        // Fill the rest from full pool
        for (i in nonEmptyPools.size until length) {
            chars[i] = fullPool[Random.nextInt(fullPool.length)]
        }

        // Shuffle in place
        for (i in chars.indices.reversed()) {
            val j = Random.nextInt(i + 1)
            val tmp = chars[i]
            chars[i] = chars[j]
            chars[j] = tmp
        }

        return chars.concatToString()
    }

    private fun String.filterAmbiguous(exclude: Boolean): String {
        if (!exclude) return this
        return this.filter { it !in AMBIGUOUS }
    }
}
