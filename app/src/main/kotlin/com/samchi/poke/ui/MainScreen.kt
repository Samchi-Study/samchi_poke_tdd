package com.samchi.poke.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samchi.feature.jungwon.presentation.JungwonRoute
import com.samchi.feature.sanghyeong.navigator.SangHyeongRoute
import com.samchi.feature.woosung.naviagtion.WoosungRoute
import com.samchi.feature.woosung.naviagtion.addNestedLoginGraph
import com.samchi.poke.feature.jinkwang.JinKwangRoute
import com.samchi.poke.kanghwi.presentation.KanghwiRoute


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
            startDestination = NavigationItem.JinKwang.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            NavigationItem.entries.forEach {
                composable(
                    it.route
                ) {
                    when (it.destination.route) {
                        NavigationItem.JinKwang.route -> JinKwangRoute()
                        NavigationItem.JungWon.route -> JungwonRoute()
                        NavigationItem.KangHwi.route -> KanghwiRoute()
                        NavigationItem.SangHyeong.route -> SangHyeongRoute()
                    }
                }
            }


            addNestedLoginGraph(
                pokemonClicked = {
                    navController.navigate(WoosungRoute.Detail(it))
                },
                popBackStack = {
                navController.getBackStackEntry<WoosungRoute.List>().savedStateHandle["pokemon"]  = it
                navController.popBackStack()
                }
            )
        }
    }
}
