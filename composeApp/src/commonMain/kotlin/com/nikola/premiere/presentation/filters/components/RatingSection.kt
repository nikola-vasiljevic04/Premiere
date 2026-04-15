package com.nikola.premiere.presentation.filters.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun RatingSection(
    minRating: Float,
    onRatingChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("MINIMUM RATING", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // Magija za zaokruživanje: 8.54321 -> 85.4321 -> 85 -> 8.5
        val displayRating = (minRating * 10.0).roundToInt() / 10.0

        // Ispisujemo 0.0 levo, izabranu ocenu u sredini, i 10.0 desno
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0.0", color = Color.Gray)
            Text(text = "$displayRating+", color = Color.White, fontWeight = FontWeight.Bold)
            Text("10.0", color = Color.Gray)
        }

        Slider(
            value = minRating,
            onValueChange = onRatingChange, // Šaljemo tačan Float nazad u ViewModel
            valueRange = 0f..10f, // Ograničavamo slajder od 0 do 10
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFE50914), // Netflix crvena za kružić
                activeTrackColor = Color(0xFFE50914), // Netflix crvena za popunjeni deo
                inactiveTrackColor = Color.DarkGray // Tamno siva za prazan deo
            )
        )
    }
}