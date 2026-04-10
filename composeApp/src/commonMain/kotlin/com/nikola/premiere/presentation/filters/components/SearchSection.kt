package com.nikola.premiere.presentation.filters.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchSection(
    searchQuery: String,
    onQueryChange: (String) -> Unit
){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("SEARCH BY TITLE", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onQueryChange, // Šalje svako ukucano slovo nazad gore
            placeholder = { Text("e.g. Batman", color = Color.DarkGray) },

            // Lupa sa leve strane
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray
                )
            },

            // "X" dugme sa desne strane (pojavljuje se samo ako tekst nije prazan)
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) { // Klik na X šalje prazan string
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Search",
                            tint = Color.Gray
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true, // Sprečava da polje raste u više redova (kao Enter na tastaturi)
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = Color(0xFFE50914) // Netflix crveni kursor za lepši efekat
            )
        )
    }
}