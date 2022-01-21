# Dota Heroes educational app

This app is based on video course "Modularizing Android Apps" at https://codingwithmitch.com/courses/modularizing-android-apps/.

Its main focus has been on developing a multi-module app, with core structures independent of Android SDK,
that is why the following technologies were used:

- Networking is performed with Ktor instead of Retrofit
- Caching is performed with SqlDelight instead of Room

The UI, including the navigation system, is build entirely with Jetpack Compose.

## Architecture

![Clean Architecture](https://github.com/aasmc/DotaHeroes/blob/master/art/architecture.png?raw=true)
