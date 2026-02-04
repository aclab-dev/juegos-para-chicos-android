package com.juegosparachicos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juegosparachicos.ui.screens.splash.SplashScreen
import com.juegosparachicos.ui.screens.menu.MenuScreen
import com.juegosparachicos.ui.screens.memory.MemoryGameScreen
import com.juegosparachicos.ui.screens.paint.PaintScreen
import com.juegosparachicos.ui.screens.colors.ColorsScreen

/**
 * NavGraph central de la aplicación
 * Define todas las rutas y transiciones entre pantallas
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onShowInterstitial: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        // Pantalla de inicio (Splash)
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToMenu = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Menú principal
        composable(Screen.Menu.route) {
            MenuScreen(
                onNavigateToMemory = {
                    navController.navigate(Screen.Memory.route)
                },
                onNavigateToPaint = {
                    navController.navigate(Screen.Paint.route)
                },
                onNavigateToColors = {
                    navController.navigate(Screen.Colors.route)
                }
            )
        }

        // Juego de Memoria
        composable(Screen.Memory.route) {
            MemoryGameScreen(
                onNavigateBack = {
                    onShowInterstitial()
                    navController.popBackStack()
                }
            )
        }

        // Juego de Pintar
        composable(Screen.Paint.route) {
            PaintScreen(
                onNavigateBack = {
                    onShowInterstitial()
                    navController.popBackStack()
                }
            )
        }

        // Juego de Colores y Formas
        composable(Screen.Colors.route) {
            ColorsScreen(
                onNavigateBack = {
                    onShowInterstitial()
                    navController.popBackStack()
                }
            )
        }
    }
}
