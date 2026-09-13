import React, { useState } from 'react';
import { 
  FileCode, 
  Copy, 
  Check, 
  Download, 
  FolderTree, 
  GitBranch, 
  Terminal, 
  FileCheck,
  ShieldCheck
} from 'lucide-react';
import { REPO_FILES, RepoFile } from '../utils/repoFiles';
import { downloadAndroidRepoZip } from '../utils/zipExporter';
import confetti from 'canvas-confetti';

export const RepoExplorer: React.FC = () => {
  const [selectedFile, setSelectedFile] = useState<RepoFile>(REPO_FILES[0]);
  const [copied, setCopied] = useState(false);
  const [isDownloading, setIsDownloading] = useState(false);

  const handleCopyCode = () => {
    navigator.clipboard.writeText(selectedFile.content);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleDownloadZip = async () => {
    try {
      setIsDownloading(true);
      await downloadAndroidRepoZip();
      confetti({
        particleCount: 80,
        spread: 70,
        origin: { y: 0.6 }
      });
    } catch (err) {
      console.error('Failed to download zip', err);
    } finally {
      setIsDownloading(false);
    }
  };

  return (
    <div id="repo-explorer-root" className="w-full max-w-6xl mx-auto space-y-6">
      {/* Top Banner with 1-Click ZIP Download and GitHub Actions Callout */}
      <div className="p-6 rounded-3xl bg-gradient-to-r from-indigo-950/80 via-slate-900 to-slate-900 border border-indigo-800/40 flex flex-col md:flex-row md:items-center justify-between gap-4 shadow-xl">
        <div className="space-y-1">
          <div className="flex items-center space-x-2">
            <span className="px-2.5 py-0.5 rounded-full text-xs font-bold bg-indigo-600/30 text-indigo-300 border border-indigo-500/30 flex items-center space-x-1">
              <GitBranch className="w-3 h-3" />
              <span>Full GitHub Repo Tree</span>
            </span>
            <span className="px-2.5 py-0.5 rounded-full text-xs font-bold bg-emerald-950 text-emerald-300 border border-emerald-800/40 flex items-center space-x-1">
              <ShieldCheck className="w-3 h-3" />
              <span>CI/CD build.yml Included</span>
            </span>
          </div>
          <h2 className="text-xl font-bold text-white tracking-tight">PlanCraft Android Project Repository</h2>
          <p className="text-xs text-slate-400 max-w-2xl leading-relaxed">
            Ready-to-build Android Jetpack Compose codebase equipped with Gradle wrapper scripts, AndroidManifest, 
            rich models (Calendar tasks, Project Kanban, Economy, Bills, Stakeholder timeline), and GitHub Actions build automation.
          </p>
        </div>

        <button
          id="btn-download-zip"
          onClick={handleDownloadZip}
          disabled={isDownloading}
          className="px-5 py-3 rounded-2xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-sm shadow-lg shadow-indigo-600/30 flex items-center justify-center space-x-2 transition-all active:scale-95 flex-shrink-0"
        >
          <Download className="w-4 h-4" />
          <span>{isDownloading ? 'Bundling ZIP...' : 'Download Full Android Repo (.ZIP)'}</span>
        </button>
      </div>

      {/* Explorer Workspace */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-5 rounded-3xl bg-slate-900/60 border border-slate-800 p-4 lg:p-6">
        {/* Left: File Tree */}
        <div className="lg:col-span-4 space-y-3">
          <div className="flex items-center justify-between pb-2 border-b border-slate-800 text-xs font-bold text-slate-300">
            <span className="flex items-center space-x-1.5">
              <FolderTree className="w-4 h-4 text-indigo-400" />
              <span>REPOSITORY FILES ({REPO_FILES.length})</span>
            </span>
            <span className="text-slate-500 font-mono text-[10px]">main branch</span>
          </div>

          <div className="space-y-1 overflow-y-auto max-h-[600px] pr-1">
            {REPO_FILES.map((file) => {
              const isSelected = selectedFile.path === file.path;
              const badgeColor = 
                file.category === 'workflow' ? 'text-amber-400 bg-amber-950/60 border-amber-800/40' :
                file.category === 'gradle' ? 'text-cyan-400 bg-cyan-950/60 border-cyan-800/40' :
                file.category === 'kotlin' ? 'text-indigo-300 bg-indigo-950/60 border-indigo-800/40' :
                'text-slate-400 bg-slate-800 border-slate-700';

              return (
                <button
                  key={file.path}
                  id={`file-tree-${file.path.replace(/[/.]/g, '-')}`}
                  onClick={() => setSelectedFile(file)}
                  className={`w-full text-left px-3 py-2.5 rounded-xl border text-xs font-mono transition-all flex items-center justify-between ${
                    isSelected 
                      ? 'bg-indigo-950/70 border-indigo-700 text-white shadow-sm' 
                      : 'bg-slate-950/50 border-slate-800/60 text-slate-400 hover:text-slate-200 hover:border-slate-700'
                  }`}
                >
                  <div className="flex items-center space-x-2 truncate pr-2">
                    <FileCode className={`w-3.5 h-3.5 flex-shrink-0 ${isSelected ? 'text-indigo-400' : 'text-slate-500'}`} />
                    <span className="truncate">{file.path}</span>
                  </div>
                  <span className={`text-[9px] px-1.5 py-0.5 rounded border uppercase flex-shrink-0 ${badgeColor}`}>
                    {file.category}
                  </span>
                </button>
              );
            })}
          </div>

          {/* Git Quick Push Snippet */}
          <div className="p-3 rounded-2xl bg-slate-950 border border-slate-800 text-[11px] font-mono text-slate-400 space-y-1.5">
            <p className="font-semibold text-slate-300 flex items-center space-x-1">
              <Terminal className="w-3.5 h-3.5 text-indigo-400" />
              <span>GitHub Push Instructions</span>
            </p>
            <p className="text-slate-500 text-[10px]">Push this directory to your GitHub remote:</p>
            <div className="bg-slate-900 p-2 rounded-lg text-slate-300 overflow-x-auto text-[10px] space-y-0.5">
              <p>git init</p>
              <p>git add .</p>
              <p>git commit -m "feat: PlanCraft Android with CI/CD"</p>
              <p>git branch -M main</p>
              <p>git remote add origin &lt;repo-url&gt;</p>
              <p>git push -u origin main</p>
            </div>
          </div>
        </div>

        {/* Right: Code Viewer */}
        <div className="lg:col-span-8 flex flex-col rounded-2xl bg-slate-950 border border-slate-800 overflow-hidden">
          {/* Code Viewer Header */}
          <div className="px-4 py-3 bg-slate-900/80 border-b border-slate-800 flex items-center justify-between">
            <div className="flex items-center space-x-2 truncate">
              <span className="w-2.5 h-2.5 rounded-full bg-indigo-500"></span>
              <span className="font-mono text-xs font-bold text-slate-200 truncate">{selectedFile.path}</span>
              <span className="text-[10px] px-2 py-0.5 rounded bg-slate-800 text-slate-400 uppercase font-mono">
                {selectedFile.language}
              </span>
            </div>

            <button
              id="copy-code-btn"
              onClick={handleCopyCode}
              className="px-3 py-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-300 text-xs font-semibold flex items-center space-x-1.5 transition-colors"
            >
              {copied ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
              <span>{copied ? 'Copied!' : 'Copy File'}</span>
            </button>
          </div>

          {/* Code Body */}
          <div className="p-4 overflow-auto max-h-[580px] font-mono text-xs leading-relaxed text-slate-300 bg-slate-950">
            <pre className="whitespace-pre">
              {selectedFile.content}
            </pre>
          </div>
        </div>
      </div>
    </div>
  );
};
