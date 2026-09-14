package com.plancraft.android.model

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val role: UserRole = UserRole.CONTRIBUTOR,
    val department: String = "Engineering",
    val avatarUrl: String? = null,
    val status: UserStatus = UserStatus.ONLINE,
    val bio: String = "Productive contributor to the PlanCraft ecosystem."
)

enum class UserRole {
    ADMIN, MANAGER, CONTRIBUTOR, VIEW_ONLY
}

enum class UserStatus {
    ONLINE, AWAY, BUSY, OFFLINE
}
