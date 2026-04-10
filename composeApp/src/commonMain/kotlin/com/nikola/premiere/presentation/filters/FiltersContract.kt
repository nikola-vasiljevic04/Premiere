package com.nikola.premiere.presentation.filters

import com.nikola.premiere.data.model.Genre

interface FiltersContract {

    data class State(
        val searchQuery:  String = "",
        val genreState: GenreState = GenreState.Loading,
        val selectedGenreId: Int? = null,
        val minYear : Int? = null,
        val maxYear : Int? = null,
        val minRating: Float = 0f
    )
    sealed interface GenreState{
        data object Loading: GenreState
        data class Success(val genres: List<Genre>): GenreState
        data class Error(val message: String): GenreState
    }

    sealed interface Intent{
        data object LoadGenres: Intent

        data class ChangeSearchQuery(val query: String): Intent
        data class SelectGenre(val genreId: Int?): Intent
        data class SetMinRating(val rating: Float): Intent
        data class SetMinYear(val year: Int?) : Intent
        data class SetMaxYear(val year: Int?) : Intent
        data object ApplyFilters : Intent
        data object ClearFilters : Intent
    }
    sealed interface Effect{
        data object NavigateBack: Effect
        data class NavigateBackWithFilters(
            val query: String,
            val genreId: Int?,
            val minRating: Float,
            val minYear: Int?,
            val maxYear: Int?
        ): Effect
    }
}