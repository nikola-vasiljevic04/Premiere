package com.nikola.premiere.data.repository

import com.nikola.premiere.data.model.*
import com.nikola.premiere.data.remote.MoviesApi
import com.nikola.premiere.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val api: MoviesApi
) : MovieRepository {

    override suspend fun getMovies(
        page: Int, pageSize: Int, sortBy: String, sortOrder: String,
        query: String?, genreId: Int?, minYear: Int?, maxYear: Int?, minRating: Float?
    ): PaginatedResponse<MovieListItem> {
        return api.getMovies(page, pageSize, sortBy, sortOrder, query, genreId, minYear, maxYear, minRating)
    }

    override suspend fun getGenres(): List<Genre> {
        return api.getGenres()
    }

    override suspend fun getConfig(): List<ConfigEntry> {
        return api.getConfig()
    }

    override suspend fun getMovieDetails(id: String): Movie {
        return api.getMovieDetails(id)
    }

    override suspend fun getMovieCast(id: String, pageSize: Int): PaginatedResponse<PersonSummary> {
        return api.getMovieCast(id, pageSize)
    }

    override suspend fun getMovieImages(id: String, type: String): MovieImages {
        return api.getMovieImages(id, type)
    }

    override suspend fun getMovieVideos(id: String, type: String): List<Video> {
        return api.getMovieVideos(id, type)
    }
}