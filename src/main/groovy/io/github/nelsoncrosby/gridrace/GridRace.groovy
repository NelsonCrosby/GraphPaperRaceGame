package io.github.nelsoncrosby.gridrace

import org.newdawn.slick.*

/**
 * Root game loop class
 */
class GridRace extends BasicGame {

    GridRace() {
        super((String) CONST['title'])
    }

    @Override
    void init(GameContainer gameContainer) throws SlickException {
        println 'init'
    }

    @Override
    void update(GameContainer gameContainer, int i) throws SlickException {
        print 'update\r'
    }

    @Override
    void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        print 'render\r'
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
