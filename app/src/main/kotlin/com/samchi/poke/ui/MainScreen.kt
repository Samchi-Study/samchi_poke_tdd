package com.samchi.poke.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samchi.feature.jungwon.presentation.JungwonRoute
import com.samchi.feature.sanghyeong.navigator.SangHyeongRoute
import com.samchi.feature.woosung.screen.list.WoosungRoute
import com.samchi.poke.common.ui.CustomSnackbarHostState
import com.samchi.poke.feature.jinkwang.JinKwangRoute
import com.samchi.poke.kanghwi.presentation.KanghwiRoute


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(CustomSnackbarHostState provides snackbarHostState) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomAppBar {
                    BottomNavigationBar(navController = navController)
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
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
                            NavigationItem.WooSung.route -> WoosungRoute()
                        }
                    }
                }
            }
        }
    }
}
