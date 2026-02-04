package com.juegosparachicos.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * Gestor centralizado de anuncios AdMob
 *
 * POLÍTICAS IMPLEMENTADAS:
 * - Banners: solo en menú principal y pantalla final de memoria
 * - Interstitial: solo al volver al menú, máximo 1 cada 3 retornos
 * - Todos los anuncios marcados como child-directed (configurado en Application)
 *
 * IDs DE PRUEBA (reemplazar antes de publicar):
 * - Banner: ca-app-pub-3940256099942544/6300978111
 * - Interstitial: ca-app-pub-3940256099942544/1033173712
 */
class AdManager(private val context: Context) {

    companion object {
        private const val TAG = "AdManager"

        // IDs de prueba de Google AdMob
        const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
        const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

        // Frecuencia de interstitial: 1 cada 3 retornos
        private const val INTERSTITIAL_FREQUENCY = 3

        // DataStore para persistir contador
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ads_prefs")
        private val MENU_RETURN_COUNT_KEY = intPreferencesKey("menu_return_count")
    }

    private var interstitialAd: InterstitialAd? = null
    private var isLoadingInterstitial = false

    init {
        // Pre-cargar el primer interstitial
        loadInterstitialAd()
    }

    /**
     * Carga un anuncio interstitial en background
     * Se carga anticipadamente para estar listo cuando se necesite
     */
    private fun loadInterstitialAd() {
        if (isLoadingInterstitial || interstitialAd != null) {
            Log.d(TAG, "Ya hay un interstitial cargado o cargando")
            return
        }

        isLoadingInterstitial = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Interstitial cargado correctamente")
                    interstitialAd = ad
                    isLoadingInterstitial = false

                    // Configurar callback para cuando se cierre
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log.d(TAG, "Interstitial cerrado")
                            interstitialAd = null
                            // Pre-cargar el siguiente
                            loadInterstitialAd()
                        }

                        override fun onAdFailedToShowFullScreenContent(error: AdError) {
                            Log.e(TAG, "Error al mostrar interstitial: ${error.message}")
                            interstitialAd = null
                            loadInterstitialAd()
                        }
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "Error al cargar interstitial: ${error.message}")
                    interstitialAd = null
                    isLoadingInterstitial = false
                }
            }
        )
    }

    /**
     * Intenta mostrar un interstitial al volver al menú
     * Respeta la frecuencia de 1 cada 3 retornos
     */
    suspend fun tryShowInterstitialOnMenuReturn(activity: Activity) {
        val currentCount = getMenuReturnCount()
        val newCount = currentCount + 1

        Log.d(TAG, "Retorno al menú #$newCount")

        // Guardar nuevo contador
        incrementMenuReturnCount()

        // Verificar si toca mostrar anuncio
        if (newCount % INTERSTITIAL_FREQUENCY == 0) {
            Log.d(TAG, "Mostrando interstitial (cada $INTERSTITIAL_FREQUENCY retornos)")
            showInterstitialIfReady(activity)
        } else {
            Log.d(TAG, "No mostrar interstitial todavía (falta ${INTERSTITIAL_FREQUENCY - (newCount % INTERSTITIAL_FREQUENCY)} retornos)")
        }
    }

    /**
     * Muestra el interstitial si está listo
     */
    private fun showInterstitialIfReady(activity: Activity) {
        interstitialAd?.let { ad ->
            ad.show(activity)
            Log.d(TAG, "Interstitial mostrado")
        } ?: run {
            Log.w(TAG, "Interstitial no está listo, cargando...")
            loadInterstitialAd()
        }
    }

    /**
     * Obtiene el contador actual de retornos al menú
     */
    private suspend fun getMenuReturnCount(): Int {
        return context.dataStore.data.map { preferences ->
            preferences[MENU_RETURN_COUNT_KEY] ?: 0
        }.first()
    }

    /**
     * Incrementa el contador de retornos al menú
     */
    private suspend fun incrementMenuReturnCount() {
        context.dataStore.edit { preferences ->
            val current = preferences[MENU_RETURN_COUNT_KEY] ?: 0
            preferences[MENU_RETURN_COUNT_KEY] = current + 1
        }
    }
}

/**
 * Composable para mostrar un banner de AdMob
 * Uso: colocar en la parte inferior del menú o pantallas específicas
 */
@Composable
fun AdBanner() {
    val context = LocalContext.current

    val adView = remember {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = AdManager.BANNER_AD_UNIT_ID
            loadAd(AdRequest.Builder().build())
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            adView.destroy()
        }
    }

    AndroidView(
        factory = { adView }
    )
}
