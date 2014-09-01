package io.github.nelsoncrosby.gprg

import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.*

/**
 *
 */
class GPRGame extends BasicGame {
    Track track
    GPRGame() {
        super(CONST.TITLE)
    }

    @Override
    void init(GameContainer gc) throws SlickException {
        gc.showFPS = false
        track = new Track(Track.getResourceAsStream('test1.track'))
    }

    @Override
    void update(GameContainer gc, int delta) throws SlickException {

    }

    @Override
    void render(GameContainer gc, Graphics gx) throws SlickException {
        track.render(gx)
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
        appgc.setDisplayMode(640, 480, false)
        appgc.start()
    }
}
