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
import com.plancraft.android.model.*
import com.plancraft.android.ui.theme.*

@Composable
fun CalendarScreen(
    tasks: List<Task>,
    projects: List<Project>,
    onToggleTaskStatus: (String) -> Unit,
    onAddTask: (Task) -> Unit
) {
    var selectedDate by remember { mutableStateOf("2026-09-14") }
    var selectedProjectFilter by remember { mutableStateOf<String?>("All") }
    var showAddTaskDialog by remember { mutableStateOf(false) }

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
                        color = Color.White,
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
                        Text(text = "Sep 2026", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
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
                        horizontalAlignment = Alignment.CenterAlignmentLine(Alignment.CenterVertically),
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
                            color = Color.White
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
                    FilterChip(
                        selected = selectedProjectFilter == proj.id,
                        onClick = { selectedProjectFilter = proj.id },
                        label = { Text(proj.name.take(18) + if (proj.name.length > 18) "..." else "") }
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
                    color = Color.White
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
                            color = Color.White,
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
                        TaskCard(task = task, onToggle = { onToggleTaskStatus(task.id) })
                    }
                }
            }
        }
    }

    if (showAddTaskDialog) {
        AddTaskDialog(
            selectedDate = selectedDate,
            projects = projects,
            onDismiss = { showAddTaskDialog = false },
            onSave = { newTask ->
                onAddTask(newTask)
                showAddTaskDialog = false
            }
        )
    }
}

@Composable
fun TaskCard(
    task: Task,
    onToggle: () -> Unit
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
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                        color = if (isDone) TextMuted else Color.White
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
            Divider(color = SlateBorder)
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
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
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
                            assigneeId = "user-1",
                            assigneeName = "Kaelen Voss",
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
