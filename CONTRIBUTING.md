How To Contribute
=================

You can contribute in many possible ways, either opening/commenting issues or
fixing bugs/adding new features.

If you're planning to contribute with source code, please consider the workflow
below:

1. Fork the project on GitHub
2. Clone your own Fork
3. Set up the project locally
4. Create an upstream remote and sync your local copy
5. Create a branch based on `develop` to add your changes
6. Push your working branch to your fork
7. Create a new pull request from your branch into the upstream `develop` branch
8. Look out for any code feedback and respond accordingly

## Environment setup

This repository contains multiple projects using different technologies.

The Android mobile application is written using [Kotlin][1]. To work on it
you're better going with [Android Studio][2].

The Android mobile application depends on an HTTP API. It was written in
JavaScript, running on a [Node.js][3] environment. Since it is aimed to run on a
docker container you should have [docker][4] installed in your system.

[1]: https://kotlinlang.org/
[2]: https://developer.android.com/studio
[3]: https://nodejs.org/en/
[4]: https://www.docker.com/
