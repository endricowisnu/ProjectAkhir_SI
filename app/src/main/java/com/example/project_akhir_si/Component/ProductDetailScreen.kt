package com.example.project_akhir_si.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.project_akhir_si.data.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController, productId: String?, db: FirebaseFirestore) {
    val product = remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(productId) {
        productId?.let {
            val userId = Firebase.auth.currentUser?.uid ?: ""
            db.collection("users").document(userId)
                .collection("products").document(it).get()
                .addOnSuccessListener { document ->
                    product.value = document.toObject(Product::class.java)
                }
                .addOnFailureListener { exception ->
                    println("Error fetching product: ${exception.message}")
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("MaterialPage") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            product.value?.let { product ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (product.imageUrl.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(product.imageUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.Gray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier
                        .fillMaxWidth(),

                    ){
                        Text(
                            text = "Quantity",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = product.amountOfProduct.toString(),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier
                        .fillMaxWidth(),
                    ){
                        Text(
                            text = "Price",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Rp." + product.price.toString() + ",00",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier
                        .fillMaxWidth()
                    ) {
                        Text(
                            text = "Description ",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = product.description.toString(),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                        )
                    }
                }
            } ?: Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
