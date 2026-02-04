# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Mantener clases de AdMob
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# Mantener clases de la app para debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
