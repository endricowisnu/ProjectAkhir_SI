package com.example.project_akhir_si

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.project_akhir_si.Component.NavBar
import com.example.project_akhir_si.Component.SearchBar
import com.example.project_akhir_si.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MaterialPage(navController: NavController, db: FirebaseFirestore) {
    val products = remember { mutableStateOf(emptyList<Product>()) }
    var searchQuery by remember { mutableStateOf("") }

    // Function to fetch products from the database
    val fetchProducts = {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        db.collection("users").document(userId).collection("products")
            .get()
            .addOnSuccessListener { result ->
                products.value = result.toObjects(Product::class.java)
            }
            .addOnFailureListener { exception ->
                println("Error fetching products: ${exception.message}")
            }
    }

    // Fetch products initially and whenever we come back to this screen
    LaunchedEffect(Unit) {
        fetchProducts()
    }

    Scaffold(
        bottomBar = {
            NavBar(navController, modifier = Modifier
                .fillMaxWidth()
                .height(25.dp))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF5AB2FF)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(onSearchQueryChange = { query ->
                searchQuery = query
            })
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate(Routes.addproductscreen) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF396EB0),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .size(80.dp, 50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = "Add Stuff",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF396EB0),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .size(80.dp, 50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.more),
                        contentDescription = "More",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Filter products based on search query
            val filteredProducts = products.value.filter { product ->
                product.name.contains(searchQuery, ignoreCase = true)
            }

            // Display the list of products
            LazyColumn {
                items(filteredProducts) { product ->
                    ProductCard(product = product)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (product.imageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(product.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.name, fontWeight = FontWeight.Bold)
            Text(text = "Amount: ${product.amountOfProduct}")
            Text(text = "Price: ${product.price}")
        }
    }
}
