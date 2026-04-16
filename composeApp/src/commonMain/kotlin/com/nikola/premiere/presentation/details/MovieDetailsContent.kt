package com.nikola.premiere.presentation.details
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nikola.premiere.domain.usecase.FullMovieData
import com.nikola.premiere.presentation.details.components.CastItem
import com.nikola.premiere.presentation.details.components.HeroSection
import com.nikola.premiere.presentation.details.components.ImagesGallerySection
import com.nikola.premiere.presentation.details.components.InfoSection
import com.nikola.premiere.presentation.details.components.OverviewSection
import com.nikola.premiere.presentation.details.components.TitleAndInfoSection

@Composable
fun MovieDetailsContent(
    data : FullMovieData,
    viewModel: DetailsViewModel
) {
    val movie = data.movie

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            HeroSection(
                backdropPath = movie.backdropPath,
                videos = data.videos,
                onBackClicked = {viewModel.setIntent(DetailsContract.Intent.NavigateBack)},
                onPlayClicked = {videoKey -> viewModel.setIntent(DetailsContract.Intent.PlayTrailer(videoKey))},
            )
        }
        item { TitleAndInfoSection(movie = movie) }
        item { OverviewSection(movie.overview) }
        item { InfoSection(movie = movie) }
        item { ImagesGallerySection(backdrops = data.backdrops) }
        item {
            Text(
                text = "ACTORS",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
        }
        items(data.cast) { person ->
            CastItem(person = person)
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}
