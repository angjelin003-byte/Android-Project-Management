import JSZip from 'jszip';
import { REPO_FILES } from './repoFiles';

export async function downloadAndroidRepoZip(): Promise<void> {
  const zip = new JSZip();

  // Add all Android files
  for (const file of REPO_FILES) {
    if (file.path === 'gradlew') {
      zip.file(file.path, file.content, { unixPermissions: '755' });
    } else {
      zip.file(file.path, file.content);
    }
  }

  // Fetch and bundle the official gradle-wrapper.jar so gradlew works immediately without ClassNotFoundException
  try {
    const jarRes = await fetch('https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar');
    if (jarRes.ok) {
      const jarBuffer = await jarRes.arrayBuffer();
      zip.file('gradle/wrapper/gradle-wrapper.jar', jarBuffer, { binary: true });
    }
  } catch (err) {
    console.warn('Could not bundle remote wrapper jar into zip:', err);
  }

  const blob = await zip.generateAsync({ type: 'blob' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'plancraft-android-repo.zip';
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}
