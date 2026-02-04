package com.juegosparachicos.navigation

/**
 * Sealed class que define todas las pantallas de la app
 * Facilita la navegación type-safe con Compose Navigation
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Menu : Screen("menu")
    object Memory : Screen("memory")
    object Paint : Screen("paint")
    object Colors : Screen("colors")
}
