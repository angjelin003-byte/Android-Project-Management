import React from 'react';
import { 
  PlayCircle, 
  CheckCircle2, 
  PackageCheck, 
  Cpu, 
  Flame, 
  ArrowRight,
  GitPullRequest,
  DownloadCloud,
  FileCheck
} from 'lucide-react';

export const WorkflowGuide: React.FC = () => {
  const steps = [
    {
      title: "1. Trigger Matrix",
      desc: "Triggers on every 'push' and 'pull_request' to 'main' or 'master', as well as manual trigger via 'workflow_dispatch'.",
      icon: GitPullRequest,
      color: "text-indigo-400 bg-indigo-950/60 border-indigo-800/40"
    },
    {
      title: "2. JDK 17 & SDK Provisioning",
      desc: "Installs Eclipse Temurin JDK 17, caches Gradle wrappers & dependencies to shave 70% off build times, and pulls the Android SDK 34.",
      icon: Cpu,
      color: "text-cyan-400 bg-cyan-950/60 border-cyan-800/40"
    },
    {
      title: "3. Unit Testing & APK Compilation",
      desc: "Runs './gradlew testDebugUnitTest' and compiles native bytecode with './gradlew assembleDebug --stacktrace'.",
      icon: Flame,
      color: "text-amber-400 bg-amber-950/60 border-amber-800/40"
    },
    {
      title: "4. Artifact Publication",
      desc: "Publishes 'plancraft-app-debug' (app-debug.apk) and optional App Bundle (app-debug.aab) available for 14 days in GitHub Actions.",
      icon: PackageCheck,
      color: "text-emerald-400 bg-emerald-950/60 border-emerald-800/40"
    }
  ];

  return (
    <div id="workflow-guide-root" className="w-full max-w-6xl mx-auto space-y-6">
      {/* Header */}
      <div className="text-center max-w-2xl mx-auto space-y-2">
        <span className="px-3 py-1 rounded-full text-xs font-bold bg-indigo-900/50 text-indigo-300 border border-indigo-700/50 uppercase tracking-wider">
          GitHub Actions CI/CD Architecture
        </span>
        <h2 className="text-2xl font-bold text-white tracking-tight">
          Automated Android APK Build Pipeline (<code className="text-indigo-400 font-mono text-xl">build.yml</code>)
        </h2>
        <p className="text-xs text-slate-400 leading-relaxed">
          How GitHub Actions turns your committed Kotlin source code into an installable Android APK on every push.
        </p>
      </div>

      {/* 4 Pipeline Steps Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        {steps.map((s, idx) => {
          const Icon = s.icon;
          return (
            <div
              key={s.title}
              className="p-5 rounded-2xl bg-slate-900/80 border border-slate-800 space-y-3 flex flex-col justify-between"
            >
              <div className="space-y-3">
                <div className={`w-10 h-10 rounded-xl border flex items-center justify-center ${s.color}`}>
                  <Icon className="w-5 h-5" />
                </div>
                <h3 className="text-sm font-bold text-white">{s.title}</h3>
                <p className="text-xs text-slate-400 leading-relaxed">{s.desc}</p>
              </div>
              <div className="text-[10px] font-mono text-slate-500 pt-2 border-t border-slate-800/60">
                Stage 0{idx + 1} of 04
              </div>
            </div>
          );
        })}
      </div>

      {/* Artifact Location Guide */}
      <div className="p-6 rounded-3xl bg-slate-900/60 border border-slate-800 space-y-4">
        <h3 className="text-base font-bold text-white flex items-center space-x-2">
          <DownloadCloud className="w-5 h-5 text-indigo-400" />
          <span>Where to find your compiled APK after pushing to GitHub:</span>
        </h3>
        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-3 text-xs">
          <div className="p-4 rounded-xl bg-slate-950 border border-slate-800/80 space-y-1">
            <span className="font-bold text-indigo-400">Step 1</span>
            <p className="text-slate-200 font-medium">Navigate to GitHub "Actions" tab</p>
            <p className="text-[11px] text-slate-500">Click the repository's top "Actions" navigation link.</p>
          </div>

          <div className="p-4 rounded-xl bg-slate-950 border border-slate-800/80 space-y-1">
            <span className="font-bold text-indigo-400">Step 2</span>
            <p className="text-slate-200 font-medium">Select latest workflow run</p>
            <p className="text-[11px] text-slate-500">Look for "Build Android App & Generate APK" with a green checkmark.</p>
          </div>

          <div className="p-4 rounded-xl bg-slate-950 border border-slate-800/80 space-y-1">
            <span className="font-bold text-indigo-400">Step 3</span>
            <p className="text-slate-200 font-medium">Download "plancraft-app-debug.zip"</p>
            <p className="text-[11px] text-slate-500">Scroll to "Artifacts" at the bottom of the summary page to install on Android.</p>
          </div>
        </div>
      </div>
    </div>
  );
};
