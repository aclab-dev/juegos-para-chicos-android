# Guía de Assets para Juegos para Chicos

Este documento describe todos los assets (recursos) que necesitás agregar a la aplicación antes de publicarla.

## 📁 Estructura de Directorios

```
app/src/main/res/
├── raw/              # Archivos de audio
├── mipmap-*/         # Íconos de la app (diferentes densidades)
└── drawable/         # Imágenes (opcional, actualmente se usan emojis)
```

## 🔊 Archivos de Audio Necesarios

Todos los archivos de audio deben estar en formato **OGG** (recomendado) o MP3.
Colocar en: `app/src/main/res/raw/`

### Sonidos Generales
- `button_click.ogg` - Click en botón (0.1 - 0.3 seg)
- `success.ogg` - Acción exitosa general (0.5 - 1 seg)

### Juego de Memoria
- `card_flip.ogg` - Sonido al voltear carta (0.2 - 0.4 seg)
- `match_found.ogg` - Pareja encontrada (0.5 - 1 seg)
- `game_complete.ogg` - Juego completado (1 - 2 seg, alegre)

### Juego de Colores y Formas
- `correct_answer.ogg` - Respuesta correcta (0.5 - 1 seg, alegre)
- `incorrect_answer.ogg` - Respuesta incorrecta (0.3 - 0.5 seg, suave, NO aterrador)

### Juego de Pintar (opcional)
- `clear_canvas.ogg` - Borrar canvas (0.2 - 0.4 seg)

### Instrucciones de Voz (Juego Colores y Formas)
Opcionalmente, podés agregar archivos de voz que digan las instrucciones:
- `instruction_circle_red.ogg`
- `instruction_square_blue.ogg`
- etc.

### Dónde Conseguir Sonidos

1. **Freesound.org** (gratis, Creative Commons)
   - https://freesound.org
   - Buscar: "success sound", "click sound", "kids game sound"

2. **Zapsplat** (gratis con atribución)
   - https://www.zapsplat.com

3. **Crear propios** (Text-to-Speech para instrucciones)
   - Google Cloud Text-to-Speech
   - Amazon Polly
   - Usar voces en español, preferiblemente neutro o amigable

### Activar Sonidos en el Código

Una vez que agregues los archivos de audio:

1. Abrir `app/src/main/java/com/juegosparachicos/utils/SoundManager.kt`
2. Descomentar la sección `loadSounds()`
3. Verificar que los nombres de archivo coincidan

## 🎨 Ícono de la Aplicación

Necesitás crear un ícono para la app en diferentes tamaños.

### Herramientas Recomendadas

1. **Android Asset Studio** (online, gratis)
   - https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html
   - Sube una imagen 512x512 y genera todos los tamaños

2. **Canva** (diseño del ícono)
   - https://www.canva.com
   - Template: "App Icon"

### Tamaños Necesarios

Los archivos deben llamarse `ic_launcher.png` y `ic_launcher_round.png`:

- `mipmap-mdpi/` - 48x48 px
- `mipmap-hdpi/` - 72x72 px
- `mipmap-xhdpi/` - 96x96 px
- `mipmap-xxhdpi/` - 144x144 px
- `mipmap-xxxhdpi/` - 192x192 px

### Recomendaciones de Diseño

- Colores: Usar la paleta de la app (rosa #FF6B9D, amarillo #FFD93D, celeste #4ECDC4)
- Elementos: Puede incluir formas geométricas, números, o un personaje simple
- Simple: Que sea reconocible incluso en tamaño pequeño
- Sin texto: Evitar texto, usar solo íconos o formas

## 🖼️ Imágenes Adicionales (Opcional)

Actualmente la app usa emojis para todo (animales en memoria, formas básicas en colores).

Si querés usar imágenes personalizadas:

### Juego de Memoria
Colocar en `app/src/main/res/drawable/`:
- `animal_dog.png`
- `animal_cat.png`
- `animal_mouse.png`
- etc. (12 animales en total)

Tamaño recomendado: 128x128 px, formato PNG con transparencia

Después modificar `MemoryGameScreen.kt` para usar las imágenes en lugar de emojis.

## 📝 Checklist antes de Publicar

- [ ] Todos los archivos de audio agregados en `res/raw/`
- [ ] Código de `SoundManager.kt` descomentado y probado
- [ ] Ícono de la app en todas las densidades
- [ ] Probar la app en un dispositivo real
- [ ] Verificar que los sonidos no sean muy fuertes
- [ ] Verificar que los sonidos de error no asusten a los niños

## 🎵 Niveles de Volumen Recomendados

En el código, podés ajustar el volumen de cada sonido:

```kotlin
soundManager.playSound(SoundType.SUCCESS, volume = 0.7f) // 70% volumen
soundManager.playSound(SoundType.INCORRECT_ANSWER, volume = 0.4f) // Más suave
```

Sonidos de error deben ser más suaves que sonidos de éxito.

## 📄 Licencias

Si usás sonidos o imágenes de terceros:
- Verificar la licencia (Creative Commons, etc.)
- Si requiere atribución, agregar en la Política de Privacidad o en una sección "Créditos" en la app
- Para apps infantiles, preferir contenido con licencia libre sin restricciones
