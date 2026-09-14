package com.plancraft.android

import com.plancraft.android.model.*
import org.junit.Test
import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun user_initialization_isCorrect() {
        val user = User(name = "Test User", email = "test@example.com", role = UserRole.ADMIN)
        assertEquals("Test User", user.name)
        assertEquals("test@example.com", user.email)
        assertEquals(UserRole.ADMIN, user.role)
        assertEquals(UserStatus.ONLINE, user.status)
    }

    @Test
    fun project_initialization_isCorrect() {
        val project = Project(
            id = "p1",
            name = "Project 1",
            client = "Client A",
            totalBudget = 1000.0,
            leadManager = "Manager 1"
        )
        assertEquals("Project 1", project.name)
        assertEquals(1000.0, project.totalBudget, 0.1)
        assertEquals(ProjectStatus.PLANNING, project.status)
    }

    @Test
    fun finance_models_initialization_isCorrect() {
        val bill = Bill(title = "Rent", amount = 1200.0)
        assertEquals("Rent", bill.title)
        assertEquals(1200.0, bill.amount, 0.1)
        assertFalse(bill.isPaid)

        val income = Income(title = "Invoice #1", amount = 5000.0)
        assertEquals(5000.0, income.amount, 0.1)
        assertEquals("Pending", income.status)

        val budget = BudgetAllocation(category = ExpenseCategory.SALARIES_CONTRACTORS, allocatedAmount = 10000.0)
        assertEquals(10000.0, budget.allocatedAmount, 0.1)
        assertEquals(0.0, budget.spentAmount, 0.1)
    }

    @Test
    fun stakeholder_models_initialization_isCorrect() {
        val member = TeamMember(name = "Alice", role = "Lead Dev")
        assertEquals("Alice", member.name)
        assertEquals("Lead Dev", member.role)
        assertEquals(0.0, member.hourlyRate, 0.1)

        val phase = ProjectTimelinePhase(phaseName = "Beta", progress = 45)
        assertEquals("Beta", phase.phaseName)
        assertEquals(45, phase.progress)
        assertTrue(phase.involvedGroups.isEmpty())
    }

    @Test
    fun task_initialization_isCorrect() {
        val task = Task(title = "Fix UI", description = "Minor bug", durationHours = 5.0)
        assertEquals("Fix UI", task.title)
        assertEquals(5.0, task.durationHours, 0.1)
        assertEquals(TaskStatus.TODO, task.status)
        assertTrue(task.isBillable)
    }
}
