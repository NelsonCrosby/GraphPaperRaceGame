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
- Building should be simple. Provided your IDE supports dependencies via
   Gradle and Groovy/Java compiling, you should be able to use that. Don't
   write code that might compromise this.


### Directory structure ###

It's gonna be this way, because we might eventually go to Maven or something
 which enforces this.

- `src/main/groovy`: all Groovy sources
- `src/main/java`: all Java sources
- `src/main/resources`: any extras that end up in the jar
- `src/test/groovy`: all tests written in Groovy
- `src/test/java`: you can guess
- `src/test/resources`: extras that end up in the jar, only visible to test 
   sources.


## IDEA Project setup ###

1. File -> Project Structure...
2. (on left) Modules -> (tab in right pane) Sources
3. Notice 'Mark As:' above directory tree
    - If any of the following directories are missing, you may need to come
       back here later.
4. Mark `src/main/groovy` as Sources Root
5. Mark `src/main/java` as Sources Root
6. Mark `src/main/resources` as Resources Root
7. Mark `src/test/groovy` as Test Sources Root
8. Mark `src/test/java` as Test Sources Root
9. Mark `src/test/resources` as Test Resources Root
10. (on left) Libraries -> (middle section) Add (the plus icon)
     - `lib/lwjgl.jar` and `lib/lwjgl_util.jar`
11. Add `lib/slick.jar`
12. Apply/Close
13. Open `src/main/groovy/io.github.nelsoncrosby.gridrace/GridRace.groovy`
14. Right-Click -> Create GridRace.main()...
15. VM Options: `-Djava.library.path=lib/natives/$your_os`
     - Where `$your_os` is one of `windows`, `macosx`, or `linux`
16. Apply/Close
17. Run, and hope for the best.
