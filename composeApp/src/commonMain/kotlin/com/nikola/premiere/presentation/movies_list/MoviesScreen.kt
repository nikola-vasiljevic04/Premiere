package com.nikola.premiere.presentation.movies_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nikola.premiere.data.model.MovieListItem
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChipDefaults
import org.jetbrains.compose.resources.painterResource
import premiere.composeapp.generated.resources.Res
import premiere.composeapp.generated.resources.ic_logo
import kotlin.math.round

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MoviesScreen(
    viewModel: MoviesViewModel,
    onThemeToggle: () -> Unit,
    onNavigateToDetails: (String)-> Unit,
    onNavigateToFilters:()-> Unit
){
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit){
        viewModel.effect.collect{
            effect -> when(effect){
                is MoviesContract.Effect.NavigateToDetails -> onNavigateToDetails(effect.movieId)
                is MoviesContract.Effect.NavigateToFilters -> onNavigateToFilters()
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(Res.drawable.ic_logo),
                            contentDescription = "Premiere Logo",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Premiere", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = Icons.Filled.DarkMode,
                            contentDescription = "Promeni temu",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Button(
                        onClick = { viewModel.onFilterClicked() },
                        shape = CircleShape,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Filters")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ){
        paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ){
            when(val currentState = state){
                is MoviesContract.State.Loading->{
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary ,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is MoviesContract.State.Error ->{
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.setIntent(MoviesContract.Intent.LoadMovies) },
                            shape   = CircleShape,
                        ) {
                            Text("Pokušaj ponovo")
                        }
                    }
                }
                is MoviesContract.State.Empty ->{
                    Text(
                        text = "No movies found",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                is MoviesContract.State.Success ->{
                    Column(modifier = Modifier.fillMaxSize()){
                        SortBar(
                            currentSort = currentState.currentSort,
                            totalMovies = currentState.totalMovies,
                            onSortChanged = {
                                newSort-> viewModel.setIntent(MoviesContract.Intent.ChangeSort(newSort))
                            }
                        )
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(currentState.movies){
                                movie->
                                MovieCard(
                                    movie = movie,
                                    onClick = {
                                        viewModel.onMovieClicked(movie.imdbId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

    }

}
@Composable
fun SortBar(
    currentSort:String,
    totalMovies: Int,
    onSortChanged:(String)-> Unit
){
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Box{
            FilterChip(
                selected = false,
                onClick = {expanded = true},
                label = {Text("Sort: $currentSort⬇")},
                shape = CircleShape,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface
                ),
                border = null
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false},
            containerColor = MaterialTheme.colorScheme.surface
        ){
            val options = listOf("imdb_rating", "year", "title", "popularity")
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option,color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        onSortChanged(option)
                        expanded = false
                    }
                )
            }
        }
        Text(text = "$totalMovies movies", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
    }
}

@Composable
fun MovieCard(
    movie: MovieListItem,
    onClick: ()-> Unit
){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(
        model = "https://image.tmdb.org/t/p/w185${movie.posterPath}",
        contentDescription = movie.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(8.dp)
            .width(92.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.DarkGray)
    )

        // Detalji filma
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = movie.year?.toString() ?: "N/A",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = movie.imdbRating?.toString() ?: "N/A",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${formatVotes(movie.imdbVotes) ?: 0} votes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
           //za ucitavanje zanrova ako je landscape da uzme vise od 2
            BoxWithConstraints {
                val availableWidth = maxWidth

                val genreCount = when {
                    availableWidth > 400.dp -> 7
                    availableWidth > 250.dp -> 5
                    else -> 2
                }
            // zanrovi
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                movie.genres.take(genreCount).forEach { genre ->
                    SuggestionChip(
                        onClick = { },
                        label = { Text(genre.name, style = MaterialTheme.typography.labelSmall) },
                        shape = CircleShape,
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.height(25.dp)
                    )
                }
            }
            }
        }

    }
    }
}
private fun formatVotes(votes: Int?): String = when {
    votes == null -> "0"
    votes >= 1_000_000 -> {
        val f = votes / 1_000_000f
        "${(round(f * 10f) / 10f).toString().removeSuffix(".0")}M"
    }
    votes >= 1_000 -> {
        val f = votes / 1_000f
        "${(round(f * 10f) / 10f).toString().removeSuffix(".0")}K"
    }
    else -> votes.toString()
}
