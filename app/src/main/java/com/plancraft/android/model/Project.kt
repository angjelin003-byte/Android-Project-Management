package com.plancraft.android.model

enum class ProjectStatus {
    PLANNING,
    IN_PROGRESS,
    ON_HOLD,
    COMPLETED
}

enum class ProjectPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

data class Project(
    val id: String,
    val name: String,
    val client: String,
    val description: String,
    val status: ProjectStatus,
    val priority: ProjectPriority,
    val startDate: String,
    val targetEndDate: String,
    val totalBudget: Double,
    val totalSpent: Double,
    val leadManager: String,
    val completionPercentage: Int,
    val tags: List<String> = emptyList()
)
