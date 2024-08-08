package com.example.project_akhir_si.data

import android.content.ClipDescription
import com.google.firebase.Timestamp

data class Product(
    val id: String = "",
    val name: String = "",
    var amountOfProduct: Int = 0,
    val imageUrl: String = "",
    val price: Int = 0,
    val description: String = "",
    val userId: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val dateProduct: String = ""
)
