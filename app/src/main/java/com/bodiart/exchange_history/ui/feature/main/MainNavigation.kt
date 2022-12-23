package com.bodiart.exchange_history.ui.feature.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bodiart.exchange_history.ui.feature.calculator.CalculatorScreen
import com.bodiart.exchange_history.ui.feature.history.HistoryScreen
import com.bodiart.exchange_history.ui.feature.home.HomeScreen


/**
 * Fast navigation implementation
 */

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationNode.Home.route) {
        composable(NavigationNode.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            NavigationNode.Calculator.buildRouteTemplate(),
            arguments = listOf(navArgument(NavigationNode.Calculator.argDate) { type = NavType.StringType })
        ) {
            CalculatorScreen(navController = navController)
        }
        composable(NavigationNode.History.route) {
            HistoryScreen()
        }
    }
}

sealed class NavigationNode(val route: String) {

    object Home : NavigationNode("home")

    object Calculator : NavigationNode("calculator") {
        const val argDate = "argDate"

        fun buildRoute(date: String) = "$route/$date"

        fun buildRouteTemplate() = "$route/{$argDate}"
    }

    object History : NavigationNode("history")
}