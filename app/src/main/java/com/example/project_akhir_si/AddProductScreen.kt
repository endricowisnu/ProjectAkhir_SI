package com.example.project_akhir_si

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.project_akhir_si.data.Product
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import java.util.Date
import java.util.UUID

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController, onSubmit: (Product) -> Unit) {
    var productName by remember { mutableStateOf("") }
    var amountOfProduct by remember { mutableStateOf("") }
    var productImageUrl by remember { mutableStateOf<Uri?>(null) }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { url -> productImageUrl = url }
    )

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00BFFF))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Add Your Product",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.height(13.dp))
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                .clickable { imagePickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (productImageUrl != null) {
                                Image(
                                    painter = rememberImagePainter(productImageUrl),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add Image",
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(13.dp))
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Product Name", textAlign = TextAlign.Center) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(13.dp))
                OutlinedTextField(
                    value = amountOfProduct,
                    onValueChange = { amountOfProduct = it },
                    label = { Text("Amount of Product", textAlign = TextAlign.Center) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(13.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price", textAlign = TextAlign.Center) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(13.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description", textAlign = TextAlign.Center) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(13.dp))

                // Mengganti OutlinedTextField dengan Button untuk pemilihan tanggal
                Button(
                    onClick = { datePickerDialog.show() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = if (selectedDate.isNotEmpty()) selectedDate else "Select Date")
                }

                Spacer(modifier = Modifier.height(13.dp))

                if (showError) {
                    Text(
                        text = "Error: Please ensure all fields are filled out correctly and valid values are entered.",
                        color = Color.Red,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                if (uploadError) {
                    Text(
                        text = "Error: Failed to upload image.",
                        color = Color.Red,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(70.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.size(80.dp, 50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Button(
                        onClick = {
                            val amount = amountOfProduct.toIntOrNull()
                            val productPrice = price.toIntOrNull()

                            if (productName.isNotEmpty() && amount != null && productPrice != null && productImageUrl != null && selectedDate.isNotEmpty()) {
                                isUploading = true
                                val storageRef = FirebaseStorage.getInstance().reference.child("product_images/${UUID.randomUUID()}")
                                val uploadTask = storageRef.putFile(productImageUrl!!)

                                uploadTask.addOnSuccessListener {
                                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                                        val currentUser = Firebase.auth.currentUser
                                        val userId = currentUser?.uid ?: ""
                                        val product = Product(
                                            name = productName,
                                            amountOfProduct = amount,
                                            imageUrl = uri.toString(),
                                            price = productPrice,
                                            userId = userId,
                                            description = description,
                                            timestamp = Timestamp.now(),
                                            dateProduct = selectedDate
                                        )

                                        // Log the product details for debugging
                                        println("Adding product: $product")

                                        onSubmit(product)
                                        navController.previousBackStackEntry?.savedStateHandle?.set("productAdded", true)
                                        navController.popBackStack()
                                        isUploading = false
                                    }.addOnFailureListener {
                                        isUploading = false
                                        uploadError = true
                                    }
                                }.addOnFailureListener {
                                    isUploading = false
                                    uploadError = true
                                }
                            } else {
                                showError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.size(80.dp, 50.dp),
                        enabled = !isUploading
                    ) {
                        if (isUploading) {
                            CircularProgressIndicator(color = Color.White)
                        } else {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
