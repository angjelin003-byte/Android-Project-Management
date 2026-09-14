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
}
