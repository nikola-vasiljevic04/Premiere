package com.nikola.premiere
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.nikola.premiere.di.networkModule
import com.nikola.premiere.presentation.navigation.AppNavigation
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(networkModule)
    }) {
        MaterialTheme(colorScheme = darkColorScheme()) {
            AppNavigation()
        }
    }
}