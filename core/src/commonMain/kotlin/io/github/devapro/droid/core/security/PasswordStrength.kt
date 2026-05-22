package io.github.devapro.droid.core.security

import kotlin.math.ln
import kotlin.math.max

enum class PasswordStrengthLevel(val label: String, val score: Int) {
    VERY_WEAK("Very weak", 0),
    WEAK("Weak", 1),
    FAIR("Fair", 2),
    STRONG("Strong", 3),
    VERY_STRONG("Very strong", 4)
}

data class PasswordStrength(
    val level: PasswordStrengthLevel,
    val entropyBits: Double
)

object PasswordStrengthEstimator {

    private val COMMON_PASSWORDS = setOf(
        "password", "123456", "12345678", "qwerty", "abc123", "password1",
        "letmein", "welcome", "admin", "monkey", "111111", "iloveyou",
        "dragon", "sunshine", "princess", "qwerty123", "football", "passw0rd",
        "starwars", "master", "hello", "freedom", "whatever", "trustno1"
    )

    fun estimate(password: String): PasswordStrength {
        if (password.isEmpty()) {
            return PasswordStrength(PasswordStrengthLevel.VERY_WEAK, 0.0)
        }

        val lower = password.lowercase()
        if (lower in COMMON_PASSWORDS) {
            return PasswordStrength(PasswordStrengthLevel.VERY_WEAK, 0.0)
        }

        val poolSize = calculatePoolSize(password)
        val baseEntropy = if (poolSize > 0) password.length * log2(poolSize.toDouble()) else 0.0

        val penalty = patternPenalty(password)
        val entropy = max(0.0, baseEntropy - penalty)

        val level = when {
            entropy < 28 -> PasswordStrengthLevel.VERY_WEAK
            entropy < 36 -> PasswordStrengthLevel.WEAK
            entropy < 60 -> PasswordStrengthLevel.FAIR
            entropy < 90 -> PasswordStrengthLevel.STRONG
            else -> PasswordStrengthLevel.VERY_STRONG
        }
        return PasswordStrength(level, entropy)
    }

    private fun calculatePoolSize(password: String): Int {
        var pool = 0
        if (password.any { it.isLowerCase() }) pool += 26
        if (password.any { it.isUpperCase() }) pool += 26
        if (password.any { it.isDigit() }) pool += 10
        if (password.any { !it.isLetterOrDigit() }) pool += 32
        return pool
    }

    private fun patternPenalty(password: String): Double {
        var penalty = 0.0

        // Penalty for repeated characters
        val distinctRatio = password.toSet().size.toDouble() / password.length
        if (distinctRatio < 0.5) penalty += 10.0

        // Penalty for sequential ascending characters
        var sequentialRun = 1
        for (i in 1 until password.length) {
            if (password[i].code == password[i - 1].code + 1) {
                sequentialRun++
                if (sequentialRun >= 3) penalty += 2.0
            } else {
                sequentialRun = 1
            }
        }

        // Penalty for all-digit short passwords (likely PIN)
        if (password.all { it.isDigit() } && password.length <= 8) penalty += 8.0

        return penalty
    }

    private fun log2(value: Double): Double = ln(value) / ln(2.0)
}
