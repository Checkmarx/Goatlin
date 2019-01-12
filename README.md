Goatlin
===========

**Goatlin** is a deliberately insecure Mobile application developed by
Checkmarx Security Research Team to support [Kotlin Secure Coding Practices][1].

This repository contains both the Android mobile application
(`packages/clients/android`) and the back-end API server
(`packages/services/api`).

## Requirements

To play with the mobile application you should have the following tools:

* [Android Studio][2] to run the mobile application on a Virtual Device;
* [Docker][3] and [Docker Compose][4] to build and run the back-end API server.

## How to start

1. Run the following command on repository's root directory to build and run the
   back-end API server
   ```
   docker-compose up -d
   ```
2. Start Android Studio and load the project at `packages/clients/android`
   2.1 Choose "Run" > "Run 'app'" (Shift + F10)
   2.2 Select one of the available Virtual Devices

[1]: https://github.com/Checkmarx/Kotlin-SCP
[2]: https://developer.android.com/studio/
[3]: https://www.docker.com/
[4]: https://docs.docker.com/compose/install/
