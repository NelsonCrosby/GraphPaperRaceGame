package io.github.nelsoncrosby.gprg

import groovy.util.logging.Log
import io.github.nelsoncrosby.gprg.entity.Camera
import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.*

/**
 * Root class for handling game creation and updating.
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 * @author Riley Steyn (github/RSteyn)
 */
@Log
class GPRGame extends BasicGame {
    private Closure exit

    BoundInput input
    Camera camera
    Track track

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
        exit = gc.&exit

        log.fine 'gc settings'
        gc.showFPS = true
        log.fine 'Constructing resources'
        track = new Track(Track.getResourceAsStream('test1.track'))
        camera = new Camera(gc)

        log.finer 'Constructing input'
        input = new BoundInput(new Input(gc.height), [
                'cameraUp':    { int delta -> camera.moveY(CONST.UP,    delta) },
                'cameraDown':  { int delta -> camera.moveY(CONST.DOWN,  delta) },
                'cameraLeft':  { int delta -> camera.moveX(CONST.LEFT,  delta) },
                'cameraRight': { int delta -> camera.moveX(CONST.RIGHT, delta) }
        ])

        log.info 'Game started'
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
    }

    /**
     * Handle event-based keyboard input
     *
     * @param key The key-code that has been pressed
     * @param c Character that the key represents
     *
     * @author Nelson Crosby
     */
    @Override
    void keyPressed(int key, char c) {
        if (key == Input.KEY_END) {
            log.info 'Quit on KEY_END'
            exit()
        }
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
/**
     * Entry point.
     *
     * @param args Command-line arguments
     *
     * @author Nelson Crosby
     */
    static void main(String[] args) {
        // Stops your system yelling if game controllers aren't found
        log.fine 'Disabling controllers'
        Input.disableControllers()

        log.fine 'Constructing GPRGame'
        new GPRGame(960, 720)
    }
}
