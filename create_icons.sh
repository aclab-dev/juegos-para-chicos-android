#!/bin/bash

# Script para generar íconos PNG placeholder
# Requiere ImageMagick instalado: brew install imagemagick

echo "Generando íconos placeholder..."

# Colores
BG_COLOR="#FF6B9D"
FG_COLOR="#FFFFFF"

# Función para crear un ícono simple
create_icon() {
    local size=$1
    local output=$2

    convert -size ${size}x${size} xc:"$BG_COLOR" \
        -gravity center \
        -pointsize $((size/3)) \
        -fill "$FG_COLOR" \
        -annotate +0+0 "🎮" \
        "$output"
}

# Crear directorios si no existen
mkdir -p app/src/main/res/mipmap-mdpi
mkdir -p app/src/main/res/mipmap-hdpi
mkdir -p app/src/main/res/mipmap-xhdpi
mkdir -p app/src/main/res/mipmap-xxhdpi
mkdir -p app/src/main/res/mipmap-xxxhdpi

# Generar íconos en diferentes tamaños
create_icon 48 "app/src/main/res/mipmap-mdpi/ic_launcher.png"
create_icon 48 "app/src/main/res/mipmap-mdpi/ic_launcher_round.png"

create_icon 72 "app/src/main/res/mipmap-hdpi/ic_launcher.png"
create_icon 72 "app/src/main/res/mipmap-hdpi/ic_launcher_round.png"

create_icon 96 "app/src/main/res/mipmap-xhdpi/ic_launcher.png"
create_icon 96 "app/src/main/res/mipmap-xhdpi/ic_launcher_round.png"

create_icon 144 "app/src/main/res/mipmap-xxhdpi/ic_launcher.png"
create_icon 144 "app/src/main/res/mipmap-xxhdpi/ic_launcher_round.png"

create_icon 192 "app/src/main/res/mipmap-xxxhdpi/ic_launcher.png"
create_icon 192 "app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png"

echo "✅ Íconos generados correctamente"
echo "Nota: Estos son íconos placeholder. Reemplazalos con íconos profesionales antes de publicar."
