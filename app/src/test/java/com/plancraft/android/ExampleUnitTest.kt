package com.plancraft.android

import com.plancraft.android.model.Project
import com.plancraft.android.model.User
import com.plancraft.android.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4L, 2L + 2L)
    }

    @Test
    fun user_initialization_isCorrect() {
        val user = User(name = "Test User", email = "test@example.com", role = UserRole.ADMIN)
        assertEquals("Test User", user.name)
        assertEquals("test@example.com", user.email)
    }

    @Test
    fun project_initialization_isCorrect() {
        val project = Project(name = "Project 1")
        assertEquals("Project 1", project.name)
    }
}
