package com.example.project_akhir_si.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.project_akhir_si.data.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ItemMaterialUpdate(product: Product, db: FirebaseFirestore) {
    var amountIn by remember { mutableStateOf("") }
    var amountOut by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = "Detail Produk", style = MaterialTheme.typography.titleLarge)
            Text(text = "Nama Produk: ${product.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Harga: ${product.price}", style = MaterialTheme.typography.bodyLarge)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Produk Masuk: ")
                OutlinedTextField(
                    value = amountIn,
                    onValueChange = { amountIn = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF396EB0),
                        contentColor = Color.White
                    ),
                    onClick = {
                        if (amountIn.isNotBlank()) {
                            val newAmount = product.amountOfProduct + amountIn.toInt()
                            updateProductAmount(product.id, newAmount, db, snackbarHostState, coroutineScope)
                        }
                    }
                ) {
                    Text("Tambah")
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Produk Keluar: ")
                OutlinedTextField(
                    value = amountOut,
                    onValueChange = { amountOut = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF396EB0),
                        contentColor = Color.White
                    ),
                    onClick = {
                        if (amountOut.isNotBlank()) {
                            val amountOutInt = amountOut.toIntOrNull() // Safely parse the input to an Int
                            if (amountOutInt != null) {
                                val newAmount = product.amountOfProduct - amountOutInt
                                if (newAmount > 0) {
                                    if (amountOutInt > product.amountOfProduct) {
                                        updateProductAmount(product.id, newAmount, db, snackbarHostState, coroutineScope)
                                    } else {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Jumlah produk tidak boleh kurang dari 0!")
                                        }
                                    }
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Jumlah produk tidak boleh lebih dari jumlah produk tersedia!")
                                    }
                                }
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Input tidak valid!")
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Input tidak boleh kosong!")
                            }
                        }
                    }

                ) {
                    Text("Kurang")
                }
            }

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF396EB0),
                    contentColor = Color.White
                ),
                onClick = {
                    deleteProduct(product.id, db, snackbarHostState, coroutineScope)
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            ) {
                Text("Hapus")
            }
        }
    }
    SnackbarHost(hostState = snackbarHostState)
}

private fun updateProductAmount(
    productId: String,
    newAmount: Int,
    db: FirebaseFirestore,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    db.collection("users").document(Firebase.auth.currentUser!!.uid).collection("products").document(productId)
        .update("amountOfProduct", newAmount)
        .addOnSuccessListener {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Jumlah produk berhasil diperbarui!")
            }
        }
        .addOnFailureListener { e ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Error updating product amount: $e")
            }
        }
}

private fun deleteProduct(
    productId: String,
    db: FirebaseFirestore,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    db.collection("users").document(Firebase.auth.currentUser!!.uid).collection("products").document(productId)
        .delete()
        .addOnSuccessListener {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Produk berhasil dihapus!")
            }
        }
        .addOnFailureListener { e ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Error deleting product: $e")
            }
        }
}
