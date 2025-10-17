package dev.michalkonkel.nbp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
