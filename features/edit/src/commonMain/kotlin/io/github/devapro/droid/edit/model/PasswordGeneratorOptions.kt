package io.github.devapro.droid.edit.model

data class PasswordGeneratorOptions(
    val length: Int = 16,
    val includeUppercase: Boolean = true,
    val includeLowercase: Boolean = true,
    val includeDigits: Boolean = true,
    val includeSymbols: Boolean = true,
    val excludeAmbiguous: Boolean = false
) {
    companion object {
        const val MIN_LENGTH = 4
        const val MAX_LENGTH = 64
    }
}
