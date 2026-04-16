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
import kotlin.math.round

@Composable
fun InfoSection(movie: com.nikola.premiere.data.model.Movie) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).offset(y = (-20).dp)) {
        Text(
            text = "INFO",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

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
fun formatMoney(amount: Long?): String {
    if (amount == null || amount <= 0) return "N/A"

    return when {
        amount >= 1_000_000_000 -> {
            val f = amount / 1_000_000_000f
            "$${(round(f * 10) / 10).toString().removeSuffix(".0")}B"
        }
        amount >= 1_000_000 -> {
            val f = amount / 1_000_000f
            "$${(round(f * 10) / 10).toString().removeSuffix(".0")}M"
        }
        amount >= 1_000 -> {
            val f = amount / 1_000f
            "$${(round(f * 10) / 10).toString().removeSuffix(".0")}K"
        }
        else -> "$$amount"
    }
}
@Composable
fun InfoCard(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.background)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.background, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }
}