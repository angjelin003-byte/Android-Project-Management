package com.plancraft.android.ui.calendar

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
import com.plancraft.android.model.Task
import com.plancraft.android.model.TaskPriority
import com.plancraft.android.model.TaskStatus
import com.plancraft.android.ui.theme.*
import com.plancraft.android.ui.components.GenericEditDialog

@Composable
fun CalendarScreen(
    tasks: List<Task>,
    projects: List<Project>,
    onToggleTaskStatus: (String) -> Unit,
    onAddTask: (Task) -> Unit,
    onUpdateTask: (Task) -> Unit = {},
    onDeleteTask: (String) -> Unit = {},
    onUpdateProject: (Project) -> Unit = {},
    onDeleteProject: (String) -> Unit = {},
    isExpanded: Boolean = false
) {
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var editingProjectFromChip by remember { mutableStateOf<Project?>(null) }
    var selectedDate by remember { mutableStateOf("2026-09-14") }
    var selectedProjectFilter by remember { mutableStateOf<String?>("All") }
    var showAddTaskDialog by remember { mutableStateOf(false) }

    if (isExpanded) {
        YearView(tasks = tasks, onSelectDate = { selectedDate = it })
    } else {
        // Calendar dates for September 2026
    val calendarDays = listOf(
        "2026-09-11" to "Fri 11",
        "2026-09-12" to "Sat 12",
        "2026-09-13" to "Sun 13",
        "2026-09-14" to "Mon 14",
        "2026-09-15" to "Tue 15",
        "2026-09-16" to "Wed 16",
        "2026-09-17" to "Thu 17",
        "2026-09-18" to "Fri 18",
        "2026-09-19" to "Sat 19",
        "2026-09-20" to "Sun 20",
        "2026-09-21" to "Mon 21",
        "2026-09-22" to "Tue 22",
        "2026-09-23" to "Wed 23",
        "2026-09-24" to "Thu 24",
        "2026-09-25" to "Fri 25"
    )

    val filteredTasks = tasks.filter { task ->
        (selectedProjectFilter == "All" || task.projectId == selectedProjectFilter) &&
        task.date == selectedDate
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true },
                containerColor = IndigoPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        containerColor = SlateBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Project Calendar",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "September 2026 • Sprint & Deliverable Schedule",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SlateSurfaceVariant,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = IndigoSecondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Sep 2026", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Horizontal Calendar Bar
            Text(
                text = "SELECT DATE",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(calendarDays) { (dateKey, label) ->
                    val isSelected = selectedDate == dateKey
                    val tasksOnDate = tasks.count { it.date == dateKey }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(64.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) IndigoPrimary else SlateCard)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) IndigoSecondary else SlateBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedDate = dateKey }
                            .padding(vertical = 10.dp, horizontal = 4.dp)
                    ) {
                        val parts = label.split(" ")
                        Text(
                            text = parts[0].uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color.White.copy(alpha = 0.8f) else TextSecondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = parts[1],
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (tasksOnDate > 0) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Color.White else AmberWarning)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Project Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedProjectFilter == "All",
                        onClick = { selectedProjectFilter = "All" },
                        label = { Text("All Projects (${tasks.size})") }
                    )
                }
                items(projects) { proj ->
                    var chipMenuOpen by remember { mutableStateOf(false) }
                    FilterChip(
                        selected = selectedProjectFilter == proj.id,
                        onClick = { selectedProjectFilter = proj.id },
                        label = { Text(proj.name.take(16) + if (proj.name.length > 16) "..." else "") },
                        trailingIcon = {
                            Box {
                                IconButton(
                                    onClick = { chipMenuOpen = true },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit or Delete minitab",
                                        tint = if (selectedProjectFilter == proj.id) IndigoPrimary else TextSecondary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = chipMenuOpen,
                                    onDismissRequest = { chipMenuOpen = false },
                                    modifier = Modifier.background(SlateSurface)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Edit Project", color = TextPrimary) },
                                        onClick = {
                                            chipMenuOpen = false
                                            editingProjectFromChip = proj
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = null,
                                                tint = IndigoSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Delete Project", color = RoseDanger) },
                                        onClick = {
                                            chipMenuOpen = false
                                            onDeleteProject(proj.id)
                                            if (selectedProjectFilter == proj.id) {
                                                selectedProjectFilter = "All"
                                            }
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = RoseDanger,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Task List for Selected Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Scheduled Tasks (${filteredTasks.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                Text(
                    text = "Date: $selectedDate",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SlateCard)
                        .border(1.dp, SlateBorder, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No tasks due on $selectedDate",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Tap the '+' button to schedule a new task.",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredTasks, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            onToggle = { onToggleTaskStatus(task.id) },
                            onEdit = { editingTask = task },
                            onDelete = { onDeleteTask(task.id) }
                        )
                    }
                }
            }
        }
    }
    }

    editingProjectFromChip?.let { proj ->
        GenericEditDialog(
            title = "Project",
            fields = mapOf(
                "name" to proj.name,
                "client" to proj.client,
                "description" to proj.description,
                "budget" to proj.totalBudget.toString()
            ),
            onDismiss = { editingProjectFromChip = null },
            onSave = { updated ->
                onUpdateProject(proj.copy(
                    name = updated["name"] ?: proj.name,
                    client = updated["client"] ?: proj.client,
                    description = updated["description"] ?: proj.description,
                    totalBudget = updated["budget"]?.toDoubleOrNull() ?: proj.totalBudget
                ))
                editingProjectFromChip = null
            },
            onDelete = {
                onDeleteProject(proj.id)
                if (selectedProjectFilter == proj.id) {
                    selectedProjectFilter = "All"
                }
                editingProjectFromChip = null
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
                "time" to (task.time ?: ""),
                "hours" to task.durationHours.toString(),
                "cost" to task.costImpact.toString(),
                "milestone" to (task.milestone ?: "")
            ),
            onDismiss = { editingTask = null },
            onSave = { updated ->
                onUpdateTask(task.copy(
                    title = updated["title"] ?: task.title,
                    description = updated["description"] ?: task.description,
                    assigneeName = updated["assignee"] ?: task.assigneeName,
                    date = updated["date"] ?: task.date,
                    time = updated["time"]?.ifBlank { null },
                    durationHours = updated["hours"]?.toDoubleOrNull() ?: task.durationHours,
                    costImpact = updated["cost"]?.toDoubleOrNull() ?: task.costImpact,
                    milestone = updated["milestone"]?.ifBlank { null }
                ))
                editingTask = null
            },
            onDelete = {
                onDeleteTask(task.id)
                editingTask = null
            }
        )
    }

    if (showAddTaskDialog) {
        AddTaskDialog(
            selectedDate = selectedDate,
            projects = projects,
            onDismiss = { showAddTaskDialog = false },
            onSave = { newTask: Task ->
                onAddTask(newTask)
                showAddTaskDialog = false
            }
        )
    }
}

@Composable
fun YearView(tasks: List<Task>, onSelectDate: (String) -> Unit) {
    val months = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "Annual Project Velocity",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Status heatmap and delivery roadmap for 2026",
                fontSize = 12.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(months.chunked(3)) { rowMonths ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowMonths.forEach { month ->
                    MonthGrid(month = month, tasks = tasks, onSelectDate = onSelectDate, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun MonthGrid(month: String, tasks: List<Task>, onSelectDate: (String) -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SlateCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = month, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IndigoSecondary)
            Spacer(modifier = Modifier.height(6.dp))
            
            // Simplified 5x7 grid for visualization
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(5) { rowIndex ->
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(7) { colIndex ->
                            val day = rowIndex * 7 + colIndex + 1
                            if (day <= 30) {
                                // Mock date mapping
                                val date = "2026-09-${day.toString().padStart(2, '0')}"
                                val tasksOnDay = tasks.count { it.date == date }
                                
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(
                                            when {
                                                tasksOnDay > 2 -> EmeraldSuccess
                                                tasksOnDay > 0 -> IndigoPrimary
                                                else -> SlateSurfaceVariant
                                            }
                                        )
                                        .clickable { onSelectDate(date) }
                                )
                            } else {
                                Spacer(modifier = Modifier.size(10.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onToggle: () -> Unit,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val isDone = task.status == TaskStatus.DONE
    val priorityColor = when (task.priority) {
        TaskPriority.URGENT -> RoseDanger
        TaskPriority.HIGH -> AmberWarning
        TaskPriority.MEDIUM -> CyanAccent
        TaskPriority.LOW -> TextSecondary
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Priority indicator dot + Project name
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(priorityColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task.projectName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = IndigoSecondary
                    )
                }

                // Right edge: Status badge + Pencil button with Edit & Delete options
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when (task.status) {
                            TaskStatus.DONE -> EmeraldSuccess.copy(alpha = 0.2f)
                            TaskStatus.IN_PROGRESS -> IndigoPrimary.copy(alpha = 0.2f)
                            TaskStatus.IN_REVIEW -> VioletAccent.copy(alpha = 0.2f)
                            else -> SlateSurfaceVariant
                        }
                    ) {
                        Text(
                            text = task.status.name.replace("_", " "),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when (task.status) {
                                TaskStatus.DONE -> EmeraldSuccess
                                TaskStatus.IN_PROGRESS -> IndigoSecondary
                                TaskStatus.IN_REVIEW -> VioletAccent
                                else -> TextSecondary
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    var taskMenuOpen by remember { mutableStateOf(false) }
                    Box {
                        IconButton(
                            onClick = { taskMenuOpen = true },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit or Delete minitab",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = taskMenuOpen,
                            onDismissRequest = { taskMenuOpen = false },
                            modifier = Modifier.background(SlateSurface)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit Task", color = TextPrimary) },
                                onClick = {
                                    taskMenuOpen = false
                                    onEdit()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = IndigoSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete Task", color = RoseDanger) },
                                onClick = {
                                    taskMenuOpen = false
                                    onDelete()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = RoseDanger,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
                Checkbox(
                    checked = isDone,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = EmeraldSuccess,
                        uncheckedColor = SlateBorder
                    )
                )

                Spacer(modifier = Modifier.width(6.dp))

                Column {
                    Text(
                        text = task.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isDone) TextMuted else TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = task.assigneeName, fontSize = 12.sp, color = TextSecondary)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${task.durationHours}h (${task.time ?: "Anytime"})", fontSize = 12.sp, color = TextSecondary)

                    if (task.costImpact > 0) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "$${task.costImpact.toInt()}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AmberWarning
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddTaskDialog(
    selectedDate: String,
    projects: List<Project>,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedProject by remember { mutableStateOf(projects.firstOrNull()?.id ?: "proj-1") }
    var priority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var hours by remember { mutableStateOf("3.0") }
    var cost by remember { mutableStateOf("450") }
    var assigneeName by remember { mutableStateOf("Unassigned") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule Task for $selectedDate", color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Simple Project Selector
                Text("Select Project:", color = TextSecondary, fontSize = 12.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(projects) { proj ->
                        val isSelected = selectedProject == proj.id
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) IndigoPrimary else SlateSurfaceVariant,
                            modifier = Modifier.clickable { selectedProject = proj.id }
                        ) {
                            Text(
                                proj.name.take(15), 
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = assigneeName,
                    onValueChange = { assigneeName = it },
                    label = { Text("Assignee Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = hours,
                        onValueChange = { hours = it },
                        label = { Text("Est. Hours") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = cost,
                        onValueChange = { cost = it },
                        label = { Text("Cost Impact ($)") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val proj = projects.find { it.id == selectedProject } ?: projects.first()
                        val newTask = Task(
                            id = "task-${System.currentTimeMillis()}",
                            projectId = proj.id,
                            projectName = proj.name,
                            title = title,
                            description = description,
                            date = selectedDate,
                            time = "09:00 AM",
                            durationHours = hours.toDoubleOrNull() ?: 2.0,
                            status = TaskStatus.TODO,
                            priority = priority,
                            assigneeId = "user-new",
                            assigneeName = assigneeName,
                            costImpact = cost.toDoubleOrNull() ?: 0.0,
                            isBillable = true
                        )
                        onSave(newTask)
                    }
                }
            ) {
                Text("Schedule Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        containerColor = SlateSurface
    )
}
