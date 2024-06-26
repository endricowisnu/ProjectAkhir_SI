package com.example.project_akhir_si.Component

sealed class BottomNavItem(val route : String,val label: String) {
    object Homecreen :BottomNavItem("HomeScreen", "homescreen")
    object MaterialScreen :BottomNavItem("MaterialScreen", "materialscreen")
    object ProfileScreen :BottomNavItem("ProfileScreen", "profilescreen")

}