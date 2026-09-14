package com.plancraft.android.ui.economy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun EconomyScreen(
    bills: List<Bill>,
    incomes: List<Income>,
    expenses: List<Expense>,
    budgetAllocations: List<BudgetAllocation> = emptyList(),
    onToggleBillPaid: (String) -> Unit,
    onUpdateBill: (Bill) -> Unit = {},
    onUpdateIncome: (Income) -> Unit = {},
    onUpdateExpense: (Expense) -> Unit = {},
    onUpdateBudgetAllocation: (BudgetAllocation) -> Unit = {},
    onAddBill: (Bill) -> Unit = {},
    onAddIncome: (Income) -> Unit = {},
    onAddExpense: (Expense) -> Unit = {}
) {
    var editingBill by remember { mutableStateOf<Bill?>(null) }
    var editingIncome by remember { mutableStateOf<Income?>(null) }
    var editingExpense by remember { mutableStateOf<Expense?>(null) }
    var editingBudgetAllocation by remember { mutableStateOf<BudgetAllocation?>(null) }
    var showAddBill by remember { mutableStateOf(false) }
    var showAddIncome by remember { mutableStateOf(false) }
    var showAddExpense by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Bills, 1 = Income, 2 = Expenses, 3 = Category Budgets

    val totalIncome = incomes.filter { it.status == "Received" }.sumOf { it.amount }
    val pendingIncome = incomes.filter { it.status == "Pending" }.sumOf { it.amount }
    val totalExpenses = expenses.sumOf { it.amount }
    val unpaidBills = bills.filter { !it.isPaid }.sumOf { it.amount }
    val netCashflow = totalIncome - totalExpenses

    Scaffold(
        floatingActionButton = {
            if (selectedTab in 0..2) {
                FloatingActionButton(
                    onClick = {
                        when (selectedTab) {
                            0 -> showAddBill = true
                            1 -> showAddIncome = true
                            2 -> showAddExpense = true
                        }
                    },
                    containerColor = IndigoPrimary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
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
        Text(
            text = "Economy & Budget",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Financial performance, bills, revenues & cost centers",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Financial KPI Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Net Cashflow
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SlateBorder, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = SlateCard)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "NET CASHFLOW", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "+$${netCashflow.toInt()}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSuccess
                    )
                    Text(text = "Operating Margin", fontSize = 11.sp, color = TextSecondary)
                }
            }

            // Unpaid Bills
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SlateBorder, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = SlateCard)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "UNPAID BILLS", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$${unpaidBills.toInt()}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberWarning
                    )
                    Text(text = "${bills.count { !it.isPaid }} payables due", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sub-tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = SlateSurface,
            contentColor = Color.White,
            edgePadding = 0.dp,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Bills (${bills.size})") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Income (${incomes.size})") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Expenses (${expenses.size})") }
            )
            Tab(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                text = { Text("Category Budgets") }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Content
        when (selectedTab) {
            0 -> BillsList(bills = bills, onTogglePaid = onToggleBillPaid, onEdit = { editingBill = it })
            1 -> IncomesList(incomes = incomes, pendingTotal = pendingIncome, onEdit = { editingIncome = it })
            2 -> ExpensesList(expenses = expenses, totalExpenses = totalExpenses, onEdit = { editingExpense = it })
            3 -> CategoryBudgetsView(allocations = budgetAllocations, onEdit = { editingBudgetAllocation = it })
        }
    }
    }

    editingBudgetAllocation?.let { alloc ->
        GenericEditDialog(
            title = "Budget Allocation",
            fields = mapOf("allocated" to alloc.allocatedAmount.toString(), "spent" to alloc.spentAmount.toString()),
            onDismiss = { editingBudgetAllocation = null },
            onSave = { updated ->
                onUpdateBudgetAllocation(alloc.copy(
                    allocatedAmount = updated["allocated"]?.toDoubleOrNull() ?: alloc.allocatedAmount,
                    spentAmount = updated["spent"]?.toDoubleOrNull() ?: alloc.spentAmount
                ))
                editingBudgetAllocation = null
            }
        )
    }

    editingBill?.let { bill ->
        GenericEditDialog(
            title = "Bill",
            fields = mapOf(
                "title" to bill.title, 
                "vendor" to bill.vendor,
                "amount" to bill.amount.toString(),
                "dueDate" to bill.dueDate,
                "recurring" to (bill.recurringPeriod ?: ""),
                "invoice" to (bill.invoiceNumber ?: "")
            ),
            onDismiss = { editingBill = null },
            onSave = { updated ->
                onUpdateBill(bill.copy(
                    title = updated["title"] ?: bill.title,
                    vendor = updated["vendor"] ?: bill.vendor,
                    amount = updated["amount"]?.toDoubleOrNull() ?: bill.amount,
                    dueDate = updated["dueDate"] ?: bill.dueDate,
                    recurringPeriod = updated["recurring"]?.ifBlank { null },
                    invoiceNumber = updated["invoice"]?.ifBlank { null }
                ))
                editingBill = null
            }
        )
    }

    editingIncome?.let { inc ->
        GenericEditDialog(
            title = "Income",
            fields = mapOf(
                "title" to inc.title, 
                "project" to inc.projectName,
                "amount" to inc.amount.toString(),
                "date" to inc.date,
                "status" to inc.status,
                "reference" to inc.referenceCode
            ),
            onDismiss = { editingIncome = null },
            onSave = { updated ->
                onUpdateIncome(inc.copy(
                    title = updated["title"] ?: inc.title,
                    projectName = updated["project"] ?: inc.projectName,
                    amount = updated["amount"]?.toDoubleOrNull() ?: inc.amount,
                    date = updated["date"] ?: inc.date,
                    status = updated["status"] ?: inc.status,
                    referenceCode = updated["reference"] ?: inc.referenceCode
                ))
                editingIncome = null
            }
        )
    }

    editingExpense?.let { exp ->
        GenericEditDialog(
            title = "Expense",
            fields = mapOf(
                "description" to exp.description, 
                "project" to exp.projectName,
                "amount" to exp.amount.toString(),
                "date" to exp.date,
                "loggedBy" to exp.loggedBy,
                "paymentMethod" to exp.paymentMethod
            ),
            onDismiss = { editingExpense = null },
            onSave = { updated ->
                onUpdateExpense(exp.copy(
                    description = updated["description"] ?: exp.description,
                    projectName = updated["project"] ?: exp.projectName,
                    amount = updated["amount"]?.toDoubleOrNull() ?: exp.amount,
                    date = updated["date"] ?: exp.date,
                    loggedBy = updated["loggedBy"] ?: exp.loggedBy,
                    paymentMethod = updated["paymentMethod"] ?: exp.paymentMethod
                ))
                editingExpense = null
            }
        )
    }

    if (showAddBill) {
        GenericEditDialog(
            title = "Add Bill",
            fields = mapOf("title" to "", "vendor" to "", "amount" to "0"),
            onDismiss = { showAddBill = false },
            onSave = { fields ->
                val newBill = Bill(
                    id = "bill-${System.currentTimeMillis()}",
                    title = fields["title"] ?: "",
                    vendor = fields["vendor"] ?: "",
                    amount = fields["amount"]?.toDoubleOrNull() ?: 0.0,
                    dueDate = "2026-10-01",
                    isPaid = false,
                    category = ExpenseCategory.MISCELLANEOUS,
                    recurringPeriod = null,
                    invoiceNumber = null
                )
                onAddBill(newBill)
                showAddBill = false
            }
        )
    }

    if (showAddIncome) {
        GenericEditDialog(
            title = "Add Income",
            fields = mapOf("title" to "", "amount" to "0", "status" to "Pending"),
            onDismiss = { showAddIncome = false },
            onSave = { fields ->
                val newIncome = Income(
                    id = "inc-${System.currentTimeMillis()}",
                    projectId = "none",
                    projectName = "New Income",
                    title = fields["title"] ?: "",
                    amount = fields["amount"]?.toDoubleOrNull() ?: 0.0,
                    date = "2026-09-15",
                    status = fields["status"] ?: "Pending",
                    source = IncomeSource.CONSULTING_SERVICES,
                    referenceCode = "REF-${System.currentTimeMillis()}"
                )
                onAddIncome(newIncome)
                showAddIncome = false
            }
        )
    }

    if (showAddExpense) {
        GenericEditDialog(
            title = "Add Expense",
            fields = mapOf("description" to "", "amount" to "0"),
            onDismiss = { showAddExpense = false },
            onSave = { fields ->
                val newExp = Expense(
                    id = "exp-${System.currentTimeMillis()}",
                    projectId = "none",
                    projectName = "Unassigned",
                    category = ExpenseCategory.MISCELLANEOUS,
                    amount = fields["amount"]?.toDoubleOrNull() ?: 0.0,
                    date = "2026-09-15",
                    description = fields["description"] ?: "",
                    loggedBy = "User",
                    paymentMethod = "Card"
                )
                onAddExpense(newExp)
                showAddExpense = false
            }
        )
    }
}

@Composable
fun BillsList(bills: List<Bill>, onTogglePaid: (String) -> Unit, onEdit: (Bill) -> Unit = {}) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(bills, key = { it.id }) { bill ->
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = bill.vendor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CyanAccent
                            )
                            bill.recurringPeriod?.let { rec ->
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = SlateSurfaceVariant
                                ) {
                                    Text(
                                        text = rec,
                                        fontSize = 10.sp,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = bill.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { onEdit(bill) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(14.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Due: ${bill.dueDate} • ${bill.invoiceNumber ?: "No Ref"}",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${"%,.2f".format(bill.amount)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (bill.isPaid) TextSecondary else Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = { onTogglePaid(bill.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (bill.isPaid) SlateSurfaceVariant else IndigoPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text(
                                text = if (bill.isPaid) "PAID ✓" else "PAY NOW",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (bill.isPaid) EmeraldSuccess else Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IncomesList(incomes: List<Income>, pendingTotal: Double, onEdit: (Income) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .border(1.dp, SlateBorder, RoundedCornerShape(10.dp)),
            colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Pending Inbound Pipeline", color = TextSecondary, fontSize = 13.sp)
                Text(text = "$${pendingTotal.toInt()} Pending", color = AmberWarning, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(incomes, key = { it.id }) { inc ->
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = inc.projectName, fontSize = 11.sp, color = IndigoSecondary, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = inc.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = { onEdit(inc) }, modifier = Modifier.size(20.dp)) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(12.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "${inc.source.name.replace("_", " ")} • ${inc.date}", fontSize = 12.sp, color = TextMuted)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "+$${"%,.2f".format(inc.amount)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (inc.status == "Received") EmeraldSuccess.copy(alpha = 0.2f) else AmberWarning.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = inc.status,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (inc.status == "Received") EmeraldSuccess else AmberWarning,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpensesList(expenses: List<Expense>, totalExpenses: Double, onEdit: (Expense) -> Unit = {}) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(expenses, key = { it.id }) { exp ->
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = exp.category.name.replace("_", " & "), fontSize = 11.sp, color = CyanAccent)
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = exp.description, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { onEdit(exp) }, modifier = Modifier.size(20.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(12.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "By ${exp.loggedBy} via ${exp.paymentMethod} • ${exp.date}", fontSize = 12.sp, color = TextMuted)
                    }
                    Text(
                        text = "-$${"%,.2f".format(exp.amount)}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseDanger
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBudgetsView(allocations: List<BudgetAllocation>, onEdit: (BudgetAllocation) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(allocations) { alloc ->
            val name = alloc.category.name.replace("_", " & ")
            val budget = alloc.allocatedAmount
            val spent = alloc.spentAmount
            val pct = if (budget > 0) ((spent / budget) * 100).toInt() else 0
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
                            Text(text = name, fontWeight = FontWeight.SemiBold, color = Color.White, fontSize = 14.sp)
                            IconButton(onClick = { onEdit(alloc) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(14.dp))
                            }
                        }
                        Text(text = "$pct%", fontWeight = FontWeight.Bold, color = if (pct > 80) AmberWarning else EmeraldSuccess, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = if (budget > 0) (spent / budget).toFloat() else 0f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (pct > 80) AmberWarning else IndigoPrimary,
                        trackColor = SlateSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Spent: $${spent.toInt()}", fontSize = 12.sp, color = TextSecondary)
                        Text(text = "Allocated: $${budget.toInt()}", fontSize = 12.sp, color = TextMuted)
                    }
                }
            }
        }
    }
}
