package com.juegosparachicos

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

/**
 * Application class para inicializar componentes globales
 * Principalmente: AdMob configurado para contenido infantil
 */
class JuegosApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicializar AdMob
        MobileAds.initialize(this) { initializationStatus ->
            // Log de inicialización (opcional, para debugging)
            initializationStatus.adapterStatusMap.forEach { (adapter, status) ->
                println("AdMob Adapter: $adapter - Status: ${status.description}")
            }
        }

        // CRÍTICO: Configurar AdMob para contenido dirigido a niños
        // Esto cumple con COPPA y políticas de "Designed for Families"
        val requestConfiguration = RequestConfiguration.Builder()
            .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
            .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
            .build()

        MobileAds.setRequestConfiguration(requestConfiguration)
    }
}
