package com.plancraft.android.model

enum class TaskStatus {
    BACKLOG,
    TODO,
    IN_PROGRESS,
    IN_REVIEW,
    DONE
}

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

data class Task(
    val id: String = "",
    val projectId: String = "",
    val projectName: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "", // Format YYYY-MM-DD
    val time: String? = null,
    val durationHours: Double = 2.0,
    val status: TaskStatus = TaskStatus.TODO,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val assigneeId: String = "",
    val assigneeName: String = "",
    val costImpact: Double = 0.0,
    val milestone: String? = null,
    val isBillable: Boolean = true
)
