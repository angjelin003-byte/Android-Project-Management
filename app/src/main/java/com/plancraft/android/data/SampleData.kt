package com.plancraft.android.data

import com.plancraft.android.model.*

object SampleData {
    val sampleProjects = listOf(
        Project(
            id = "proj-1",
            name = "NexGen Cloud ERP Modernization",
            client = "Apex Global Holdings",
            description = "Enterprise migration to distributed cloud architecture with automated telemetry and multi-tenant security.",
            status = ProjectStatus.IN_PROGRESS,
            priority = ProjectPriority.CRITICAL,
            startDate = "2026-08-01",
            targetEndDate = "2026-12-15",
            totalBudget = 185000.0,
            totalSpent = 92400.0,
            leadManager = "Elena Vance",
            completionPercentage = 54,
            tags = listOf("Cloud", "FinTech", "Kubernetes")
        ),
        Project(
            id = "proj-2",
            name = "Smart Logistics IoT Gateway",
            client = "Nordic Freight Dynamics",
            description = "Embedded sensor network integration with low-latency route dispatch and cold-chain compliance monitoring.",
            status = ProjectStatus.IN_PROGRESS,
            priority = ProjectPriority.HIGH,
            startDate = "2026-07-15",
            targetEndDate = "2026-11-30",
            totalBudget = 120000.0,
            totalSpent = 68500.0,
            leadManager = "Marcus Sterling",
            completionPercentage = 62,
            tags = listOf("IoT", "Hardware", "Android")
        ),
        Project(
            id = "proj-3",
            name = "Mobile Financial Portal & Biometrics",
            client = "Vanguard Horizon Trust",
            description = "Next-generation biometric auth, real-time FX settlement, and institutional portfolio dashboard.",
            status = ProjectStatus.PLANNING,
            priority = ProjectPriority.HIGH,
            startDate = "2026-09-01",
            targetEndDate = "2027-02-28",
            totalBudget = 145000.0,
            totalSpent = 16000.0,
            leadManager = "Sarah Chen",
            completionPercentage = 18,
            tags = listOf("Mobile", "Security", "Biometrics")
        ),
        Project(
            id = "proj-4",
            name = "Green Energy Grid Automation",
            client = "Helios Renewable Power",
            description = "Substation SCADA modernization and AI power shedding optimizer.",
            status = ProjectStatus.COMPLETED,
            priority = ProjectPriority.MEDIUM,
            startDate = "2026-03-01",
            targetEndDate = "2026-08-20",
            totalBudget = 95000.0,
            totalSpent = 91800.0,
            leadManager = "Tariq Al-Mansoor",
            completionPercentage = 100,
            tags = listOf("CleanTech", "AI", "Automation")
        )
    )

    val sampleTasks = listOf(
        Task(
            id = "task-101",
            projectId = "proj-1",
            projectName = "NexGen Cloud ERP",
            title = "Finalize OAuth 2.0 PKCE Token Exchange",
            description = "Verify mutual TLS handshake and multi-factor session revocation in staging.",
            date = "2026-09-14",
            time = "09:30 AM",
            durationHours = 3.5,
            status = TaskStatus.IN_PROGRESS,
            priority = TaskPriority.URGENT,
            assigneeId = "user-1",
            assigneeName = "Kaelen Voss",
            costImpact = 1450.0,
            milestone = "M3: Security Audit",
            isBillable = true
        ),
        Task(
            id = "task-102",
            projectId = "proj-1",
            projectName = "NexGen Cloud ERP",
            title = "Database Migration Stress Testing",
            description = "Execute 50,000 IOPS benchmark on Postgres read replicas.",
            date = "2026-09-14",
            time = "02:00 PM",
            durationHours = 4.0,
            status = TaskStatus.TODO,
            priority = TaskPriority.HIGH,
            assigneeId = "user-2",
            assigneeName = "Daria Lin",
            costImpact = 1200.0,
            milestone = "M3: Security Audit",
            isBillable = true
        ),
        Task(
            id = "task-103",
            projectId = "proj-2",
            projectName = "Smart Logistics IoT",
            title = "BLE Sensor Firmware v2.1 Verification",
            description = "Flash test 40 physical nodes and capture packet loss metrics under 2.4GHz interference.",
            date = "2026-09-15",
            time = "10:00 AM",
            durationHours = 5.0,
            status = TaskStatus.TODO,
            priority = TaskPriority.HIGH,
            assigneeId = "user-3",
            assigneeName = "Liam O'Connor",
            costImpact = 950.0,
            milestone = "M2: Field Beta",
            isBillable = true
        ),
        Task(
            id = "task-104",
            projectId = "proj-3",
            projectName = "Mobile Financial Portal",
            title = "Design Sprint: Biometric Fallback Wireframes",
            description = "Review accessibility compliance for PIN unlock and voice authentication.",
            date = "2026-09-16",
            time = "11:00 AM",
            durationHours = 2.5,
            status = TaskStatus.IN_REVIEW,
            priority = TaskPriority.MEDIUM,
            assigneeId = "user-4",
            assigneeName = "Chloe Dubois",
            costImpact = 850.0,
            milestone = "M1: UX Specs",
            isBillable = true
        ),
        Task(
            id = "task-105",
            projectId = "proj-1",
            projectName = "NexGen Cloud ERP",
            title = "Client Stakeholder Sprint Review",
            description = "Demo containerized microservices and automated rollback pipeline to Apex steering board.",
            date = "2026-09-18",
            time = "03:00 PM",
            durationHours = 2.0,
            status = TaskStatus.TODO,
            priority = TaskPriority.URGENT,
            assigneeId = "user-5",
            assigneeName = "Elena Vance",
            costImpact = 700.0,
            milestone = "M3: Security Audit",
            isBillable = true
        ),
        Task(
            id = "task-106",
            projectId = "proj-2",
            projectName = "Smart Logistics IoT",
            title = "Cold-Chain Sensor Calibration Certs",
            description = "Submit ISO-9001 compliance logs to regulatory oversight committee.",
            date = "2026-09-21",
            time = "01:30 PM",
            durationHours = 3.0,
            status = TaskStatus.TODO,
            priority = TaskPriority.MEDIUM,
            assigneeId = "user-6",
            assigneeName = "Marcus Sterling",
            costImpact = 1100.0,
            milestone = "M2: Field Beta",
            isBillable = false
        ),
        Task(
            id = "task-107",
            projectId = "proj-1",
            projectName = "NexGen Cloud ERP",
            title = "Automated Canary Deployment to US-East",
            description = "Deploy v1.4.0 with 5% traffic split and synthetic latency probes.",
            date = "2026-09-23",
            time = "08:00 PM",
            durationHours = 2.0,
            status = TaskStatus.TODO,
            priority = TaskPriority.HIGH,
            assigneeId = "user-1",
            assigneeName = "Kaelen Voss",
            costImpact = 800.0,
            milestone = "M4: Production Rollout",
            isBillable = true
        ),
        Task(
            id = "task-108",
            projectId = "proj-3",
            projectName = "Mobile Financial Portal",
            title = "Penetration Testing Scope Review",
            description = "Kick off third-party black-box penetration test with Cure53 team.",
            date = "2026-09-25",
            time = "10:30 AM",
            durationHours = 1.5,
            status = TaskStatus.TODO,
            priority = TaskPriority.HIGH,
            assigneeId = "user-2",
            assigneeName = "Daria Lin",
            costImpact = 2500.0,
            milestone = "M1: UX Specs",
            isBillable = true
        )
    )

    val sampleBills = listOf(
        Bill(
            id = "bill-1",
            title = "AWS Cloud Infrastructure - Multi-Region",
            vendor = "Amazon Web Services Inc.",
            amount = 4850.00,
            dueDate = "2026-09-20",
            isPaid = false,
            category = ExpenseCategory.INFRASTRUCTURE_CLOUD,
            recurringPeriod = "Monthly",
            invoiceNumber = "INV-AWS-2026-089"
        ),
        Bill(
            id = "bill-2",
            title = "JetBrains All Products Team License",
            vendor = "JetBrains s.r.o.",
            amount = 1890.00,
            dueDate = "2026-09-25",
            isPaid = false,
            category = ExpenseCategory.LICENSES_SOFTWARE,
            recurringPeriod = "Annual",
            invoiceNumber = "JB-992341"
        ),
        Bill(
            id = "bill-3",
            title = "Specialized Hardware Lab Test Rigs",
            vendor = "Nordic Signal Labs",
            amount = 8400.00,
            dueDate = "2026-09-28",
            isPaid = false,
            category = ExpenseCategory.HARDWARE_EQUIPMENT,
            recurringPeriod = null,
            invoiceNumber = "NSL-88210"
        ),
        Bill(
            id = "bill-4",
            title = "IP & Cross-Border Compliance Retainer",
            vendor = "Baker & Sterling LLP",
            amount = 6200.00,
            dueDate = "2026-09-12",
            isPaid = true,
            category = ExpenseCategory.LEGAL_COMPLIANCE,
            recurringPeriod = "Quarterly",
            invoiceNumber = "BS-7712"
        ),
        Bill(
            id = "bill-5",
            title = "Datadog Telemetry Enterprise Tier",
            vendor = "Datadog Inc.",
            amount = 1450.00,
            dueDate = "2026-09-10",
            isPaid = true,
            category = ExpenseCategory.INFRASTRUCTURE_CLOUD,
            recurringPeriod = "Monthly",
            invoiceNumber = "DD-449102"
        )
    )

    val sampleIncomes = listOf(
        Income(
            id = "inc-1",
            projectId = "proj-1",
            projectName = "NexGen Cloud ERP",
            source = IncomeSource.CLIENT_MILESTONE,
            title = "Milestone 2 Sign-off: Core Architecture",
            amount = 55000.00,
            date = "2026-09-02",
            status = "Received",
            referenceCode = "ACH-APEX-901"
        ),
        Income(
            id = "inc-2",
            projectId = "proj-2",
            projectName = "Smart Logistics IoT",
            source = IncomeSource.RETAINER_FEE,
            title = "Monthly IoT Firmware Sprint Retainer",
            amount = 28000.00,
            date = "2026-09-05",
            status = "Received",
            referenceCode = "WIRE-NDF-442"
        ),
        Income(
            id = "inc-3",
            projectId = "proj-3",
            projectName = "Mobile Financial Portal",
            source = IncomeSource.CLIENT_MILESTONE,
            title = "Project Inception & Architecture Grant",
            amount = 42500.00,
            date = "2026-09-18",
            status = "Pending",
            referenceCode = "INV-VHT-001"
        ),
        Income(
            id = "inc-4",
            projectId = "proj-1",
            projectName = "NexGen Cloud ERP",
            source = IncomeSource.CONSULTING_SERVICES,
            title = "High-Availability Audit Workshop",
            amount = 12500.00,
            date = "2026-09-22",
            status = "Pending",
            referenceCode = "INV-APEX-WS4"
        )
    )

    val sampleExpenses = listOf(
        Expense(
            id = "exp-1",
            projectId = "proj-1",
            projectName = "NexGen Cloud ERP",
            category = ExpenseCategory.INFRASTRUCTURE_CLOUD,
            description = "Staging Kubernetes cluster spot instances",
            amount = 2450.00,
            date = "2026-09-04",
            loggedBy = "Kaelen Voss",
            paymentMethod = "Corporate Card"
        ),
        Expense(
            id = "exp-2",
            projectId = "proj-2",
            projectName = "Smart Logistics IoT",
            category = ExpenseCategory.SALARIES_CONTRACTORS,
            description = "Embedded RF antenna optimization contractor",
            amount = 6800.00,
            date = "2026-09-07",
            loggedBy = "Marcus Sterling",
            paymentMethod = "Wire Transfer"
        ),
        Expense(
            id = "exp-3",
            projectId = "proj-1",
            projectName = "NexGen Cloud ERP",
            category = ExpenseCategory.LEGAL_COMPLIANCE,
            description = "SOC2 Type II interim readiness report fee",
            amount = 4500.00,
            date = "2026-09-09",
            loggedBy = "Elena Vance",
            paymentMethod = "ACH"
        ),
        Expense(
            id = "exp-4",
            projectId = "proj-3",
            projectName = "Mobile Financial Portal",
            category = ExpenseCategory.LICENSES_SOFTWARE,
            description = "Figma Organization team seat renewals",
            amount = 1120.00,
            date = "2026-09-11",
            loggedBy = "Chloe Dubois",
            paymentMethod = "Corporate Card"
        ),
        Expense(
            id = "exp-5",
            projectId = "proj-2",
            projectName = "Smart Logistics IoT",
            category = ExpenseCategory.HARDWARE_EQUIPMENT,
            description = "Nordic Semiconductor nRF5340 development kits",
            amount = 1850.00,
            date = "2026-09-12",
            loggedBy = "Liam O'Connor",
            paymentMethod = "Corporate Card"
        )
    )

    val sampleTeamMembers = listOf(
        TeamMember("user-1", "Kaelen Voss", "Principal Cloud Architect", "kaelen.v@company.org", StakeholderGroupType.CORE_ENGINEERING, 160.0, 100, "Q1 - Q4 2026"),
        TeamMember("user-2", "Daria Lin", "Lead Database & SecOps", "daria.l@company.org", StakeholderGroupType.CORE_ENGINEERING, 145.0, 90, "Q2 - Q4 2026"),
        TeamMember("user-3", "Liam O'Connor", "Embedded Firmware Lead", "liam.o@company.org", StakeholderGroupType.CORE_ENGINEERING, 140.0, 85, "Q1 - Q3 2026"),
        TeamMember("user-4", "Chloe Dubois", "Staff Product Designer", "chloe.d@company.org", StakeholderGroupType.PRODUCT_DESIGN, 125.0, 75, "Q2 - Q4 2026"),
        TeamMember("user-5", "Elena Vance", "VP of Technology / Executive", "elena.v@company.org", StakeholderGroupType.EXECUTIVE_LEADERSHIP, 195.0, 50, "Ongoing"),
        TeamMember("user-6", "Marcus Sterling", "Program Director", "marcus.s@company.org", StakeholderGroupType.FINANCE_OPERATIONS, 150.0, 80, "Ongoing"),
        TeamMember("user-7", "Dr. Aris Thorne", "Cryptographic Security Auditor", "aris@cipher-audit.io", StakeholderGroupType.EXTERNAL_CONSULTANTS, 220.0, 40, "Q3 - Q4 2026"),
        TeamMember("user-8", "Victoria Wright", "Client Tech Sponsor", "vwright@apexholdings.com", StakeholderGroupType.CLIENT_STAKEHOLDERS, 0.0, 25, "Quarterly Reviews")
    )

    val sampleTimelinePhases = listOf(
        ProjectTimelinePhase(
            id = "phase-1",
            phaseName = "Phase 1: Discovery & Architecture Baseline",
            quarter = "Q1 2026 (Jan - Mar)",
            startDate = "2026-01-05",
            endDate = "2026-03-31",
            progress = 100,
            involvedGroups = listOf(
                StakeholderGroupType.EXECUTIVE_LEADERSHIP,
                StakeholderGroupType.PRODUCT_DESIGN,
                StakeholderGroupType.CLIENT_STAKEHOLDERS
            ),
            headCount = 8,
            estimatedBudget = 65000.0
        ),
        ProjectTimelinePhase(
            id = "phase-2",
            phaseName = "Phase 2: Core Engineering & Sensor Prototyping",
            quarter = "Q2 2026 (Apr - Jun)",
            startDate = "2026-04-01",
            endDate = "2026-06-30",
            progress = 100,
            involvedGroups = listOf(
                StakeholderGroupType.CORE_ENGINEERING,
                StakeholderGroupType.PRODUCT_DESIGN,
                StakeholderGroupType.FINANCE_OPERATIONS
            ),
            headCount = 14,
            estimatedBudget = 140000.0
        ),
        ProjectTimelinePhase(
            id = "phase-3",
            phaseName = "Phase 3: Integration, Security Hardening & Beta",
            quarter = "Q3 2026 (Jul - Sep)",
            startDate = "2026-07-01",
            endDate = "2026-09-30",
            progress = 75,
            involvedGroups = listOf(
                StakeholderGroupType.CORE_ENGINEERING,
                StakeholderGroupType.EXTERNAL_CONSULTANTS,
                StakeholderGroupType.QUALITY_ASSURANCE,
                StakeholderGroupType.CLIENT_STAKEHOLDERS
            ),
            headCount = 19,
            estimatedBudget = 165000.0
        ),
        ProjectTimelinePhase(
            id = "phase-4",
            phaseName = "Phase 4: Enterprise Rollout & Multi-Tenant Scale",
            quarter = "Q4 2026 (Oct - Dec)",
            startDate = "2026-10-01",
            endDate = "2026-12-31",
            progress = 15,
            involvedGroups = listOf(
                StakeholderGroupType.CORE_ENGINEERING,
                StakeholderGroupType.EXECUTIVE_LEADERSHIP,
                StakeholderGroupType.FINANCE_OPERATIONS,
                StakeholderGroupType.CLIENT_STAKEHOLDERS
            ),
            headCount = 16,
            estimatedBudget = 130000.0
        )
    )
}
