package com.plancraft.android.data

import com.plancraft.android.model.*
import java.util.UUID

object SampleData {
    val sampleProjects = listOf(
        Project(
            id = "proj-1",
            name = "Project Phoenix",
            client = "Starlight Industries",
            description = "Main cloud migration initiative",
            status = ProjectStatus.IN_PROGRESS,
            priority = ProjectPriority.HIGH,
            startDate = "2026-09-01",
            targetEndDate = "2026-12-31",
            totalBudget = 50000.0,
            totalSpent = 12500.0,
            leadManager = "Alex Rivera",
            completionPercentage = 25
        ),
        Project(
            id = "proj-2",
            name = "Mobile App Overhaul",
            client = "Internal",
            description = "Revamping the Android and iOS presence",
            status = ProjectStatus.PLANNING,
            priority = ProjectPriority.MEDIUM,
            startDate = "2026-10-15",
            targetEndDate = "2027-03-01",
            totalBudget = 25000.0,
            totalSpent = 0.0,
            leadManager = "Jordan Smith",
            completionPercentage = 0
        )
    )

    val sampleTasks = listOf(
        Task(
            id = UUID.randomUUID().toString(),
            projectId = "proj-1",
            projectName = "Project Phoenix",
            title = "Database Schema Design",
            description = "Finalize the PostgreSQL schema for the core module",
            date = "2026-09-14",
            time = "10:00 AM",
            durationHours = 4.0,
            status = TaskStatus.IN_PROGRESS,
            priority = TaskPriority.HIGH,
            assigneeName = "Sam Chen",
            costImpact = 200.0
        ),
        Task(
            id = UUID.randomUUID().toString(),
            projectId = "proj-1",
            projectName = "Project Phoenix",
            title = "API Documentation",
            description = "Complete Swagger specs for the auth endpoints",
            date = "2026-09-15",
            time = "02:00 PM",
            durationHours = 2.0,
            status = TaskStatus.TODO,
            priority = TaskPriority.MEDIUM,
            assigneeName = "Alex Rivera",
            costImpact = 100.0
        ),
        Task(
            id = UUID.randomUUID().toString(),
            projectId = "proj-2",
            projectName = "Mobile App Overhaul",
            title = "Initial Requirements Gathering",
            description = "Meet with stakeholders for feature prioritizing",
            date = "2026-09-16",
            time = "11:00 AM",
            durationHours = 3.0,
            status = TaskStatus.TODO,
            priority = TaskPriority.MEDIUM,
            assigneeName = "Jordan Smith",
            costImpact = 150.0
        )
    )

    val sampleBills = listOf(
        Bill(id = "bill-1", title = "Cloud Hosting", amount = 1200.0, dueDate = "2026-10-01", isPaid = false),
        Bill(id = "bill-2", title = "Software Licenses", amount = 450.0, dueDate = "2026-09-25", isPaid = true)
    )

    val sampleIncomes = listOf(
        Income(id = "inc-1", title = "Client Milestone 1", amount = 5000.0, date = "2026-09-10", status = "Received"),
        Income(id = "inc-2", title = "Consulting Fee", amount = 2000.0, date = "2026-09-20", status = "Pending")
    )

    val sampleExpenses = listOf(
        Expense(id = "exp-1", title = "MacBook Pro M3", category = ExpenseCategory.HARDWARE_EQUIPMENT, amount = 2499.0, date = "2026-09-05", loggedBy = "Jordan Smith"),
        Expense(id = "exp-2", title = "AWS Monthly", category = ExpenseCategory.INFRASTRUCTURE_CLOUD, amount = 850.0, date = "2026-09-01", loggedBy = "Sam Chen")
    )

    val sampleTeamMembers = listOf(
        TeamMember(id = "mem-1", name = "Alex Rivera", role = "Lead Manager", email = "alex@plancraft.io", allocationPercentage = 100),
        TeamMember(id = "mem-2", name = "Jordan Smith", role = "UX Designer", email = "jordan@plancraft.io", allocationPercentage = 85),
        TeamMember(id = "mem-3", name = "Sam Chen", role = "Senior Dev", email = "sam@plancraft.io", allocationPercentage = 100)
    )

    val sampleTimelinePhases = listOf(
        ProjectTimelinePhase(id = "ph-1", phaseName = "Infrastructure Setup", quarter = "Q3 2026", progress = 80),
        ProjectTimelinePhase(id = "ph-2", phaseName = "Core Development", quarter = "Q4 2026", progress = 15)
    )

    val sampleBudgetAllocations = listOf(
        BudgetAllocation(category = ExpenseCategory.INFRASTRUCTURE_CLOUD, allocatedAmount = 10000.0, spentAmount = 2500.0),
        BudgetAllocation(category = ExpenseCategory.SALARIES_CONTRACTORS, allocatedAmount = 30000.0, spentAmount = 15000.0),
        BudgetAllocation(category = ExpenseCategory.HARDWARE_EQUIPMENT, allocatedAmount = 5000.0, spentAmount = 3500.0)
    )
}
