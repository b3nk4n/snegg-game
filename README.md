# Snegg ![GitHub](https://img.shields.io/github/license/b3nk4n/snegg-game)

A free Snake clone for Android with hand-drawn graphics by Benjamin Kan (former Benjamin Sautermeister) and Vanessa Kan. The game is powered by [LibGDX](https://libgdx.com/), and this project's main purpose was to learn that cross-platform Java game development framework by example.

<p align="center">
    <img alt="App Logo" src="android/res/drawable-xxxhdpi/ic_launcher.png">
</p>

You can download the game from [Google Play Store](https://play.google.com/store/apps/details?id=de.bsautermeister.snegg).

### Features
- Online leaderboards and achievements

## Technical Setup

Use Java 11 to build and run the project.

## Technical Setup

Use Java 11 to build and run the project.

## IntelliJ

### Desktop run configuration

On MacOS, the VM argument `-XstartOnFirstThread` is required to launch the project on desktop.
Setting this flag is already defined the in the `desktop:run` Gradle task. However, if you simply
run the main method of the `DesktopLaumcher` class, the auto-created IntelliJ run configuration does
not actually use that Gradle task. Instead, simply create this run configuration yourself:

1. Select _Edit configurations..._
2. Add a new _Gradle_ configuration
3. Use `desktop:run` as the command to _Run_
4. Launch the created run configuration

While this might only be strictly necessary for MacOS, it does not harm to do that for any platform,
to ensure the proper Gradle task to run the desktop project is used.

### iOS run configuration

1. Install the **MobiVM** plugin into Android Studio
2. Install Xcode
3. Create a run configuration
    1. Select _Edit configurations..._
    2. Add a new _RoboVM iOS_ configuration
    3. Select the project's _Module_
    4. Select _Simulator_ toggle (which does not need a provisioning profile)
4. Launch the run configuration

## Acknowledgements

Thanks to Scott Holmes for allowing us to use this his song in our non-commercial game. The song is free to use under [FREE Creative Commons License](https://scottholmesmusic.com/licensing/).

## License

This work is published under [MIT][mit] License.

[mit]: https://github.com/b3nk4n/snegg-game/blob/main/LICENSE
