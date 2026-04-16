package com.nikola.premiere.presentation.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    onNavigateBack: () -> Unit
){
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DetailsContract.Effect.NavigateBack -> onNavigateBack()
                is DetailsContract.Effect.OpenYoutube -> {
                    uriHandler.openUri("https://www.youtube.com/watch?v=${effect.videoKey}")
                }
            }
        }
    }
    Scaffold(
        containerColor = Color(0xFF121212)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val currentState = state) {
                is DetailsContract.State.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is DetailsContract.State.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
                    }
                }

                is DetailsContract.State.Success -> {
                    MovieDetailsContent(data = currentState.data, viewModel = viewModel)
                }
            }
        }
    }
}