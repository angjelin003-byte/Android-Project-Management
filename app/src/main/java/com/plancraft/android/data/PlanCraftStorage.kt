package com.plancraft.android.data

import android.content.Context
import android.content.SharedPreferences
import com.plancraft.android.model.*
import org.json.JSONArray
import org.json.JSONObject

object PlanCraftStorage {
    private const val PREFS_NAME = "plancraft_prefs"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveTheme(context: Context, isDark: Boolean) {
        getPrefs(context).edit().putBoolean("is_dark_theme", isDark).apply()
    }

    fun loadTheme(context: Context, defaultDark: Boolean = true): Boolean {
        return getPrefs(context).getBoolean("is_dark_theme", defaultDark)
    }

    fun saveTasks(context: Context, tasks: List<Task>) {
        val arr = JSONArray()
        tasks.forEach { t ->
            val obj = JSONObject().apply {
                put("id", t.id)
                put("projectId", t.projectId)
                put("projectName", t.projectName)
                put("title", t.title)
                put("description", t.description)
                put("date", t.date)
                put("status", t.status.name)
                put("priority", t.priority.name)
                put("assigneeId", t.assigneeId)
                put("assigneeName", t.assigneeName)
            }
            arr.put(obj)
        }
        getPrefs(context).edit().putString("tasks_json", arr.toString()).apply()
    }

    fun loadTasks(context: Context): List<Task>? {
        val str = getPrefs(context).getString("tasks_json", null) ?: return null
        try {
            val arr = JSONArray(str)
            val list = mutableListOf<Task>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    Task(
                        id = obj.optString("id"),
                        projectId = obj.optString("projectId"),
                        projectName = obj.optString("projectName"),
                        title = obj.optString("title"),
                        description = obj.optString("description"),
                        date = obj.optString("date"),
                        status = try { TaskStatus.valueOf(obj.optString("status", "TODO")) } catch(e: Exception) { TaskStatus.TODO },
                        priority = try { TaskPriority.valueOf(obj.optString("priority", "MEDIUM")) } catch(e: Exception) { TaskPriority.MEDIUM },
                        assigneeId = obj.optString("assigneeId"),
                        assigneeName = obj.optString("assigneeName")
                    )
                )
            }
            return list
        } catch (e: Exception) {
            return null
        }
    }

    fun saveProjects(context: Context, projects: List<Project>) {
        val arr = JSONArray()
        projects.forEach { p ->
            val obj = JSONObject().apply {
                put("id", p.id)
                put("name", p.name)
                put("client", p.client)
                put("description", p.description)
                put("status", p.status.name)
                put("priority", p.priority.name)
                put("startDate", p.startDate)
                put("targetEndDate", p.targetEndDate)
                put("totalBudget", p.totalBudget)
                put("totalSpent", p.totalSpent)
                put("leadManager", p.leadManager)
                put("completionPercentage", p.completionPercentage)
            }
            arr.put(obj)
        }
        getPrefs(context).edit().putString("projects_json", arr.toString()).apply()
    }

    fun loadProjects(context: Context): List<Project>? {
        val str = getPrefs(context).getString("projects_json", null) ?: return null
        try {
            val arr = JSONArray(str)
            val list = mutableListOf<Project>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    Project(
                        id = obj.optString("id"),
                        name = obj.optString("name"),
                        client = obj.optString("client"),
                        description = obj.optString("description"),
                        status = try { ProjectStatus.valueOf(obj.optString("status", "PLANNING")) } catch(e: Exception) { ProjectStatus.PLANNING },
                        priority = try { ProjectPriority.valueOf(obj.optString("priority", "MEDIUM")) } catch(e: Exception) { ProjectPriority.MEDIUM },
                        startDate = obj.optString("startDate"),
                        targetEndDate = obj.optString("targetEndDate"),
                        totalBudget = obj.optDouble("totalBudget", 0.0),
                        totalSpent = obj.optDouble("totalSpent", 0.0),
                        leadManager = obj.optString("leadManager"),
                        completionPercentage = obj.optInt("completionPercentage", 0)
                    )
                )
            }
            return list
        } catch (e: Exception) {
            return null
        }
    }

    fun saveBills(context: Context, bills: List<Bill>) {
        val arr = JSONArray()
        bills.forEach { b ->
            val obj = JSONObject().apply {
                put("id", b.id)
                put("title", b.title)
                put("vendor", b.vendor)
                put("amount", b.amount)
                put("dueDate", b.dueDate)
                put("isPaid", b.isPaid)
                put("invoiceNumber", b.invoiceNumber ?: "")
            }
            arr.put(obj)
        }
        getPrefs(context).edit().putString("bills_json", arr.toString()).apply()
    }

    fun loadBills(context: Context): List<Bill>? {
        val str = getPrefs(context).getString("bills_json", null) ?: return null
        try {
            val arr = JSONArray(str)
            val list = mutableListOf<Bill>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    Bill(
                        id = obj.optString("id"),
                        title = obj.optString("title"),
                        vendor = obj.optString("vendor"),
                        amount = obj.optDouble("amount", 0.0),
                        dueDate = obj.optString("dueDate"),
                        isPaid = obj.optBoolean("isPaid", false),
                        invoiceNumber = obj.optString("invoiceNumber").ifBlank { null }
                    )
                )
            }
            return list
        } catch (e: Exception) {
            return null
        }
    }

    fun saveIncomes(context: Context, incomes: List<Income>) {
        val arr = JSONArray()
        incomes.forEach { inc ->
            val obj = JSONObject().apply {
                put("id", inc.id)
                put("projectId", inc.projectId)
                put("projectName", inc.projectName)
                put("title", inc.title)
                put("amount", inc.amount)
                put("date", inc.date)
                put("status", inc.status)
                put("referenceCode", inc.referenceCode)
            }
            arr.put(obj)
        }
        getPrefs(context).edit().putString("incomes_json", arr.toString()).apply()
    }

    fun loadIncomes(context: Context): List<Income>? {
        val str = getPrefs(context).getString("incomes_json", null) ?: return null
        try {
            val arr = JSONArray(str)
            val list = mutableListOf<Income>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    Income(
                        id = obj.optString("id"),
                        projectId = obj.optString("projectId"),
                        projectName = obj.optString("projectName"),
                        title = obj.optString("title"),
                        amount = obj.optDouble("amount", 0.0),
                        date = obj.optString("date"),
                        status = obj.optString("status", "Pending"),
                        referenceCode = obj.optString("referenceCode")
                    )
                )
            }
            return list
        } catch (e: Exception) {
            return null
        }
    }

    fun saveExpenses(context: Context, expenses: List<Expense>) {
        val arr = JSONArray()
        expenses.forEach { exp ->
            val obj = JSONObject().apply {
                put("id", exp.id)
                put("projectId", exp.projectId)
                put("projectName", exp.projectName)
                put("description", exp.description)
                put("amount", exp.amount)
                put("date", exp.date)
                put("loggedBy", exp.loggedBy)
                put("paymentMethod", exp.paymentMethod)
            }
            arr.put(obj)
        }
        getPrefs(context).edit().putString("expenses_json", arr.toString()).apply()
    }

    fun loadExpenses(context: Context): List<Expense>? {
        val str = getPrefs(context).getString("expenses_json", null) ?: return null
        try {
            val arr = JSONArray(str)
            val list = mutableListOf<Expense>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    Expense(
                        id = obj.optString("id"),
                        projectId = obj.optString("projectId"),
                        projectName = obj.optString("projectName"),
                        description = obj.optString("description"),
                        amount = obj.optDouble("amount", 0.0),
                        date = obj.optString("date"),
                        loggedBy = obj.optString("loggedBy"),
                        paymentMethod = obj.optString("paymentMethod", "Corporate Card")
                    )
                )
            }
            return list
        } catch (e: Exception) {
            return null
        }
    }

    fun savePhases(context: Context, phases: List<ProjectTimelinePhase>) {
        val arr = JSONArray()
        phases.forEach { p ->
            val obj = JSONObject().apply {
                put("id", p.id)
                put("phaseName", p.phaseName)
                put("startDate", p.startDate)
                put("endDate", p.endDate)
                put("progress", p.progress)
                put("estimatedBudget", p.estimatedBudget)
            }
            arr.put(obj)
        }
        getPrefs(context).edit().putString("phases_json", arr.toString()).apply()
    }

    fun loadPhases(context: Context): List<ProjectTimelinePhase>? {
        val str = getPrefs(context).getString("phases_json", null) ?: return null
        try {
            val arr = JSONArray(str)
            val list = mutableListOf<ProjectTimelinePhase>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    ProjectTimelinePhase(
                        id = obj.optString("id"),
                        phaseName = obj.optString("phaseName"),
                        startDate = obj.optString("startDate"),
                        endDate = obj.optString("endDate"),
                        progress = obj.optInt("progress", 0),
                        estimatedBudget = obj.optDouble("estimatedBudget", 0.0)
                    )
                )
            }
            return list
        } catch (e: Exception) {
            return null
        }
    }

    fun saveMembers(context: Context, members: List<TeamMember>) {
        val arr = JSONArray()
        members.forEach { m ->
            val obj = JSONObject().apply {
                put("id", m.id)
                put("name", m.name)
                put("role", m.role)
                put("email", m.email)
                put("hourlyRate", m.hourlyRate)
                put("activePeriod", m.activePeriod)
            }
            arr.put(obj)
        }
        getPrefs(context).edit().putString("members_json", arr.toString()).apply()
    }

    fun loadMembers(context: Context): List<TeamMember>? {
        val str = getPrefs(context).getString("members_json", null) ?: return null
        try {
            val arr = JSONArray(str)
            val list = mutableListOf<TeamMember>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    TeamMember(
                        id = obj.optString("id"),
                        name = obj.optString("name"),
                        role = obj.optString("role"),
                        email = obj.optString("email"),
                        hourlyRate = obj.optDouble("hourlyRate", 0.0),
                        activePeriod = obj.optString("activePeriod")
                    )
                )
            }
            return list
        } catch (e: Exception) {
            return null
        }
    }
}
