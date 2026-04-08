package com.nikola.premiere.di

import com.nikola.premiere.data.remote.MoviesApi
import com.nikola.premiere.domain.usecase.GetMoviesUseCase
import com.nikola.premiere.domain.repository.MovieRepository
import com.nikola.premiere.domain.usecase.GetMovieDetailsUseCase
import com.nikola.premiere.presentation.details.DetailsViewModel
import com.nikola.premiere.presentation.movies_list.MoviesViewModel
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.dsl.viewModel

val networkModule = module {

    // 1. Pravimo Ktor HttpClient koji zna da parsira JSON
    single {
        val httpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            defaultRequest {
                url("https://rma.finlab.rs/")
            }
        }

        // 2. Ktorfitu dajemo taj klijent
        Ktorfit.Builder()
            .httpClient(httpClient)
            .build()
    }

    // 3. Kreiramo implementaciju MoviesApi interfejsa
    single<MoviesApi> {
        val ktorfit = get<Ktorfit>()
        ktorfit.create<MoviesApi>()
    }
    // 4. Repozitorijum (prosledi api zavisnost automatski pomoću get())
    single<MovieRepository> {
        com.nikola.premiere.data.repository.MovieRepositoryImpl(api = get())
    }
    factory { GetMoviesUseCase(repository = get()) }

    // 2. ViewModel sada prima GetMoviesUseCase umesto Repozitorijuma
    viewModel { MoviesViewModel(getMoviesUseCase = get()) }

    factory { GetMovieDetailsUseCase(repository = get()) }

    // 2. Registrujemo ViewModel (SavedStateHandle se ubacuje automatski!)
    viewModel { DetailsViewModel(getMovieDetailsUseCase = get(), savedStateHandle = get()) }

}