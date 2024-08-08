package com.example.project_akhir_si.Component

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.project_akhir_si.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

@Composable
fun RecentItems(userId: String?) {
    val db = FirebaseFirestore.getInstance()
    var recentItems by remember { mutableStateOf(listOf<Product>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        if (userId != null) {
            Log.d("RecentItems", "Fetching data for userId: $userId")
            db.collection("users").document(userId).collection("products")
                .orderBy("timestamp", Query.Direction.DESCENDING) // Mengurutkan berdasarkan timestamp terbaru
                .limit(5)
                .get()
                .addOnSuccessListener { documents ->
                    val items = mutableListOf<Product>()
                    for (document in documents) {
                        val product = document.toObject<Product>()
                        items.add(product)
                    }
                    recentItems = items
                    isLoading = false
                    Log.d("RecentItems", "Fetched ${items.size} items")
                }
                .addOnFailureListener { exception ->
                    errorMessage = exception.message
                    isLoading = false
                    Log.e("RecentItems", "Error fetching data: $errorMessage")
                }
        } else {
            Log.e("RecentItems", "UserId is null")
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Recent Items", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        when {
            isLoading -> {
                Text("Loading...", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            errorMessage != null -> {
                Text("Error: $errorMessage", color = androidx.compose.ui.graphics.Color.Red)
            }
            else -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recentItems) { product ->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .width(150.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                AsyncImage(
                                    model = product.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .fillMaxSize()
                                )
                                Text(
                                    text = product.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    text = "Price: \$${product.price}",
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
