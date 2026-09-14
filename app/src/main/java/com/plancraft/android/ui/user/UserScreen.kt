package com.plancraft.android.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plancraft.android.model.User
import com.plancraft.android.model.UserRole
import com.plancraft.android.model.UserStatus
import com.plancraft.android.ui.theme.*

@Composable
fun UserScreen(user: User, onUpdateUser: (User) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            ProfileHeader(user)
        }

        item {
            UserStatsRow()
        }

        item {
            AccountSection(user, onUpdateUser)
        }

        item {
            SecuritySection()
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Sign Out logic */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sign Out of Session", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileHeader(user: User) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        color = SlateCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    modifier = Modifier.size(90.dp),
                    shape = CircleShape,
                    color = SlateSurfaceVariant
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = IndigoSecondary,
                        modifier = Modifier.size(50.dp).padding(16.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            when (user.status) {
                                UserStatus.ONLINE -> EmeraldSuccess
                                UserStatus.AWAY -> AmberWarning
                                else -> TextMuted
                            }
                        )
                        .border(3.dp, SlateCard, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = user.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = user.role.name.lowercase().replaceFirstChar { it.uppercase() }, color = IndigoSecondary, fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.bio,
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun UserStatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(modifier = Modifier.weight(1f), label = "Projects Led", value = "0", icon = Icons.Default.FolderOpen)
        StatCard(modifier = Modifier.weight(1f), label = "Active Tasks", value = "0", icon = Icons.Default.Assignment)
    }
}

@Composable
fun StatCard(modifier: Modifier, label: String, value: String, icon: ImageVector) {
    Surface(
        modifier = modifier,
        color = SlateCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = label, fontSize = 11.sp, color = TextMuted)
        }
    }
}

@Composable
fun AccountSection(user: User, onUpdateUser: (User) -> Unit) {
    Column {
        Text(
            text = "ACCOUNT SETTINGS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SlateCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                AccountItem(Icons.Default.Email, "Email", user.email)
                Divider(color = SlateBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                AccountItem(Icons.Default.Business, "Department", user.department)
                Divider(color = SlateBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                AccountItem(Icons.Default.Language, "Language", "English (US)")
            }
        }
    }
}

@Composable
fun SecuritySection() {
    Column {
        Text(
            text = "SECURITY & PRIVACY",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp, top = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SlateCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                AccountItem(Icons.Default.Lock, "Change Password", "Updated 2m ago")
                Divider(color = SlateBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                AccountItem(Icons.Default.VpnKey, "Two-Factor Auth", "Enabled")
                Divider(color = SlateBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                AccountItem(Icons.Default.Visibility, "Profile Visibility", "Team Only")
            }
        }
    }
}

@Composable
fun AccountItem(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Edit Item */ }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = IndigoSecondary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, color = Color.White, fontSize = 14.sp)
        }
        Text(text = value, color = TextSecondary, fontSize = 13.sp)
    }
}
