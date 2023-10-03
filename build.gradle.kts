// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()

        // ... other repositories you might have
    }

    dependencies {
        val nav_version = "2.7.3" // Ensure this version matches your Navigation library version
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath("com.google.gms:google-services:4.3.15")
        // ... your other classpath dependencies
    }
}

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
}

// ... rest of your top-level Gradle configurations
