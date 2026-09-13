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

enum class AppDestination(val title: String, val icon: ImageVector) {
    CALENDAR("Calendar", Icons.Default.CalendarMonth),
    PROJECTS("Projects", Icons.Default.Folder),
    ECONOMY("Economy", Icons.Default.AccountBalanceWallet),
    PEOPLE("People", Icons.Default.People)
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
    val phases by remember { mutableStateOf(SampleData.sampleTimelinePhases) }
    val members by remember { mutableStateOf(SampleData.sampleTeamMembers) }

    Scaffold(
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
                        onUpdateTask = { updated -> tasks = tasks.map { if (it.id == updated.id) updated else it } }
                    )
                }
                AppDestination.PROJECTS -> {
                    ProjectsScreen(
                        projects = projects,
                        tasks = tasks,
                        onSelectProject = { /* Filter or navigate */ },
                        onUpdateProject = { updated -> projects = projects.map { if (it.id == updated.id) updated else it } },
                        onUpdateTask = { updated -> tasks = tasks.map { if (it.id == updated.id) updated else it } }
                    )
                }
                AppDestination.ECONOMY -> {
                    EconomyScreen(
                        bills = bills,
                        incomes = incomes,
                        expenses = expenses,
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
                        onUpdateExpense = { updated -> expenses = expenses.map { if (it.id == updated.id) updated else it } }
                    )
                }
                AppDestination.PEOPLE -> {
                    TeamTimelineScreen(
                        phases = phases,
                        members = members,
                        onUpdatePhase = { updated -> phases = phases.map { if (it.id == updated.id) updated else it } },
                        onUpdateMember = { updated -> members = members.map { if (it.id == updated.id) updated else it } }
                    )
                }
            }
        }
    }
}
