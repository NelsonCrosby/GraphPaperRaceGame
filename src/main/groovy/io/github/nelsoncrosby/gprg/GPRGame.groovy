package io.github.nelsoncrosby.gprg

import io.github.nelsoncrosby.gprg.entity.Camera
import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.*

/**
 *
 */
class GPRGame extends BasicGame {
    BoundInput input
    Camera camera
    Track track
    GPRGame() {
        super(CONST.TITLE)
    }

    @Override
    void init(GameContainer gc) throws SlickException {
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

    @Override
    void update(GameContainer gc, int delta) throws SlickException {
        input.test(delta)
    }

    @Override
    void render(GameContainer gc, Graphics gx) throws SlickException {
        track.render(gx, camera)
    }

    /**
     * Entry point.
     * @param args Command-line arguments
     */
    static void main(String[] args) {
        // Stops your system yelling if game controllers aren't found
        Input.disableControllers()

        // Initialize the game
        // May throw SlickException
        AppGameContainer appgc = new AppGameContainer(new GPRGame())
        appgc.setDisplayMode(960, 720, false)
        appgc.start()
    }
}
