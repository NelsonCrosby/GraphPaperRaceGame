# Graph Paper Race Game #
[![Build Status](https://travis-ci.org/NelsonCrosby/GraphPaperRaceGame.svg?branch=master)](https://travis-ci.org/NelsonCrosby/GraphPaperRaceGame)

GPRG is based off of a game traditionally played on grid paper. A track is
 drawn on a grid. A starting line is determined, and a number of 'vehicles' are
 drawn at this line. Vehicles move in turn. Each turn, a vehicle may accelerate
 or decelerate by -1, 0 or 1 M/s ([Manhattan-units] per second), on each axis
 independently. The goal is to be the first to the finish line (which may also
 be the starting line in a circuit).

[Manhattan-units]: http://en.wikipedia.org/wiki/Taxicab_geometry


## Project ##

This game will initially be implemented in Groovy using [Slick2D] 1.0.1.
 Official IRC is [EsperNet]#gprg (because why not).

[Slick2D]: http://slick.ninjacave.com/
[EsperNet]: http://webchat.esper.net/


### Ground rules ###

- Keep all lines to a maximum of 80 characters.
    - The hard limit for XML is 120 characters.
    - The hard limit for Java source is 120 characters, but try to keep it down
        to 80 where possible.
    - Generated files have no limit, as that can sometimes get too tricky to
        manage.
- It should always be possible to create a standalone distribution JAR that
    doesn't require any extra configuration on the part of the user.
- Gradle must be used for resolving dependencies (including natives).
- Doc-comment (`/** */`) everything.
    - Add an `@author` to any method or class you have contributed to.
        - Do not add an `@author` to any abstract or empty methods.


### Directory structure ###

It's gonna be this way, because Gradle prefers it like this. Also, it makes
 a fair bit of sense (everything has its place).

- `src/main/groovy`: all Groovy sources
- `src/main/java`: all Java sources
- `src/main/resources`: any extras that end up in the jar
- `src/test/groovy`: all tests written in Groovy
- `src/test/java`: you can guess
- `src/test/resources`: extras that end up in the jar, only visible to test
    sources.
- `build/`: Anything that Gradle has created
- `bulid/docs/groovydoc`: OF NOTE - HTML form of documentation ends up here
    (after running `gradle groovydoc`).


### IDE setup ###

- Import the project from `build.gradle`.
- Set the following in your run configurations:
    - Main class: `io.github.nelsoncrosby.gprg.GPRGame`
- **Optional:** Use custom natives
    - Run the Gradle task to get natives for your platform
        - Windows: `gradle windowsNatives`
        - Mac: `gradle osxNatives`
        - Linux: `gradle linuxNatives`
    - In your run configurations, set the VM option
            `-Dorg.lwjgl.librarypath=lib/natives-<platform>`
- **Optional:** Use custom input bindings
    - Create a directory `junk` in the project root (it's already in gitignore)
    - Create a file `junk/input.properties`
    - Check `src/main/resources/io/github/nelsoncrosby/gprg/input.properties`
            for a list of bindings you can use
    - In your run configurations, set the VM option
            `-Dio.github.nelsoncrosby.gprg.inputBindings=junk/input.properties`


### Creating a distribution ###

- Run `gradle fatJar`
- The distribution jar can be found in `build/libs`
- Natives will be automatically extracted upon running the jar


### Finding something to help with ###

If you want to contribute, check the [issue tracker]. Use the fork/pull-request
 contribution method.


[Issue tracker]: https://github.com/NelsonCrosby/GraphPaperRaceGame/issues
