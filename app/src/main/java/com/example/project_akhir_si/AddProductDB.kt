package com.example.project_akhir_si

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.project_akhir_si.data.DBase
import com.example.project_akhir_si.data.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AddProduct(navController: NavController) {
    val dBase = DBase()
    val currentUser = Firebase.auth.currentUser
    val userId = currentUser?.uid ?: ""

    AddProductScreen(navController = navController) { product ->
        val productWithUserId = product.copy(userId = userId)
        dBase.saveProductsToFirestore(productWithUserId) { result ->
            if (result) {
                navController.popBackStack()
            } else {
                // Handle failure, e.g., show an error message
            }
        }
    }
}
