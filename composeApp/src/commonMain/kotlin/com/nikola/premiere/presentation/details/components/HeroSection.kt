package com.nikola.premiere.presentation.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nikola.premiere.data.model.Video

@Composable
fun HeroSection(
    backdropPath: String?,
    videos:List<Video>,
    onBackClicked: ()-> Unit,
    onPlayClicked: (String) -> Unit
){
    Box(modifier = Modifier.fillMaxWidth().height(280.dp)){
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w1280$backdropPath",
            contentDescription = "Movie BackDrop",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier.padding(16.dp)
                .align(Alignment.TopStart)
                .background(Color.Black.copy(alpha = 0.4f), CircleShape)
        ){
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        val firstTrailer = videos.firstOrNull()
        if(firstTrailer != null){
            IconButton(
                onClick = {onPlayClicked(firstTrailer.key)},
                modifier = Modifier.size(64.dp)
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ){
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play Trailer",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}