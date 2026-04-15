package com.nikola.premiere.presentation.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nikola.premiere.domain.usecase.GetGenresUseCase
import com.nikola.premiere.presentation.movies_list.MoviesContract
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FiltersViewModel(
    private val getGenresUseCase: GetGenresUseCase
): ViewModel() {
    private val _state = MutableStateFlow(FiltersContract.State())
    val state: StateFlow<FiltersContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FiltersContract.Effect>()
    val effect = _effect.asSharedFlow()

    private val intentFlow = MutableSharedFlow<FiltersContract.Intent>()

    private fun sendEffect(effect: FiltersContract.Effect){
        viewModelScope.launch{
            _effect.emit(effect)
        }
    }
    fun setIntent(intent : FiltersContract.Intent){
        viewModelScope.launch {
            intentFlow.emit(intent)
        }

    }
    init {
        viewModelScope.launch {
            intentFlow.collect {
                intent-> handleIntent(intent)
            }
        }
    }

    private suspend fun handleIntent(intent: FiltersContract.Intent) {
        when(intent){
            is FiltersContract.Intent.LoadGenres-> fetchGenres()
            is FiltersContract.Intent.ChangeSearchQuery ->{
                _state.update { it.copy(searchQuery = intent.query) }
            }
            is FiltersContract.Intent.SelectGenre->{
                val newGenreId = if (_state.value.selectedGenreId == intent.genreId) null else intent.genreId
                _state.update { it.copy(selectedGenreId = newGenreId)}
            }
            is FiltersContract.Intent.SetMinRating ->{
                _state.update { it.copy(minRating = intent.rating) }
            }
            is FiltersContract.Intent.SetMinYear ->{
                _state.update { it.copy(minYear = intent.year)}
            }
            is FiltersContract.Intent.SetMaxYear -> {
                _state.update { it.copy(maxYear = intent.year) }
            }
            FiltersContract.Intent.ClearFilters -> {
                _state.update {
                    it.copy(
                        searchQuery = "",
                        selectedGenreId = null,
                        minYear = null,
                        maxYear = null,
                        minRating = 0f
                    )
                }
            }

            FiltersContract.Intent.ApplyFilters ->{
                val currentState = _state.value
                val effectToSend =  FiltersContract.Effect.NavigateBackWithFilters(
                    query = currentState.searchQuery,
                    genreId = currentState.selectedGenreId,
                    minRating = currentState.minRating,
                    minYear = currentState.minYear,
                    maxYear = currentState.maxYear
                )
                _effect.emit(effectToSend)
            }

        }
    }
    private suspend fun fetchGenres() {
        _state.update{
            it.copy(genreState = FiltersContract.GenreState.Loading)
        }
        try {
            val genres = getGenresUseCase()
            if (genres.isEmpty()) {
                _state.update { it.copy(genreState = FiltersContract.GenreState.Error("Nema žanrova u bazi")) }
            } else {
                _state.update { it.copy(genreState = FiltersContract.GenreState.Success(genres)) }
            }
        }catch (e: Exception){
            e.printStackTrace()
            _state.update {
                it.copy(genreState = FiltersContract.GenreState.Error(e.message?: "Greska pri ucitavanju zanrova"))
            }
        }
    }
}
