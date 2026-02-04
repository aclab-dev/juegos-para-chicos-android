# 📋 Quick Reference - Juegos para Chicos

Referencia rápida de comandos y configuraciones más usadas.

## 🚀 Comandos Gradle

```bash
# Compilar proyecto
./gradlew build

# Limpiar build
./gradlew clean

# Ejecutar en dispositivo conectado
./gradlew installDebug

# Generar APK debug
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk

# Generar APK release (sin firmar)
./gradlew assembleRelease

# Generar AAB firmado (para Play Store)
./gradlew bundleRelease
# Output: app/build/outputs/bundle/release/app-release.aab

# Ver dependencias
./gradlew app:dependencies
```

## 📱 IDs de AdMob

### IDs de Prueba (Actuales)
```kotlin
// Application ID
ca-app-pub-3940256099942544~3347511713

// Banner
ca-app-pub-3940256099942544/6300978111

// Interstitial
ca-app-pub-3940256099942544/1033173712
```

### Dónde Cambiarlos

**1. AndroidManifest.xml** (línea ~22)
```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="TU_APP_ID" />
```

**2. AdManager.kt** (líneas ~25-26)
```kotlin
const val BANNER_AD_UNIT_ID = "TU_BANNER_ID"
const val INTERSTITIAL_AD_UNIT_ID = "TU_INTERSTITIAL_ID"
```

## 🎨 Colores Principales

```kotlin
// Paleta principal (ui/theme/Color.kt)
Primary = Color(0xFFFF6B9D)      // Rosa
PrimaryDark = Color(0xFFE55A8A)  // Rosa oscuro
Accent = Color(0xFFFFD93D)       // Amarillo
Background = Color(0xFFFFF9E6)   // Beige claro

// Colores de pintura
PaintRed = Color(0xFFFF6B6B)
PaintBlue = Color(0xFF4ECDC4)
PaintYellow = Color(0xFFFFE66D)
PaintGreen = Color(0xFF95E1D3)
PaintPurple = Color(0xFFC77DFF)
PaintOrange = Color(0xFFA94D)
```

## 🔧 Configuraciones Importantes

### Versionado (app/build.gradle.kts)
```kotlin
versionCode = 1          // Incrementar con cada release
versionName = "1.0.0"    // Seguir semver: MAJOR.MINOR.PATCH
```

### Frecuencia de Interstitial (ads/AdManager.kt)
```kotlin
private const val INTERSTITIAL_FREQUENCY = 3  // 1 ad cada 3 retornos
```

### Min/Target SDK (app/build.gradle.kts)
```kotlin
minSdk = 24      // Android 7.0 (94% de dispositivos)
targetSdk = 34   // Android 14 (más reciente)
```

## 📂 Estructura de Archivos

```
juegos-para-chicos/
├── app/
│   ├── build.gradle.kts                 ← Config del módulo
│   └── src/main/
│       ├── AndroidManifest.xml          ← Permisos y config
│       ├── java/com/juegosparachicos/
│       │   ├── MainActivity.kt          ← Entry point
│       │   ├── JuegosApplication.kt     ← Inicializa AdMob
│       │   ├── ads/
│       │   │   └── AdManager.kt         ← Gestor anuncios
│       │   ├── navigation/
│       │   │   ├── Screen.kt            ← Rutas
│       │   │   └── NavGraph.kt          ← Navegación
│       │   ├── ui/
│       │   │   ├── theme/               ← Colores y tipografía
│       │   │   └── screens/             ← Pantallas de juegos
│       │   └── utils/
│       │       └── SoundManager.kt      ← Sonidos
│       └── res/
│           ├── values/                  ← Strings, colores
│           ├── raw/                     ← Audio files (vacío)
│           └── mipmap-*/                ← Íconos (placeholders)
├── build.gradle.kts                     ← Config raíz
├── settings.gradle.kts                  ← Módulos
├── gradle.properties                    ← Props de Gradle
└── [Docs]/                              ← README, guías, etc.
```

## 🎮 Navegación de Pantallas

```
Splash (2s)
   ↓
Menu Principal
   ├→ Memoria → [Selector Nivel] → [Juego] → [Completado] → Menu
   ├→ Pintar → [Canvas] → Menu
   └→ Colores y Formas → [Juego] → Menu
```

## 📝 Archivos de Texto Importantes

| Archivo | Propósito |
|---------|-----------|
| [START_HERE.md](START_HERE.md) | 🚀 Empieza por aquí |
| [README.md](README.md) | 📚 Documentación técnica completa |
| [PUBLISHING_GUIDE.md](PUBLISHING_GUIDE.md) | 🎯 Guía paso a paso para publicar |
| [ASSETS_GUIDE.md](ASSETS_GUIDE.md) | 🎨 Qué assets agregar y dónde |
| [PRIVACY_POLICY_TEMPLATE.md](PRIVACY_POLICY_TEMPLATE.md) | 📄 Política de privacidad |
| [CHANGELOG.md](CHANGELOG.md) | 📋 Control de versiones |
| [QUICK_REFERENCE.md](QUICK_REFERENCE.md) | ⚡ Este archivo |

## 🔊 Agregar Sonidos

### 1. Colocar archivos en:
```
app/src/main/res/raw/
├── button_click.ogg
├── success.ogg
├── card_flip.ogg
├── match_found.ogg
├── game_complete.ogg
├── correct_answer.ogg
└── incorrect_answer.ogg
```

### 2. Descomentar en SoundManager.kt (línea ~52):
```kotlin
soundIds[SoundType.BUTTON_CLICK] = soundPool!!.load(context, R.raw.button_click, 1)
// ... etc
```

### 3. Usar en código:
```kotlin
val soundManager = remember { SoundManager(LocalContext.current) }
DisposableEffect(Unit) {
    onDispose { soundManager.release() }
}

// Al hacer click:
soundManager.playSound(SoundManager.SoundType.SUCCESS)
```

## 🐛 Debugging

### Ver logs de la app:
```bash
# Android Studio: Logcat tab
# O por terminal:
adb logcat | grep "JuegosParaChicos\|AdManager\|SoundManager"
```

### Limpiar caché:
```bash
./gradlew clean
rm -rf .gradle
rm -rf app/build
# Luego: Build → Rebuild Project en Android Studio
```

### Reset emulador:
```bash
# En Android Studio:
# Tools → Device Manager → [Tu emulador] → ⋮ → Wipe Data
```

## 🎯 Testing Checklist

Antes de publicar, probar:

### Funcionalidad
- [ ] Splash screen se muestra 2 segundos
- [ ] Menú principal muestra 3 botones
- [ ] Banner aparece en menú
- [ ] Juego de Memoria funciona en ambos niveles
- [ ] Pareja encontrada muestra animación
- [ ] Juego completado muestra diálogo con banner
- [ ] Juego de Pintar dibuja correctamente
- [ ] Todos los 6 colores funcionan
- [ ] Botón borrar limpia el canvas
- [ ] Juego de Colores muestra instrucción
- [ ] Respuesta correcta celebra
- [ ] Genera nuevas rondas automáticamente
- [ ] Botón Home funciona en todos los juegos

### Anuncios
- [ ] Banner carga en menú principal
- [ ] Banner carga en pantalla de juego completado
- [ ] Interstitial aparece al volver al menú (cada 3 veces)
- [ ] No hay anuncios durante gameplay

### Performance
- [ ] Sin crashes
- [ ] Sin lags al dibujar
- [ ] Animaciones fluidas
- [ ] App funciona sin internet (excepto anuncios)

## 🚀 Publicar Update

```bash
# 1. Actualizar versión en app/build.gradle.kts
versionCode += 1
versionName = "1.0.1"

# 2. Documentar en CHANGELOG.md

# 3. Generar AAB firmado
./gradlew bundleRelease

# 4. Subir a Play Console
# → Production → Create new release
# → Upload app-release.aab
# → Add release notes
# → Review → Roll out
```

## 📞 Links Útiles

| Recurso | URL |
|---------|-----|
| Google Play Console | https://play.google.com/console |
| AdMob | https://admob.google.com |
| Android Developer Docs | https://developer.android.com |
| Jetpack Compose Docs | https://developer.android.com/jetpack/compose |
| Stack Overflow Android | https://stackoverflow.com/questions/tagged/android |
| Freesound (Audio gratis) | https://freesound.org |
| Android Asset Studio | https://romannurik.github.io/AndroidAssetStudio/ |

## 💡 Tips

### Desarrollo más rápido:
- Usar dispositivo real en vez de emulador
- Habilitar "Instant Run" en Android Studio
- Usar emulador con hardware acelerado (HAXM/KVM)

### Mejores prácticas:
- Commit frecuentes en Git
- Probar en múltiples dispositivos
- No usar IDs de prueba en producción
- Leer logs de crashes en Play Console

### Monetización:
- CPM (earnings per 1000 impressions) es bajo en apps infantiles
- No hacer click en tus propios anuncios (banean cuenta)
- Balancear monetización con experiencia de usuario
- Considerar versión premium sin ads en futuro

---

**Para más información, consultar el README.md completo.**
