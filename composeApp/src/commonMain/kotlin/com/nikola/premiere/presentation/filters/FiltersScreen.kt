package com.nikola.premiere.presentation.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nikola.premiere.presentation.filters.components.GenresSection
import com.nikola.premiere.presentation.filters.components.RatingSection
import com.nikola.premiere.presentation.filters.components.SearchSection
import com.nikola.premiere.presentation.filters.components.YearSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    viewModel: FiltersViewModel,
    onNavigateBack:()-> Unit,
){
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit){
        viewModel.effect.collect {
            effect -> when(effect){
                is FiltersContract.Effect.NavigateBack -> onNavigateBack()
            }
        }
    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Filter Movies", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },

                actions = {
                    TextButton(onClick = { viewModel.setIntent(FiltersContract.Intent.ClearFilters) }) {
                        Text("Clear All", color = Color.Red, fontSize = 16.sp) // Crvena boja kao na slici
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)

                    .padding(16.dp)
            ) {
                Button(
                    onClick = { viewModel.setIntent(FiltersContract.Intent.ApplyFilters) },
                    modifier = Modifier.fillMaxWidth().height(50.dp), // Malo veće dugme za lakši klik
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Apply Filters", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ){paddingValues ->
        LazyColumn (
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp) ,
            verticalArrangement = Arrangement.spacedBy(20.dp)
            ){
            item {
                SearchSection(
                    searchQuery = state.searchQuery,
                    onQueryChange = { viewModel.setIntent(FiltersContract.Intent.ChangeSearchQuery(it)) }
                )
            }
            item {
                GenresSection(
                    genreState = state.genreState,
                    selectedGenreId = state.selectedGenreId,
                    onGenreSelected = {viewModel.setIntent(FiltersContract.Intent.SelectGenre(it))}
                )
            }
            item {
                YearSection(
                    minYear = state.minYear?.toString() ?: "",
                    maxYear = state.maxYear?.toString() ?: "",
                    onMinYearChange = { viewModel.setIntent(FiltersContract.Intent.SetMinYear(it)) },
                    onMaxYearChange = { viewModel.setIntent(FiltersContract.Intent.SetMaxYear(it)) }
                )
            }
            item {
                RatingSection(
                    minRating = state.minRating,
                    onRatingChange = {viewModel.setIntent(FiltersContract.Intent.SetMinRating(it))}
                )
            }

        }
    }


}