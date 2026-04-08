package com.nikola.premiere.presentation.movies_list

import com.nikola.premiere.data.model.MovieListItem

interface MoviesContract {
    sealed interface State {
        data object Loading : State
        data class Success(
            val movies: List<MovieListItem>,
            val currentSort: String = "imdb_rating",
            val totalMovies: Int = 0
        ) : State
        data class Error(val message: String) : State
        data object Empty : State
    }
    sealed class Intent{
        data object LoadMovies: Intent()
        data class ChangeSort(
            val sortBy: String
        ): Intent()
    }
    sealed class Effect{
        data class NavigateToDetails(val movieId: String): Effect()
        data object NavigateToFilters: Effect()
    }
}