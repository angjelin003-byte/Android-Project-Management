package com.plancraft.android.ui.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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

@Composable
fun ProjectsScreen(
    projects: List<Project>,
    tasks: List<Task>,
    onSelectProject: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Project Portfolios, 1 = Kanban Board

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Project Portfolios",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Active initiatives, milestones & delivery roadmaps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // View Mode Switcher
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SlateSurface,
            contentColor = Color.White,
            indicator = {},
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Layers, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Portfolios (${projects.size})")
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ViewKanban, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Kanban Board")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            // Portfolio Overview Cards
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(projects, key = { it.id }) { project ->
                    val projectTasks = tasks.filter { it.projectId == project.id }
                    val completedTasks = projectTasks.count { it.status == TaskStatus.DONE }
                    val budgetPercent = if (project.totalBudget > 0) ((project.totalSpent / project.totalBudget) * 100).toInt() else 0

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SlateCard),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
                            .clickable { onSelectProject(project.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = project.client.uppercase(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CyanAccent,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = project.name,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (project.status) {
                                        ProjectStatus.IN_PROGRESS -> IndigoPrimary.copy(alpha = 0.2f)
                                        ProjectStatus.COMPLETED -> EmeraldSuccess.copy(alpha = 0.2f)
                                        ProjectStatus.PLANNING -> CyanAccent.copy(alpha = 0.2f)
                                        ProjectStatus.ON_HOLD -> AmberWarning.copy(alpha = 0.2f)
                                    }
                                ) {
                                    Text(
                                        text = project.status.name.replace("_", " "),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = when (project.status) {
                                            ProjectStatus.IN_PROGRESS -> IndigoSecondary
                                            ProjectStatus.COMPLETED -> EmeraldSuccess
                                            ProjectStatus.PLANNING -> CyanAccent
                                            ProjectStatus.ON_HOLD -> AmberWarning
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = project.description,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Completion Progress Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Milestone Completion",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "${project.completionPercentage}%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = project.completionPercentage / 100f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = IndigoPrimary,
                                trackColor = SlateSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Economy & Budget Snapshot inside Project Card
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SlateSurfaceVariant)
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "BUDGET ALLOCATED", fontSize = 10.sp, color = TextMuted)
                                    Text(text = "$${project.totalBudget.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Column {
                                    Text(text = "CURRENT SPENT", fontSize = 10.sp, color = TextMuted)
                                    Text(text = "$${project.totalSpent.toInt()} (${budgetPercent}%)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (budgetPercent > 85) AmberWarning else EmeraldSuccess)
                                }
                                Column {
                                    Text(text = "TASKS DONE", fontSize = 10.sp, color = TextMuted)
                                    Text(text = "$completedTasks / ${projectTasks.size}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Project Manager & Timeline
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = project.leadManager, fontSize = 12.sp, color = TextSecondary)
                                }

                                Text(
                                    text = "${project.startDate} → ${project.targetEndDate}",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Kanban View across Status Columns
            KanbanView(tasks = tasks)
        }
    }
}

@Composable
fun KanbanView(tasks: List<Task>) {
    val columns = listOf(
        TaskStatus.TODO to "To Do",
        TaskStatus.IN_PROGRESS to "In Progress",
        TaskStatus.IN_REVIEW to "In Review",
        TaskStatus.DONE to "Completed"
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(columns) { (status, title) ->
            val colTasks = tasks.filter { it.status == status }

            Column(
                modifier = Modifier
                    .width(260.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SlateSurface)
                    .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Surface(
                        shape = CircleShape,
                        color = SlateSurfaceVariant
                    ) {
                        Text(
                            text = colTasks.size.toString(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = SlateBorder)
                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(colTasks, key = { it.id }) { task ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = SlateCard),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = task.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = task.projectName,
                                    fontSize = 11.sp,
                                    color = CyanAccent
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = task.assigneeName, fontSize = 11.sp, color = TextMuted)
                                    Text(text = task.date, fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
