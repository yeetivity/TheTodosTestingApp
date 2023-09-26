buildscript {
    repositories {
        mavenCentral()
        maven("https://maven.google.com")
        google()  // Google's Maven repository
    }
    dependencies {
        // Dependency Injection library
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.46.1")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}