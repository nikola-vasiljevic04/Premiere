package com.nikola.premiere.domain.usecase

import com.nikola.premiere.data.model.MovieListItem
import com.nikola.premiere.data.model.PaginatedResponse
import com.nikola.premiere.domain.repository.MovieRepository

class GetMoviesUseCase(
    private val repository: MovieRepository
){
    suspend operator fun invoke(
        sortBy: String = "imdb_rating"
    ): PaginatedResponse<MovieListItem> {
        return repository.getMovies(
            pageSize = 30,
            sortBy = sortBy
        )
    }

}