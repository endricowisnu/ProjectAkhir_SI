package com.example.project_akhir_si.Component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateDisplay(modifier: Modifier = Modifier) {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH)
    val formattedDate = currentDate.format(formatter)

    Text(
        text = formattedDate,
        style = MaterialTheme.typography.displaySmall,
        color = Color.Black,
        modifier = modifier.padding(start = 16.dp, top = 8.dp)
    )
}
