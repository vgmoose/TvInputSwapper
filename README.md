## TvInputSwapper
An Android TV app that runs in the background, and lets you hold the Mute button on the remote for 2 seconds to cycle through HDMI inputs/sources.

### Purpose
If you're using an Android TV with an external streaming device, such as the [Roku Stick](https://www.roku.com/products/roku-streaming-stick-4k), the remote is able to act as a semi-Universal remote and control the power/volume of the TV. However! It is not able to change HDMI inputs. This makes it less convenient to use other HDMI devices, and requires using the original TV remote to change sources.

This workaround gets around the fact that the actual remote doesn't have a Source button, by re-using the buttons that it does have to tell the Android TV-side to change sources, allowing only one remote (in this case the Roku one)  control both devices.

### Usage
Download the [APK file](https://github.com/vgmoose/TvInputSwapper/releases), install it by [sideloading](https://www.androidpolice.com/how-to-sideload-any-application-on-android-tv/), and then enable it to capture Input events under Android's Accessibility settings.

Then holding mute should cycle between inputs. If it does not work with your remote or TV, please [file an issue](https://github.com/vgmoose/TvInputSwapper/issues) with more information.

### How it Works
This app uses the [TvInputManager](https://developer.android.com/reference/android/media/tv/TvInputManager) API to change between passthrough inputs programmatically.

1. [List out](https://github.com/vgmoose/TvInputSwapper/blob/main/app/src/main/java/llc/newt/inputswapper/AccessibilityService.kt#L46-L57) the possible passthrough inputs
2. [Receive key up/down events](https://github.com/vgmoose/TvInputSwapper/blob/main/app/src/main/java/llc/newt/inputswapper/AccessibilityService.kt#L78-L93) to start and stop a 2 second timer
3. [Launch the android.media.tv intent](https://github.com/vgmoose/TvInputSwapper/blob/main/app/src/main/java/llc/newt/inputswapper/AccessibilityService.kt#L110-L113) with the next HDMI channel