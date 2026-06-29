# --- Cryptography ---
-keep class dev.whyoleg.cryptography.** { *; }

# --- Kotlin serialization ---
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class io.github.devapro.droid.**$$serializer { *; }
-keepclassmembers class io.github.devapro.droid.** {
    *** Companion;
}
-keep @kotlinx.serialization.Serializable class io.github.devapro.droid.** { *; }

# --- Koin ---
-keep class org.koin.** { *; }
-keep class io.github.devapro.droid.**.*Di* { *; }
-keep class io.github.devapro.droid.**.*DiKt { *; }

# --- Voyager navigation ---
-keep class cafe.adriel.voyager.** { *; }

# --- Ktor client ---
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
# Ktor debug detector references JDK-only management APIs unavailable on Android.
-dontwarn java.lang.management.**

# --- Compose runtime (reflection used by some navigation/state) ---
-keep class androidx.compose.runtime.** { *; }

# --- App entry points ---
-keep class io.github.devapro.** { *; }
-keep class io.github.devapro.droid.** { *; }
