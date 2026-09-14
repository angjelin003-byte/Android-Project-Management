package com.plancraft.android.model

enum class StakeholderGroupType {
    EXECUTIVE_LEADERSHIP,
    CORE_ENGINEERING,
    PRODUCT_DESIGN,
    FINANCE_OPERATIONS,
    EXTERNAL_CONSULTANTS,
    CLIENT_STAKEHOLDERS,
    QUALITY_ASSURANCE
}

data class TeamMember(
    val id: String = "",
    val name: String = "",
    val role: String = "",
    val email: String = "",
    val group: StakeholderGroupType = StakeholderGroupType.CORE_ENGINEERING,
    val hourlyRate: Double = 0.0,
    val allocationPercentage: Int = 0,
    val activePeriod: String = ""
)

data class ProjectTimelinePhase(
    val id: String = "",
    val phaseName: String = "",
    val quarter: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val progress: Int = 0,
    val involvedGroups: List<StakeholderGroupType> = emptyList(),
    val headCount: Int = 0,
    val estimatedBudget: Double = 0.0
)
