package com.nikola.premiere.presentation.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun InfoSection(movie: com.nikola.premiere.data.model.Movie) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).offset(y = (-20).dp)) {
        Text(
            text = "INFO",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val formatMoney = { amount: Long? ->
                if (amount != null && amount > 0) "$${amount / 1_000_000}M" else "N/A"
            }

            InfoCard(title = "Budget", value = formatMoney(movie.budget), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InfoCard(title = "Revenue", value = formatMoney(movie.revenue), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InfoCard(title = "Language", value = movie.languageCode?.uppercase() ?: "N/A", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InfoCard(title = "Popularity", value = movie.popularity?.toInt()?.toString() ?: "N/A", modifier = Modifier.weight(1f))
        }
    }
}
@Composable
fun InfoCard(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }
}