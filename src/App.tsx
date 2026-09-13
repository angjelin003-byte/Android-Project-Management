/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import React, { useState } from 'react';
import { 
  Smartphone, 
  FolderTree, 
  Workflow, 
  Download, 
  Calendar as CalendarIcon, 
  FolderKanban, 
  Wallet, 
  Users, 
  GitBranch, 
  Sparkles,
  ExternalLink,
  ShieldCheck,
  CheckCircle2
} from 'lucide-react';
import { AndroidSimulator } from './components/AndroidSimulator';
import { RepoExplorer } from './components/RepoExplorer';
import { WorkflowGuide } from './components/WorkflowGuide';
import { Task, Project, Bill, Income, Expense, StakeholderPhase, TeamMember } from './types';
import { downloadAndroidRepoZip } from './utils/zipExporter';
import confetti from 'canvas-confetti';

// Initial realistic dataset
const INITIAL_PROJECTS: Project[] = [
  {
    id: 'proj-1',
    name: 'NexGen Cloud ERP Modernization',
    client: 'Apex Global Holdings',
    description: 'Enterprise migration to distributed cloud architecture with automated telemetry and multi-tenant security.',
    status: 'IN_PROGRESS',
    priority: 'CRITICAL',
    startDate: '2026-08-01',
    targetEndDate: '2026-12-15',
    totalBudget: 185000,
    totalSpent: 92400,
    leadManager: 'Elena Vance',
    completionPercentage: 54,
    tags: ['Cloud', 'FinTech', 'Kubernetes']
  },
  {
    id: 'proj-2',
    name: 'Smart Logistics IoT Gateway',
    client: 'Nordic Freight Dynamics',
    description: 'Embedded sensor network integration with low-latency route dispatch and cold-chain compliance monitoring.',
    status: 'IN_PROGRESS',
    priority: 'HIGH',
    startDate: '2026-07-15',
    targetEndDate: '2026-11-30',
    totalBudget: 120000,
    totalSpent: 68500,
    leadManager: 'Marcus Sterling',
    completionPercentage: 62,
    tags: ['IoT', 'Hardware', 'Android']
  },
  {
    id: 'proj-3',
    name: 'Mobile Financial Portal & Biometrics',
    client: 'Vanguard Horizon Trust',
    description: 'Next-generation biometric auth, real-time FX settlement, and institutional portfolio dashboard.',
    status: 'PLANNING',
    priority: 'HIGH',
    startDate: '2026-09-01',
    targetEndDate: '2027-02-28',
    totalBudget: 145000,
    totalSpent: 16000,
    leadManager: 'Sarah Chen',
    completionPercentage: 18,
    tags: ['Mobile', 'Security', 'Biometrics']
  }
];

const INITIAL_TASKS: Task[] = [
  {
    id: 'task-101',
    projectId: 'proj-1',
    projectName: 'NexGen Cloud ERP',
    title: 'Finalize OAuth 2.0 PKCE Token Exchange',
    description: 'Verify mutual TLS handshake and multi-factor session revocation in staging.',
    date: '2026-09-14',
    time: '09:30 AM',
    durationHours: 3.5,
    status: 'IN_PROGRESS',
    priority: 'URGENT',
    assigneeId: 'user-1',
    assigneeName: 'Kaelen Voss',
    costImpact: 1450,
    milestone: 'M3: Security Audit',
    isBillable: true
  },
  {
    id: 'task-102',
    projectId: 'proj-1',
    projectName: 'NexGen Cloud ERP',
    title: 'Database Migration Stress Testing',
    description: 'Execute 50,000 IOPS benchmark on Postgres read replicas.',
    date: '2026-09-14',
    time: '02:00 PM',
    durationHours: 4.0,
    status: 'TODO',
    priority: 'HIGH',
    assigneeId: 'user-2',
    assigneeName: 'Daria Lin',
    costImpact: 1200,
    milestone: 'M3: Security Audit',
    isBillable: true
  },
  {
    id: 'task-103',
    projectId: 'proj-2',
    projectName: 'Smart Logistics IoT',
    title: 'BLE Sensor Firmware v2.1 Verification',
    description: 'Flash test 40 physical nodes and capture packet loss metrics under 2.4GHz interference.',
    date: '2026-09-15',
    time: '10:00 AM',
    durationHours: 5.0,
    status: 'TODO',
    priority: 'HIGH',
    assigneeId: 'user-3',
    assigneeName: 'Liam O Connor',
    costImpact: 950,
    milestone: 'M2: Field Beta',
    isBillable: true
  },
  {
    id: 'task-104',
    projectId: 'proj-3',
    projectName: 'Mobile Financial Portal',
    title: 'Design Sprint: Biometric Fallback Wireframes',
    description: 'Review accessibility compliance for PIN unlock and voice authentication.',
    date: '2026-09-16',
    time: '11:00 AM',
    durationHours: 2.5,
    status: 'IN_REVIEW',
    priority: 'MEDIUM',
    assigneeId: 'user-4',
    assigneeName: 'Chloe Dubois',
    costImpact: 850,
    milestone: 'M1: UX Specs',
    isBillable: true
  },
  {
    id: 'task-105',
    projectId: 'proj-1',
    projectName: 'NexGen Cloud ERP',
    title: 'Client Stakeholder Sprint Review',
    description: 'Demo containerized microservices and automated rollback pipeline to Apex steering board.',
    date: '2026-09-18',
    time: '03:00 PM',
    durationHours: 2.0,
    status: 'TODO',
    priority: 'URGENT',
    assigneeId: 'user-5',
    assigneeName: 'Elena Vance',
    costImpact: 700,
    milestone: 'M3: Security Audit',
    isBillable: true
  },
  {
    id: 'task-106',
    projectId: 'proj-2',
    projectName: 'Smart Logistics IoT',
    title: 'Cold-Chain Sensor Calibration Certs',
    description: 'Submit ISO-9001 compliance logs to regulatory oversight committee.',
    date: '2026-09-21',
    time: '01:30 PM',
    durationHours: 3.0,
    status: 'TODO',
    priority: 'MEDIUM',
    assigneeId: 'user-6',
    assigneeName: 'Marcus Sterling',
    costImpact: 1100,
    milestone: 'M2: Field Beta',
    isBillable: false
  }
];

const INITIAL_BILLS: Bill[] = [
  {
    id: 'bill-1',
    title: 'AWS Cloud Infrastructure - Multi-Region',
    vendor: 'Amazon Web Services Inc.',
    amount: 4850,
    dueDate: '2026-09-20',
    isPaid: false,
    category: 'INFRASTRUCTURE_CLOUD',
    recurringPeriod: 'Monthly',
    invoiceNumber: 'INV-AWS-2026-089'
  },
  {
    id: 'bill-2',
    title: 'JetBrains All Products Team License',
    vendor: 'JetBrains s.r.o.',
    amount: 1890,
    dueDate: '2026-09-25',
    isPaid: false,
    category: 'LICENSES_SOFTWARE',
    recurringPeriod: 'Annual',
    invoiceNumber: 'JB-992341'
  },
  {
    id: 'bill-3',
    title: 'Specialized Hardware Lab Test Rigs',
    vendor: 'Nordic Signal Labs',
    amount: 8400,
    dueDate: '2026-09-28',
    isPaid: false,
    category: 'HARDWARE_EQUIPMENT',
    recurringPeriod: undefined,
    invoiceNumber: 'NSL-88210'
  },
  {
    id: 'bill-4',
    title: 'IP & Cross-Border Compliance Retainer',
    vendor: 'Baker & Sterling LLP',
    amount: 6200,
    dueDate: '2026-09-12',
    isPaid: true,
    category: 'LEGAL_COMPLIANCE',
    recurringPeriod: 'Quarterly',
    invoiceNumber: 'BS-7712'
  }
];

const INITIAL_INCOMES: Income[] = [
  {
    id: 'inc-1',
    projectId: 'proj-1',
    projectName: 'NexGen Cloud ERP',
    source: 'CLIENT_MILESTONE',
    title: 'Milestone 2 Sign-off: Core Architecture',
    amount: 55000,
    date: '2026-09-02',
    status: 'Received',
    referenceCode: 'ACH-APEX-901'
  },
  {
    id: 'inc-2',
    projectId: 'proj-2',
    projectName: 'Smart Logistics IoT',
    source: 'RETAINER_FEE',
    title: 'Monthly IoT Firmware Sprint Retainer',
    amount: 28000,
    date: '2026-09-05',
    status: 'Received',
    referenceCode: 'WIRE-NDF-442'
  },
  {
    id: 'inc-3',
    projectId: 'proj-3',
    projectName: 'Mobile Financial Portal',
    source: 'CLIENT_MILESTONE',
    title: 'Project Inception & Architecture Grant',
    amount: 42500,
    date: '2026-09-18',
    status: 'Pending',
    referenceCode: 'INV-VHT-001'
  }
];

const INITIAL_EXPENSES: Expense[] = [
  {
    id: 'exp-1',
    projectId: 'proj-1',
    projectName: 'NexGen Cloud ERP',
    category: 'INFRASTRUCTURE_CLOUD',
    description: 'Staging Kubernetes cluster spot instances',
    amount: 2450,
    date: '2026-09-04',
    loggedBy: 'Kaelen Voss',
    paymentMethod: 'Corporate Card'
  },
  {
    id: 'exp-2',
    projectId: 'proj-2',
    projectName: 'Smart Logistics IoT',
    category: 'SALARIES_CONTRACTORS',
    description: 'Embedded RF antenna optimization contractor',
    amount: 6800,
    date: '2026-09-07',
    loggedBy: 'Marcus Sterling',
    paymentMethod: 'Wire Transfer'
  },
  {
    id: 'exp-3',
    projectId: 'proj-1',
    projectName: 'NexGen Cloud ERP',
    category: 'LEGAL_COMPLIANCE',
    description: 'SOC2 Type II interim readiness report fee',
    amount: 4500,
    date: '2026-09-09',
    loggedBy: 'Elena Vance',
    paymentMethod: 'ACH'
  }
];

const INITIAL_PHASES: StakeholderPhase[] = [
  {
    id: 'phase-1',
    phaseName: 'Phase 1: Discovery & Architecture Baseline',
    quarter: 'Q1 2026 (Jan - Mar)',
    startDate: '2026-01-05',
    endDate: '2026-03-31',
    progress: 100,
    involvedGroups: ['EXECUTIVE_LEADERSHIP', 'PRODUCT_DESIGN', 'CLIENT_STAKEHOLDERS'],
    headCount: 8,
    estimatedBudget: 65000
  },
  {
    id: 'phase-2',
    phaseName: 'Phase 2: Core Engineering & Sensor Prototyping',
    quarter: 'Q2 2026 (Apr - Jun)',
    startDate: '2026-04-01',
    endDate: '2026-06-30',
    progress: 100,
    involvedGroups: ['CORE_ENGINEERING', 'PRODUCT_DESIGN', 'FINANCE_OPERATIONS'],
    headCount: 14,
    estimatedBudget: 140000
  },
  {
    id: 'phase-3',
    phaseName: 'Phase 3: Integration, Security Hardening & Beta',
    quarter: 'Q3 2026 (Jul - Sep)',
    startDate: '2026-07-01',
    endDate: '2026-09-30',
    progress: 75,
    involvedGroups: ['CORE_ENGINEERING', 'EXTERNAL_CONSULTANTS', 'QUALITY_ASSURANCE', 'CLIENT_STAKEHOLDERS'],
    headCount: 19,
    estimatedBudget: 165000
  },
  {
    id: 'phase-4',
    phaseName: 'Phase 4: Enterprise Rollout & Multi-Tenant Scale',
    quarter: 'Q4 2026 (Oct - Dec)',
    startDate: '2026-10-01',
    endDate: '2026-12-31',
    progress: 15,
    involvedGroups: ['CORE_ENGINEERING', 'EXECUTIVE_LEADERSHIP', 'FINANCE_OPERATIONS', 'CLIENT_STAKEHOLDERS'],
    headCount: 16,
    estimatedBudget: 130000
  }
];

const INITIAL_MEMBERS: TeamMember[] = [
  { id: 'user-1', name: 'Kaelen Voss', role: 'Principal Cloud Architect', email: 'kaelen.v@company.org', group: 'CORE_ENGINEERING', hourlyRate: 160, allocationPercentage: 100, activePeriod: 'Q1 - Q4 2026' },
  { id: 'user-2', name: 'Daria Lin', role: 'Lead Database & SecOps', email: 'daria.l@company.org', group: 'CORE_ENGINEERING', hourlyRate: 145, allocationPercentage: 90, activePeriod: 'Q2 - Q4 2026' },
  { id: 'user-3', name: 'Liam O Connor', role: 'Embedded Firmware Lead', email: 'liam.o@company.org', group: 'CORE_ENGINEERING', hourlyRate: 140, allocationPercentage: 85, activePeriod: 'Q1 - Q3 2026' },
  { id: 'user-4', name: 'Chloe Dubois', role: 'Staff Product Designer', email: 'chloe.d@company.org', group: 'PRODUCT_DESIGN', hourlyRate: 125, allocationPercentage: 75, activePeriod: 'Q2 - Q4 2026' },
  { id: 'user-5', name: 'Elena Vance', role: 'VP of Technology / Executive', email: 'elena.v@company.org', group: 'EXECUTIVE_LEADERSHIP', hourlyRate: 195, allocationPercentage: 50, activePeriod: 'Ongoing' },
  { id: 'user-6', name: 'Marcus Sterling', role: 'Program Director', email: 'marcus.s@company.org', group: 'FINANCE_OPERATIONS', hourlyRate: 150, allocationPercentage: 80, activePeriod: 'Ongoing' },
  { id: 'user-7', name: 'Dr. Aris Thorne', role: 'Cryptographic Security Auditor', email: 'aris@cipher-audit.io', group: 'EXTERNAL_CONSULTANTS', hourlyRate: 220, allocationPercentage: 40, activePeriod: 'Q3 - Q4 2026' },
  { id: 'user-8', name: 'Victoria Wright', role: 'Client Tech Sponsor', email: 'vwright@apexholdings.com', group: 'CLIENT_STAKEHOLDERS', hourlyRate: 0, allocationPercentage: 25, activePeriod: 'Quarterly Reviews' }
];

export default function App() {
  const [activeView, setActiveView] = useState<'simulator' | 'explorer' | 'cicd'>('simulator');
  const [tasks, setTasks] = useState<Task[]>(INITIAL_TASKS);
  const [projects] = useState<Project[]>(INITIAL_PROJECTS);
  const [bills, setBills] = useState<Bill[]>(INITIAL_BILLS);
  const [incomes] = useState<Income[]>(INITIAL_INCOMES);
  const [expenses] = useState<Expense[]>(INITIAL_EXPENSES);
  const [phases] = useState<StakeholderPhase[]>(INITIAL_PHASES);
  const [members] = useState<TeamMember[]>(INITIAL_MEMBERS);

  const handleToggleTask = (taskId: string) => {
    setTasks(prev => prev.map(t => {
      if (t.id === taskId) {
        return { ...t, status: t.status === 'DONE' ? 'TODO' : 'DONE' };
      }
      return t;
    }));
  };

  const handleToggleBill = (billId: string) => {
    setBills(prev => prev.map(b => {
      if (b.id === billId) {
        return { ...b, isPaid: !b.isPaid };
      }
      return b;
    }));
  };

  const handleAddTask = (newTask: Task) => {
    setTasks(prev => [newTask, ...prev]);
    confetti({
      particleCount: 50,
      spread: 60,
      origin: { y: 0.7 }
    });
  };

  const handleQuickDownload = async () => {
    await downloadAndroidRepoZip();
    confetti({
      particleCount: 70,
      spread: 80,
      origin: { y: 0.5 }
    });
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col selection:bg-indigo-500 selection:text-white">
      {/* Top Main Navigation Bar */}
      <header className="sticky top-0 z-40 bg-slate-950/80 backdrop-blur-md border-b border-slate-800/80">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 h-16 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-indigo-500 to-cyan-500 flex items-center justify-center shadow-md shadow-indigo-500/20">
              <Smartphone className="w-5 h-5 text-white" />
            </div>
            <div>
              <div className="flex items-center space-x-2">
                <h1 className="text-base font-bold text-white tracking-tight">PlanCraft Android</h1>
                <span className="text-[10px] font-mono px-2 py-0.5 rounded-full bg-indigo-900/50 text-indigo-300 border border-indigo-700/50">
                  v1.0.0 (API 34)
                </span>
              </div>
              <p className="text-[11px] text-slate-400">Android App Repository with GitHub Actions CI</p>
            </div>
          </div>

          {/* View Mode Switcher */}
          <div className="flex items-center space-x-1 sm:space-x-2 bg-slate-900/90 p-1 rounded-2xl border border-slate-800 text-xs font-semibold">
            <button
              id="view-mode-simulator"
              onClick={() => setActiveView('simulator')}
              className={`px-3 py-1.5 rounded-xl flex items-center space-x-1.5 transition-all ${
                activeView === 'simulator'
                  ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/30'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <Smartphone className="w-3.5 h-3.5" />
              <span className="hidden sm:inline">Android Simulator</span>
              <span className="sm:hidden">App</span>
            </button>

            <button
              id="view-mode-explorer"
              onClick={() => setActiveView('explorer')}
              className={`px-3 py-1.5 rounded-xl flex items-center space-x-1.5 transition-all ${
                activeView === 'explorer'
                  ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/30'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <FolderTree className="w-3.5 h-3.5" />
              <span className="hidden sm:inline">Source Code & Tree</span>
              <span className="sm:hidden">Repo</span>
            </button>

            <button
              id="view-mode-cicd"
              onClick={() => setActiveView('cicd')}
              className={`px-3 py-1.5 rounded-xl flex items-center space-x-1.5 transition-all ${
                activeView === 'cicd'
                  ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/30'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <Workflow className="w-3.5 h-3.5" />
              <span className="hidden sm:inline">build.yml CI/CD</span>
              <span className="sm:hidden">CI/CD</span>
            </button>
          </div>

          {/* Quick Export Button */}
          <button
            id="top-quick-download"
            onClick={handleQuickDownload}
            className="hidden md:flex items-center space-x-1.5 px-4 py-2 rounded-xl bg-slate-900 hover:bg-slate-800 border border-slate-700 text-xs font-bold text-slate-200 transition-colors"
          >
            <Download className="w-3.5 h-3.5 text-indigo-400" />
            <span>Export .ZIP</span>
          </button>
        </div>
      </header>

      {/* Feature Pillar Badges Strip */}
      <div className="bg-slate-900/40 border-b border-slate-800/60 py-2 px-4">
        <div className="max-w-7xl mx-auto flex flex-wrap items-center justify-between gap-2 text-xs">
          <div className="flex flex-wrap items-center gap-3">
            <span className="flex items-center space-x-1.5 text-slate-300">
              <CalendarIcon className="w-3.5 h-3.5 text-indigo-400" />
              <span>Calendar Format Tasks ({tasks.length})</span>
            </span>
            <span className="text-slate-700 hidden sm:inline">•</span>
            <span className="flex items-center space-x-1.5 text-slate-300">
              <FolderKanban className="w-3.5 h-3.5 text-cyan-400" />
              <span>Project Management & Kanban ({projects.length})</span>
            </span>
            <span className="text-slate-700 hidden sm:inline">•</span>
            <span className="flex items-center space-x-1.5 text-slate-300">
              <Wallet className="w-3.5 h-3.5 text-emerald-400" />
              <span>Economy, Bills & Cashflow ({bills.length} bills)</span>
            </span>
            <span className="text-slate-700 hidden sm:inline">•</span>
            <span className="flex items-center space-x-1.5 text-slate-300">
              <Users className="w-3.5 h-3.5 text-amber-400" />
              <span>Groups & Phases Timeline ({phases.length} quarters)</span>
            </span>
          </div>

          <div className="flex items-center space-x-2 text-[11px] font-mono text-slate-400">
            <GitBranch className="w-3 h-3 text-emerald-400" />
            <span>.github/workflows/build.yml</span>
          </div>
        </div>
      </div>

      {/* Main View Area */}
      <main className="flex-1 py-6 px-4 sm:px-6">
        {activeView === 'simulator' && (
          <div className="space-y-6">
            <div className="text-center max-w-xl mx-auto space-y-1">
              <span className="text-[10px] font-mono uppercase tracking-widest text-indigo-400 font-bold">
                Live Jetpack Compose Architecture Demo
              </span>
              <h2 className="text-xl sm:text-2xl font-bold text-white tracking-tight">
                Android App Interactive Simulation
              </h2>
              <p className="text-xs text-slate-400">
                Test the mobile interface locally with calendar scheduling, projects, financial bills, and stakeholder roadmaps.
              </p>
            </div>

            <AndroidSimulator
              tasks={tasks}
              projects={projects}
              bills={bills}
              incomes={incomes}
              expenses={expenses}
              phases={phases}
              members={members}
              onToggleTask={handleToggleTask}
              onToggleBill={handleToggleBill}
              onAddTask={handleAddTask}
            />
          </div>
        )}

        {activeView === 'explorer' && (
          <RepoExplorer />
        )}

        {activeView === 'cicd' && (
          <WorkflowGuide />
        )}
      </main>

      {/* Footer */}
      <footer className="border-t border-slate-900 bg-slate-950 py-6 px-4 text-center text-xs text-slate-500 font-mono">
        <p>PlanCraft Android Repository • Kotlin 1.9.23 • Jetpack Compose • GitHub Actions CI/CD build.yml</p>
      </footer>
    </div>
  );
}
