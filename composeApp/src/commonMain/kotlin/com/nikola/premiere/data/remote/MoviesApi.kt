package com.nikola.premiere.data.remote

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import com.nikola.premiere.data.model.*

interface MoviesApi {

    @GET("movies")
    suspend fun getMovies(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 30,
        @Query("sort_by") sortBy: String = "imdb_rating",
        @Query("sort_order") sortOrder: String = "desc",
        @Query("query") query: String? = null,
        @Query("genre_id") genreId: Int? = null,
        @Query("min_year") minYear: Int? = null,
        @Query("max_year") maxYear: Int? = null,
        @Query("min_rating") minRating: Float? = null
    ): PaginatedResponse<MovieListItem>

    @GET("genres")
    suspend fun getGenres(): List<Genre>

    @GET("config")
    suspend fun getConfig(): List<ConfigEntry>

    @GET("movies/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: String
    ): Movie

    @GET("movies/{id}/cast")
    suspend fun getMovieCast(
        @Path("id") id: String,
        @Query("page_size") pageSize: Int = 10
    ): PaginatedResponse<PersonSummary>

    @GET("movies/{id}/images")
    suspend fun getMovieImages(
        @Path("id") id: String,
        @Query("type") type: String = "backdrop"
    ): MovieImages

    @GET("movies/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") id: String,
        @Query("type") type: String = "Trailer"
    ): List<Video>
}