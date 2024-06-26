package com.example.project_akhir_si.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_akhir_si.R
import com.example.project_akhir_si.Routes

@Composable
fun NavBar(navController: NavController, modifier: Modifier =Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .height(64.dp)

    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavButton(iconResId = R.drawable.home, onClick = { navController.navigate(Routes.homescreen) })
            NavButton(iconResId = R.drawable.box,  onClick = { navController.navigate(Routes.materialpage) })
            NavButton(iconResId = R.drawable.profile, onClick = {navController.navigate(Routes.profilescreen)})
        }
    }
}

@Composable
fun NavButton(iconResId: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(100.dp, 60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
    }
}

