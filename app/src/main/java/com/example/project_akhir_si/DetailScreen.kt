package com.example.project_akhir_si

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_akhir_si.Component.NavBar
import com.example.project_akhir_si.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun DetailScreen(navController: NavController, productId: String?, db: FirebaseFirestore) {
    var product by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(productId) {
        if (!productId.isNullOrBlank()) {
            try {
                val documentSnapshot = db.collection("products").document(productId).get().await()
                if (documentSnapshot.exists()) {
                    product = documentSnapshot.toObject(Product::class.java)
                } else {
                    println("Document does not exist for ID: $productId")
                }
            } catch (e: Exception) {
                println("Error getting product details: $e")
                // Handle error gracefully, e.g., show a toast or error message
            }
        }
    }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            product?.let {
                Text(text = "Detail Produk")
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Nama Produk: ${it.name}")
                Text(text = "Harga: ${it.price}")
                Text(text = "Jumlah Tersedia: ${it.amountOfProduct}")

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* TODO: Implement edit functionality */ },
                    colors = ButtonDefaults.buttonColors(

                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Edit Produk")


                }
            }
        }
    }
