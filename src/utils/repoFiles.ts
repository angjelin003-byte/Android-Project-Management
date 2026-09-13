export interface RepoFile {
  path: string;
  category: 'workflow' | 'gradle' | 'manifest' | 'kotlin' | 'res' | 'doc';
  language: string;
  content: string;
}

export const REPO_FILES: RepoFile[] = [
  {
    path: '.github/workflows/build.yml',
    category: 'workflow',
    language: 'yaml',
    content: `name: Android CI / Build APK

on:
  push:
    branches: [ "main", "master" ]
  pull_request:
    branches: [ "main", "master" ]
  workflow_dispatch:

jobs:
  build:
    name: Build Android App & Generate APK
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Setup Android SDK
        uses: android-actions/setup-android@v3

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Ensure Gradle Wrapper JAR exists
        run: |
          if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
            echo "gradle-wrapper.jar missing. Downloading Gradle 8.4 wrapper JAR..."
            mkdir -p gradle/wrapper
            curl -fsSL -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar
          fi
          ls -lh gradle/wrapper/gradle-wrapper.jar

      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@v3

      - name: Run Unit Tests
        run: ./gradlew testDebugUnitTest --continue

      - name: Build Debug APK
        run: ./gradlew assembleDebug --stacktrace

      - name: Upload Debug APK
        uses: actions/upload-artifact@v4
        with:
          name: plancraft-app-debug
          path: app/build/outputs/apk/debug/app-debug.apk
          retention-days: 14

      - name: Build Android App Bundle (AAB)
        run: ./gradlew bundleDebug --stacktrace
        continue-on-error: true

      - name: Upload Debug Bundle
        uses: actions/upload-artifact@v4
        with:
          name: plancraft-app-bundle
          path: app/build/outputs/bundle/debug/app-debug.aab
          retention-days: 14`
  },
  {
    path: 'gradlew',
    category: 'gradle',
    language: 'bash',
    content: `#!/bin/sh
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "Downloading gradle-wrapper.jar..."
    mkdir -p gradle/wrapper
    curl -fsSL -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar
fi
exec java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain "$@"`
  },
  {
    path: 'gradlew.bat',
    category: 'gradle',
    language: 'bat',
    content: `@echo off
if not exist "gradle\\wrapper\\gradle-wrapper.jar" (
    echo Downloading gradle-wrapper.jar...
    if not exist "gradle\\wrapper" mkdir "gradle\\wrapper"
    curl -fsSL -o "gradle\\wrapper\\gradle-wrapper.jar" https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar
)
@java -classpath "%~dp0gradle\\wrapper\\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*`
  },
  {
    path: 'gradle/wrapper/gradle-wrapper.properties',
    category: 'gradle',
    language: 'properties',
    content: `distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\\://services.gradle.org/distributions/gradle-8.4-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists`
  },
  {
    path: 'build.gradle.kts',
    category: 'gradle',
    language: 'kotlin',
    content: `// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}`
  },
  {
    path: 'settings.gradle.kts',
    category: 'gradle',
    language: 'kotlin',
    content: `pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\\\.android.*")
                includeGroupByRegex("com\\\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PlanCraft"
include(":app")`
  },
  {
    path: 'gradle.properties',
    category: 'gradle',
    language: 'properties',
    content: `org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.nonTransitiveRClass=true
kotlin.code.style=official
org.gradle.caching=true
org.gradle.parallel=true`
  },
  {
    path: 'app/build.gradle.kts',
    category: 'gradle',
    language: 'kotlin',
    content: `plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.plancraft.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.plancraft.android"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.11" }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    implementation(platform("androidx.compose:compose-bom:2024.02.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
}`
  },
  {
    path: 'app/src/main/AndroidManifest.xml',
    category: 'manifest',
    language: 'xml',
    content: `<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.PlanCraft">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.PlanCraft">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>`
  },
  {
    path: 'app/src/main/java/com/plancraft/android/MainActivity.kt',
    category: 'kotlin',
    language: 'kotlin',
    content: `package com.plancraft.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.plancraft.android.ui.theme.PlanCraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanCraftTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlanCraftApp()
                }
            }
        }
    }
}`
  },
  {
    path: 'app/src/main/java/com/plancraft/android/PlanCraftApp.kt',
    category: 'kotlin',
    language: 'kotlin',
    content: `package com.plancraft.android

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.plancraft.android.data.SampleData
import com.plancraft.android.model.*
import com.plancraft.android.ui.calendar.CalendarScreen
import com.plancraft.android.ui.economy.EconomyScreen
import com.plancraft.android.ui.projects.ProjectsScreen
import com.plancraft.android.ui.timeline.TeamTimelineScreen

enum class AppDestination(val title: String) {
    CALENDAR("Calendar"),
    PROJECTS("Projects"),
    ECONOMY("Economy"),
    PEOPLE("People")
}

@Composable
fun PlanCraftApp() {
    var currentDestination by remember { mutableStateOf(AppDestination.CALENDAR) }
    var tasks by remember { mutableStateOf(SampleData.sampleTasks) }
    var projects by remember { mutableStateOf(SampleData.sampleProjects) }
    var bills by remember { mutableStateOf(SampleData.sampleBills) }
    var incomes by remember { mutableStateOf(SampleData.sampleIncomes) }
    var expenses by remember { mutableStateOf(SampleData.sampleExpenses) }
    val phases by remember { mutableStateOf(SampleData.sampleTimelinePhases) }
    val members by remember { mutableStateOf(SampleData.sampleTeamMembers) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                // Bottom bar navigation implementation
            }
        }
    ) { padding ->
        // Destination views: Calendar, Projects, Economy, People & Timeline
    }
}`
  },
  {
    path: 'app/src/main/java/com/plancraft/android/ui/calendar/CalendarScreen.kt',
    category: 'kotlin',
    language: 'kotlin',
    content: `package com.plancraft.android.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.plancraft.android.model.*

@Composable
fun CalendarScreen(
    tasks: List<Task>,
    projects: List<Project>,
    onToggleTaskStatus: (String) -> Unit,
    onAddTask: (Task) -> Unit
) {
    // Interactive calendar grid with task chips, deadline badges and filter by project
}`
  },
  {
    path: 'app/src/main/java/com/plancraft/android/ui/economy/EconomyScreen.kt',
    category: 'kotlin',
    language: 'kotlin',
    content: `package com.plancraft.android.ui.economy

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.plancraft.android.model.*

@Composable
fun EconomyScreen(
    bills: List<Bill>,
    incomes: List<Income>,
    expenses: List<Expense>,
    onToggleBillPaid: (String) -> Unit
) {
    // Economy and budget, bills, income and expenses
}`
  },
  {
    path: 'app/src/main/java/com/plancraft/android/ui/timeline/TeamTimelineScreen.kt',
    category: 'kotlin',
    language: 'kotlin',
    content: `package com.plancraft.android.ui.timeline

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.plancraft.android.model.*

@Composable
fun TeamTimelineScreen(
    phases: List<ProjectTimelinePhase>,
    members: List<TeamMember>
) {
    // Groups of people involved over time, quarterly phases and allocation
}`
  },
  {
    path: 'README.md',
    category: 'doc',
    language: 'markdown',
    content: `# PlanCraft Android — Enterprise Project, Budget & Calendar Management
Native Android application with Jetpack Compose (Material 3) and GitHub Actions CI build.yml.`
  },
  {
    path: 'app/src/main/res/drawable/ic_launcher_background.xml',
    category: 'xml',
    language: 'xml',
    content: `<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:fillColor="#4F46E5"
        android:pathData="M0,0h108v108h-108z" />
</vector>`
  },
  {
    path: 'app/src/main/res/drawable/ic_launcher_foreground.xml',
    category: 'xml',
    language: 'xml',
    content: `<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path 
        android:fillColor="#FFFFFF" 
        android:pathData="M54,24 l30,50 h-60 z" />
</vector>`
  },
  {
    path: 'app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml',
    category: 'xml',
    language: 'xml',
    content: `<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>`
  },
  {
    path: 'app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml',
    category: 'xml',
    language: 'xml',
    content: `<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>`
  }
];
