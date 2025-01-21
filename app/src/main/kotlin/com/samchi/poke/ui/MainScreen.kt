package com.samchi.poke.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samchi.feature.woosung.screen.list.WoosungRoute
import com.samchi.poke.feature.jinkwang.JinKwangRoute
import com.samchi.poke.kanghwi.KanghwiRoute


@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.WooSung.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            NavigationItem.entries.forEach {
                composable(
                    it.route
                ) {
                    when (it.destination.route) {
                        NavigationItem.JinKwang.route -> JinKwangRoute()
                        NavigationItem.JungWon.route -> Unit
                        NavigationItem.KangHwi.route -> KanghwiRoute()
                        NavigationItem.SangHyeong.route -> Unit
                        NavigationItem.WooSung.route -> WoosungRoute()
                    }
                }
            }
        }
    }
}
