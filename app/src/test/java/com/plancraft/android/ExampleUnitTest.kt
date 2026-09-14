package com.plancraft.android

import com.plancraft.android.model.Bill
import com.plancraft.android.model.BudgetAllocation
import com.plancraft.android.model.ExpenseCategory
import com.plancraft.android.model.Income
import com.plancraft.android.model.Project
import com.plancraft.android.model.ProjectStatus
import com.plancraft.android.model.ProjectTimelinePhase
import com.plancraft.android.model.Task
import com.plancraft.android.model.TaskStatus
import com.plancraft.android.model.TeamMember
import com.plancraft.android.model.User
import com.plancraft.android.model.UserRole
import com.plancraft.android.model.UserStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4L, (2 + 2).toLong())
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
        assertEquals(1000.0, project.totalBudget, 0.001)
        assertEquals(ProjectStatus.PLANNING, project.status)
    }

    @Test
    fun finance_models_initialization_isCorrect() {
        val bill = Bill(title = "Rent", amount = 1200.0)
        assertEquals("Rent", bill.title)
        assertEquals(1200.0, bill.amount, 0.001)
        assertFalse(bill.isPaid)

        val income = Income(title = "Invoice #1", amount = 5000.0)
        assertEquals(5000.0, income.amount, 0.001)
        assertEquals("Pending", income.status)

        val budget = BudgetAllocation(category = ExpenseCategory.SALARIES_CONTRACTORS, allocatedAmount = 10000.0)
        assertEquals(10000.0, budget.allocatedAmount, 0.001)
        assertEquals(0.0, budget.spentAmount, 0.001)
    }

    @Test
    fun stakeholder_models_initialization_isCorrect() {
        val member = TeamMember(name = "Alice", role = "Lead Dev")
        assertEquals("Alice", member.name)
        assertEquals("Lead Dev", member.role)
        assertEquals(0.0, member.hourlyRate, 0.001)

        val phase = ProjectTimelinePhase(phaseName = "Beta", progress = 45)
        assertEquals("Beta", phase.phaseName)
        assertEquals(45, phase.progress)
        assertTrue(phase.involvedGroups.isEmpty())
    }

    @Test
    fun task_initialization_isCorrect() {
        val task = Task(title = "Fix UI", description = "Minor bug", durationHours = 5.0)
        assertEquals("Fix UI", task.title)
        assertEquals(5.0, task.durationHours, 0.001)
        assertEquals(TaskStatus.TODO, task.status)
        assertTrue(task.isBillable)
    }
}
