package io.github.devapro

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform