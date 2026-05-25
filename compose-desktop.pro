-keep class dev.whyoleg.cryptography.*
-keep class dev.whyoleg.cryptography.providers.jdk.*

# DataStore Preferences bundles its own Protobuf runtime, which uses reflection
# on generated field names (e.g. `preferences_`). Obfuscating them breaks DataStore.
-keep class androidx.datastore.preferences.protobuf.** { *; }
-keep class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite { *; }
-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}
-keep class androidx.datastore.preferences.PreferencesProto** { *; }

# JNA is used by FileKit's native file pickers on macOS/Windows/Linux. Its native
# bridge resolves Java classes and methods by their original names, so obfuscating
# `com.sun.jna.**` breaks the file picker with UnsatisfiedLinkError.
-keep class com.sun.jna.** { *; }
-keepclassmembers class * extends com.sun.jna.** { *; }
-dontwarn com.sun.jna.**

# FileKit calls into platform-specific dialog code via JNA on desktop. Keep its
# classes so that platform-side reflection lookups continue to resolve.
-keep class io.github.vinceglb.filekit.** { *; }
-dontwarn io.github.vinceglb.filekit.**
