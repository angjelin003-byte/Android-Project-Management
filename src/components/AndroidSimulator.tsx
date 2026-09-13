import React, { useState } from 'react';
import { 
  Calendar as CalendarIcon, 
  FolderKanban, 
  Wallet, 
  Users, 
  Plus, 
  CheckCircle2, 
  Circle, 
  Clock, 
  AlertCircle,
  TrendingUp,
  Receipt,
  Layers,
  Sparkles,
  ChevronRight,
  ShieldCheck,
  Building,
  DollarSign
} from 'lucide-react';
import { Task, Project, Bill, Income, Expense, StakeholderPhase, TeamMember, TaskPriority } from '../types';

interface AndroidSimulatorProps {
  tasks: Task[];
  projects: Project[];
  bills: Bill[];
  incomes: Income[];
  expenses: Expense[];
  phases: StakeholderPhase[];
  members: TeamMember[];
  onToggleTask: (id: string) => void;
  onToggleBill: (id: string) => void;
  onAddTask: (task: Task) => void;
}

export const AndroidSimulator: React.FC<AndroidSimulatorProps> = ({
  tasks,
  projects,
  bills,
  incomes,
  expenses,
  phases,
  members,
  onToggleTask,
  onToggleBill,
  onAddTask
}) => {
  const [currentTab, setCurrentTab] = useState<'calendar' | 'projects' | 'economy' | 'people'>('calendar');
  const [selectedDate, setSelectedDate] = useState('2026-09-14');
  const [selectedProjectFilter, setSelectedProjectFilter] = useState('All');
  const [showAddModal, setShowAddModal] = useState(false);
  const [projectSubTab, setProjectSubTab] = useState<'cards' | 'kanban'>('cards');
  const [economySubTab, setEconomySubTab] = useState<'bills' | 'income' | 'expenses' | 'budget'>('bills');
  const [peopleSubTab, setPeopleSubTab] = useState<'timeline' | 'team'>('timeline');

  // New task form state
  const [newTitle, setNewTitle] = useState('');
  const [newDesc, setNewDesc] = useState('');
  const [newHours, setNewHours] = useState('3.0');
  const [newCost, setNewCost] = useState('450');
  const [newPriority, setNewPriority] = useState<TaskPriority>('MEDIUM');

  const calendarDates = [
    { key: '2026-09-13', day: 'SUN', num: '13' },
    { key: '2026-09-14', day: 'MON', num: '14' },
    { key: '2026-09-15', day: 'TUE', num: '15' },
    { key: '2026-09-16', day: 'WED', num: '16' },
    { key: '2026-09-17', day: 'THU', num: '17' },
    { key: '2026-09-18', day: 'FRI', num: '18' },
    { key: '2026-09-19', day: 'SAT', num: '19' },
    { key: '2026-09-20', day: 'SUN', num: '20' },
    { key: '2026-09-21', day: 'MON', num: '21' },
    { key: '2026-09-22', day: 'TUE', num: '22' }
  ];

  const filteredTasks = tasks.filter(t => 
    (selectedProjectFilter === 'All' || t.projectId === selectedProjectFilter) &&
    t.date === selectedDate
  );

  const totalIncome = incomes.filter(i => i.status === 'Received').reduce((acc, i) => acc + i.amount, 0);
  const totalExpenses = expenses.reduce((acc, e) => acc + e.amount, 0);
  const unpaidBillsTotal = bills.filter(b => !b.isPaid).reduce((acc, b) => acc + b.amount, 0);
  const netCashflow = totalIncome - totalExpenses;

  const handleCreateTask = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTitle.trim()) return;
    const proj = projects[0];
    const task: Task = {
      id: `task-${Date.now()}`,
      projectId: proj.id,
      projectName: proj.name,
      title: newTitle,
      description: newDesc || 'Scheduled task via PlanCraft interactive client.',
      date: selectedDate,
      time: '10:00 AM',
      durationHours: parseFloat(newHours) || 2.0,
      status: 'TODO',
      priority: newPriority,
      assigneeId: 'user-1',
      assigneeName: 'Kaelen Voss',
      costImpact: parseFloat(newCost) || 0,
      isBillable: true
    };
    onAddTask(task);
    setNewTitle('');
    setNewDesc('');
    setShowAddModal(false);
  };

  return (
    <div id="android-device-container" className="flex flex-col items-center justify-center p-2 sm:p-4">
      {/* Android Device Outer Bezel */}
      <div 
        id="android-phone-frame" 
        className="w-full max-w-[430px] h-[830px] bg-slate-900 border-[8px] border-slate-800 rounded-[44px] shadow-2xl overflow-hidden flex flex-col relative ring-1 ring-slate-700/50"
      >
        {/* Top Camera Punch Hole & Status Bar */}
        <div className="h-10 bg-slate-950 px-6 flex items-center justify-between text-xs text-slate-400 font-mono select-none z-20">
          <span>9:41</span>
          <div className="w-16 h-4 bg-slate-900 rounded-full flex items-center justify-center">
            <div className="w-2.5 h-2.5 bg-slate-950 rounded-full border border-slate-700"></div>
          </div>
          <div className="flex items-center space-x-1.5">
            <span className="text-[10px]">5G</span>
            <div className="w-4 h-2.5 border border-slate-400 rounded-sm p-0.5 flex items-center">
              <div className="w-full h-full bg-emerald-400 rounded-xs"></div>
            </div>
          </div>
        </div>

        {/* Screen Header Bar */}
        <div className="bg-slate-950/90 backdrop-blur px-5 pt-3 pb-3 border-b border-slate-800/80 flex items-center justify-between z-10">
          <div>
            <div className="flex items-center space-x-2">
              <span className="w-2 h-2 rounded-full bg-indigo-500 animate-pulse"></span>
              <span className="text-xs font-semibold tracking-wider text-indigo-400 uppercase">PlanCraft Android</span>
            </div>
            <h1 className="text-base font-bold text-white tracking-tight">
              {currentTab === 'calendar' && 'Sprint Calendar & Tasks'}
              {currentTab === 'projects' && 'Project Management'}
              {currentTab === 'economy' && 'Economy & Budget'}
              {currentTab === 'people' && 'Groups & Timeline'}
            </h1>
          </div>
          {currentTab === 'calendar' && (
            <button
              id="add-task-btn"
              onClick={() => setShowAddModal(true)}
              className="w-8 h-8 rounded-full bg-indigo-600 hover:bg-indigo-500 text-white flex items-center justify-center shadow-lg transition-transform active:scale-95"
              title="Add task to selected date"
            >
              <Plus className="w-4 h-4" />
            </button>
          )}
        </div>

        {/* Scrollable Main Android Content Body */}
        <div className="flex-1 overflow-y-auto bg-slate-950 px-4 py-3 space-y-4 select-none scrollbar-thin scrollbar-thumb-slate-800">
          
          {/* TAB 1: CALENDAR VIEW */}
          {currentTab === 'calendar' && (
            <div className="space-y-4">
              {/* Date Scroll Strip */}
              <div>
                <div className="flex items-center justify-between text-xs text-slate-400 mb-2 font-medium">
                  <span>SEPTEMBER 2026</span>
                  <span className="text-indigo-400 font-mono">Select Day</span>
                </div>
                <div className="flex space-x-2 overflow-x-auto pb-1 scrollbar-none">
                  {calendarDates.map((item) => {
                    const isSelected = selectedDate === item.key;
                    const count = tasks.filter(t => t.date === item.key).length;
                    return (
                      <button
                        key={item.key}
                        id={`calendar-date-${item.key}`}
                        onClick={() => setSelectedDate(item.key)}
                        className={`flex-shrink-0 w-14 py-2 rounded-xl flex flex-col items-center border transition-all ${
                          isSelected 
                            ? 'bg-indigo-600 border-indigo-500 text-white shadow-md shadow-indigo-600/30' 
                            : 'bg-slate-900/90 border-slate-800 text-slate-300 hover:border-slate-700'
                        }`}
                      >
                        <span className={`text-[10px] font-semibold ${isSelected ? 'text-indigo-200' : 'text-slate-400'}`}>
                          {item.day}
                        </span>
                        <span className="text-base font-bold my-0.5">{item.num}</span>
                        {count > 0 ? (
                          <span className={`w-1.5 h-1.5 rounded-full ${isSelected ? 'bg-white' : 'bg-amber-400'}`}></span>
                        ) : (
                          <span className="w-1.5 h-1.5"></span>
                        )}
                      </button>
                    );
                  })}
                </div>
              </div>

              {/* Project Filter Chips */}
              <div className="flex items-center space-x-2 overflow-x-auto py-1 scrollbar-none text-xs">
                <button
                  id="filter-all"
                  onClick={() => setSelectedProjectFilter('All')}
                  className={`px-3 py-1 rounded-full font-medium transition-colors ${
                    selectedProjectFilter === 'All' 
                      ? 'bg-slate-800 text-white border border-slate-700' 
                      : 'bg-slate-950 text-slate-400 border border-slate-900 hover:text-slate-200'
                  }`}
                >
                  All Projects ({tasks.length})
                </button>
                {projects.map(p => (
                  <button
                    key={p.id}
                    id={`filter-${p.id}`}
                    onClick={() => setSelectedProjectFilter(p.id)}
                    className={`px-3 py-1 rounded-full font-medium whitespace-nowrap transition-colors ${
                      selectedProjectFilter === p.id 
                        ? 'bg-indigo-900/60 text-indigo-200 border border-indigo-700' 
                        : 'bg-slate-950 text-slate-400 border border-slate-900 hover:text-slate-200'
                    }`}
                  >
                    {p.name.split(' ')[0]}
                  </button>
                ))}
              </div>

              {/* Task Items List */}
              <div className="space-y-2.5">
                <div className="flex items-center justify-between text-xs text-slate-400">
                  <span>TASKS FOR {selectedDate}</span>
                  <span>{filteredTasks.length} Scheduled</span>
                </div>

                {filteredTasks.length === 0 ? (
                  <div className="p-8 text-center rounded-2xl bg-slate-900/60 border border-slate-800/60 text-slate-400">
                    <CheckCircle2 className="w-8 h-8 text-emerald-500/80 mx-auto mb-2" />
                    <p className="text-sm font-medium text-slate-200">No tasks on this date</p>
                    <p className="text-xs text-slate-500 mt-1">Tap the '+' button above to schedule a task.</p>
                  </div>
                ) : (
                  filteredTasks.map(task => {
                    const isDone = task.status === 'DONE';
                    const priorityColor = 
                      task.priority === 'URGENT' ? 'bg-rose-500' :
                      task.priority === 'HIGH' ? 'bg-amber-500' :
                      task.priority === 'MEDIUM' ? 'bg-cyan-500' : 'bg-slate-500';

                    return (
                      <div
                        key={task.id}
                        id={`task-item-${task.id}`}
                        className={`p-3.5 rounded-2xl border transition-all ${
                          isDone 
                            ? 'bg-slate-900/40 border-slate-800/40 opacity-70' 
                            : 'bg-slate-900/90 border-slate-800 hover:border-slate-700'
                        }`}
                      >
                        <div className="flex items-start justify-between">
                          <div className="flex items-center space-x-2">
                            <span className={`w-2 h-2 rounded-full ${priorityColor}`}></span>
                            <span className="text-[11px] font-semibold text-indigo-400">{task.projectName}</span>
                          </div>
                          <span className={`text-[10px] px-2 py-0.5 rounded-md font-semibold ${
                            isDone ? 'bg-emerald-950 text-emerald-400 border border-emerald-800/50' : 'bg-slate-800 text-slate-300'
                          }`}>
                            {task.status}
                          </span>
                        </div>

                        <div className="flex items-start space-x-3 mt-2.5">
                          <button
                            id={`toggle-task-${task.id}`}
                            onClick={() => onToggleTask(task.id)}
                            className="mt-0.5 text-slate-400 hover:text-emerald-400 transition-colors"
                          >
                            {isDone ? (
                              <CheckCircle2 className="w-5 h-5 text-emerald-400" />
                            ) : (
                              <Circle className="w-5 h-5 text-slate-600 hover:text-slate-400" />
                            )}
                          </button>
                          <div className="flex-1">
                            <h4 className={`text-sm font-semibold text-slate-100 ${isDone ? 'line-through text-slate-500' : ''}`}>
                              {task.title}
                            </h4>
                            <p className="text-xs text-slate-400 mt-1 line-clamp-2 leading-relaxed">
                              {task.description}
                            </p>
                          </div>
                        </div>

                        <div className="mt-3 pt-2.5 border-t border-slate-800/60 flex items-center justify-between text-[11px] text-slate-400">
                          <span className="flex items-center space-x-1">
                            <span>👤 {task.assigneeName}</span>
                          </span>
                          <div className="flex items-center space-x-2.5">
                            <span className="flex items-center space-x-1">
                              <Clock className="w-3 h-3 text-slate-500" />
                              <span>{task.durationHours}h ({task.time || '10:00'})</span>
                            </span>
                            {task.costImpact > 0 && (
                              <span className="font-semibold text-amber-400">
                                ${task.costImpact.toLocaleString()}
                              </span>
                            )}
                          </div>
                        </div>
                      </div>
                    );
                  })
                )}
              </div>
            </div>
          )}

          {/* TAB 2: PROJECTS & KANBAN */}
          {currentTab === 'projects' && (
            <div className="space-y-4">
              {/* Sub tabs */}
              <div className="grid grid-cols-2 p-1 bg-slate-900 border border-slate-800 rounded-xl text-xs font-semibold">
                <button
                  id="tab-portfolios"
                  onClick={() => setProjectSubTab('cards')}
                  className={`py-1.5 rounded-lg transition-all ${
                    projectSubTab === 'cards' ? 'bg-indigo-600 text-white shadow' : 'text-slate-400 hover:text-slate-200'
                  }`}
                >
                  Portfolios ({projects.length})
                </button>
                <button
                  id="tab-kanban"
                  onClick={() => setProjectSubTab('kanban')}
                  className={`py-1.5 rounded-lg transition-all ${
                    projectSubTab === 'kanban' ? 'bg-indigo-600 text-white shadow' : 'text-slate-400 hover:text-slate-200'
                  }`}
                >
                  Kanban Board
                </button>
              </div>

              {projectSubTab === 'cards' ? (
                <div className="space-y-3">
                  {projects.map(p => {
                    const projTasks = tasks.filter(t => t.projectId === p.id);
                    const doneCount = projTasks.filter(t => t.status === 'DONE').length;
                    const spentPct = Math.min(100, Math.round((p.totalSpent / p.totalBudget) * 100));

                    return (
                      <div
                        key={p.id}
                        id={`project-card-${p.id}`}
                        className="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 hover:border-slate-700 transition-all space-y-3"
                      >
                        <div className="flex items-start justify-between">
                          <div>
                            <span className="text-[10px] font-bold tracking-wider uppercase text-cyan-400">{p.client}</span>
                            <h3 className="text-sm font-bold text-white mt-0.5">{p.name}</h3>
                          </div>
                          <span className="text-[10px] px-2 py-0.5 rounded-md font-semibold bg-indigo-950 text-indigo-300 border border-indigo-800/50">
                            {p.status}
                          </span>
                        </div>

                        <p className="text-xs text-slate-400 line-clamp-2 leading-relaxed">{p.description}</p>

                        <div>
                          <div className="flex justify-between text-xs text-slate-300 font-medium mb-1">
                            <span>Milestone Progress</span>
                            <span className="font-bold text-indigo-400">{p.completionPercentage}%</span>
                          </div>
                          <div className="w-full h-1.5 bg-slate-800 rounded-full overflow-hidden">
                            <div 
                              className="h-full bg-gradient-to-r from-indigo-500 to-cyan-400 rounded-full"
                              style={{ width: `${p.completionPercentage}%` }}
                            ></div>
                          </div>
                        </div>

                        {/* Economy Snapshot in Project */}
                        <div className="grid grid-cols-3 gap-2 p-2.5 rounded-xl bg-slate-950/80 border border-slate-800/80 text-center">
                          <div>
                            <p className="text-[9px] text-slate-500 uppercase font-semibold">BUDGET</p>
                            <p className="text-xs font-bold text-slate-200 mt-0.5">${(p.totalBudget / 1000).toFixed(0)}k</p>
                          </div>
                          <div>
                            <p className="text-[9px] text-slate-500 uppercase font-semibold">SPENT</p>
                            <p className={`text-xs font-bold mt-0.5 ${spentPct > 80 ? 'text-amber-400' : 'text-emerald-400'}`}>
                              ${(p.totalSpent / 1000).toFixed(0)}k ({spentPct}%)
                            </p>
                          </div>
                          <div>
                            <p className="text-[9px] text-slate-500 uppercase font-semibold">TASKS</p>
                            <p className="text-xs font-bold text-cyan-400 mt-0.5">{doneCount}/{projTasks.length}</p>
                          </div>
                        </div>

                        <div className="flex items-center justify-between text-[11px] text-slate-400 pt-1">
                          <span>Manager: <strong className="text-slate-300">{p.leadManager}</strong></span>
                          <span>{p.startDate.slice(5)} → {p.targetEndDate.slice(5)}</span>
                        </div>
                      </div>
                    );
                  })}
                </div>
              ) : (
                /* Kanban Columns */
                <div className="space-y-3">
                  {(['TODO', 'IN_PROGRESS', 'DONE'] as const).map(col => {
                    const colTasks = tasks.filter(t => t.status === col);
                    const colTitle = col === 'TODO' ? 'To Do' : col === 'IN_PROGRESS' ? 'In Progress' : 'Completed';
                    return (
                      <div key={col} className="p-3 rounded-2xl bg-slate-900/60 border border-slate-800 space-y-2">
                        <div className="flex items-center justify-between text-xs font-bold text-slate-300 px-1">
                          <span>{colTitle}</span>
                          <span className="w-5 h-5 rounded-full bg-slate-800 text-indigo-400 flex items-center justify-center text-[10px]">
                            {colTasks.length}
                          </span>
                        </div>
                        {colTasks.map(task => (
                          <div
                            key={task.id}
                            className="p-2.5 rounded-xl bg-slate-950 border border-slate-800/80 text-xs space-y-1"
                          >
                            <p className="font-semibold text-slate-200">{task.title}</p>
                            <div className="flex items-center justify-between text-[10px] text-slate-400">
                              <span className="text-cyan-400">{task.projectName.split(' ')[0]}</span>
                              <span>{task.assigneeName}</span>
                            </div>
                          </div>
                        ))}
                      </div>
                    );
                  })}
                </div>
              )}
            </div>
          )}

          {/* TAB 3: ECONOMY, BUDGET, BILLS, INCOME & EXPENSES */}
          {currentTab === 'economy' && (
            <div className="space-y-4">
              {/* Financial KPI Summary Cards */}
              <div className="grid grid-cols-2 gap-2.5">
                <div className="p-3 rounded-2xl bg-slate-900/90 border border-slate-800 space-y-1">
                  <span className="text-[10px] font-bold uppercase text-slate-400 flex items-center space-x-1">
                    <TrendingUp className="w-3 h-3 text-emerald-400" />
                    <span>Net Cash Flow</span>
                  </span>
                  <p className="text-lg font-extrabold text-emerald-400">
                    +${netCashflow.toLocaleString()}
                  </p>
                  <p className="text-[10px] text-slate-500">Received - Total Expenses</p>
                </div>
                <div className="p-3 rounded-2xl bg-slate-900/90 border border-slate-800 space-y-1">
                  <span className="text-[10px] font-bold uppercase text-slate-400 flex items-center space-x-1">
                    <AlertCircle className="w-3 h-3 text-amber-400" />
                    <span>Unpaid Bills</span>
                  </span>
                  <p className="text-lg font-extrabold text-amber-400">
                    ${unpaidBillsTotal.toLocaleString()}
                  </p>
                  <p className="text-[10px] text-slate-500">{bills.filter(b => !b.isPaid).length} payables pending</p>
                </div>
              </div>

              {/* Economy sub-tabs */}
              <div className="grid grid-cols-4 p-1 bg-slate-900 border border-slate-800 rounded-xl text-[11px] font-semibold text-center">
                {(['bills', 'income', 'expenses', 'budget'] as const).map(tab => (
                  <button
                    key={tab}
                    id={`economy-subtab-${tab}`}
                    onClick={() => setEconomySubTab(tab)}
                    className={`py-1.5 rounded-lg capitalize transition-all ${
                      economySubTab === tab ? 'bg-indigo-600 text-white shadow' : 'text-slate-400 hover:text-slate-200'
                    }`}
                  >
                    {tab}
                  </button>
                ))}
              </div>

              {/* Sub-tab 1: Bills */}
              {economySubTab === 'bills' && (
                <div className="space-y-2.5">
                  <div className="flex items-center justify-between text-xs text-slate-400">
                    <span>ACCOUNTS PAYABLE & BILLS</span>
                    <span>{bills.length} Invoices</span>
                  </div>
                  {bills.map(bill => (
                    <div
                      key={bill.id}
                      id={`bill-${bill.id}`}
                      className="p-3.5 rounded-2xl bg-slate-900/90 border border-slate-800 flex items-center justify-between"
                    >
                      <div className="space-y-1 flex-1 pr-2">
                        <div className="flex items-center space-x-2">
                          <span className="text-[11px] font-semibold text-cyan-400">{bill.vendor}</span>
                          {bill.recurringPeriod && (
                            <span className="text-[9px] px-1.5 py-0.5 rounded bg-slate-800 text-slate-400">
                              {bill.recurringPeriod}
                            </span>
                          )}
                        </div>
                        <h4 className="text-xs font-bold text-white">{bill.title}</h4>
                        <p className="text-[10px] text-slate-400">Due: {bill.dueDate} • {bill.invoiceNumber}</p>
                      </div>

                      <div className="text-right space-y-1.5">
                        <p className={`text-sm font-bold ${bill.isPaid ? 'text-slate-400 line-through' : 'text-white'}`}>
                          ${bill.amount.toLocaleString()}
                        </p>
                        <button
                          id={`pay-bill-${bill.id}`}
                          onClick={() => onToggleBill(bill.id)}
                          className={`text-[10px] px-2.5 py-1 rounded-lg font-bold transition-all ${
                            bill.isPaid 
                              ? 'bg-emerald-950 text-emerald-400 border border-emerald-800/60' 
                              : 'bg-indigo-600 hover:bg-indigo-500 text-white'
                          }`}
                        >
                          {bill.isPaid ? 'PAID ✓' : 'PAY NOW'}
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              )}

              {/* Sub-tab 2: Income */}
              {economySubTab === 'income' && (
                <div className="space-y-2.5">
                  <div className="flex items-center justify-between text-xs text-slate-400">
                    <span>REVENUES & CLIENT MILESTONES</span>
                    <span className="text-emerald-400 font-bold">+${totalIncome.toLocaleString()} Received</span>
                  </div>
                  {incomes.map(inc => (
                    <div
                      key={inc.id}
                      className="p-3.5 rounded-2xl bg-slate-900/90 border border-slate-800 flex items-center justify-between"
                    >
                      <div className="space-y-0.5 flex-1">
                        <span className="text-[10px] font-bold text-indigo-400">{inc.projectName}</span>
                        <h4 className="text-xs font-bold text-white">{inc.title}</h4>
                        <p className="text-[10px] text-slate-400">{inc.source.replace('_', ' ')} • {inc.date}</p>
                      </div>
                      <div className="text-right space-y-1">
                        <p className="text-sm font-bold text-emerald-400">+${inc.amount.toLocaleString()}</p>
                        <span className={`text-[9px] px-2 py-0.5 rounded-md font-bold ${
                          inc.status === 'Received' ? 'bg-emerald-950 text-emerald-400' : 'bg-amber-950 text-amber-400'
                        }`}>
                          {inc.status}
                        </span>
                      </div>
                    </div>
                  ))}
                </div>
              )}

              {/* Sub-tab 3: Expenses */}
              {economySubTab === 'expenses' && (
                <div className="space-y-2.5">
                  <div className="flex items-center justify-between text-xs text-slate-400">
                    <span>OPERATIONAL EXPENSES</span>
                    <span className="text-rose-400 font-bold">-${totalExpenses.toLocaleString()} Total</span>
                  </div>
                  {expenses.map(exp => (
                    <div
                      key={exp.id}
                      className="p-3 rounded-2xl bg-slate-900/90 border border-slate-800 flex items-center justify-between"
                    >
                      <div className="space-y-0.5 flex-1 pr-2">
                        <span className="text-[10px] font-semibold text-cyan-400">{exp.category.replace(/_/g, ' ')}</span>
                        <h4 className="text-xs font-semibold text-slate-100">{exp.description}</h4>
                        <p className="text-[10px] text-slate-400">{exp.loggedBy} via {exp.paymentMethod} • {exp.date}</p>
                      </div>
                      <span className="text-sm font-bold text-rose-400 whitespace-nowrap">
                        -${exp.amount.toLocaleString()}
                      </span>
                    </div>
                  ))}
                </div>
              )}

              {/* Sub-tab 4: Department Budgets */}
              {economySubTab === 'budget' && (
                <div className="space-y-3">
                  {[
                    { name: 'Infrastructure & Cloud', spent: 31200, total: 45000 },
                    { name: 'Engineering & Payroll', spent: 84000, total: 120000 },
                    { name: 'Software Licenses & Tools', spent: 12400, total: 18000 },
                    { name: 'Hardware & Lab Equipment', spent: 19200, total: 25000 },
                    { name: 'Legal, Audits & Compliance', spent: 18500, total: 35000 }
                  ].map(cat => {
                    const pct = Math.round((cat.spent / cat.total) * 100);
                    return (
                      <div key={cat.name} className="p-3 rounded-2xl bg-slate-900/90 border border-slate-800 space-y-1.5">
                        <div className="flex justify-between text-xs font-semibold text-slate-200">
                          <span>{cat.name}</span>
                          <span className={pct > 80 ? 'text-amber-400' : 'text-emerald-400'}>{pct}%</span>
                        </div>
                        <div className="w-full h-1.5 bg-slate-800 rounded-full overflow-hidden">
                          <div 
                            className={`h-full rounded-full ${pct > 80 ? 'bg-amber-500' : 'bg-indigo-500'}`}
                            style={{ width: `${pct}%` }}
                          ></div>
                        </div>
                        <div className="flex justify-between text-[10px] text-slate-400">
                          <span>Spent: ${cat.spent.toLocaleString()}</span>
                          <span>Limit: ${cat.total.toLocaleString()}</span>
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </div>
          )}

          {/* TAB 4: GROUPS OF PEOPLE INVOLVED OVER TIME */}
          {currentTab === 'people' && (
            <div className="space-y-4">
              <div className="grid grid-cols-2 p-1 bg-slate-900 border border-slate-800 rounded-xl text-xs font-semibold">
                <button
                  id="tab-phases"
                  onClick={() => setPeopleSubTab('timeline')}
                  className={`py-1.5 rounded-lg transition-all ${
                    peopleSubTab === 'timeline' ? 'bg-indigo-600 text-white shadow' : 'text-slate-400 hover:text-slate-200'
                  }`}
                >
                  Phases Over Time
                </button>
                <button
                  id="tab-team"
                  onClick={() => setPeopleSubTab('team')}
                  className={`py-1.5 rounded-lg transition-all ${
                    peopleSubTab === 'team' ? 'bg-indigo-600 text-white shadow' : 'text-slate-400 hover:text-slate-200'
                  }`}
                >
                  Contributors ({members.length})
                </button>
              </div>

              {peopleSubTab === 'timeline' ? (
                <div className="space-y-3">
                  {phases.map(phase => (
                    <div
                      key={phase.id}
                      className="p-4 rounded-2xl bg-slate-900/90 border border-slate-800 space-y-3"
                    >
                      <div className="flex items-center justify-between">
                        <span className="text-[10px] font-bold tracking-wider uppercase text-indigo-400">
                          {phase.quarter}
                        </span>
                        <span className={`text-[10px] px-2 py-0.5 rounded-md font-bold ${
                          phase.progress === 100 
                            ? 'bg-emerald-950 text-emerald-400 border border-emerald-800/50' 
                            : 'bg-amber-950 text-amber-400 border border-amber-800/50'
                        }`}>
                          {phase.progress === 100 ? 'COMPLETED' : `${phase.progress}% ACTIVE`}
                        </span>
                      </div>

                      <div>
                        <h3 className="text-sm font-bold text-white">{phase.phaseName}</h3>
                        <p className="text-[11px] text-slate-400 mt-0.5">{phase.startDate} → {phase.endDate}</p>
                      </div>

                      {/* Involved Stakeholder Groups */}
                      <div>
                        <p className="text-[10px] text-slate-500 font-semibold uppercase mb-1.5">
                          GROUPS ENGAGED IN THIS PHASE
                        </p>
                        <div className="flex flex-wrap gap-1.5">
                          {phase.involvedGroups.map(grp => (
                            <span 
                              key={grp} 
                              className="text-[10px] px-2 py-0.5 rounded-md bg-slate-800/90 text-cyan-300 border border-slate-700/80 font-medium"
                            >
                              {grp.replace(/_/g, ' ')}
                            </span>
                          ))}
                        </div>
                      </div>

                      <div className="flex items-center justify-between pt-2 border-t border-slate-800 text-xs text-slate-400">
                        <span className="flex items-center space-x-1.5">
                          <Users className="w-3.5 h-3.5 text-indigo-400" />
                          <span>{phase.headCount} Headcount Allocated</span>
                        </span>
                        <span className="font-semibold text-emerald-400">
                          ${(phase.estimatedBudget / 1000).toFixed(0)}k Phase Budget
                        </span>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                /* Stakeholders & Team Members Directory */
                <div className="space-y-2.5">
                  {members.map(member => (
                    <div
                      key={member.id}
                      className="p-3 rounded-2xl bg-slate-900/90 border border-slate-800 flex items-center space-x-3"
                    >
                      <div className="w-10 h-10 rounded-full bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center font-bold text-white text-xs">
                        {member.name.split(' ').map(n => n[0]).join('')}
                      </div>
                      <div className="flex-1 min-w-0">
                        <h4 className="text-xs font-bold text-white truncate">{member.name}</h4>
                        <p className="text-[11px] text-slate-400 truncate">{member.role}</p>
                        <p className="text-[10px] text-indigo-400 mt-0.5">{member.activePeriod} • {member.group.replace(/_/g, ' ')}</p>
                      </div>
                      <div className="text-right">
                        <span className={`text-[10px] font-bold px-2 py-0.5 rounded-md ${
                          member.allocationPercentage >= 90 ? 'bg-amber-950 text-amber-400' : 'bg-emerald-950 text-emerald-400'
                        }`}>
                          {member.allocationPercentage}% Load
                        </span>
                        {member.hourlyRate > 0 && (
                          <p className="text-[10px] text-slate-400 mt-1">${member.hourlyRate}/hr</p>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

        </div>

        {/* Android Bottom Navigation Bar */}
        <div className="h-16 bg-slate-950 border-t border-slate-800/80 px-4 grid grid-cols-4 items-center z-10">
          <button
            id="nav-btn-calendar"
            onClick={() => setCurrentTab('calendar')}
            className={`flex flex-col items-center justify-center space-y-1 transition-colors ${
              currentTab === 'calendar' ? 'text-indigo-400' : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            <CalendarIcon className="w-5 h-5" />
            <span className="text-[10px] font-semibold">Calendar</span>
          </button>

          <button
            id="nav-btn-projects"
            onClick={() => setCurrentTab('projects')}
            className={`flex flex-col items-center justify-center space-y-1 transition-colors ${
              currentTab === 'projects' ? 'text-indigo-400' : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            <FolderKanban className="w-5 h-5" />
            <span className="text-[10px] font-semibold">Projects</span>
          </button>

          <button
            id="nav-btn-economy"
            onClick={() => setCurrentTab('economy')}
            className={`flex flex-col items-center justify-center space-y-1 transition-colors ${
              currentTab === 'economy' ? 'text-indigo-400' : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            <Wallet className="w-5 h-5" />
            <span className="text-[10px] font-semibold">Economy</span>
          </button>

          <button
            id="nav-btn-people"
            onClick={() => setCurrentTab('people')}
            className={`flex flex-col items-center justify-center space-y-1 transition-colors ${
              currentTab === 'people' ? 'text-indigo-400' : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            <Users className="w-5 h-5" />
            <span className="text-[10px] font-semibold">People</span>
          </button>
        </div>

        {/* Bottom Home Pill */}
        <div className="h-3 bg-slate-950 flex items-center justify-center pb-1">
          <div className="w-32 h-1 bg-slate-700 rounded-full"></div>
        </div>

        {/* Modal: Schedule Task */}
        {showAddModal && (
          <div className="absolute inset-0 bg-black/70 backdrop-blur-sm flex items-end sm:items-center justify-center p-4 z-50 animate-fade-in">
            <div className="w-full bg-slate-900 border border-slate-800 rounded-3xl p-5 space-y-4 shadow-2xl">
              <div className="flex items-center justify-between">
                <h3 className="text-sm font-bold text-white">Schedule Task for {selectedDate}</h3>
                <button 
                  onClick={() => setShowAddModal(false)}
                  className="text-slate-400 hover:text-white text-sm"
                >
                  ✕
                </button>
              </div>

              <form onSubmit={handleCreateTask} className="space-y-3">
                <div>
                  <label className="text-[11px] font-semibold text-slate-400 block mb-1">Task Title</label>
                  <input
                    type="text"
                    required
                    value={newTitle}
                    onChange={e => setNewTitle(e.target.value)}
                    placeholder="e.g., Code Review for Security Module"
                    className="w-full px-3 py-2 rounded-xl bg-slate-950 border border-slate-800 text-xs text-white focus:outline-none focus:border-indigo-500"
                  />
                </div>

                <div>
                  <label className="text-[11px] font-semibold text-slate-400 block mb-1">Description</label>
                  <textarea
                    rows={2}
                    value={newDesc}
                    onChange={e => setNewDesc(e.target.value)}
                    placeholder="Deliverable specifications and acceptance criteria"
                    className="w-full px-3 py-2 rounded-xl bg-slate-950 border border-slate-800 text-xs text-white focus:outline-none focus:border-indigo-500"
                  />
                </div>

                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="text-[11px] font-semibold text-slate-400 block mb-1">Estimated Hours</label>
                    <input
                      type="number"
                      step="0.5"
                      value={newHours}
                      onChange={e => setNewHours(e.target.value)}
                      className="w-full px-3 py-2 rounded-xl bg-slate-950 border border-slate-800 text-xs text-white focus:outline-none focus:border-indigo-500"
                    />
                  </div>
                  <div>
                    <label className="text-[11px] font-semibold text-slate-400 block mb-1">Cost Impact ($)</label>
                    <input
                      type="number"
                      value={newCost}
                      onChange={e => setNewCost(e.target.value)}
                      className="w-full px-3 py-2 rounded-xl bg-slate-950 border border-slate-800 text-xs text-white focus:outline-none focus:border-indigo-500"
                    />
                  </div>
                </div>

                <div>
                  <label className="text-[11px] font-semibold text-slate-400 block mb-1">Priority</label>
                  <div className="grid grid-cols-4 gap-1">
                    {(['LOW', 'MEDIUM', 'HIGH', 'URGENT'] as const).map(p => (
                      <button
                        type="button"
                        key={p}
                        onClick={() => setNewPriority(p)}
                        className={`py-1 rounded-lg text-[10px] font-bold border transition-all ${
                          newPriority === p 
                            ? 'bg-indigo-600 border-indigo-500 text-white' 
                            : 'bg-slate-950 border-slate-800 text-slate-400'
                        }`}
                      >
                        {p}
                      </button>
                    ))}
                  </div>
                </div>

                <div className="flex space-x-2 pt-2">
                  <button
                    type="button"
                    onClick={() => setShowAddModal(false)}
                    className="flex-1 py-2.5 rounded-xl bg-slate-800 text-slate-300 text-xs font-semibold hover:bg-slate-700"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="flex-1 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-bold shadow-lg"
                  >
                    Add to Calendar
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
