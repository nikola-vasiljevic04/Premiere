package com.nikola.premiere
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.nikola.premiere.di.networkModule
import com.nikola.premiere.presentation.navigation.AppNavigation
import com.nikola.premiere.presentation.theme.PremiereTheme
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(networkModule)
    }) {
        val systemInDark = isSystemInDarkTheme()
        var isDarkTheme by remember { mutableStateOf(systemInDark) }
        LaunchedEffect(systemInDark) {
            isDarkTheme = systemInDark
        }
        PremiereTheme(darkTheme = isDarkTheme) {
            AppNavigation(
                isDarkTheme = isDarkTheme,
                onThemeToggle = { isDarkTheme = !isDarkTheme }
            )
        }
    }
}