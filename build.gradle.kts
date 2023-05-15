// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
//    dependencies {
//        classpath("com.android.tools.build:gradle:8.0.0")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
//    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
//    id 'com.android.application' version '8.0.0' apply false
//    id 'com.android.library' version '8.0.0' apply false
//    id 'org.jetbrains.kotlin.android' version '1.7.20' apply false
}