#!/usr/bin/env python3
"""
Script para generar íconos PNG placeholder para la app
Usa PIL/Pillow si está disponible, o crea PNGs mínimos válidos
"""

import os
import sys

# Intentar importar PIL
try:
    from PIL import Image, ImageDraw, ImageFont
    HAS_PIL = True
except ImportError:
    HAS_PIL = False
    print("⚠️  PIL/Pillow no está instalado. Creando íconos mínimos...")
    print("   Para íconos mejores, instala: pip install Pillow")

# Configuración
BG_COLOR = (255, 107, 157)  # Rosa #FF6B9D
FG_COLOR = (255, 255, 255)  # Blanco

SIZES = {
    'mdpi': 48,
    'hdpi': 72,
    'xhdpi': 96,
    'xxhdpi': 144,
    'xxxhdpi': 192
}

def create_icon_with_pil(size, output_path):
    """Crea un ícono usando PIL"""
    img = Image.new('RGB', (size, size), BG_COLOR)
    draw = ImageDraw.Draw(img)

    # Dibujar un círculo blanco en el centro
    margin = size // 4
    draw.ellipse([margin, margin, size - margin, size - margin],
                 fill=FG_COLOR, outline=FG_COLOR)

    # Dibujar un círculo rosa más pequeño adentro
    margin2 = size // 3
    draw.ellipse([margin2, margin2, size - margin2, size - margin2],
                 fill=BG_COLOR, outline=BG_COLOR)

    # Guardar
    img.save(output_path, 'PNG')
    print(f"✅ Creado: {output_path}")

def create_minimal_png(size, output_path):
    """Crea un PNG mínimo válido sin PIL"""
    # PNG mínimo de 1x1 pixel rosa
    # Este es un PNG válido de 1 pixel que se escalará
    png_data = bytes.fromhex(
        '89504e470d0a1a0a'  # PNG signature
        '0000000d49484452'  # IHDR chunk
        '000000010000000108060000001f15c4890000000a49444154'
        '789c6300010000050001'
        '0d0a2db40000000049454e44ae426082'
    )

    # Crear un PNG simple del tamaño correcto
    # Nota: esto crea un PNG mínimo que Android aceptará
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    with open(output_path, 'wb') as f:
        f.write(png_data)
    print(f"✅ Creado (minimal): {output_path}")

def main():
    print("🎨 Generando íconos de la aplicación...")

    base_path = "app/src/main/res"

    for density, size in SIZES.items():
        # Crear directorio
        mipmap_dir = os.path.join(base_path, f"mipmap-{density}")
        os.makedirs(mipmap_dir, exist_ok=True)

        # Crear ambos íconos (normal y round)
        for icon_name in ['ic_launcher.png', 'ic_launcher_round.png']:
            output_path = os.path.join(mipmap_dir, icon_name)

            if HAS_PIL:
                create_icon_with_pil(size, output_path)
            else:
                create_minimal_png(size, output_path)

    print("\n✅ Íconos generados correctamente")
    print("\n📝 Nota: Estos son íconos placeholder temporales.")
    print("   Reemplazalos con íconos profesionales antes de publicar.")
    print("   Usa: https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html")

if __name__ == '__main__':
    main()
