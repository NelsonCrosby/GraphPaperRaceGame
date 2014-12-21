package io.github.nelsoncrosby.gprg.states

import groovy.util.logging.Log
import io.github.nelsoncrosby.gprg.BoundInput
import io.github.nelsoncrosby.gprg.Direction
import io.github.nelsoncrosby.gprg.entity.Camera
import io.github.nelsoncrosby.gprg.entity.Entity
import io.github.nelsoncrosby.gprg.entity.Player
import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import org.newdawn.slick.SlickException
import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame

/**
 * The state representing the actual game
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 * @author Riley Steyn (github/RSteyn)
 */
@Log
class GameState extends BasicGameState {
    /** Provides a slightly nicer input binding system */
    BoundInput input
    /** Camera object controlling the screen */
    Camera camera
    /** Currently active track and track variables */
    Track track
    int trackID
    List<String> tracks = [
            'oval',
            'really-long',
            'test1'
    ]
    /** Active entities */
    List<Entity> entities
    /** Player variables */
    int playerID
    // State-specific variables
    /** Holds whether the state is to be restarted soon */
    boolean isRestarting
    /** Holds time left until restart */
    float remainingSeconds
    /** The time (in ms) to wait for respawn */
    private static final int RESPAWN_WAIT_TIME = 2000
    /** A game-unique ID for this state */
    final UUID stateID = UUID.randomUUID()

    /**
     * An integer ID for this state.
     * Uses the time_low section of the UUID generated for this state instance
     *
     * @return The 32 most-significant bits of the game-unique ID for this state
     */
    @Override
    int getID() {
        // Get the time_low section into the least-significant half of a long
        long time_low = stateID.mostSignificantBits >> 4
        // Return the least-significant half (the time_low section)
        return time_low.intValue()
    }

    /**
     * Initialize any resources needed and start the game.
     *
     * @param gc The {@link GameContainer} context
     * @param game The {@link StateBasedGame} currently running
     * @throws SlickException
     *
     * @author Nelson Crosby
     * @author Riley Steyn
     */
    @Override
    void init(GameContainer gc, StateBasedGame game) throws SlickException {
        log.fine 'gc settings'
        gc.showFPS = true

        log.finer 'Constructing input'
        Map<String, Closure> pollBindings = [
                camUp   : { int delta -> camera.move(Direction.UP, delta) },
                camDown : { int delta -> camera.move(Direction.DOWN, delta) },
                camLeft : { int delta -> camera.move(Direction.LEFT, delta) },
                camRight: { int delta -> camera.move(Direction.RIGHT, delta) }
        ]
        Map<String, Closure> eventBindings = [
                quit        : { log.info 'Quit on EVENT:quit'; gc.exit()  },
                accelUp     : { currentPlayer.accelerate(Direction.UP)    },
                accelDown   : { currentPlayer.accelerate(Direction.DOWN)  },
                accelLeft   : { currentPlayer.accelerate(Direction.LEFT)  },
                accelRight  : { currentPlayer.accelerate(Direction.RIGHT) },
                nextTurn    : { performTurn() },
                nextTrack   : { nextTrack(gc, game) },
                restart     : { game.enterState(getID()) },
                incPlayers  : { entities.add(nextPlayer) },
                decPlayers  : { if (players.size() > 1) entities.remove(players[-1]) }
        ]
        input = new BoundInput(gc.input, pollBindings, eventBindings)
        trackID = 0
    }

    /**
     * We have entered this state (need to start/restart game)
     * @author Nelson Crosby
     */
    @Override
    void enter(GameContainer gc, StateBasedGame game) throws SlickException {
        log.info 'Restarting game'
        log.fine 'Constructing resources'
        track = new Track(tracks.get(trackID), Track)
        camera = new Camera(gc, new Vector2f(45*12, -15*12))
        entities = []
        playerID = 0
        isRestarting = false

        Player.resetColors()
        genPlayers(1)

        log.info 'Game started'
    }

    @Override
    void leave(GameContainer container, StateBasedGame game) throws SlickException {

    }

    /**
     * Render code.
     * Called about once every update (but let's not rely on this).
     *
     * @param gc GameContainer context
     * @param gx Graphics context to draw to
     * @throws SlickException
     *
     * @author Riley Steyn
     */
    @Override
    void render(GameContainer gc, StateBasedGame game, Graphics gx) throws SlickException {
        track.render(gx, camera)
        entities.each { it.render(gx, camera) }
        if (isRestarting) {
            // If the game is to be restarted, draw timer
            gx.color = Color.white
            gx.drawString("Restarting in $remainingSeconds", 300, 20)
        } else {
            // Render current player's movement decisions
            currentPlayer.renderMovement(gx, camera, camera.getScreenPos(
                            camera.getWorldPos(
                                    currentPlayer.pos, currentPlayer.gridSize
                            )
                    )
            )
            currentPlayer.renderPath(gx, camera, camera.getScreenPos(
                    camera.getWorldPos(
                            currentPlayer.pos, currentPlayer.gridSize
                    )
                )
            )
        }
        // Draw the paths of all dead players
        for (Entity entity: entities) {
            if (entity instanceof Player && entity.isCrashed) {
                entity.renderPath(
                        gx, camera,
                        camera.getScreenPos(
                                camera.getWorldPos(entity.pos, entity.gridSize)
                        )
                )
            }
        }
        gx.color = Color.white
        // TODO: Remove this debugging code
        gx.drawString(playerID as String, 20, 20)
        gx.drawString(currentPlayer.pos as String, 20, 40)
        gx.drawString(isRestarting as String, 20, 60)
    }

    /**
     * Contents of update loop.
     *
     * @param gc GameContainer context
     * @param game The {@link StateBasedGame} currently running
     * @param delta Milliseconds since last called
     * @throws SlickException
     *
     * @author Nelson Crosby
     */
    @Override
    void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        input.test(delta)
        if (isRestarting) {
            remainingSeconds -= delta
            if (remainingSeconds <= 0) {
                game.enterState(getID())  // Restart state
            }
        } else {
            int restartCalls = 0  // Number of entities asking for restart
            for (Entity entity : entities) {
                if (entity.update(delta, track) /* Update entity */) {
                    // Entity requests game restart
                    restartCalls += 1
                    if (restartCalls == players.size()) {
                        // All players are asking for restart
                        log.fine 'Restart timer started'
                        isRestarting = true
                        remainingSeconds = RESPAWN_WAIT_TIME
                    }
                }
            }
        }
    }

    /**
     * Performs current player's turn, moves to next player
     *
     * @author Riley Steyn
     */
    void performTurn() {
        currentPlayer.performTurn(track)

        // Kills current player if collision
        for (i in 0..entities.size()-1) {
            Entity entity = entities.get(i)
            if (entity.getClass() == Player) {
                // Only players may collide
                if (entity.pos == currentPlayer.pos && i != playerID) {
                    // The player is at the same position as another,
                    // both have crashed
                    entity.crashPlayer()
                    currentPlayer.crashPlayer()
                }
            }
        }

        // Iterate to next non-crashed player
        // This whole code segment would be nicer with a do-until loop
        int crashCount = 0
        playerID += 1
        playerID %= players.size()
        while (players[playerID].isCrashed && crashCount < players.size()) {
            crashCount += 1
            playerID += 1
            playerID %= players.size()
        }


    }

    // Input methods
    /**
     * Notify {@link #input} that a key has been pressed
     */
    @Override
    void keyPressed(int key, char c) {
        input.keyPressed(key, c)
    }

    /**
     * Get the current player.
     * Current player is the last in {@link #entities}.
     *
     * @return Currently active player
     *
     * @author Nelson Crosby
     */
    Player getCurrentPlayer() {
        return players[playerID] as Player
        // return entities.find { it instanceof Player } as Player
    }

    /**
     * Finds the next starting position and returns an associated player.
     *
     * @return A player in a valid starting position
     *
     * @author Nelson Crosby
     */
    Player getNextPlayer() {
        Vector2f pos = track.startLocations[players.size() % track.startLocations.size()]
        return pos == null ? null /* Can't get a start position, so we don't
                                     know where we can put the player */
                : Player.getNext(players.size(), pos.x as int, pos.y as int)
    }

    List<Player> getPlayers() {
        entities.findAll { it instanceof Player }
    }

    /**
     * Populates entities list with players
     * @param numOfPlayers number of players to be added.
     *
     * @author Riley Steyn
     */
    void genPlayers(int numOfPlayers) {
        log.info 'Generating players'
        for (i in 0..numOfPlayers-1) {
            entities.add(nextPlayer)
        }
    }

    /**
     * Simply iterate trackID to next id, then restart
     * @param gc The {@link GameContainer} context
     * @param game The {@link StateBasedGame} currently running
     * @author Riley Steyn
     */
    private void nextTrack(GameContainer gc, StateBasedGame game) {
        trackID += 1
        if (trackID == tracks.size()) {
            trackID = 0
        }
        game.enterState(getID())
    }
}
