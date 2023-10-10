# Travel Help Android App Setup

## Introduction

Travel Help is an Android application designed to assist travelers with various features and functionalities.

## Prerequisites

Ensure to have the following prerequisites installed on your development environment:

- Android Studio
- Android SDK with API level 33 (Android 13)
- Android SDK with API level 29 (Android 10)
- Pixel 3a emulator or physical device

## Installation Steps

1. **Clone the Repository:**

    ```
    git clone <repo_url>
    ```

2. **Open the Project:**

   Launch Android Studio and select "Open an existing Android Studio project." Navigate to the cloned repository directory and select the `TravelHelp` project folder.

3. **Configure SDK Versions:**

   In the `build.gradle` files (usually located in the `app` module), ensure that the `compileSdkVersion` and `targetSdkVersion` are set to 33. Also, make sure the `minSdkVersion` is set to 29.

    ```groovy
    android {
        compileSdkVersion 33
        defaultConfig {
            applicationId "com.sliit.travelhelp"
            minSdkVersion 26
            targetSdkVersion 33
            // ...
        }
        // ...
    }
    ```

4. **Sync Gradle:**

   Android Studio should prompt you to sync the project with Gradle files. If not, click the "Sync Now" button in the toolbar.

5. **Run on Pixel 3a:**

   Connect your Pixel 3a device via USB or start the Pixel 3a emulator. Make sure the device is recognized by Android Studio.

6. **Build and Run:**

   Click the "Run" button (green triangle) in Android Studio. Choose the Pixel 3a device from the list of available devices/emulators.

7. **Wait for Installation:**

   Android Studio will build the app and install it on your Pixel 3a device. This may take a few moments.

8. **Launch the App:**

   Once the installation is complete, the "Travel Help" app should automatically launch on your Pixel 3a.


