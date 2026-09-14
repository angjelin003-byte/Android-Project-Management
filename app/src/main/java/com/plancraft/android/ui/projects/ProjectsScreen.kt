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
import com.plancraft.android.model.Project
import com.plancraft.android.model.ProjectPriority
import com.plancraft.android.model.ProjectStatus
import com.plancraft.android.model.Task
import com.plancraft.android.model.TaskPriority
import com.plancraft.android.model.TaskStatus
import com.plancraft.android.ui.theme.*
import com.plancraft.android.ui.components.GenericEditDialog

@Composable
fun ProjectsScreen(
    projects: List<Project>,
    tasks: List<Task>,
    onSelectProject: (String) -> Unit,
    onUpdateProject: (Project) -> Unit = {},
    onUpdateTask: (Task) -> Unit = {},
    onAddProject: (Project) -> Unit = {},
    onAddTask: (Task) -> Unit = {}
) {
    var editingProject by remember { mutableStateOf<Project?>(null) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var showAddProject by remember { mutableStateOf(false) }
    var showAddTask by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Project Portfolios, 1 = Kanban Board

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = { showAddProject = true },
                    containerColor = IndigoPrimary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Project")
                }
            } else if (selectedTab == 1) {
                FloatingActionButton(
                    onClick = { showAddTask = true },
                    containerColor = IndigoPrimary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
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
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = project.name,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
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
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(onClick = { editingProject = project }, modifier = Modifier.size(28.dp)) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(18.dp))
                                    }
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
            KanbanView(tasks = tasks, onEditTask = { editingTask = it })
        }
    }
    }

    editingProject?.let { proj ->
        GenericEditDialog(
            title = "Project",
            fields = mapOf(
                "name" to proj.name, 
                "client" to proj.client, 
                "budget" to proj.totalBudget.toString(),
                "description" to proj.description,
                "startDate" to proj.startDate,
                "endDate" to proj.targetEndDate,
                "manager" to proj.leadManager,
                "completion" to proj.completionPercentage.toString()
            ),
            onDismiss = { editingProject = null },
            onSave = { updated ->
                onUpdateProject(proj.copy(
                    name = updated["name"] ?: proj.name,
                    client = updated["client"] ?: proj.client,
                    totalBudget = updated["budget"]?.toDoubleOrNull() ?: proj.totalBudget,
                    description = updated["description"] ?: proj.description,
                    startDate = updated["startDate"] ?: proj.startDate,
                    targetEndDate = updated["endDate"] ?: proj.targetEndDate,
                    leadManager = updated["manager"] ?: proj.leadManager,
                    completionPercentage = updated["completion"]?.toIntOrNull() ?: proj.completionPercentage
                ))
                editingProject = null
            }
        )
    }

    editingTask?.let { task ->
        GenericEditDialog(
            title = "Task",
            fields = mapOf(
                "title" to task.title, 
                "description" to task.description,
                "assignee" to task.assigneeName,
                "date" to task.date,
                "hours" to task.durationHours.toString(),
                "cost" to task.costImpact.toString()
            ),
            onDismiss = { editingTask = null },
            onSave = { updated ->
                onUpdateTask(task.copy(
                    title = updated["title"] ?: task.title,
                    description = updated["description"] ?: task.description,
                    assigneeName = updated["assignee"] ?: task.assigneeName,
                    date = updated["date"] ?: task.date,
                    durationHours = updated["hours"]?.toDoubleOrNull() ?: task.durationHours,
                    costImpact = updated["cost"]?.toDoubleOrNull() ?: task.costImpact
                ))
                editingTask = null
            }
        )
    }

    if (showAddProject) {
        GenericEditDialog(
            title = "Add Project",
            fields = mapOf("name" to "", "client" to "", "budget" to "0", "description" to ""),
            onDismiss = { showAddProject = false },
            onSave = { fields ->
                val newProject = Project(
                    id = "proj-${System.currentTimeMillis()}",
                    name = fields["name"] ?: "New Project",
                    client = fields["client"] ?: "",
                    description = fields["description"] ?: "",
                    status = ProjectStatus.PLANNING,
                    priority = ProjectPriority.MEDIUM,
                    startDate = "2026-09-01",
                    targetEndDate = "2026-12-31",
                    totalBudget = fields["budget"]?.toDoubleOrNull() ?: 0.0,
                    totalSpent = 0.0,
                    leadManager = "Unassigned",
                    completionPercentage = 0,
                    tags = listOf()
                )
                onAddProject(newProject)
                showAddProject = false
            }
        )
    }

    if (showAddTask) {
        GenericEditDialog(
            title = "Add Task",
            fields = mapOf("title" to "", "description" to ""),
            onDismiss = { showAddTask = false },
            onSave = { fields ->
                val newTask = Task(
                    id = "task-${System.currentTimeMillis()}",
                    projectId = "none",
                    projectName = "Unassigned",
                    title = fields["title"] ?: "New Task",
                    description = fields["description"] ?: "",
                    date = "2026-09-01",
                    time = "12:00 PM",
                    durationHours = 1.0,
                    status = TaskStatus.TODO,
                    priority = TaskPriority.MEDIUM,
                    assigneeId = "none",
                    assigneeName = "Unassigned",
                    costImpact = 0.0,
                    milestone = null,
                    isBillable = false
                )
                onAddTask(newTask)
                showAddTask = false
            }
        )
    }
}

@Composable
fun KanbanView(tasks: List<Task>, onEditTask: (Task) -> Unit = {}) {
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
                HorizontalDivider(color = SlateBorder)
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = task.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = { onEditTask(task) }, modifier = Modifier.size(24.dp)) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(14.dp))
                                    }
                                }
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
