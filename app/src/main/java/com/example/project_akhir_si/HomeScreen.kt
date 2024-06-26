package com.example.project_akhir_si

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_akhir_si.Component.ButtonStock
import com.example.project_akhir_si.Component.ItemProfile
import com.example.project_akhir_si.Component.NavBar
import com.example.project_akhir_si.Component.StockProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser
    var storeName by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        if (user != null) {
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        storeName = document.getString("storeName") ?: ""
                    }
                }
        }
    }

    Scaffold(
        bottomBar = { NavBar(navController, modifier = Modifier.fillMaxWidth().height(56.dp)) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF5AB2FF))
        ) {
            ItemProfile(storeName = storeName)
            StockProfile()
            ButtonStock(navController)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
