package com.nikola.premiere.presentation.movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikola.premiere.domain.usecase.GetMoviesUseCase
import com.nikola.premiere.presentation.state.FilterData
import com.nikola.premiere.presentation.state.FilterManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel (
    private val getMoviesUseCase: GetMoviesUseCase,
    private val filterManager: FilterManager
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
            intentFlow.collect { intent ->
                handleIntent(intent)
            }
        }
        viewModelScope.launch {
            filterManager.activeFilters.collect { noviFilteri ->
                fetchMovies(sortBy = currentSortValue, filteri = noviFilteri)
            }
        }
    }
    private suspend fun handleIntent(intent: MoviesContract.Intent) {
        when (intent) {
            is MoviesContract.Intent.LoadMovies -> {
                fetchMovies(sortBy = currentSortValue, filteri = filterManager.activeFilters.value)
            }
            is MoviesContract.Intent.ChangeSort -> {
                currentSortValue = intent.sortBy
                fetchMovies(sortBy = currentSortValue,filteri = filterManager.activeFilters.value)
            }
        }
    }
    fun setIntent(intent: MoviesContract.Intent) {
        viewModelScope.launch {
            intentFlow.emit(intent)
        }
    }

    private suspend fun fetchMovies(sortBy: String,filteri: FilterData) {
        setState { MoviesContract.State.Loading }
        try {
            val response = getMoviesUseCase(
                sortBy = sortBy,
                query = filteri.query,
                genreId = filteri.genreId,
                minRating = filteri.minRating,
                minYear = filteri.minYear,
                maxYear = filteri.maxYear
            )

            if (response.items.isEmpty()) {
                setState {MoviesContract.State.Empty}
            } else {
                setState { MoviesContract.State.Success(
                    movies = response.items,
                    currentSort = sortBy,
                    totalMovies = response.totalItems
                )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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