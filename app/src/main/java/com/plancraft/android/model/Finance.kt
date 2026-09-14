package com.plancraft.android.model

enum class ExpenseCategory {
    INFRASTRUCTURE_CLOUD,
    SALARIES_CONTRACTORS,
    LICENSES_SOFTWARE,
    HARDWARE_EQUIPMENT,
    LEGAL_COMPLIANCE,
    MARKETING_OUTREACH,
    MISCELLANEOUS
}

enum class IncomeSource {
    CLIENT_MILESTONE,
    RETAINER_FEE,
    INVESTOR_GRANT,
    CONSULTING_SERVICES,
    LICENSING_ROYALTIES
}

data class Bill(
    val id: String = "",
    val title: String = "",
    val vendor: String = "",
    val amount: Double = 0.0,
    val dueDate: String = "",
    val isPaid: Boolean = false,
    val category: ExpenseCategory = ExpenseCategory.MISCELLANEOUS,
    val recurringPeriod: String? = null,
    val invoiceNumber: String? = null
)

data class Income(
    val id: String = "",
    val projectId: String = "",
    val projectName: String = "",
    val source: IncomeSource = IncomeSource.CONSULTING_SERVICES,
    val title: String = "",
    val amount: Double = 0.0,
    val date: String = "",
    val status: String = "Pending",
    val referenceCode: String = ""
)

data class Expense(
    val id: String = "",
    val projectId: String = "",
    val projectName: String = "",
    val category: ExpenseCategory = ExpenseCategory.MISCELLANEOUS,
    val description: String = "",
    val amount: Double = 0.0,
    val date: String = "",
    val loggedBy: String = "",
    val paymentMethod: String = "Corporate Card"
)

data class BudgetAllocation(
    val category: ExpenseCategory,
    val allocatedAmount: Double,
    val spentAmount: Double
)
