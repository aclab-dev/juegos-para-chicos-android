package com.juegosparachicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.juegosparachicos.ads.AdManager
import com.juegosparachicos.navigation.NavGraph
import com.juegosparachicos.ui.theme.JuegosParaChicosTheme
import kotlinx.coroutines.launch

/**
 * Activity principal de la aplicación
 * Configura la navegación y el gestor de anuncios
 */
class MainActivity : ComponentActivity() {

    private lateinit var adManager: AdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar gestor de anuncios
        adManager = AdManager(applicationContext)

        setContent {
            JuegosParaChicosTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        onShowInterstitial = {
                            // Callback cuando se vuelve al menú desde un juego
                            coroutineScope.launch {
                                adManager.tryShowInterstitialOnMenuReturn(this@MainActivity)
                            }
                        }
                    )
                }
            }
        }
    }
}
