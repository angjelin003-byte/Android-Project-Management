package com.plancraft.android

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.plancraft.android.data.SampleData
import com.plancraft.android.data.PlanCraftStorage
import com.plancraft.android.model.*
import com.plancraft.android.ui.calendar.CalendarScreen
import com.plancraft.android.ui.economy.EconomyScreen
import com.plancraft.android.ui.projects.ProjectsScreen
import com.plancraft.android.ui.theme.PlanCraftTheme
import com.plancraft.android.ui.timeline.TeamTimelineScreen
import com.plancraft.android.ui.settings.SettingsScreen
import com.plancraft.android.ui.user.UserScreen
import com.plancraft.android.ui.theme.*

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
    val context = LocalContext.current
    var currentDestination by remember { mutableStateOf(AppDestination.CALENDAR) }

    var isDarkTheme by remember { mutableStateOf(PlanCraftStorage.loadTheme(context, true)) }

    // State holders initialized with stored data or rich sample data
    var tasks by remember { mutableStateOf(PlanCraftStorage.loadTasks(context) ?: SampleData.sampleTasks) }
    var projects by remember { mutableStateOf(PlanCraftStorage.loadProjects(context) ?: SampleData.sampleProjects) }
    var bills by remember { mutableStateOf(PlanCraftStorage.loadBills(context) ?: SampleData.sampleBills) }
    var incomes by remember { mutableStateOf(PlanCraftStorage.loadIncomes(context) ?: SampleData.sampleIncomes) }
    var expenses by remember { mutableStateOf(PlanCraftStorage.loadExpenses(context) ?: SampleData.sampleExpenses) }
    var phases by remember { mutableStateOf(PlanCraftStorage.loadPhases(context) ?: SampleData.sampleTimelinePhases) }
    var members by remember { mutableStateOf(PlanCraftStorage.loadMembers(context) ?: SampleData.sampleTeamMembers) }
    var budgetAllocations by remember { mutableStateOf(SampleData.sampleBudgetAllocations) }
    var isCalendarExpanded by remember { mutableStateOf(false) }

    var currentUser by remember { 
        mutableStateOf(User(name = "New User", email = "user@plancraft.io", role = UserRole.ADMIN)) 
    }

    // Save data whenever it updates
    LaunchedEffect(tasks) { PlanCraftStorage.saveTasks(context, tasks) }
    LaunchedEffect(projects) { PlanCraftStorage.saveProjects(context, projects) }
    LaunchedEffect(bills) { PlanCraftStorage.saveBills(context, bills) }
    LaunchedEffect(incomes) { PlanCraftStorage.saveIncomes(context, incomes) }
    LaunchedEffect(expenses) { PlanCraftStorage.saveExpenses(context, expenses) }
    LaunchedEffect(phases) { PlanCraftStorage.savePhases(context, phases) }
    LaunchedEffect(members) { PlanCraftStorage.saveMembers(context, members) }

    PlanCraftTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("PlanCraft", fontWeight = FontWeight.Bold, color = Color.White) },
                    actions = {
                        IconButton(onClick = { 
                            // Update button toggles theme between light and dark and persists it
                            isDarkTheme = !isDarkTheme
                            PlanCraftStorage.saveTheme(context, isDarkTheme)
                        }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Update Theme & Sync", tint = Color.White)
                        }
                        if (currentDestination == AppDestination.CALENDAR) {
                            IconButton(onClick = { isCalendarExpanded = !isCalendarExpanded }) {
                                Icon(
                                    if (isCalendarExpanded) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
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
                            onDeleteTask = { id -> tasks = tasks.filter { it.id != id } },
                            onUpdateProject = { updated -> projects = projects.map { if (it.id == updated.id) updated else it } },
                            onDeleteProject = { id -> projects = projects.filter { it.id != id } },
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
                            onDeleteProject = { id -> projects = projects.filter { it.id != id } },
                            onUpdateTask = { updated -> tasks = tasks.map { if (it.id == updated.id) updated else it } },
                            onDeleteTask = { id -> tasks = tasks.filter { it.id != id } },
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
                            onDeleteBill = { id -> bills = bills.filter { it.id != id } },
                            onUpdateIncome = { updated -> incomes = incomes.map { if (it.id == updated.id) updated else it } },
                            onDeleteIncome = { id -> incomes = incomes.filter { it.id != id } },
                            onUpdateExpense = { updated -> expenses = expenses.map { if (it.id == updated.id) updated else it } },
                            onDeleteExpense = { id -> expenses = expenses.filter { it.id != id } },
                            onUpdateBudgetAllocation = { updated -> 
                                budgetAllocations = budgetAllocations.map { 
                                    if (it.category == updated.category) updated else it 
                                } 
                            },
                            onDeleteBudgetAllocation = { cat -> budgetAllocations = budgetAllocations.filter { it.category != cat } },
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
                            onDeletePhase = { id -> phases = phases.filter { it.id != id } },
                            onUpdateMember = { updated -> 
                                val oldName = members.find { it.id == updated.id }?.name
                                members = members.map { if (it.id == updated.id) updated else it }
                                
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
                            onDeleteMember = { id -> members = members.filter { it.id != id } },
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
}
