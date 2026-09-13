import JSZip from 'jszip';
import { REPO_FILES } from './repoFiles';

export async function downloadAndroidRepoZip(): Promise<void> {
  const zip = new JSZip();

  // Add all Android files
  for (const file of REPO_FILES) {
    zip.file(file.path, file.content);
  }

  // Add gradle wrapper script and properties
  zip.file(
    'gradle/wrapper/gradle-wrapper.properties',
    `distributionBase=GRADLE_USER_HOME\ndistributionPath=wrapper/dists\ndistributionUrl=https\\://services.gradle.org/distributions/gradle-8.4-bin.zip\nnetworkTimeout=10000\nvalidateDistributionUrl=true\nzipStoreBase=GRADLE_USER_HOME\nzipStorePath=wrapper/dists\n`
  );

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

  zip.file(
    'gradlew',
    `#!/bin/sh\nexec java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain "$@"\n`,
    { unixPermissions: '755' }
  );

  zip.file(
    'gradlew.bat',
    `@rem Gradle startup script for Windows\r\n@java -classpath "%~dp0gradle\\wrapper\\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*\r\n`
  );

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
