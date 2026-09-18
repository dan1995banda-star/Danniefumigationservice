# DANNIE FUMIGATION SERVICES — Android App

GitHub-ready Android project for **DANNIE FUMIGATION SERVICES**.

## What is included
- Professional blue/green Android app design
- DANNIE FUMIGATION SERVICES logo
- Services and service-detail pages
- Service price list
- WhatsApp quote/booking buttons
- Facebook link
- Booking form
- Local service images bundled in the app
- GitHub Actions workflow that automatically builds the APK

## Build the APK on GitHub
1. Create a new GitHub repository, for example `DannieFumigationServices`.
2. Upload **all files and folders inside this project folder** to the repository root. Do not upload the outer ZIP as the only repository file.
3. Commit the files to the `main` branch.
4. Open **Actions** in GitHub.
5. Select **Build Dannie Fumigation APK**.
6. Click **Run workflow** and select `main` if GitHub asks for a branch.
7. Wait for the workflow to finish.
8. Open the completed workflow run and download the artifact named **Dannie-Fumigation-debug-APK**.
9. Extract the artifact and install `app-debug.apk` on an Android phone.

The workflow installs JDK 17, Android SDK 35 and Gradle 8.9 automatically, then builds the debug APK.

## App details
- Application ID: `mw.dannie.fumigation`
- Version: `2.1`
- Minimum Android: API 24 (Android 7.0)
- Target/Compile SDK: 35
