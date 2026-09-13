export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'IN_REVIEW' | 'DONE';
export type TaskPriority = 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';

export interface Task {
  id: string;
  projectId: string;
  projectName: string;
  title: string;
  description: string;
  date: string; // YYYY-MM-DD
  time?: string;
  durationHours: number;
  status: TaskStatus;
  priority: TaskPriority;
  assigneeId: string;
  assigneeName: string;
  costImpact: number;
  milestone?: string;
  isBillable: boolean;
}

export interface Project {
  id: string;
  name: string;
  client: string;
  description: string;
  status: 'PLANNING' | 'IN_PROGRESS' | 'ON_HOLD' | 'COMPLETED';
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  startDate: string;
  targetEndDate: string;
  totalBudget: number;
  totalSpent: number;
  leadManager: string;
  completionPercentage: number;
  tags: string[];
}

export interface Bill {
  id: string;
  title: string;
  vendor: string;
  amount: number;
  dueDate: string;
  isPaid: boolean;
  category: string;
  recurringPeriod?: string;
  invoiceNumber?: string;
}

export interface Income {
  id: string;
  projectId: string;
  projectName: string;
  source: string;
  title: string;
  amount: number;
  date: string;
  status: 'Received' | 'Pending';
  referenceCode: string;
}

export interface Expense {
  id: string;
  projectId: string;
  projectName: string;
  category: string;
  description: string;
  amount: number;
  date: string;
  loggedBy: string;
  paymentMethod: string;
}

export interface StakeholderPhase {
  id: string;
  phaseName: string;
  quarter: string;
  startDate: string;
  endDate: string;
  progress: number;
  involvedGroups: string[];
  headCount: number;
  estimatedBudget: number;
}

export interface TeamMember {
  id: string;
  name: string;
  role: string;
  email: string;
  group: string;
  hourlyRate: number;
  allocationPercentage: number;
  activePeriod: string;
}
