package com.nikola.premiere.presentation.details

import com.nikola.premiere.domain.usecase.FullMovieData

interface DetailsContract {

    sealed interface State {
        data object Loading : State
        data class Success(val data: FullMovieData) : State
        data class Error(val message: String) : State
    }
    sealed interface Intent{
        data object LoadDetails : Intent
        data class PlayTrailer(val videoKey: String): Intent
        data object NavigateBack: Intent
    }
    sealed interface Effect{
        data class OpenYoutube(val videoKey: String): Effect
        data object NavigateBack : Effect
    }
}