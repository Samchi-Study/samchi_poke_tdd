package com.samchi.poke.ui

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = NavigationItem.entries
    var selectedItem by remember { mutableIntStateOf(0) }
    var currentRoute by remember { mutableStateOf(NavigationItem.JinKwang) }

    items.forEachIndexed { index, navigationItem ->
        if (navigationItem == currentRoute) {
            selectedItem = index
        }
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = {  },
                label = { Text(item.name) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    currentRoute = item
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}