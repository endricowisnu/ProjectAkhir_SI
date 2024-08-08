package com.example.project_akhir_si

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.project_akhir_si.Component.HamburgerMenu
import com.example.project_akhir_si.Component.NavBar
import com.example.project_akhir_si.Component.SearchBar
import com.example.project_akhir_si.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MaterialPage(navController: NavController, db: FirebaseFirestore) {
    val products = remember { mutableStateOf(emptyList<Product>()) }
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }

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

        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<Boolean>("productAdded")?.observeForever { productAdded ->
            if (productAdded) {
                showSnackbar = true
                savedStateHandle.set("productAdded", false)
            }
        }
    }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(
                message = "Product successfully added",
                duration = SnackbarDuration.Short
            )
            showSnackbar = false
        }
    }

    var drawerState = rememberDrawerState(DrawerValue.Closed)
    var scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState= drawerState,
        drawerContent = {
            ModalDrawerSheet {
                HamburgerMenu(navController, drawerState)
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Auto Manager") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                NavBar(navController, modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp))
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                        onClick = { navController.navigate(Routes.manageproduct) },
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
                        ProductCard(navController = navController, product = product)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

}

@Composable
fun ProductCard(navController: NavController, product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clickable {
                Log.d("Navigation", "Navigating to productdetailscreen/${product.id}")
                navController.navigate("productdetailscreen/${product.id}")
            },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (product.imageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(product.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(end = 16.dp)
                )
            }
            Column {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Rp${product.price}",
                    color = Color.Red,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 16.dp))
                Text(
                    text = "Stok: ${product.amountOfProduct}")
            }
        }
    }
}
