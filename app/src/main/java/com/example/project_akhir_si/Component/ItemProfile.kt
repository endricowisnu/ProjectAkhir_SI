package com.example.project_akhir_si.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_akhir_si.R

@Composable
fun ItemProfile(storeName: String) {
    Card(
        modifier = Modifier
            .padding(16.dp, 60.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Hello, ",
                    fontSize = 20.sp
                )
                Text(
                    text = storeName,
                    fontSize = 30.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profilepicture1),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.viewmore),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ItemProfilePreview() {
    ItemProfile(storeName = "Toko Sinar Jaya")
}
