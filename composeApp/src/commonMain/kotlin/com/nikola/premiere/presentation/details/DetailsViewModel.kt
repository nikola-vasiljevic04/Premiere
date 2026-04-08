package com.nikola.premiere.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikola.premiere.domain.usecase.GetMovieDetailsUseCase
import com.nikola.premiere.presentation.movies_list.MoviesContract
import com.nikola.premiere.presentation.navigation.movieIdOrThrow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val movieId: String = savedStateHandle.movieIdOrThrow
    private val _state = MutableStateFlow<DetailsContract.State>(DetailsContract.State.Loading)
    val state: StateFlow<DetailsContract.State>  = _state.asStateFlow()

    private val intentFlow = MutableSharedFlow<DetailsContract.Intent>()
    private val _effect = MutableSharedFlow<DetailsContract.Effect>()
    val effect = _effect.asSharedFlow()
    private fun sendEffect(effect: DetailsContract.Effect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
    private fun setState(
        reducer: DetailsContract.State.() -> DetailsContract.State
    ) {
        _state.value = _state.value.reducer()
    }
    init {
        viewModelScope.launch {
            intentFlow.collect { intent ->
                handleIntent(intent)
            }
        }
        // Odmah po paljenju ekrana pokrećemo učitavanje
        setIntent(DetailsContract.Intent.LoadDetails)
    }
    fun setIntent(intent: DetailsContract.Intent) {
        viewModelScope.launch {
            intentFlow.emit(intent)
        }
    }
    private suspend fun handleIntent(intent: DetailsContract.Intent) {
        when (intent) {
            is DetailsContract.Intent.LoadDetails -> fetchDetails()
            is DetailsContract.Intent.PlayTrailer -> sendEffect(DetailsContract.Effect.OpenYoutube(intent.videoKey))
            is DetailsContract.Intent.NavigateBack -> sendEffect(DetailsContract.Effect.NavigateBack)
        }
    }
    private suspend fun fetchDetails() {
        setState { DetailsContract.State.Loading }
        try {
            // Zovemo tvoj UseCase koji sve radi paralelno
            val fullData = getMovieDetailsUseCase(movieId = movieId)

            setState { DetailsContract.State.Success(data = fullData) }
        } catch (e: Exception) {
            e.printStackTrace()
            setState {
                DetailsContract.State.Error(
                    message = e.message ?: "Došlo je do greške pri učitavanju detalja."
                )
            }
        }
    }
}