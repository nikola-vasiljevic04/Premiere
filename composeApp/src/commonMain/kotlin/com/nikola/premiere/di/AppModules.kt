package com.nikola.premiere.di

import com.nikola.premiere.data.remote.MoviesApi
import com.nikola.premiere.domain.usecase.GetMoviesUseCase
import com.nikola.premiere.domain.repository.MovieRepository
import com.nikola.premiere.domain.usecase.GetGenresUseCase
import com.nikola.premiere.domain.usecase.GetMovieDetailsUseCase
import com.nikola.premiere.presentation.details.DetailsViewModel
import com.nikola.premiere.presentation.filters.FiltersViewModel
import com.nikola.premiere.presentation.movies_list.MoviesViewModel
import com.nikola.premiere.presentation.state.FilterManager
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.dsl.viewModel

val networkModule = module {

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
        Ktorfit.Builder()
            .httpClient(httpClient)
            .build()
    }

    single<MoviesApi> {
        val ktorfit = get<Ktorfit>()
        ktorfit.create<MoviesApi>()
    }
    single<MovieRepository> {
        com.nikola.premiere.data.repository.MovieRepositoryImpl(api = get())
    }
    single { FilterManager() }

    factory { GetMoviesUseCase(repository = get()) }
    viewModel {
        MoviesViewModel(
            getMoviesUseCase = get(),
            filterManager = get()
        )
    }

    factory { GetMovieDetailsUseCase(repository = get()) }

    viewModel { DetailsViewModel(getMovieDetailsUseCase = get(), savedStateHandle = get()) }
    factory { GetGenresUseCase(repository = get()) }
    viewModel {
        FiltersViewModel(
            getGenresUseCase = get(),
            filterManager = get()
        )
    }
}