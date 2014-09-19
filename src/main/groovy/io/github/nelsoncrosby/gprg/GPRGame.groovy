package io.github.nelsoncrosby.gprg

import io.github.nelsoncrosby.gprg.entity.Camera
import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.*

/**
 * Root class for handling game creation and updating.
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 * @author Riley Steyn (github/RSteyn)
 */
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

        AppGameContainer appgc = new AppGameContainer(this)
        appgc.setDisplayMode(w, h, false)
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

        gc.showFPS = true
        track = new Track(Track.getResourceAsStream('test1.track'))
        camera = new Camera(gc)

        input = new BoundInput(new Input(gc.height), [
                'cameraUp':    { int delta -> camera.moveY(CONST.UP,    delta) },
                'cameraDown':  { int delta -> camera.moveY(CONST.DOWN,  delta) },
                'cameraLeft':  { int delta -> camera.moveX(CONST.LEFT,  delta) },
                'cameraRight': { int delta -> camera.moveX(CONST.RIGHT, delta) }
        ])
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
        if (key == Input.KEY_END) exit()
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
        Input.disableControllers()

        new GPRGame(960, 720)
    }
}
