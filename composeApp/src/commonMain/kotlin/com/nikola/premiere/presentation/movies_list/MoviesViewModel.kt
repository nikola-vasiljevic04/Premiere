package com.nikola.premiere.presentation.movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikola.premiere.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MoviesViewModel (
    private val getMoviesUseCase: GetMoviesUseCase
): ViewModel(){
    private val _state = MutableStateFlow<MoviesContract.State>(MoviesContract.State.Loading)
    val state: StateFlow<MoviesContract.State>  = _state.asStateFlow()

    private val intentFlow = MutableSharedFlow<MoviesContract.Intent>()
    private val _effect = MutableSharedFlow<MoviesContract.Effect>()
    val effect = _effect.asSharedFlow()
    private fun sendEffect(effect: MoviesContract.Effect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
    private fun setState(
        reducer: MoviesContract.State.() -> MoviesContract.State
    ) {
        _state.value = _state.value.reducer()
    }
    private var currentSortValue = "imdb_rating"

    init {
        viewModelScope.launch {
            // Slušamo sve intente u pozadini
            intentFlow.collect { intent ->
                handleIntent(intent)
            }
        }
        setIntent(MoviesContract.Intent.LoadMovies)
    }
    private suspend fun handleIntent(intent: MoviesContract.Intent) {
        when (intent) {
            is MoviesContract.Intent.LoadMovies -> {
                fetchMovies(sortBy = currentSortValue)
            }
            is MoviesContract.Intent.ChangeSort -> {
                currentSortValue = intent.sortBy
                fetchMovies(sortBy = currentSortValue)
            }
        }
    }
    fun setIntent(intent: MoviesContract.Intent) {
        viewModelScope.launch {
            intentFlow.emit(intent)
        }
    }

    private suspend fun fetchMovies(sortBy: String) {
        // 1. ZUM! Sve nestaje, pojavljuje se krug za učitavanje
        setState { MoviesContract.State.Loading }
        try {
            val response = getMoviesUseCase(sortBy = sortBy)

            if (response.items.isEmpty()) {
                // 2a. Nema rezultata
                setState {MoviesContract.State.Empty}
            } else {
                // 2b. Sve je super, iscrtaj listu
                setState { MoviesContract.State.Success(
                    movies = response.items,
                    currentSort = sortBy,
                    totalMovies = response.totalItems
                )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 2c. Nešto je puklo, iscrtaj error ekran
            setState {
                MoviesContract.State.Error(
                    message = e.message ?: "Došlo je do greške pri učitavanju."
                )
            }
        }
    }
    fun onMovieClicked(movieId: String) {
        viewModelScope.launch {
            sendEffect(MoviesContract.Effect.NavigateToDetails(movieId))
        }
    }

    fun onFilterClicked() {
        viewModelScope.launch {
            sendEffect(MoviesContract.Effect.NavigateToFilters)
        }
    }
}