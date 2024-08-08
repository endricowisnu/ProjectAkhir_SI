package com.example.project_akhir_si

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.project_akhir_si.Component.NavBar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var storeName by remember { mutableStateOf("") }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val auth = Firebase.auth
    val db = Firebase.firestore
    val userId = auth.currentUser?.uid

    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        name = document.getString("name") ?: ""
                        email = document.getString("email") ?: ""
                        storeName = document.getString("storeName") ?: ""
                        profileImageUrl = document.getString("profileImageUrl")
                    }
                }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> profileImageUri = uri }
    )

    val context = LocalContext.current

    // Function to handle sign out
    fun handleSignOut(context: Context) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        navController.navigate(Routes.loginPage) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    // Function to handle save profile
    fun handleSaveProfile() {
        val user = auth.currentUser
        if (user != null) {
            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "storeName" to storeName,
                "profileImageUrl" to profileImageUrl
            )

            if (profileImageUri != null) {
                val storageRef = Firebase.storage.reference.child("profile_images/${user.uid}.jpg")
                Log.d("ProfileScreen", "Uploading image to: ${storageRef.path}")
                storageRef.putFile(profileImageUri!!)
                    .addOnSuccessListener { uploadTask ->
                        Log.d("ProfileScreen", "Image upload successful")
                        uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->
                            userData["profileImageUrl"] = uri.toString()
                            db.collection("users").document(user.uid).set(userData)
                                .addOnSuccessListener {
                                    Log.d("ProfileScreen", "Profile data saved successfully")

                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Berhasil mengubah data")
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Log.e("ProfileScreen", "Failed to save profile data", e)
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("ProfileScreen", "Failed to upload image", e)
                    }
            } else {
                db.collection("users").document(user.uid).set(userData)
                    .addOnSuccessListener {
                        Log.d("ProfileScreen", "Profile data saved successfully")
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Berhasil mengubah data")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("ProfileScreen", "Failed to save profile data", e)
                    }
            }
        }
    }

    Scaffold(
        bottomBar = { NavBar(navController, modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF5AB2FF))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null) {
                    Image(
                        painter = rememberImagePainter(profileImageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (profileImageUrl != null) {
                    Image(
                        painter = rememberImagePainter(profileImageUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Text(
                text = "Edit image",
                color = Color.Blue,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { imagePickerLauncher.launch("image/*") }
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.Black) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                readOnly = true
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name", color = Color.Black) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = storeName,
                onValueChange = { storeName = it },
                label = { Text("Store Name", color = Color.Black) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = { Text("Old Password", color = Color.Black) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password", color = Color.Black) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { handleSignOut(context) }) {
                    Text(text = "Log Out")
                }
                Button(onClick = { handleSaveProfile() }) {
                    Text(text = "Save Profile")
                }
            }
        }
    }
}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}
