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
    val id: String,
    val name: String,
    val role: String,
    val email: String,
    val group: StakeholderGroupType,
    val hourlyRate: Double,
    val allocationPercentage: Int,
    val activePeriod: String
)

data class ProjectTimelinePhase(
    val id: String,
    val phaseName: String,
    val quarter: String,
    val startDate: String,
    val endDate: String,
    val progress: Int,
    val involvedGroups: List<StakeholderGroupType>,
    val headCount: Int,
    val estimatedBudget: Double
)
