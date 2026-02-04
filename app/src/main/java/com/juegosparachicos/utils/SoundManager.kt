package com.juegosparachicos.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log

/**
 * Gestor de sonidos para la aplicación
 * Maneja todos los efectos de sonido de los juegos
 *
 * IMPORTANTE: Los archivos de audio deben estar en app/src/main/res/raw/
 * Formatos recomendados: OGG o MP3 (OGG es más eficiente)
 * Duración recomendada: 0.5 - 2 segundos para efectos
 */
class SoundManager(private val context: Context) {

    companion object {
        private const val TAG = "SoundManager"
        private const val MAX_STREAMS = 3
    }

    private var soundPool: SoundPool? = null
    private val soundIds = mutableMapOf<SoundType, Int>()
    private var isInitialized = false

    /**
     * Tipos de sonidos disponibles en la app
     */
    enum class SoundType {
        TAP,        // Sonido de tap/click en botones e interacciones
        SUCCESS,    // Sonido de acción correcta o acierto
        END         // Sonido de finalización de juego
    }

    init {
        initializeSoundPool()
        loadSounds()
    }

    /**
     * Inicializa el SoundPool con configuración optimizada para niños
     */
    private fun initializeSoundPool() {
        try {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(MAX_STREAMS)
                .setAudioAttributes(audioAttributes)
                .build()

            isInitialized = true
            Log.d(TAG, "SoundPool inicializado correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error al inicializar SoundPool: ${e.message}")
            isInitialized = false
        }
    }

    /**
     * Carga todos los sonidos desde recursos
     *
     * Los archivos de audio deben estar en app/src/main/res/raw/
     * - tap.mp3: Sonido de tap/click
     * - success.mp3: Sonido de acierto
     * - end.mp3: Sonido de finalización
     *
     * Si algún archivo no existe, se registra el error pero la app sigue funcionando.
     */
    private fun loadSounds() {
        if (!isInitialized || soundPool == null) {
            Log.w(TAG, "SoundPool no inicializado, saltando carga de sonidos")
            return
        }

        try {
            // Obtener el ID del recurso de forma dinámica
            val tapId = context.resources.getIdentifier("tap", "raw", context.packageName)
            val successId = context.resources.getIdentifier("success", "raw", context.packageName)
            val endId = context.resources.getIdentifier("end", "raw", context.packageName)

            // Cargar sonidos solo si los recursos existen
            if (tapId != 0) {
                soundIds[SoundType.TAP] = soundPool!!.load(context, tapId, 1)
            } else {
                Log.w(TAG, "Archivo tap.mp3 no encontrado en res/raw/")
            }

            if (successId != 0) {
                soundIds[SoundType.SUCCESS] = soundPool!!.load(context, successId, 1)
            } else {
                Log.w(TAG, "Archivo success.mp3 no encontrado en res/raw/")
            }

            if (endId != 0) {
                soundIds[SoundType.END] = soundPool!!.load(context, endId, 1)
            } else {
                Log.w(TAG, "Archivo end.mp3 no encontrado en res/raw/")
            }

            Log.d(TAG, "Sonidos cargados: ${soundIds.size}/3")
        } catch (e: Exception) {
            Log.e(TAG, "Error al cargar sonidos: ${e.message}")
        }
    }

    /**
     * Reproduce un sonido
     *
     * @param soundType Tipo de sonido a reproducir
     * @param volume Volumen (0.0 a 1.0), por defecto 1.0
     */
    fun playSound(soundType: SoundType, volume: Float = 1.0f) {
        if (!isInitialized || soundPool == null) {
            Log.w(TAG, "SoundPool no disponible")
            return
        }

        val soundId = soundIds[soundType]
        if (soundId == null) {
            Log.w(TAG, "Sonido no cargado: $soundType")
            return
        }

        try {
            soundPool?.play(
                soundId,
                volume,
                volume,
                1,
                0,
                1.0f
            )
            Log.d(TAG, "Reproduciendo sonido: $soundType")
        } catch (e: Exception) {
            Log.e(TAG, "Error al reproducir sonido: ${e.message}")
        }
    }

    /**
     * Libera recursos del SoundPool
     * Llamar cuando la app se cierre o el gestor ya no se necesite
     */
    fun release() {
        try {
            soundPool?.release()
            soundPool = null
            soundIds.clear()
            isInitialized = false
            Log.d(TAG, "SoundManager liberado")
        } catch (e: Exception) {
            Log.e(TAG, "Error al liberar SoundManager: ${e.message}")
        }
    }
}

/**
 * Ejemplo de uso del SoundManager en Composables:
 *
 * val context = LocalContext.current
 * val soundManager = remember { SoundManager(context) }
 * DisposableEffect(Unit) {
 *     onDispose { soundManager.release() }
 * }
 *
 * // En un botón o interacción
 * Button(onClick = {
 *     soundManager.playSound(SoundManager.SoundType.TAP)
 * }) { Text("Jugar") }
 *
 * // Al acertar
 * soundManager.playSound(SoundManager.SoundType.SUCCESS)
 *
 * // Al finalizar juego
 * soundManager.playSound(SoundManager.SoundType.END)
 */
