package com.nikola.premiere

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nikola.premiere.di.networkModule
import com.nikola.premiere.domain.repository.MovieRepository
import com.nikola.premiere.presentation.movies_list.MoviesScreen
import com.nikola.premiere.presentation.movies_list.MoviesViewModel
import com.nikola.premiere.presentation.navigation.AppNavigation
import kotlinx.coroutines.launch
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    // 1. Pokrećemo Koin i učitavamo naš network modul
    KoinApplication(application = {
        modules(networkModule)
    }) {
        MaterialTheme(colorScheme = darkColorScheme()) {
            AppNavigation()
        }
    }
}

@Composable
fun TestScreen() {
    // 2. Tražimo od Koina da nam da repozitorijum
    val repository = koinInject<MovieRepository>()
    val coroutineScope = rememberCoroutineScope()

    // Stanje teksta na ekranu
    var statusText by remember { mutableStateOf("Klikni dugme da testiraš API") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // <-- OVO JE ISPRAVLJENO
    ) {
        Text(
            text = statusText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(onClick = {
            statusText = "Učitavam podatke sa rma.finlab.rs..."

            // 3. Pokrećemo mrežni poziv u coroutine-i
            coroutineScope.launch {
                try {
                    // Tražimo samo 1 film da proverimo da li radi
                    val response = repository.getMovies(pageSize = 1)
                    val firstMovie = response.items.firstOrNull()

                    if (firstMovie != null) {
                        statusText = """
                            USPEH! 🎉
                            
                            Naslov: ${firstMovie.title}
                            Godina: ${firstMovie.year}
                            IMDB Rating: ${firstMovie.imdbRating}
                            Poster Path: ${firstMovie.posterPath}
                        """.trimIndent()
                    } else {
                        statusText = "API je prošao, ali je vratio praznu listu."
                    }
                } catch (e: Exception) {
                    statusText = "GREŠKA:\n${e.message}"
                    e.printStackTrace()
                }
            }
        }) {
            Text("Testiraj API")
        }
    }
}