package io.github.devapro.data

/**
 * Desktop/JVM-specific implementation for system theme detection.
 * Uses system properties and AWT to determine if dark mode is active.
 */
actual fun getSystemThemeMode(): Boolean {
    return try {
        // Try to detect dark mode on different desktop platforms
        val osName = System.getProperty("os.name").lowercase()

        when {
            osName.contains("mac") -> {
                // macOS dark mode detection
                val result = Runtime.getRuntime().exec("defaults read -g AppleInterfaceStyle")
                result.waitFor()
                val output = result.inputStream.bufferedReader().readText().trim()
                output.equals("Dark", ignoreCase = true)
            }

            osName.contains("windows") -> {
                // Windows dark mode detection (simplified)
                // This is a basic implementation - Windows theme detection is more complex
                val registryQuery =
                    "reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize\" /v AppsUseLightTheme"
                val result = Runtime.getRuntime().exec(registryQuery)
                result.waitFor()
                val output = result.inputStream.bufferedReader().readText()
                output.contains("0x0") // 0x0 means dark mode is enabled
            }

            osName.contains("linux") -> {
                // Linux dark mode detection (GNOME/GTK)
                try {
                    val result = Runtime.getRuntime()
                        .exec("gsettings get org.gnome.desktop.interface gtk-theme")
                    result.waitFor()
                    val output = result.inputStream.bufferedReader().readText().trim()
                    output.lowercase().contains("dark")
                } catch (e: Exception) {
                    // Fallback: check environment variable
                    System.getenv("GTK_THEME")?.lowercase()?.contains("dark") == true
                }
            }

            else -> false // Unknown OS, default to light mode
        }
    } catch (e: Exception) {
        false // Default to light mode on any error
    }
}