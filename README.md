# 🎮 Juegos para Chicos

Aplicación Android de juegos educativos para niños de 3 a 7 años, diseñada siguiendo las políticas de **Google Play "Designed for Families"**.

## 📱 Descripción

App gratuita con 3 minijuegos infantiles:
- **Memoria**: Encuentra las parejas de animales
- **Pintar**: Canvas de dibujo libre con 6 colores
- **Colores y Formas**: Aprende colores y formas geométricas

### Características Principales
- ✅ 100% offline (sin necesidad de internet)
- ✅ Sin login ni registro
- ✅ Sin recopilación de datos personales
- ✅ Monetización con anuncios child-directed (AdMob)
- ✅ Interfaz simple y colorida
- ✅ Sin contenido inapropiado
- ✅ Cumple políticas COPPA y "Designed for Families"

## 📊 Estado del Proyecto

**MVP Funcional** - En etapa de pre-publicación

Este proyecto es un MVP (Minimum Viable Product) completamente funcional, listo para testing y optimización. Los tres minijuegos están implementados con:
- ✅ Renderizado en tiempo real optimizado
- ✅ Sistema de sonidos integrado (TAP, SUCCESS, END)
- ✅ Feedback visual apropiado para niños de 3-6 años
- ✅ UX sin frustración (sin sonidos negativos)
- ✅ Código limpio y mantenible

**Próximos pasos**:
- Agregar assets de audio (ver [ASSETS_GUIDE.md](ASSETS_GUIDE.md))
- Configurar AdMob con IDs de producción
- Crear política de privacidad
- Testing en dispositivos reales
- Publicación en Google Play Store

## 🛠️ Stack Tecnológico

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Navegación**: Navigation Compose
- **Arquitectura**: MVVM simplificado / State Hoisting
- **Persistencia**: DataStore Preferences
- **Monetización**: Google AdMob
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## 📂 Estructura del Proyecto

```
app/src/main/java/com/juegosparachicos/
├── JuegosApplication.kt          # Application class (inicializa AdMob)
├── MainActivity.kt                # Activity principal
├── ads/
│   └── AdManager.kt              # Gestor de anuncios (banners e interstitials)
├── navigation/
│   ├── Screen.kt                 # Definición de rutas
│   └── NavGraph.kt               # Configuración de navegación
├── ui/
│   ├── theme/                    # Colores, tipografía y tema
│   └── screens/
│       ├── splash/               # Pantalla de inicio
│       ├── menu/                 # Menú principal
│       ├── memory/               # Juego de Memoria
│       ├── paint/                # Juego de Pintar
│       └── colors/               # Juego de Colores y Formas
└── utils/
    └── SoundManager.kt           # Gestor de efectos de sonido
```

## 🎮 Minijuegos Detallados

### 1️⃣ Juego de Memoria

**Mecánica**:
- Dos niveles: Fácil (2x2) y Medio (3x2)
- Encuentra parejas de animales volteando cartas
- Sin tiempo límite, sin penalizaciones
- Celebración al completar con banner publicitario

**Archivos**:
- [MemoryGameScreen.kt](app/src/main/java/com/juegosparachicos/ui/screens/memory/MemoryGameScreen.kt)

### 2️⃣ Juego de Pintar

**Mecánica**:
- Canvas de dibujo libre
- Paleta de 6 colores grandes
- Botón para borrar todo
- Sin guardar dibujos

**Archivos**:
- [PaintScreen.kt](app/src/main/java/com/juegosparachicos/ui/screens/paint/PaintScreen.kt)

### 3️⃣ Juego de Colores y Formas

**Mecánica**:
- Instrucción: "Tocá el [forma] [color]"
- 3 opciones visuales
- Feedback positivo en respuestas correctas
- Feedback suave (sin alarmas) en respuestas incorrectas

**Archivos**:
- [ColorsScreen.kt](app/src/main/java/com/juegosparachicos/ui/screens/colors/ColorsScreen.kt)

## 💰 Monetización

### Configuración de Anuncios

La app usa **AdMob** con configuración específica para niños:

- `setTagForChildDirectedTreatment(TRUE)`
- `setMaxAdContentRating(G)` (contenido general)

### Ubicación de Anuncios

✅ **Permitidos**:
- Banner en menú principal (parte inferior)
- Banner en pantalla de completado del juego de Memoria
- Interstitial al volver al menú (máximo 1 cada 3 retornos)

❌ **Prohibidos**:
- Durante el juego
- Al tocar elementos del juego
- Más de 1 interstitial cada 3 retornos al menú

### IDs de Prueba (Reemplazar antes de publicar)

Actualmente usa IDs de prueba de Google:

```kotlin
// app/src/main/java/com/juegosparachicos/ads/AdManager.kt
const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
```

**Antes de publicar**:
1. Crear cuenta en [AdMob](https://admob.google.com)
2. Crear app y obtener IDs reales
3. Reemplazar en `AdManager.kt`
4. Actualizar `APPLICATION_ID` en `AndroidManifest.xml`

## 🚀 Cómo Compilar y Ejecutar

### Requisitos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- SDK de Android 34

### Pasos

1. **Clonar el repositorio**
   ```bash
   git clone <repo-url>
   cd juegos-para-chicos
   ```

2. **Abrir en Android Studio**
   - File → Open → Seleccionar carpeta del proyecto

3. **Sincronizar Gradle**
   - Android Studio lo hará automáticamente
   - O manualmente: File → Sync Project with Gradle Files

4. **Ejecutar**
   - Conectar dispositivo o iniciar emulador
   - Run → Run 'app' (o Shift+F10)

### Compilar APK

```bash
./gradlew assembleRelease
```

El APK estará en: `app/build/outputs/apk/release/`

## 📦 Assets Necesarios

La app funciona sin assets adicionales (usa emojis), pero para mejor experiencia:

Ver guía completa: [ASSETS_GUIDE.md](ASSETS_GUIDE.md)

### Archivos de Audio
Colocar en `app/src/main/res/raw/`:
- `button_click.ogg`
- `success.ogg`
- `card_flip.ogg`
- `match_found.ogg`
- `game_complete.ogg`
- `correct_answer.ogg`
- `incorrect_answer.ogg`

Después descomentar código en [SoundManager.kt](app/src/main/java/com/juegosparachicos/utils/SoundManager.kt:52)

### Ícono de la App
Generar con [Android Asset Studio](https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html) y colocar en `mipmap-*` folders.

## 🛡️ Cumplimiento de Políticas

### Google Play "Designed for Families"

Esta app está diseñada para cumplir con:

✅ **COPPA (Children's Online Privacy Protection Act)**
- No recopila información personal
- No requiere login
- No tiene compras in-app (por ahora)

✅ **Políticas de Contenido**
- Sin violencia
- Sin contenido inapropiado
- Sin links externos (excepto política de privacidad)

✅ **Políticas de Anuncios**
- Anuncios marcados como child-directed
- Sin anuncios engañosos
- Sin anuncios durante gameplay activo

### Antes de Publicar

1. **Crear Política de Privacidad**
   - Debe estar alojada en URL pública
   - Especificar que no se recopilan datos
   - Mencionar uso de AdMob

2. **Configurar en Google Play Console**
   - Target Age: 3-7 años
   - Categoría: "Designed for Families"
   - Declarar uso de anuncios

3. **Reemplazar IDs de AdMob de prueba**

4. **Firmar la app**
   ```bash
   ./gradlew bundleRelease
   ```

5. **Probar en dispositivos reales**
   - Verificar que anuncios funcionen
   - Verificar que no hay crashes
   - Probar todos los juegos

## 🔧 Configuración Avanzada

### Cambiar Frecuencia de Interstitial

En [AdManager.kt](app/src/main/java/com/juegosparachicos/ads/AdManager.kt:28):

```kotlin
private const val INTERSTITIAL_FREQUENCY = 3  // Cambiar número
```

### Agregar Nuevo Juego

1. Crear nueva carpeta en `ui/screens/minuevogame/`
2. Crear `MiNuevoGameScreen.kt`
3. Agregar ruta en `navigation/Screen.kt`:
   ```kotlin
   object MiNuevoGame : Screen("mi_nuevo_game")
   ```
4. Agregar en `navigation/NavGraph.kt`
5. Agregar botón en `MenuScreen.kt`

### Cambiar Colores

Editar [ui/theme/Color.kt](app/src/main/java/com/juegosparachicos/ui/theme/Color.kt)

## 📝 TODO / Mejoras Futuras

- [ ] Agregar más niveles al juego de Memoria (4x3, 4x4)
- [ ] Instrucciones de voz en el juego de Colores y Formas
- [ ] Más formas geométricas (estrella, corazón, etc.)
- [ ] Sistema de logros (sin recompensas externas)
- [ ] Modo oscuro (opcional)
- [ ] Más juegos educativos
- [ ] Soporte para tablets (landscape)

## 🐛 Solución de Problemas

### Error: "AdMob no carga anuncios"
- Verificar conexión a internet
- En emulador puede tardar más
- IDs de prueba solo funcionan en modo debug

### Error: "App crash al compilar"
- Clean project: Build → Clean Project
- Invalidate caches: File → Invalidate Caches / Restart

### Error: "Gradle sync failed"
- Verificar versión de JDK (debe ser 17)
- Verificar conexión a internet
- Borrar `.gradle` folder y re-sincronizar

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

Desarrollado siguiendo especificaciones de app infantil para Google Play.

## 📞 Contacto

Para consultas sobre publicación o escalabilidad del proyecto, contactar al desarrollador.

---

**Nota**: Esta app está lista para testing. Antes de publicar en Google Play, completar los pasos en la sección "Antes de Publicar".
