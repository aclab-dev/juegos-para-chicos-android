# Changelog

Todos los cambios notables de este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/lang/es/).

## [1.0.0] - Por publicar

### Agregado
- ✨ Splash screen con animación
- ✨ Menú principal con 3 juegos
- 🎮 Juego de Memoria con 2 niveles (2x2 y 3x2)
- 🎨 Juego de Pintar con canvas y 6 colores
- 🔷 Juego de Colores y Formas con instrucciones interactivas
- 💰 Integración de AdMob (banners e interstitials)
- 🔊 Sistema de gestión de sonidos (SoundManager)
- 📱 Navegación con Jetpack Compose Navigation
- 🎨 Tema personalizado con colores amigables para niños
- 🛡️ Configuración COPPA-compliant para anuncios
- 📄 Política de privacidad template
- 📚 Documentación completa (README, guías de assets y publicación)

### Características
- 100% offline (excepto anuncios)
- Sin recopilación de datos personales
- Interfaz simple y colorida
- Optimizado para niños de 3-7 años
- Cumple políticas "Designed for Families"

### Técnico
- Min SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Kotlin 1.9.22
- Jetpack Compose con Material 3
- AdMob 22.6.0

---

## Versionado

**Formato**: MAJOR.MINOR.PATCH

- **MAJOR**: Cambios incompatibles en la API o reestructuración completa
- **MINOR**: Nueva funcionalidad compatible hacia atrás (nuevo juego, features)
- **PATCH**: Corrección de bugs, mejoras menores

---

## [Futuras Versiones] - Ideas

### [1.1.0] - Próxima actualización menor
#### Planeado
- [ ] Agregar archivos de audio reales
- [ ] Más niveles en juego de Memoria (4x3, 4x4)
- [ ] Más formas geométricas en juego de Colores
- [ ] Optimización de rendimiento

### [1.2.0] - Expansión de contenido
#### Planeado
- [ ] Nuevo juego: Rompecabezas simple
- [ ] Nuevo juego: Encuentra las diferencias
- [ ] Sistema de feedback visual mejorado

### [2.0.0] - Gran actualización
#### Planeado
- [ ] Soporte para modo landscape (tablets)
- [ ] Multi-idioma (inglés, portugués)
- [ ] Configuraciones para padres
- [ ] Estadísticas de juego (sin online)

---

## Notas para Desarrolladores

Cuando publiques una actualización:

1. **Actualizar versión** en `app/build.gradle.kts`:
   ```kotlin
   versionCode = 2  // Incrementar
   versionName = "1.0.1"  // Seguir semver
   ```

2. **Documentar cambios** en este archivo

3. **Probar exhaustivamente**:
   - Todos los juegos funcionando
   - Anuncios cargando correctamente
   - Sin crashes
   - Probado en múltiples dispositivos

4. **Generar release firmado**:
   ```bash
   ./gradlew bundleRelease
   ```

5. **Subir a Play Console** con notas de versión en español

---

## Template para Nuevas Versiones

```markdown
## [X.Y.Z] - AAAA-MM-DD

### Agregado
- Nueva funcionalidad A
- Nueva funcionalidad B

### Modificado
- Mejora en funcionalidad C
- Actualización de dependencia D

### Corregido
- Bug #123: Descripción del bug
- Crash en situación específica

### Eliminado
- Funcionalidad obsoleta E
```
