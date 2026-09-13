# PlanCraft Android — Enterprise Project, Budget & Calendar Management

A modern, native Android application built with **Kotlin** and **Jetpack Compose (Material 3)** for full-lifecycle project planning, financial accounting, and stakeholder resource scheduling.

Includes a battle-tested **GitHub Actions CI/CD workflow (`.github/workflows/build.yml`)** that automatically builds, tests, and outputs debug APKs and Android App Bundles (AAB) on every push and pull request.

---

## 🚀 Key Modules & Capabilities

### 1. 📅 Calendar Format Task Scheduling
- **Interactive Calendar Grid**: View daily and weekly deliverable schedules with date chips and indicator badges.
- **Full Task Lifecycle**: Scheduled tasks include priority levels (*Urgent*, *High*, *Medium*, *Low*), assignees, duration, billable hour status, and cost impact.
- **One-Tap Task Management**: Toggle task status between Backlog, To-Do, In Progress, In Review, and Done.
- **Filter & Quick-Add**: Filter by project or assignee, with modal dialog to add new tasks directly onto any calendar date.

### 2. 🗂️ Project Management & Kanban Boards
- **Portfolio Health Gauges**: Track project status (*Planning*, *In Progress*, *On Hold*, *Completed*), milestone percentages, and project manager assignments.
- **Live Kanban Board**: Visual workflow across To-Do, In Progress, In Review, and Completed columns.
- **Integrated Budgets**: Instant visibility into budget spent vs. allocated per project.

### 3. 💰 Economy, Budget, Bills, Income & Expenses
- **Payables & Bills**: Track vendor bills, due dates, recurring cadences (*Monthly*, *Quarterly*, *Annual*), invoice numbers, and mark bills as paid.
- **Income & Pipeline**: Inbound client milestones, retainers, and consulting receipts with pending vs. received settlement statuses.
- **Expense Log**: Granular operational expenses categorized into Cloud Infrastructure, Payroll, Hardware, Software Licenses, Legal, and Compliance.
- **Budget Allocations**: Category-by-category ceiling limits with visual progress bars and burn rate indicators.

### 4. 👥 Groups of People Involved Over Time
- **Quarterly Project Phases**: Multi-stage roadmap (*Q1 Architecture*, *Q2 Core Prototyping*, *Q3 Beta Hardening*, *Q4 Enterprise Scale*).
- **Stakeholder Groups**: Explicit group classification:
  - Executive Leadership
  - Core Engineering
  - Product & UX Design
  - Finance & Operations
  - External Security & Legal Consultants
  - Client Stakeholders
- **Resource Allocation Over Time**: Track team members' hourly rates, capacity percentage load (e.g. 85%, 100%), and active assignment windows.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 1.9.23
- **UI Toolkit**: Jetpack Compose with Material 3 Design System
- **Minimum SDK**: Android 8.0 (API Level 26)
- **Target SDK**: Android 14 (API Level 34)
- **Build System**: Gradle 8.4 + Kotlin DSL (`build.gradle.kts`)
- **CI/CD Pipeline**: GitHub Actions (`.github/workflows/build.yml`)

---

## 🤖 GitHub Actions Workflow (`build.yml`)

The repository includes `.github/workflows/build.yml` configured to:
1. Check out repository code on `ubuntu-latest`
2. Set up **Java 17 (Temurin)** with automatic Gradle dependency caching
3. Configure the **Android SDK** and build tools
4. Run unit tests (`./gradlew testDebugUnitTest`)
5. Compile and generate the debug APK (`./gradlew assembleDebug`)
6. Upload the generated APK as a downloadable GitHub Action artifact (`plancraft-app-debug`)

To run the workflow in your own GitHub repository:
```bash
git init
git add .
git commit -m "Initial commit: PlanCraft Android App with CI/CD"
git branch -M main
git remote add origin https://github.com/<your-username>/<your-repo-name>.git
git push -u origin main
```
GitHub Actions will automatically run the build and publish the compiled `.apk` in the **Actions** tab!

---

## 💻 Local Development & Building

### Prerequisites
- **Android Studio Hedgehog / Iguana / Jellyfish** or newer
- **JDK 17** installed and configured in `JAVA_HOME`

### Command-Line Build
```bash
# Ensure Gradle Wrapper JAR is present (in case git skipped binary files)
mkdir -p gradle/wrapper
curl -fsSL -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar

# Grant execution permissions
chmod +x gradlew

# Run unit tests
./gradlew testDebugUnitTest --continue

# Build debug APK
./gradlew assembleDebug

# Output APK path:
# app/build/outputs/apk/debug/app-debug.apk
```

> **Note on `ClassNotFoundException: org.gradle.wrapper.GradleWrapperMain`:**
> If you encounter this error, it means `gradle/wrapper/gradle-wrapper.jar` was not tracked or downloaded. The single curl command above downloads the official 63KB jar and fixes it instantly. In GitHub Actions, `.github/workflows/build.yml` now runs this check automatically before compiling.

---

## 📂 Repository Structure

```
.
├── .github/
│   └── workflows/
│       └── build.yml               # GitHub Actions CI build script
├── app/
│   ├── build.gradle.kts            # App module build configuration
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml # Permissions & Application entry
│   │       ├── java/com/plancraft/android/
│   │       │   ├── MainActivity.kt
│   │       │   ├── PlanCraftApp.kt
│   │       │   ├── data/
│   │       │   │   └── SampleData.kt
│   │       │   ├── model/
│   │       │   │   ├── Project.kt
│   │       │   │   ├── Task.kt
│   │       │   │   ├── Finance.kt
│   │       │   │   └── Stakeholder.kt
│   │       │   └── ui/
│   │       │       ├── calendar/CalendarScreen.kt
│   │       │       ├── projects/ProjectsScreen.kt
│   │       │       ├── economy/EconomyScreen.kt
│   │       │       ├── timeline/TeamTimelineScreen.kt
│   │       │       └── theme/
│   │       │           ├── Color.kt
│   │       │           ├── Theme.kt
│   │       │           └── Type.kt
│   │       └── res/
│   │           ├── values/
│   │           │   ├── strings.xml
│   │           │   ├── colors.xml
│   │           │   └── themes.xml
│   │           └── xml/
├── build.gradle.kts                # Root build file
├── settings.gradle.kts             # Project settings & repositories
├── gradle.properties               # JVM args & AndroidX settings
├── gradlew                         # Unix Gradle wrapper
├── gradlew.bat                     # Windows Gradle wrapper
└── README.md                       # Documentation
```
