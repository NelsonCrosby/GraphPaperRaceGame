package io.github.nelsoncrosby.gridrace

import org.newdawn.slick.*

/**
 *
 */
class GridRace extends BasicGame {
    static final int LEFT_BUTTON = 0
    Color color
    Random rnd
    int rectX = 100
    int rectY = 100
    int rectSpeed = 5
    Input input

    GridRace() {
        super((String) CONST['title'])
    }

    @Override
    void init(GameContainer gc) throws SlickException {
        println 'init'
        input = new Input(gc.height)
        color = new Color(255, 255, 255)
        rnd = new Random()
    }

    @Override
    void update(GameContainer gc, int delta) throws SlickException {
        if (input.isKeyDown(Input.KEY_W)) {
            rectY -= rectSpeed
        }
        if (input.isKeyDown(Input.KEY_S)) {
            rectY += rectSpeed
        }
        if (input.isKeyDown(Input.KEY_A)) {
            rectX -= rectSpeed
        }
        if (input.isKeyDown(Input.KEY_D)) {
            rectX += rectSpeed
        }
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
        gx.fillRect(rectX, rectY, 100, 100)
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
