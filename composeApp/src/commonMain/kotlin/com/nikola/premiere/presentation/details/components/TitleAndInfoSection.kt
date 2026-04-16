package com.nikola.premiere.presentation.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlin.math.round

@Composable
fun TitleAndInfoSection(movie: com.nikola.premiere.data.model.Movie) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .offset(y = (-50).dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w342${movie.posterPath}",
                    contentDescription = "Movie Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(120.dp)
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f).padding(bottom = 4.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${movie.year ?: "N/A"} • ${movie.runtime ?: "N/A"} min",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "IMDb Rating",
                tint = Color(0xFFFFC107), // Zlatno žuta
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = movie.imdbRating?.toString() ?: "N/A",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface // Prati temu
            )
            Text(
                text = " /10",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Prati temu
            )
            Spacer(modifier = Modifier.width(8.dp))

            val votesText = movie.imdbVotes?.let { votes ->
                if (votes >= 1000000) "${(votes / 1000000.0).let { (it * 10).toInt() / 10.0 }}M votes"
                else if (votes >= 1000) "${votes / 1000}K votes"
                else "$votes votes"
            } ?: "0 votes"

            Text(
                text = votesText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.weight(1f))

            val tmbd = movie.tmdbRating?.let {
                round(it * 10) / 10
            }
            Text(
                text = "TMDB ${ tmbd?: "N/A"}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4285F4) // Google Blue ostaje kao akcenat
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- TREĆI RED: ŽANROVI (Tvoj stil) ---
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            movie.genres.take(4).forEach { genre ->
                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(
                            text = genre.name,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    shape = CircleShape,
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        borderWidth = 1.dp
                    ),
                    modifier = Modifier.height(28.dp)
                )
            }
        }
    }
}