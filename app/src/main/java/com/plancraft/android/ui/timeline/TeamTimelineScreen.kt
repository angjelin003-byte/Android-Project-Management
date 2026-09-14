package com.plancraft.android.ui.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plancraft.android.model.*
import com.plancraft.android.ui.theme.*
import com.plancraft.android.ui.components.GenericEditDialog

@Composable
fun TeamTimelineScreen(
    phases: List<ProjectTimelinePhase>,
    members: List<TeamMember>,
    onUpdatePhase: (ProjectTimelinePhase) -> Unit = {},
    onUpdateMember: (TeamMember) -> Unit = {},
    onAddPhase: (ProjectTimelinePhase) -> Unit = {},
    onAddMember: (TeamMember) -> Unit = {}
) {
    var editingPhase by remember { mutableStateOf<ProjectTimelinePhase?>(null) }
    var editingMember by remember { mutableStateOf<TeamMember?>(null) }
    var showAddPhase by remember { mutableStateOf(false) }
    var showAddMember by remember { mutableStateOf(false) }
    var selectedGroupFilter by remember { mutableStateOf<StakeholderGroupType?>(null) }
    var viewMode by remember { mutableStateOf(0) } // 0 = Phases Over Time, 1 = People & Groups

    val filteredMembers = if (selectedGroupFilter != null) {
        members.filter { it.group == selectedGroupFilter }
    } else {
        members
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (viewMode == 0) showAddPhase = true
                    else showAddMember = true
                },
                containerColor = IndigoPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        containerColor = SlateBackground
    ) { paddingValues ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        // Top Header
        Text(
            text = "Groups & Timeline",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Stakeholder involvement, capacity & staffing over project phases",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // View Mode Switcher
        TabRow(
            selectedTabIndex = viewMode,
            containerColor = SlateSurface,
            contentColor = Color.White,
            indicator = {},
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
        ) {
            Tab(
                selected = viewMode == 0,
                onClick = { viewMode = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timeline, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Phases Timeline")
                    }
                }
            )
            Tab(
                selected = viewMode == 1,
                onClick = { viewMode = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Groups, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Stakeholders (${members.size})")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (viewMode == 0) {
            // Timeline Phases Over Time
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(phases, key = { it.id }) { phase ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SlateCard),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = phase.quarter.uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IndigoSecondary,
                                    letterSpacing = 1.sp
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (phase.progress == 100) EmeraldSuccess.copy(alpha = 0.2f) else SlateSurfaceVariant
                                ) {
                                    Text(
                                        text = if (phase.progress == 100) "COMPLETED" else "${phase.progress}% ACTIVE",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (phase.progress == 100) EmeraldSuccess else AmberWarning,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = phase.phaseName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = { editingPhase = phase }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(16.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Duration: ${phase.startDate} → ${phase.endDate}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Key Stakeholder Groups Active in this Phase
                            Text(
                                text = "STAKEHOLDER GROUPS ENGAGED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMuted,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            FlowRowLayout(
                                groups = phase.involvedGroups
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider(color = SlateBorder)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.People, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "${phase.headCount} Assigned Contributors", fontSize = 12.sp, color = TextSecondary)
                                }
                                Text(
                                    text = "Phase Budget: $${phase.estimatedBudget.toInt()}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CyanAccent
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Stakeholder Groups & People Directory
            Column(modifier = Modifier.fillMaxSize()) {
                // Filter chips by group type
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedGroupFilter == null,
                            onClick = { selectedGroupFilter = null },
                            label = { Text("All Groups") }
                        )
                    }
                    items(StakeholderGroupType.values()) { group ->
                        FilterChip(
                            selected = selectedGroupFilter == group,
                            onClick = { selectedGroupFilter = group },
                            label = { Text(group.name.replace("_", " ").take(14)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredMembers, key = { it.id }) { member ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SlateCard),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Avatar Circle
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (member.group) {
                                                StakeholderGroupType.EXECUTIVE_LEADERSHIP -> VioletAccent
                                                StakeholderGroupType.CORE_ENGINEERING -> IndigoPrimary
                                                StakeholderGroupType.PRODUCT_DESIGN -> CyanAccent
                                                StakeholderGroupType.FINANCE_OPERATIONS -> AmberWarning
                                                else -> EmeraldSuccess
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val initials = member.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("")
                                    Text(
                                        text = initials,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = member.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        IconButton(onClick = { editingMember = member }, modifier = Modifier.size(24.dp)) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                    Text(
                                        text = member.role,
                                        fontSize = 13.sp,
                                        color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Active: ${member.activePeriod} • ${member.group.name.replace("_", " ")}",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SlateSurfaceVariant
                                    ) {
                                        Text(
                                            text = "${member.allocationPercentage}% Load",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (member.allocationPercentage >= 90) AmberWarning else EmeraldSuccess,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    if (member.hourlyRate > 0) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "$${member.hourlyRate.toInt()}/hr",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    }

    editingPhase?.let { phase ->
        GenericEditDialog(
            title = "Phase",
            fields = mapOf("name" to phase.phaseName, "budget" to phase.estimatedBudget.toString()),
            onDismiss = { editingPhase = null },
            onSave = { updated ->
                onUpdatePhase(phase.copy(
                    phaseName = updated["name"] ?: phase.phaseName,
                    estimatedBudget = updated["budget"]?.toDoubleOrNull() ?: phase.estimatedBudget
                ))
                editingPhase = null
            }
        )
    }

    editingMember?.let { member ->
        GenericEditDialog(
            title = "Team Member",
            fields = mapOf("name" to member.name, "role" to member.role, "rate" to member.hourlyRate.toString(), "allocation" to member.allocationPercentage.toString()),
            onDismiss = { editingMember = null },
            onSave = { updated ->
                onUpdateMember(member.copy(
                    name = updated["name"] ?: member.name,
                    role = updated["role"] ?: member.role,
                    hourlyRate = updated["rate"]?.toDoubleOrNull() ?: member.hourlyRate,
                    allocationPercentage = updated["allocation"]?.toIntOrNull() ?: member.allocationPercentage
                ))
                editingMember = null
            }
        )
    }

    if (showAddPhase) {
        GenericEditDialog(
            title = "Add Phase",
            fields = mapOf("name" to "", "budget" to "0", "quarter" to "Q1"),
            onDismiss = { showAddPhase = false },
            onSave = { fields ->
                val newPhase = ProjectTimelinePhase(
                    id = "phase-${System.currentTimeMillis()}",
                    projectId = "none",
                    phaseName = fields["name"] ?: "",
                    startDate = "2026-09-01",
                    endDate = "2026-09-30",
                    progress = 0,
                    involvedGroups = listOf(StakeholderGroupType.CORE_ENGINEERING),
                    headCount = 1,
                    estimatedBudget = fields["budget"]?.toDoubleOrNull() ?: 0.0,
                    quarter = fields["quarter"] ?: "Q1"
                )
                onAddPhase(newPhase)
                showAddPhase = false
            }
        )
    }

    if (showAddMember) {
        GenericEditDialog(
            title = "Add Member",
            fields = mapOf("name" to "", "role" to "", "rate" to "0", "allocation" to "100"),
            onDismiss = { showAddMember = false },
            onSave = { fields ->
                val newMem = TeamMember(
                    id = "mem-${System.currentTimeMillis()}",
                    name = fields["name"] ?: "",
                    role = fields["role"] ?: "",
                    group = StakeholderGroupType.CORE_ENGINEERING,
                    activePeriod = "2026",
                    allocationPercentage = fields["allocation"]?.toIntOrNull() ?: 100,
                    hourlyRate = fields["rate"]?.toDoubleOrNull() ?: 0.0,
                    contactEmail = null
                )
                onAddMember(newMem)
                showAddMember = false
            }
        )
    }
}

@Composable
fun FlowRowLayout(groups: List<StakeholderGroupType>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(groups) { group ->
            val color = when (group) {
                StakeholderGroupType.EXECUTIVE_LEADERSHIP -> VioletAccent
                StakeholderGroupType.CORE_ENGINEERING -> IndigoSecondary
                StakeholderGroupType.PRODUCT_DESIGN -> CyanAccent
                StakeholderGroupType.FINANCE_OPERATIONS -> AmberWarning
                StakeholderGroupType.EXTERNAL_CONSULTANTS -> RoseDanger
                StakeholderGroupType.CLIENT_STAKEHOLDERS -> EmeraldSuccess
                StakeholderGroupType.QUALITY_ASSURANCE -> TextSecondary
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = color.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f))
            ) {
                Text(
                    text = group.name.replace("_", " "),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = color,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
