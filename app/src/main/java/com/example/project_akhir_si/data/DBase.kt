package com.example.project_akhir_si.data
import com.example.project_akhir_si.Component.ItemMaterialUpdate
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_akhir_si.Component.ItemMaterial
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class DBase {
    fun saveProductsToFirestore(product: Product, onResult: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser
        val userId = user?.uid ?: return

        val newDocRef = db.collection("users").document(userId).collection("products").document()
        val productWithId = product.copy(id = newDocRef.id)
        newDocRef.set(productWithId)
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot added with ID: ${newDocRef.id}")
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
                onResult(false)
            }
    }
}

@Composable
fun ListItem(db: FirebaseFirestore) {
    var products by remember { mutableStateOf(listOf<Product>()) }

    LaunchedEffect(Unit) {
        val user = Firebase.auth.currentUser
        val userId = user?.uid ?: return@LaunchedEffect

        db.collection("users").document(userId).collection("products")
            .get()
            .addOnSuccessListener { result ->
                products = result.toObjects(Product::class.java)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(products) { product ->
            ItemMaterial(product)
        }
    }
}

@Composable
fun ListItemUpdate(db: FirebaseFirestore, navController: NavController, searchQuery: String) {
    var products by remember { mutableStateOf(listOf<Product>()) }

    LaunchedEffect(Unit) {
        val user = Firebase.auth.currentUser
        val userId = user?.uid ?: return@LaunchedEffect

        try {
            val result = db.collection("users").document(userId).collection("products")
                .get()
                .await()
            products = result.toObjects(Product::class.java)
        } catch (e: Exception) {
            println("Error getting documents: $e")
        }
    }

    val filteredProducts = products.filter { product ->
        product.name.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(filteredProducts) { product ->
            ItemMaterialUpdate(product, db)
        }
    }
}
