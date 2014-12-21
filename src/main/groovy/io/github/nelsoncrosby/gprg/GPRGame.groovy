package io.github.nelsoncrosby.gprg

import groovy.util.logging.Log
import io.github.nelsoncrosby.gprg.states.GameState
import io.github.nelsoncrosby.utils.StreamUtils
import io.github.nelsoncrosby.utils.Sys
import org.newdawn.slick.*
import org.newdawn.slick.state.StateBasedGame

/**
 * Root class for handling game creation and queueing of states
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 * @author Riley Steyn (github/RSteyn)
 */
@Log
class GPRGame extends StateBasedGame {
    private GameState trackState

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
     * Initialise the list of states making up this game
     *
     * @param gc The container holding the game
     * @throws SlickException Indicates a failure to initialise the state based game resources
     */
    @Override
    void initStatesList(GameContainer gc) throws SlickException {
        this.trackState = new GameState()
        addState(trackState)
    }

    // Misc ? (needs better classification)
    /**
     * Called when the system requests for the window to close.
     *
     * @return {@code true} when the window should be closed, {@code false}
     *         otherwise.
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
        String libPathProp = 'org.lwjgl.librarypath',
                libPath = System.getProperty(libPathProp)

        if (libPath == null) {
            // Natives aren't manually provided
            extractNatives()
        } else {
            // Natives manually defined, but we might need to make sure they
            // point to an absolute path
            System.setProperty(libPathProp, new File(libPath).absolutePath)
        }
        if (Sys.SYSTEM == Sys.MAC) {
            // Natives might be broken, try to fix
            fixOsxNatives(new File(System.getProperty(libPathProp)))
        }

        startGame()
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
        System.setProperty('org.lwjgl.librarypath', Sys.getPrivateFile(APP_NAME, 'natives').absolutePath)
    }

    /**
     * Hack to get LWJGL natives working correctly on Mac
     *
     * @param nativesDir The directory that the natives exist in
     */
    private static void fixOsxNatives(File nativesDir) {
        File oldLibLwjgl = new File(nativesDir, "liblwjgl.jnilib"),
                newLibLwjgl = new File(nativesDir, "liblwjgl.dylib")
        if (!newLibLwjgl.exists() && oldLibLwjgl.exists()) {
            oldLibLwjgl.renameTo(newLibLwjgl)
        } else {/* Shouldn't ever happen if natives configured correctly */}
    }
}
