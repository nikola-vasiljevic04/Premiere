package com.nikola.premiere.domain.repository

import com.nikola.premiere.data.model.*

interface MovieRepository {

    suspend fun getMovies(
        page: Int = 1,
        pageSize: Int = 30,
        sortBy: String = "imdb_rating",
        sortOrder: String = "desc",
        query: String? = null,
        genreId: Int? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        minRating: Float? = null
    ): PaginatedResponse<MovieListItem>

    suspend fun getGenres(): List<Genre>

    suspend fun getConfig(): List<ConfigEntry>

    suspend fun getMovieDetails(id: String): Movie

    suspend fun getMovieCast(id: String, pageSize: Int = 10): PaginatedResponse<PersonSummary>

    suspend fun getMovieImages(id: String, type: String = "backdrop"): MovieImages

    suspend fun getMovieVideos(id: String, type: String = "Trailer"): List<Video>
}