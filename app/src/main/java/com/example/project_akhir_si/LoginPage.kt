package com.example.project_akhir_si

import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5AB2FF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Auto Manager", fontSize = 34.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "login page",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Make Your Business Easier", fontSize = 12.sp, textAlign = TextAlign.End)

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.textFieldColors(),
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email address") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.textFieldColors(),
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF396EB0),
                contentColor = Color.White
            ),
            onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            message = "Sign In Successful"
                            navController.navigate(Routes.homescreen) { // Navigate to home screen
                                popUpTo(Routes.loginPage) { inclusive = true }
                            }
                        } else {
                            message = "Sign In Failed: ${task.exception?.message}"
                        }
                    }
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Sign In")
        }
        Text(text = message)

        Spacer(modifier = Modifier.height(2.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF396EB0),
                contentColor = Color.White
            ),
            onClick = { navController.navigate(Routes.signupPage) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(text = "Sign Up")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Forgot password?",
            modifier = Modifier.clickable { navController.navigate(Routes.forgotPassword) }
        )
    }
}




