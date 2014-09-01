package io.github.nelsoncrosby.gridrace

import org.newdawn.slick.*

/**
 *
 */
class GridRace extends BasicGame {
    static final int LEFT_BUTTON = 0
    Color color
    Random rnd

    GridRace() {
        super((String) CONST['title'])
    }

    @Override
    void init(GameContainer gc) throws SlickException {
        println 'init'
        color = new Color(255, 255, 255)
        rnd = new Random()
    }

    @Override
    void update(GameContainer gc, int delta) throws SlickException {

    }

    @Override
    void mouseClicked(int button, int x, int y, int clickCount) {
        if (button == LEFT_BUTTON) {
            short red = rnd.nextInt(256)
            short green = rnd.nextInt(256)
            short blue = rnd.nextInt(256)
            color = new Color(red, green, blue)
        }
    }

    @Override
    void render(GameContainer gc, Graphics gx) throws SlickException {
        gx.color = color
        gx.fillRect(100, 100, 100, 100)
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
