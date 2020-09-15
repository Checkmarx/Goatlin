Goatlin
=======

**Goatlin** (aka _Kotlin Goat_) is a deliberately insecure mobile application
developed by Checkmarx Security Research Team to support [Kotlin Secure Coding
Practices guide][1].

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
   docker-compose up -d --force-recreate --build
   ```
2. Start Android Studio and load the project at `packages/clients/android`
   * Choose "Run" > "Run 'app'" (Shift + F10)
   * Select one of the available Virtual Devices

**Note** every time you switch to a feature branch (e.g.
`feature/m3-insecure-communication`) you'll need to rebuild the API server
docker image: remember to include the `--force-recreate --build` options.

## License

**Goatlin** - _a deliberately insecure mobile application for educational
purposes_

Copyright (C) 2019  Checkmarx

This program is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program. If not, see https://www.gnu.org/licenses/.

[1]: https://github.com/Checkmarx/Kotlin-SCP
[2]: https://developer.android.com/studio/
[3]: https://www.docker.com/
[4]: https://docs.docker.com/compose/install/
