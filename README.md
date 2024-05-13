<div align="center">
  <img src="https://github.com/harissabil/Fishlog/blob/master/app/src/main/ic_launcher-playstore.png" width="150" alt="Centered Image">
  <h1>Fishlog: Angler's Logbook</h1>
</div>

### 🎣No Fishing No Life!
Fishlog is a fishing app that helps you improve your fishing experience and enjoy fishing trips more than ever.

### 🐟Log Your Catch and Identify Fish
Log your catch and identify fish with our AI-powered fish recognition feature.

### ☁️Weather and Map Integration
Get the latest weather forecast and save your favorite fishing spots with map integration based on your location.

## Screenshots

<table>
  <tbody>
    <tr>
      <td><img src="assets/screenshot/1.png?raw=true"/></td>
      <td><img src="assets/screenshot/2.png?raw=true"/></td>
      <td><img src="assets/screenshot/3.png?raw=true"/></td>
    </tr>
    <tr>
      <td><img src="assets/screenshot/4.png?raw=true"/></td>
      <td><img src="assets/screenshot/5.png?raw=true"/></td>
    </tr>
  </tbody>
</table>

## Resources

- Design System: [Figma](https://www.figma.com/design/VngcbkBgQl61ig4quofS8x/Fishlog-Design-System?node-id=305%3A24951&t=CzBpYSH4aaKEsglC-1)
- Icons: [Material Symbols](https://fonts.google.com/icons)

## Installation

To install Fishlog, there are two options available:

### Option 1: Download from Play Store

Visit the [Fishlog Play Store page](https://play.google.com/store/apps/details?id=com.harissabil.fisch).

### Option 2: Build from Source

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
4. Add the `google-services.json` file to the `app` folder. You can download the file from the Firebase console.
5. Sync the project with Gradle and run the app on an Android emulator or a physical Android device.