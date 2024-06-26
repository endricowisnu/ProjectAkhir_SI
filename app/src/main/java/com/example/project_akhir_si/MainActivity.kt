package com.example.project_akhir_si

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.project_akhir_si.data.DBase
import com.example.project_akhir_si.ui.*

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val db = FirebaseFirestore.getInstance()

        setContent {
            val navController = rememberNavController()
            val dbase = DBase()

            Surface(color = MaterialTheme.colorScheme.background) {
                NavHost(navController = navController, startDestination = Routes.loginPage) {
                    composable(Routes.loginPage) {
                        LoginPage(navController)
                    }
                    composable(Routes.signupPage) {
                        SignupPage(navController)
                    }

                    composable(Routes.forgotPassword) {
                        ForgetPasswordPage(navController)
                    }

                    composable(Routes.homescreen) {
                        HomeScreen(navController)
                    }
                    composable(Routes.materialpage) {
                        MaterialPage(navController = navController, db = db)
                    }
                    composable(Routes.profilescreen) {
                        ProfileScreen(navController)
                    }
                    composable(Routes.addproductscreen) {
                        AddProductScreen(navController = navController) { product ->
                            dbase.saveProductsToFirestore(product) { success ->
                                if (success) {
                                    // Handle success, e.g., navigate back or show a message
                                    navController.navigate(Routes.materialpage)
                                } else {
                                    // Handle failure, e.g., show an error message
                                }
                            }
                        }
                    }
                    composable(Routes.manageproduct) {
                        ManageProduct(navController)
                    }
                    composable(Routes.detailscreenproductin) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")
                        DetailScreen(navController = navController, productId = productId, db = db)
                    }
                }
            }
        }
    }
}
