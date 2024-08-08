package com.example.project_akhir_si.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun HamburgerMenu(navController: NavController, drawerState: DrawerState) {
    //val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = "https://www.example.com/user-avatar.png",
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.Gray, CircleShape)
                        .padding(2.dp)
                        .background(Color.White, CircleShape)
                        .padding(2.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Hello, User!", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Auto Manager",
            modifier = Modifier.padding(16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Divider()
        DrawerItem("HomeScreen", "Home", Icons.Default.Home, navController, drawerState)
        DrawerItem("MaterialPage", "Material", Icons.Default.List, navController, drawerState)
        DrawerItem("ProfileScreen", "Profile", Icons.Default.Person, navController, drawerState)
    }
}

@Composable
fun DrawerItem(
    route: String,
    label: String,
    icon: ImageVector,
    navController: NavController,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    ListItem(
        headlineContent = { Text(label) },
        leadingContent = { Icon(icon, contentDescription = label) },
        modifier = Modifier
            .clickable {
                scope.launch {
                    drawerState.close()
                    navController.navigate(route)
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
