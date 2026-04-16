package com.nikola.premiere.domain.usecase
import com.nikola.premiere.data.model.ImageDetail
import com.nikola.premiere.data.model.Movie
import com.nikola.premiere.data.model.PersonSummary
import com.nikola.premiere.data.model.Video
import com.nikola.premiere.domain.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class FullMovieData(
    val movie:Movie,
    val cast: List<PersonSummary>,
    val backdrops: List<ImageDetail>,
    val videos: List<Video>
)
class GetMovieDetailsUseCase (
    private val repository: MovieRepository
){
    suspend operator fun invoke(movieId: String): FullMovieData{
        return coroutineScope {
            val movieDeferred = async { repository.getMovieDetails(movieId) }
            val castDeferred = async { repository.getMovieCast(movieId, pageSize = 10) }
            val imagesDeferred = async { repository.getMovieImages(movieId, type = "backdrop") }
            val videosDeferred = async { repository.getMovieVideos(movieId, type = "Trailer") }
            FullMovieData(
                movie = movieDeferred.await(),
                cast = castDeferred.await().items,
                backdrops = imagesDeferred.await().backdrops,
                videos = videosDeferred.await(),
            )

        }
    }

}