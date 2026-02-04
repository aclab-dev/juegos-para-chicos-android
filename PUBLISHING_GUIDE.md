# 🚀 Guía de Publicación en Google Play

Esta guía te lleva paso a paso desde la app terminada hasta tenerla publicada en Google Play Store.

## ✅ Pre-requisitos

Antes de empezar, asegurarte de tener:

- [ ] App completamente funcional y probada
- [ ] Cuenta de Google Play Console (costo único: USD $25)
- [ ] Cuenta de AdMob configurada
- [ ] Política de privacidad publicada en una URL pública
- [ ] Íconos de la app en todas las resoluciones
- [ ] Capturas de pantalla de la app

## 📋 Checklist Completo

### 1. Configuración de AdMob

#### 1.1 Crear Cuenta en AdMob
1. Ir a [AdMob](https://admob.google.com)
2. Crear cuenta o usar cuenta de Google existente
3. Aceptar términos y condiciones

#### 1.2 Crear App en AdMob
1. En AdMob → "Apps" → "Agregar app"
2. Plataforma: Android
3. Nombre: "Juegos para Chicos"
4. ✅ Marcar: "La app está dirigida principalmente a niños"
5. Copiar el **App ID** (ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX)

#### 1.3 Crear Unidades de Anuncio
Crear 2 unidades:

**Banner:**
- Tipo: Banner
- Nombre: "Menu Banner"
- Copiar **Ad Unit ID**

**Interstitial:**
- Tipo: Interstitial
- Nombre: "Menu Return Interstitial"
- Copiar **Ad Unit ID**

#### 1.4 Actualizar Código
Editar 2 archivos:

**AndroidManifest.xml** (línea 22):
```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX" />
```

**AdManager.kt** (línea 25-26):
```kotlin
const val BANNER_AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"
const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/ZZZZZZZZZZ"
```

### 2. Política de Privacidad

#### 2.1 Editar Template
1. Abrir `PRIVACY_POLICY_TEMPLATE.md`
2. Reemplazar:
   - [FECHA] → Fecha actual
   - [TU_EMAIL] → Tu email de contacto
   - [TU_SITIO_WEB] → Tu sitio (opcional)

#### 2.2 Publicar en Web
Opciones gratuitas:

**Opción A: GitHub Pages**
1. Crear repo público en GitHub
2. Subir archivo como `privacy-policy.html`
3. Habilitar GitHub Pages en Settings
4. URL: `https://tu-usuario.github.io/tu-repo/privacy-policy.html`

**Opción B: Google Sites**
1. Ir a [Google Sites](https://sites.google.com)
2. Crear nuevo sitio
3. Copiar/pegar política
4. Publicar
5. Copiar URL pública

**Opción C: Netlify/Vercel**
Similar a GitHub Pages pero con más opciones.

### 3. Assets Gráficos

#### 3.1 Ícono de la App
Herramienta: [Android Asset Studio](https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html)

1. Crear imagen 512x512 px
2. Subirla al Asset Studio
3. Descargar ZIP con todos los tamaños
4. Copiar carpetas mipmap-* a `app/src/main/res/`

#### 3.2 Capturas de Pantalla
Necesitás al menos 2, recomendado 4-8:

**Qué capturar:**
- Menú principal
- Juego de Memoria (en acción)
- Juego de Pintar
- Juego de Colores y Formas

**Especificaciones:**
- Formato: PNG o JPEG
- Mínimo: 320 px en el lado más corto
- Máximo: 3840 px en el lado más largo
- Relación de aspecto: 16:9 o 9:16

**Cómo capturar:**
- En emulador: botón de cámara en la barra lateral
- En dispositivo real: botón power + volumen abajo

#### 3.3 Ícono de Funciones (Feature Graphic)
**Requerido para Google Play**

- Tamaño: **1024 x 500 px**
- Formato: PNG o JPEG
- Sin transparencia
- Contenido: Logo/título de la app + elementos visuales

Herramienta: [Canva](https://www.canva.com) (template "Feature Graphic")

### 4. Firmar la App

#### 4.1 Generar Keystore (primera vez)
```bash
keytool -genkey -v -keystore juegos-para-chicos.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias juegos-key
```

**IMPORTANTE**:
- Guardar el archivo `.jks` en lugar seguro
- Anotar la contraseña (¡NO PERDER!)
- Hacer backup del keystore

#### 4.2 Configurar Signing en Android Studio

Opción 1: Archivo de propiedades (recomendado)

Crear `keystore.properties` en raíz del proyecto:
```properties
storePassword=TU_PASSWORD
keyPassword=TU_PASSWORD
keyAlias=juegos-key
storeFile=/ruta/a/juegos-para-chicos.jks
```

**⚠️ NUNCA subir este archivo a Git**

Agregar a `.gitignore`:
```
keystore.properties
*.jks
```

Opción 2: Configurar manualmente en Build → Generate Signed Bundle

#### 4.3 Generar Android App Bundle (AAB)
```bash
./gradlew bundleRelease
```

Archivo generado en: `app/build/outputs/bundle/release/app-release.aab`

### 5. Google Play Console

#### 5.1 Crear Cuenta
1. Ir a [Google Play Console](https://play.google.com/console)
2. Pagar USD $25 (una sola vez, para siempre)
3. Completar información de cuenta

#### 5.2 Crear App
1. "Crear app"
2. Nombre: "Juegos para Chicos"
3. Idioma predeterminado: Español
4. App o juego: Juego
5. Gratis o de pago: Gratis

#### 5.3 Ficha de la Tienda

**Detalles de la app:**
- Nombre: Juegos para Chicos
- Descripción corta: "3 juegos educativos para niños de 3 a 7 años: memoria, pintar y colores"
- Descripción completa: (usar la del README, adaptar)
- Categoría: Educación / Juegos educativos
- Email de contacto: tu email

**Assets gráficos:**
- Subir ícono de la app (512x512)
- Subir feature graphic (1024x500)
- Subir capturas de pantalla (mínimo 2)

#### 5.4 Clasificación de Contenido

**Cuestionario:**
1. Categoría: Juego de simulación / educativo
2. ¿Violencia? No
3. ¿Sexual? No
4. ¿Drogas? No
5. ¿Lenguaje grosero? No
6. ¿Temas delicados? No

Resultado esperado: **EVERYONE** (apto para todos)

#### 5.5 Público Objetivo

**CRÍTICO para apps infantiles:**

1. **¿A qué grupo de edad se dirige?**
   - ✅ Marcar: 5 años o menos
   - ✅ Marcar: De 6 a 8 años

2. **¿Tiene contenido solo para adultos?**
   - ❌ No

3. **¿Sabés si tu app atrae principalmente a niños?**
   - ✅ Sí

4. **¿Tu app está en "Designed for Families"?**
   - ✅ Sí
   - Categoría: Educación / Creatividad

#### 5.6 Seguridad de Datos

**¿Recopila o comparte datos?**
- ❌ No (porque no recopilas datos personales)

**Pero marcá:**
- ✅ La app usa publicidad
- Proveedor: Google AdMob
- ✅ Anuncios child-directed

**Política de privacidad:**
- Pegar URL donde la publicaste

#### 5.7 Configuración de Precios

- ✅ Gratis
- Seleccionar países: (recomendado: todos)
- ✅ Contiene anuncios

### 6. Versión de Producción

#### 6.1 Crear Versión
1. Producción → Crear nueva versión
2. Subir AAB (`app-release.aab`)
3. Nombre de versión: 1.0.0
4. Notas de la versión: "Primera versión"

#### 6.2 Revisión
Google revisará la app:
- Tiempo: 1-7 días (usualmente 2-3)
- Puede pedir cambios o aclaraciones
- Recibirás email con el resultado

### 7. Después de la Aprobación

#### 7.1 Verificar en Play Store
- Buscar "Juegos para Chicos" en Play Store
- Verificar que todo se vea bien
- Instalar en dispositivo real

#### 7.2 Probar Anuncios Reales
⚠️ **NO hacer click en tus propios anuncios** (viola políticas de AdMob)

- Instalar en dispositivo de prueba
- Agregar como dispositivo de prueba en AdMob
- Verificar que anuncios carguen correctamente

#### 7.3 Monitoreo

**Play Console:**
- Panel de estadísticas
- Instalaciones/desinstalaciones
- Calificaciones y reseñas
- Crashes (si hay)

**AdMob:**
- Ingresos
- Impresiones
- eCPM (earnings per thousand impressions)

## 🚨 Problemas Comunes

### App rechazada: "Política de privacidad faltante"
✅ Verificar que la URL funcione y sea pública

### App rechazada: "Anuncios no aptos para niños"
✅ Verificar en AdMob que todo esté marcado como child-directed

### Crashes en producción
✅ Usar "Pre-launch report" en Play Console antes de publicar

### Bajos ingresos de anuncios
- Normal al principio (pocas descargas)
- CPM es más bajo en apps infantiles (por políticas de privacidad)
- Optimizar frecuencia de anuncios sin molestar usuarios

## 📈 Después del Lanzamiento

### Marketing (opcional)
- Compartir en redes sociales
- Pedir a familia/amigos que descarguen y califiquen
- ⚠️ NO comprar descargas o reseñas (viola políticas)

### Actualizaciones
Para publicar actualizaciones:
1. Incrementar `versionCode` y `versionName` en `build.gradle`
2. Generar nuevo AAB firmado
3. Subir a Play Console → Nueva versión

### Expansión
Ideas para futuras versiones:
- Más niveles en juegos existentes
- Nuevos minijuegos
- Soporte multiidioma
- Versión premium sin anuncios (opcional)

## 📞 Soporte

Si Google Play rechaza tu app:
- Leer cuidadosamente el email de rechazo
- Revisar [Políticas de Google Play](https://play.google.com/about/developer-content-policy/)
- Hacer cambios necesarios
- Volver a enviar

Para problemas con AdMob:
- [Centro de Ayuda de AdMob](https://support.google.com/admob)

## ✅ Checklist Final Antes de Publicar

- [ ] IDs de AdMob actualizados (no usar IDs de prueba)
- [ ] App firmada con keystore de producción
- [ ] Keystore guardado en lugar seguro (con backup)
- [ ] Política de privacidad publicada en web
- [ ] Todos los assets gráficos subidos
- [ ] Probado en múltiples dispositivos
- [ ] Sin crashes
- [ ] Cuestionarios de Play Console completados
- [ ] Marcado como "Designed for Families"
- [ ] Configuración de AdMob child-directed

---

¡Buena suerte con tu publicación! 🎉
