package io.github.nelsoncrosby.gprg

import groovy.util.logging.Log
import io.github.nelsoncrosby.gprg.entity.Camera
import io.github.nelsoncrosby.gprg.entity.Entity
import io.github.nelsoncrosby.gprg.entity.Player
import io.github.nelsoncrosby.gprg.track.Track
import io.github.nelsoncrosby.utils.StreamUtils
import io.github.nelsoncrosby.utils.Sys
import org.newdawn.slick.*
import org.newdawn.slick.geom.Vector2f

/**
 * Root class for handling game creation and updating.
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 * @author Riley Steyn (github/RSteyn)
 */
@Log
class GPRGame extends BasicGame {
    /** Provides a slightly nicer input binding system */
    BoundInput input
    /** Camera object controlling the screen */
    Camera camera
    /** Currently active track */
    Track track
    /** Active entities */
    List<Entity> entities

    /**
     * Construct the game and start it.
     * The AppGameContainer stuff is kinda weird, it should be done here.
     *
     * @param w Width for the screen
     * @param h Height for the screen
     *
     * @author Nelson Crosby
     */
    GPRGame(int w, int h) throws SlickException {
        this()

        log.fine 'Creating AppGameContainer'
        AppGameContainer appgc = new AppGameContainer(this)
        appgc.setDisplayMode(w, h, false)
        log.info 'Starting game'
        appgc.start()
    }

    /**
     * Construct the game.
     *
     * @author Nelson Crosby
     */
    GPRGame() {
        super(CONST.TITLE)
    }

    /**
     * Initialize any resources needed
     *
     * @param gc GameContainer context
     * @throws SlickException
     *
     * @author Nelson Crosby
     * @author Riley Steyn
     */
    @Override
    void init(GameContainer gc) throws SlickException {
        log.fine 'gc settings'
        gc.showFPS = true
        log.fine 'Constructing resources'
        track = new Track('standard-oval')
        camera = new Camera(gc)

        entities = [nextPlayer]

        log.finer 'Constructing input'
        Map<String, Closure> pollBindings = [
                'camUp'   : { int delta -> camera.move(Direction.UP, delta) },
                'camDown' : { int delta -> camera.move(Direction.DOWN, delta) },
                'camLeft' : { int delta -> camera.move(Direction.LEFT, delta) },
                'camRight': { int delta -> camera.move(Direction.RIGHT, delta) }
        ]
        Map<String, Closure> eventBindings = [
                'quit'      : { log.info 'Quit on EVENT:quit'; gc.exit() },
                'moveUp'    : { currentPlayer.move(Direction.UP) },
                'moveDown'  : { currentPlayer.move(Direction.DOWN) },
                'moveLeft'  : { currentPlayer.move(Direction.LEFT) },
                'moveRight' : { currentPlayer.move(Direction.RIGHT) }
        ]
        input = new BoundInput(gc.input, pollBindings, eventBindings)

        log.info 'Game started'
    }

    /**
     * Get the current player
     * Current player is the last in <code>this.entities</code>
     *
     * @return Currently active player
     *
     * @author Nelson Crosby
     */
    Player getCurrentPlayer() {
        return entities.reverse().find { it instanceof Player } as Player
    }

    Player getNextPlayer() {
        Vector2f starts = track.startLocations.poll()
        return starts == null ? null :
                Player.getNext(starts.x as int, starts.y as int)
    }

    /**
     * Contents of update loop
     *
     * @param gc GameContainer context
     * @param delta Milliseconds since last called
     * @throws SlickException
     *
     * @author Nelson Crosby
     */
    @Override
    void update(GameContainer gc, int delta) throws SlickException {
        input.test(delta)
        entities.each { it.update(delta, track) }
    }

    /**
     * Render code
     * Called about once every update
     *
     * @param gc GameContainer context
     * @param gx Graphics context to draw to
     * @throws SlickException
     *
     * @author Riley Steyn
     */
    @Override
    void render(GameContainer gc, Graphics gx) throws SlickException {
        track.render(gx, camera)
        entities.each { it.render(gx, camera) }
    }

    /**
     *
     */
    @Override
    void keyPressed(int key, char c) {
        input.keyPressed(key, c)
    }

    /**
     * Called when the system requests for the window to close.
     *
     * @return <code>true</code> when the window should be closed,
     *         <code>false</code> otherwise.
     *
     * @author Nelson Crosby
     */
    @Override
    boolean closeRequested() {
        log.info 'Quit on system request'
        return true
    }


    static final String APP_NAME = 'gprg'

    /**
     * Entry point.
     *
     * @param args Command-line arguments
     *
     * @author Nelson Crosby
     */
    static void main(String[] args) {
        try {
            startGame()
        } catch (UnsatisfiedLinkError ignored) {
            // Natives aren't manually provided
            extractNatives()
            startGame()
        }
    }

    /**
     * Begins the game
     *
     * The old {@link #main}, this makes it easier to handle providing of LWJGL
     *
     * @author Nelson Crosby
     */
    private static void startGame() {
        // Stops your system yelling if game controllers aren't found
        log.fine 'Disabling controllers'
        Input.disableControllers()

        log.fine 'Constructing GPRGame'
        new GPRGame(960, 720)
    }

    /**
     * Extracts natives provided with the jar according to natives-PLATFORM.list
     *
     * Cleans the code out of {@link #main}
     *
     * @author Nelson Crosby
     */
    private static void extractNatives() {
        log.info "Automatically providing natives"
        String nativesListFName = "natives-${Sys.SYSTEM.name()}.list"
        // Get the filename of the natives list
        File nativesListFile = Sys.getPrivateFile(APP_NAME, "natives/$nativesListFName")
        if (!nativesListFile.exists()) {
            log.info 'Natives not extracted yet - extracting...'
            // Ensure the containing directory exists
            nativesListFile.parentFile.mkdirs()
            // Get the relevant list from jar resources
            def nativesList = StreamUtils.readWholeStream(
                    GPRGame.getResourceAsStream("/$nativesListFName")
            ).toString()
            // Write to extracted file
            StreamUtils.writeToFile(nativesList, nativesListFile)

            nativesList.split('\n').each { nativeName ->
                // Extract this native
                log.fine "Extracting $nativeName..."
                StreamUtils.copyStreams(
                        GPRGame.getResourceAsStream("/$nativeName"),
                        new FileOutputStream(Sys.getPrivateFile(APP_NAME, "natives/$nativeName"))
                )
            }
        } else {/* File exists, so natives must already be extracted */}

        // Set the natives directory
        System.properties['java.library.path'] = Sys.getPrivateFile(APP_NAME, 'natives')
    }
}
