package com.plancraft.android

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.plancraft.android.data.SampleData
import com.plancraft.android.model.*
import com.plancraft.android.ui.calendar.CalendarScreen
import com.plancraft.android.ui.economy.EconomyScreen
import com.plancraft.android.ui.projects.ProjectsScreen
import com.plancraft.android.ui.theme.IndigoPrimary
import com.plancraft.android.ui.theme.SlateSurface
import com.plancraft.android.ui.theme.SlateSurfaceVariant
import com.plancraft.android.ui.theme.TextSecondary
import com.plancraft.android.ui.timeline.TeamTimelineScreen
import com.plancraft.android.ui.components.GenericEditDialog

import com.plancraft.android.ui.settings.SettingsScreen
import com.plancraft.android.ui.user.UserScreen
import com.plancraft.android.model.User
import com.plancraft.android.model.UserRole

enum class AppDestination(val title: String, val icon: ImageVector) {
    CALENDAR("Calendar", Icons.Default.CalendarMonth),
    PROJECTS("Projects", Icons.Default.Folder),
    ECONOMY("Economy", Icons.Default.AccountBalanceWallet),
    PEOPLE("People", Icons.Default.People),
    USER("User", Icons.Default.AccountCircle),
    SETTINGS("Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanCraftApp() {
    var currentDestination by remember { mutableStateOf(AppDestination.CALENDAR) }

    // State holders initialized with rich sample data
    var tasks by remember { mutableStateOf(SampleData.sampleTasks) }
    var projects by remember { mutableStateOf(SampleData.sampleProjects) }
    var bills by remember { mutableStateOf(SampleData.sampleBills) }
    var incomes by remember { mutableStateOf(SampleData.sampleIncomes) }
    var expenses by remember { mutableStateOf(SampleData.sampleExpenses) }
    var phases by remember { mutableStateOf(SampleData.sampleTimelinePhases) }
    var members by remember { mutableStateOf(SampleData.sampleTeamMembers) }
    var budgetAllocations by remember { mutableStateOf(SampleData.sampleBudgetAllocations) }
    var isCalendarExpanded by remember { mutableStateOf(false) }

    var currentUser by remember { 
        mutableStateOf(User(name = "New User", email = "user@plancraft.io", role = UserRole.ADMIN)) 
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PlanCraft", fontWeight = FontWeight.Bold, color = Color.White) },
                actions = {
                    IconButton(onClick = { 
                        // Simulate data sync/update
                        // In a real app, this would trigger a ViewModel refresh
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Update", tint = Color.White)
                    }
                    if (currentDestination == AppDestination.CALENDAR) {
                        IconButton(onClick = { isCalendarExpanded = !isCalendarExpanded }) {
                            Icon(
                                if (isCalendarExpanded) Icons.Default.OpenInFull else Icons.Default.CloseFullscreen,
                                contentDescription = "Toggle Expand",
                                tint = IndigoSecondary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = SlateSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = SlateSurface,
                contentColor = Color.White
            ) {
                AppDestination.values().forEach { destination ->
                    val selected = currentDestination == destination
                    NavigationBarItem(
                        selected = selected,
                        onClick = { currentDestination = destination },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.title,
                                tint = if (selected) IndigoPrimary else TextSecondary
                            )
                        },
                        label = {
                            Text(
                                destination.title,
                                color = if (selected) Color.White else TextSecondary
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = SlateSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentDestination) {
                AppDestination.CALENDAR -> {
                    CalendarScreen(
                        tasks = tasks,
                        projects = projects,
                        onToggleTaskStatus = { taskId ->
                            tasks = tasks.map { task ->
                                if (task.id == taskId) {
                                    val newStatus = if (task.status == TaskStatus.DONE) TaskStatus.TODO else TaskStatus.DONE
                                    task.copy(status = newStatus)
                                } else {
                                    task
                                }
                            }
                        },
                        onAddTask = { newTask ->
                            tasks = listOf(newTask) + tasks
                        },
                        onUpdateTask = { updated -> tasks = tasks.map { if (it.id == updated.id) updated else it } },
                        isExpanded = isCalendarExpanded
                    )
                }
                AppDestination.PROJECTS -> {
                    ProjectsScreen(
                        projects = projects,
                        tasks = tasks,
                        onSelectProject = { /* Filter or navigate */ },
                        onUpdateProject = { updated -> 
                            val oldName = projects.find { it.id == updated.id }?.name
                            projects = projects.map { if (it.id == updated.id) updated else it }
                            
                            // Propagate project name changes
                            if (oldName != null && oldName != updated.name) {
                                tasks = tasks.map { 
                                    if (it.projectId == updated.id || it.projectName == oldName) 
                                        it.copy(projectName = updated.name) 
                                    else it 
                                }
                                incomes = incomes.map { 
                                    if (it.projectName == oldName) it.copy(projectName = updated.name) else it 
                                }
                                expenses = expenses.map { 
                                    if (it.projectName == oldName) it.copy(projectName = updated.name) else it 
                                }
                            }
                        },
                        onUpdateTask = { updated -> tasks = tasks.map { if (it.id == updated.id) updated else it } },
                        onAddProject = { newProj -> projects = listOf(newProj) + projects },
                        onAddTask = { newTask -> tasks = listOf(newTask) + tasks }
                    )
                }
                AppDestination.ECONOMY -> {
                    EconomyScreen(
                        bills = bills,
                        incomes = incomes,
                        expenses = expenses,
                        budgetAllocations = budgetAllocations,
                        onToggleBillPaid = { billId ->
                            bills = bills.map { bill ->
                                if (bill.id == billId) {
                                    bill.copy(isPaid = !bill.isPaid)
                                } else {
                                    bill
                                }
                            }
                        },
                        onUpdateBill = { updated -> bills = bills.map { if (it.id == updated.id) updated else it } },
                        onUpdateIncome = { updated -> incomes = incomes.map { if (it.id == updated.id) updated else it } },
                        onUpdateExpense = { updated -> expenses = expenses.map { if (it.id == updated.id) updated else it } },
                        onUpdateBudgetAllocation = { updated -> 
                            budgetAllocations = budgetAllocations.map { 
                                if (it.category == updated.category) updated else it 
                            } 
                        },
                        onAddBill = { newBill -> bills = listOf(newBill) + bills },
                        onAddIncome = { newIncome -> incomes = listOf(newIncome) + incomes },
                        onAddExpense = { newExpense -> expenses = listOf(newExpense) + expenses }
                    )
                }
                AppDestination.PEOPLE -> {
                    TeamTimelineScreen(
                        phases = phases,
                        members = members,
                        onUpdatePhase = { updated -> phases = phases.map { if (it.id == updated.id) updated else it } },
                        onUpdateMember = { updated -> 
                            val oldName = members.find { it.id == updated.id }?.name
                            members = members.map { if (it.id == updated.id) updated else it }
                            
                            // Propagate member name changes
                            if (oldName != null && oldName != updated.name) {
                                tasks = tasks.map { 
                                    if (it.assigneeId == updated.id || it.assigneeName == oldName) 
                                        it.copy(assigneeName = updated.name) 
                                    else it 
                                }
                                projects = projects.map { 
                                    if (it.leadManager == oldName) it.copy(leadManager = updated.name) else it 
                                }
                                expenses = expenses.map { 
                                    if (it.loggedBy == oldName) it.copy(loggedBy = updated.name) else it 
                                }
                            }
                        },
                        onAddPhase = { newPhase -> phases = listOf(newPhase) + phases },
                        onAddMember = { newMember -> members = listOf(newMember) + members }
                    )
                }
                AppDestination.SETTINGS -> {
                    SettingsScreen()
                }
                AppDestination.USER -> {
                    UserScreen(
                        user = currentUser,
                        onUpdateUser = { currentUser = it }
                    )
                }
            }
        }
    }
}
