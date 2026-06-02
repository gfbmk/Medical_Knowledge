@rem
@rem GitHub Actions Local Build Helper
@rem This script helps build the APK locally for testing
@rem

@echo off
color 0A
echo.
echo  ╔═══════════════════════════════════════════════════════════════╗
echo  ║         Medical Knowledge - Local Build Helper               ║
echo  ╚═══════════════════════════════════════════════════════════════╝
echo.

cd /d "%~dp0"

if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo.
    echo  ⚠️  WARNING: gradle-wrapper.jar not found!
    echo.
    echo  The project is configured for GitHub Actions CI/CD builds.
    echo  To build locally, you need to download gradle-wrapper.jar first.
    echo.
    echo  Option 1: Use Android Studio (Recommended)
    echo    - Open this project in Android Studio
    echo    - Android Studio will download gradle-wrapper.jar automatically
    echo    - Click Run to build and install the app
    echo.
    echo  Option 2: Download manually
    echo    - Download gradle-wrapper.jar from:
    echo    - https://github.com/AayushSanghai/android-project-template/raw/main/gradle/wrapper/gradle-wrapper.jar
    echo    - Save it to: gradle\wrapper\gradle-wrapper.jar
    echo.
    echo  Option 3: Use system Gradle (if installed)
    echo    - Install Gradle 8.0 from https://gradle.org/releases/
    echo    - Run: gradle assembleDebug
    echo.
    echo  Press any key to open GitHub Actions in browser...
    pause >nul
    start https://github.com/gfbmk/Medical_Knowledge/actions
    exit /b
)

echo.
echo  ✓ gradle-wrapper.jar found!
echo.

echo  Starting build process...
echo.

gradlew.bat assembleDebug --stacktrace

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo  ╔═══════════════════════════════════════════════════════════════╗
    echo  ║                    Build Failed!                             ║
    echo  ╚═══════════════════════════════════════════════════════════════╝
    echo.
    echo  Possible issues:
    echo  - Check your internet connection
    echo  - Ensure JDK 17+ is installed
    echo  - Try running: gradlew.bat clean
    echo.
    pause
    exit /b 1
)

echo.
echo  ╔═══════════════════════════════════════════════════════════════╗
echo  ║                    Build Successful!                          ║
echo  ╚═══════════════════════════════════════════════════════════════╝
echo.
echo  APK Location: app\build\outputs\apk\debug\app-debug.apk
echo.
echo  Press any key to open the APK folder...
pause >nul
explorer "app\build\outputs\apk\debug"

:end