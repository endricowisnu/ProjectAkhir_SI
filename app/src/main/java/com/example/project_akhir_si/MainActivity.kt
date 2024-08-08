package com.example.project_akhir_si

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project_akhir_si.Component.ProductDetailScreen
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.example.project_akhir_si.data.DBase
import com.example.project_akhir_si.ui.*

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // Inisialisasi Firebase App Check dengan Play Integrity
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

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
                                    // Tangani keberhasilan, misalnya navigasi kembali atau tampilkan pesan
                                    navController.navigate(Routes.materialpage)
                                } else {
                                    // Tangani kegagalan, misalnya tampilkan pesan kesalahan
                                }
                            }
                        }
                    }
                    composable(Routes.manageproduct) {
                        ManageProduct(navController)
                    }
                    composable("${Routes.productdetailscreen}/{productId}",
                        arguments = listOf(navArgument("productId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")
                        ProductDetailScreen(navController = navController, productId = productId, db = db)
                    }
                }
            }
        }
    }
}
