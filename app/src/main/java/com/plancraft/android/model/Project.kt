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
    val id: String = "",
    val name: String = "",
    val client: String = "",
    val description: String = "",
    val status: ProjectStatus = ProjectStatus.PLANNING,
    val priority: ProjectPriority = ProjectPriority.MEDIUM,
    val startDate: String = "",
    val targetEndDate: String = "",
    val totalBudget: Double = 0.0,
    val totalSpent: Double = 0.0,
    val leadManager: String = "",
    val completionPercentage: Int = 0,
    val tags: List<String> = emptyList()
)
