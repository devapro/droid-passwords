import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:3.5.4"))
            implementation("io.insert-koin:koin-core")
            implementation("io.insert-koin:koin-android")
            implementation(libs.kstore.file)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.uuid)
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:3.5.4"))
            implementation("io.insert-koin:koin-core")
            implementation("io.insert-koin:koin-compose")
            implementation(libs.viewmodel)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
            // DataStore library
            implementation("androidx.datastore:datastore:1.1.7")
            // The Preferences DataStore library
            implementation("androidx.datastore:datastore-preferences:1.1.7")

            // Navigator
            implementation("cafe.adriel.voyager:voyager-navigator:1.0.1")

            // TabNavigator
            implementation("cafe.adriel.voyager:voyager-tab-navigator:1.0.1")

            // Transitions
            implementation("cafe.adriel.voyager:voyager-transitions:1.0.1")

            // FileKit
            implementation("io.github.vinceglb:filekit-core:0.10.0-beta04")
            // Enables FileKit dialogs without Compose dependencies
            implementation("io.github.vinceglb:filekit-dialogs:0.10.0-beta04")
            // Enables FileKit dialogs with Composable utilities
            implementation("io.github.vinceglb:filekit-dialogs-compose:0.10.0-beta04")
            
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
        iosMain.dependencies {

        }
    }
}

android {
    namespace = "io.github.devapro"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.github.devapro"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "io.github.devapro.MainKt"

        nativeDistributions {
            linux {
                modules("jdk.security.auth")
            }
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.github.devapro"
            packageVersion = "1.0.0"
        }
    }
}
