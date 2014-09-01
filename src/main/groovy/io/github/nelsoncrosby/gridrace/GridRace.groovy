package io.github.nelsoncrosby.gridrace

import org.newdawn.slick.*

/**
 *
 */
class GridRace extends BasicGame {

    GridRace() {
        super(CONST.TITLE)
    }

    @Override
    void init(GameContainer gc) throws SlickException {

    }

    @Override
    void update(GameContainer gc, int delta) throws SlickException {

    }

    @Override
    void render(GameContainer gc, Graphics gx) throws SlickException {

    }

    /**
     * Entry point.
     * @param args Command-line arguments
     */
    static void main(String[] args) {
        AppGameContainer appgc = new AppGameContainer(new GridRace())
        appgc.setDisplayMode(640, 480, false)
        appgc.start()
    }
}
