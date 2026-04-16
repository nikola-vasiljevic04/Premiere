package com.nikola.premiere.domain.usecase

import com.nikola.premiere.data.model.MovieListItem
import com.nikola.premiere.data.model.PaginatedResponse
import com.nikola.premiere.domain.repository.MovieRepository

class GetMoviesUseCase(
    private val repository: MovieRepository
){
    suspend operator fun invoke(
        sortBy: String = "imdb_rating",
        query: String = "",
        genreId: Int? = null,
        minRating: Float = 0f,
        minYear: Int? = null,
        maxYear: Int? = null
    ): PaginatedResponse<MovieListItem> {
        return repository.getMovies(
            pageSize = 30,
            sortBy = sortBy,
            query = query,
            genreId = genreId,
            minRating = minRating,
            minYear = minYear,
            maxYear = maxYear
        )
    }

}