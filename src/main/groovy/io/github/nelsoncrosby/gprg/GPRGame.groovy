package io.github.nelsoncrosby.gprg

import groovy.util.logging.Log
import io.github.nelsoncrosby.gprg.entity.Camera
import io.github.nelsoncrosby.gprg.entity.Entity
import io.github.nelsoncrosby.gprg.entity.Player
import io.github.nelsoncrosby.gprg.track.Track
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
                'quit'      : { log.info 'Quit on EVENT:quit'; gc.exit()  },
                'accelUp'   : { currentPlayer.accelerate(Direction.UP)    },
                'accelDown' : { currentPlayer.accelerate(Direction.DOWN)  },
                'accelLeft' : { currentPlayer.accelerate(Direction.LEFT)  },
                'accelRight': { currentPlayer.accelerate(Direction.RIGHT) },
                'nextTurn'  : { currentPlayer.performTurn(track) }
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
        gx.drawString(currentPlayer.pos as String, 20, 20)
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
