package com.nikola.premiere

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform