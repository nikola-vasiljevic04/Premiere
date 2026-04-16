package com.nikola.premiere.presentation.movies_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MoviesScreen(
    viewModel: MoviesViewModel,
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
                title = { Text("Premiere", fontWeight= FontWeight.Bold) },
                actions = {
                    Button(
                        onClick = {viewModel.onFilterClicked()},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.padding(end = 8.dp)
                    ){
                        Text("Filters")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
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
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MoviesContract.State.Error ->{
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.setIntent(MoviesContract.Intent.LoadMovies) }) {
                            Text("Pokušaj ponovo")
                        }
                    }
                }
                is MoviesContract.State.Empty ->{
                    Text(
                        text = "No movies found",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.titleMedium
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
                selected = true,
                onClick = {expanded = true},
                label = {Text("Sort: $currentSort⬇")}
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false},
        ){
            val options = listOf("imdb_rating", "year", "title", "popularity")
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSortChanged(option)
                        expanded = false
                    }
                )
            }
        }
        Text(text = "$totalMovies movies", style = MaterialTheme.typography.bodySmall)
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ){
    Row{AsyncImage(
        model = "https://image.tmdb.org/t/p/w185${movie.posterPath}",
        contentDescription = movie.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(92.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
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
                    overflow = TextOverflow.Ellipsis
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
                    tint = Color.Yellow,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = movie.imdbRating?.toString() ?: "N/A",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${movie.imdbVotes ?: 0} votes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // zanrovi
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                movie.genres.take(2).forEach { genre ->
                    SuggestionChip(
                        onClick = { },
                        label = { Text(genre.name, style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        }

    }
    }
}