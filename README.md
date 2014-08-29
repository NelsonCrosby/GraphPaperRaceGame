# GridRace #
[Trello]

GridRace is based off of a game traditionally played on grid paper. A track is
 drawn on a grid. A starting line is determined, and a number of 'vehicles' are
 drawn at this line. Vehicles move in turn. Each turn, a vehicle may accelerate
 or decelerate by -1, 0 or 1 M/s ([Manhattan-units] per second), on each axis
 independently. The goal is to be the first to the finish line (which may also
 be the starting line in a circuit).

[Trello]: https://trello.com/b/hrNlfIF5/gridrace
[Manhattan-units]: http://en.wikipedia.org/wiki/Taxicab_geometry


## Project ##

This game will initially be implemented in Groovy using [Slick2D] 1.0.1. There
 is an official [Trello] board. Official IRC is [EsperNet]#gridrace (because
 why not).

[Slick2D]: http://slick.ninjacave.com/
[EsperNet]: http://webchat.esper.net/


### Ground rules ###

- Keep all lines to a maximum of 80 characters.
    - The hard limit for XML is 120 characters.
    - The hard limit for Java source is 120 characters, but try to keep it down
        to 80 where possible.
    - Generated files have no limit, as that can sometimes get too tricky to
        manage.
- Building should be manageable by an IDE's built-in system. While dependencies
   and distribution may eventually be handled by Gradle or something (which an
   IDE should be fully able to process anyway), the IDE should be fully capable
   of accurately compiling the project itself. Relying on external tools can
   slow down this process.
- If your commit is related to a Trello card, the second line should be in the
   format `[List/Card]`. This makes for easy reference.


### Directory structure ###

It's gonna be this way, because we might eventually go to Gradle or something
 which enforces this.

- `src/main/groovy`: all Groovy sources
- `src/main/java`: all Java sources
- `src/main/resources`: any extras that end up in the jar
- `src/test/groovy`: all tests written in Groovy
- `src/test/java`: you can guess
- `src/test/resources`: extras that end up in the jar, only visible to test 
   sources.


### IDE setup ###

You should configure the following for your IDE:

- Languages
    - Java language level: 7.0
    - Groovy SDK version: 2.3.6
- Source roots
    - `src/main/groovy`: Source
    - `src/main/java`: Source
    - `src/main/resources`: Resource
    - `src/test/groovy`: Test source
    - `src/test/java`: Test source
    - `src/test/resources`: Test resources
- Libraries
    - LWJGL
        - `lib/lwjgl.jar`
        - `lib/lwjgl_util.jar`
    - Slick
        - `lib/slick.jar`
- Run configurations (for configuring LWJGL)
    - VM Options: `-Djava.library.path=lib/natives/<your_os>`
    - Working directory: `<project_root>`


### Finding something to help with ###

If you want to contribute, best thing to do would be to go to the Trello board,
 and look for an unassigned card. Then do the standard contribution thingy.
