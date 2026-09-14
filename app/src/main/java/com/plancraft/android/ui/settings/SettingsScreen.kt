package com.plancraft.android.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plancraft.android.ui.theme.*

@Composable
fun SettingsScreen() {
    var currency by remember { mutableStateOf("USD ($)") }
    var dateFormat by remember { mutableStateOf("YYYY-MM-DD") }
    var themeMode by remember { mutableStateOf("Dark Mode") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                SettingsSection(title = "App Appearance") {
                    SettingsItem(
                        icon = Icons.Default.ColorLens,
                        title = "Active Theme",
                        value = themeMode,
                        onClick = { 
                            themeMode = if (themeMode == "Dark Mode") "Light Mode" else "Dark Mode"
                        }
                    )
                }
            }

            item {
                SettingsSection(title = "Regional & Formats") {
                    SettingsItem(
                        icon = Icons.Default.Payments,
                        title = "Default Currency",
                        value = currency,
                        onClick = {
                            currency = when (currency) {
                                "USD ($)" -> "EUR (€)"
                                "EUR (€)" -> "GBP (£)"
                                else -> "USD ($)"
                            }
                        }
                    )
                    Divider(color = SlateBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                    SettingsItem(
                        icon = Icons.Default.DateRange,
                        title = "Date Format",
                        value = dateFormat,
                        onClick = {
                            dateFormat = when (dateFormat) {
                                "YYYY-MM-DD" -> "DD/MM/YYYY"
                                "DD/MM/YYYY" -> "MM-DD-YYYY"
                                else -> "YYYY-MM-DD"
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title.uppercase(),
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
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SlateSurfaceVariant,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = IndigoSecondary, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, color = Color.White, fontSize = 15.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, color = TextSecondary, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
        }
    }
}
