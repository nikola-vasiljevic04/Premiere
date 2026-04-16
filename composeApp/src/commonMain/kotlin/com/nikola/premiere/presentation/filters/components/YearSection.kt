package com.nikola.premiere.presentation.filters.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun YearSection(
    minYear: String,
    maxYear: String,
    onMinYearChange:(Int?)-> Unit,
    onMaxYearChange:(Int?)-> Unit
){
    Column {
        Text("YEAR RANGE", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            YearDropdown(
                selectedValue = minYear,
                label = "From",
                onYearSelected = onMinYearChange,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text("—", color = Color.Gray)
            Spacer(modifier = Modifier.width(16.dp))
            YearDropdown(
                selectedValue = maxYear,
                label = "To",
                onYearSelected = onMaxYearChange,
                modifier = Modifier.weight(1f)
            )

        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearDropdown(
    selectedValue: String,
    label: String,
    onYearSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val years = remember { (1900..2025).toList().reversed() }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, color = Color.Gray) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedTrailingIconColor = Color.White,
                unfocusedTrailingIconColor = Color.Gray,
                focusedBorderColor = Color.LightGray
            ),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Any year") },
                onClick = {
                    onYearSelected(null)
                    expanded = false
                }
            )
            years.forEach { year ->
                DropdownMenuItem(
                    text = { Text(year.toString()) },
                    onClick = {
                        onYearSelected(year)
                        expanded = false
                    }
                )
            }
        }
    }
}