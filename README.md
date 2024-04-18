# Fishlog

## Setup
To build this project, you need the latest stable version of [Android Studio](https://developer.android.com/studio).

1. Clone or download the project and open it in Android Studio.
2. Create a `local.properties` file in the project root folder if it doesn't exist.
3. Add the required properties to the `local.properties` file as shown below.

```android
...

MAPS_API_KEY="your key here"
FACEBOOK_APP_ID="your app id here"
fb_login_protocol_scheme="your scheme here"
FACEBOOK_CLIENT_TOKEN="your client token here"
GEMINI_API_KEY="your key here"
```
4. Add `google-services.json` file to the `app` folder. You can download the file from the Firebase console.
5. Sync the project with Gradle and run the app on an Android emulator or a physical Android device.