package io.github.nelsoncrosby.gprg.entity

import io.github.nelsoncrosby.gprg.Direction
import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.Color
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Circle
import org.newdawn.slick.geom.Line
import org.newdawn.slick.geom.Vector2f

import static io.github.nelsoncrosby.gprg.Direction.Axis

/**
 * A "car" on the track
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 * @author Riley Steyn (github/RSteyn)
 */
class Player extends Entity {
    /** The SIZE of the player when drawn */
    static final int SIZE = 6
    /** The amount smaller than SIZE of the dots for possible movement */
    static final int DOTSIZE = 5
    /** The current draw colour of the player */
    Color color
    /** Determines how the player is drawn, and whether or not it can move */
    boolean isCrashed = false
    /** Holds whether the player has crossed the finish line this turn */
    boolean crossedFinish
    /** Holds acceleration of player for each turn */
    Vector2f accel = new Vector2f(0, 0)
    /** Holds all prior coordinates */
    List<Vector2f> prevCoordinates

    /**
     * Construct to a position on the grid.
     *
     * @param gridSize Size (in pixels) of each cell on the screen
     * @param startX Starting gridX position
     * @param startY Starting gridY position
     *
     * @author Nelson Crosby
     */
    Player(int gridSize, int startX, int startY) {
        super(gridSize, startX, startY)
        prevCoordinates = []
    }

    /**
     * Perform changes to the entity.
     *
     * @param delta Milliseconds since last called
     * @param track The currently active track
     * @return {@code true} if the game should restart
     *
     * @author Nelson Crosby
     */
    @Override
    boolean update(int delta, Track track) {
        if (isCrashed) /* Crashed, should restart soon */ {
            return true // We want to restart the game
        }
        return false // No restarting
    }
    /** Timer used by {@link #update} to count for resetting (once crashed) */
    private int resetTimer = 0

    /**
     * Update the player to next turn time step.
     *
     * @param track The currently active track
     *
     * @author Riley Steyn
     */
    void performTurn(Track track) {

        if (!isCrashed && !crossedFinish) {
            if (!hasMoved && vel.lengthSquared() > 0) {
                // The player has now moved
                hasMoved = true;
            }
            vel.add(accel)
            accel = new Vector2f(0, 0)

            // Add to previous coordinates
            if (vel.lengthSquared() > 0) {
                prevCoordinates.add(pos.copy())
            }

            Vector2f playerInitialPos = pos.copy()
            move(vel)

            // Check that player is still on track
            if (!isCrashed) {
                // Check if the player has crashed
                isCrashed = !track.isOnTrack(this)
            }
            if (hasMoved && !isCrashed) /* Cannot win if also crashed */ {
                // Check whether the player has crossed the finish line
                Vector2f startLineFirst = track.startLocations.peekFirst()
                Vector2f startLineLast = track.startLocations.peekLast()
                // Holds player displacement from the start line point initially
                Vector2f initialDisp = playerInitialPos.sub(startLineFirst)
                // Holds player displacement from the start line point after moving
                Vector2f finalDisp = pos.copy().sub(startLineFirst)

                crossedFinish = false
                Direction startDir = track.info.startLineDirection
                int multiplier = startDir.multiplier
                if (startDir.axis == Axis.X) {
                    // Start line is vertical
                    if (initialDisp.x * multiplier < 0 &&
                            (startDir == Direction.RIGHT ? finalDisp.x
                                    : finalDisp.x-1) * multiplier >= 0) {
                        // Player has crossed the finish line along X this turn
                        // Check that the player is in the correct Y range
                        if (pos.y >= startLineFirst.y-2 &&
                                pos.y <= startLineLast.y+1) {
                            crossedFinish = true
                        }
                    }
                } else {
                    // Start line is horizontal
                    if (initialDisp.y * multiplier < 0 &&
                            (startDir == Direction.DOWN ? finalDisp.y
                                    : finalDisp.y-1) * multiplier >= 0) {
                        // Player has crossed the finish line along Y this turn
                        // Check that the player is in the correct X range
                        if (pos.x >= startLineFirst.x-2 &&
                                pos.x <= startLineLast.x+1) {
                            crossedFinish = true
                        }
                    }
                }
            }
        }
    }
    private boolean hasMoved = false

    /**
     * Move the player by the given distance.
     *
     * @param distance Distance to move
     *
     * @author Riley Steyn
     */
    void move(Vector2f distance) {
        pos.add(distance)
    }

    /**
     * Change player velocity by 1 g/u (grid-unit per update).
     * Acceleration is capped at 1 g/u
     *
     * @param dir {@link Direction} to accelerate in
     *
     * @author Riley Steyn
     */
    void accelerate(Direction dir) {
        if (dir.axis == Axis.Y) /* Accelerate along Y */ {
            accel.y += dir.multiplier
            // Cap acceleration
            if (accel.y > 1) accel.y = 1
            else if (accel.y < -1) accel.y = -1
        } else if (dir.axis == Axis.X) /* Accelerate along X */ {
            accel.x += dir.multiplier
            // Cap acceleration
            if (accel.x > 1) accel.x = 1
            else if (accel.x < -1) accel.x = -1
        }
    }

    /**
     * Render entity to the screen.
     *
     * @param gx {@link Graphics} context to draw to
     * @param screenPos Actual position of the entity on the screen
     *
     * @author Nelson Crosby
     */
    @Override
    void render(Graphics gx, Vector2f screenPos) {
        gx.color = color
        float x = screenPos.x - SIZE
        float y = screenPos.y - SIZE
        if (!isCrashed) {
            // Draw player as circle
            gx.fillOval(x, y, SIZE*2, SIZE*2)
        } else {
            float x2 = screenPos.x + SIZE
            float y2 = screenPos.y + SIZE
            // Crashed, draw a cross
            gx.drawLine(x, y, x2, y2)  // Draw \ line
            gx.drawLine(x2, y, x, y2)  // Swap x to draw / line
        }
    }

    void renderMovement(Graphics gx, Camera camera) {
        // Get player's next location with no acceleration
        Vector2f screenPos = camera.getScreenPos(pos, gridSize)
        float screenUnit = camera.zoom * gridSize
        int x = screenPos.x - SIZE
        int y = screenPos.y - SIZE

        // Create a ring around the currently active player
        gx.color = new Color(color.r, color.g, color.b, 0.75f)
        gx.drawOval(x-2, y-2, (SIZE+2)*2, (SIZE+2)*2)

        x += vel.x * screenUnit
        y += vel.y * screenUnit

        // Draw all nine possible movement points
        // Use half-transparency for the guide
        for (i in -1..1) {
            for (j in -1..1) {
                gx.drawOval(
                        (float) x + i*screenUnit + DOTSIZE,
                        (float) y + j*screenUnit + DOTSIZE,
                        2*(SIZE-DOTSIZE),2*(SIZE-DOTSIZE)
                )
            }
        }


        // Draw where the player will be if the next move occurs now
        x += accel.x * screenUnit
        y += accel.y * screenUnit

        // Use half-transparency for the guide
        gx.color = new Color(color.r, color.g, color.b, 0.5f)
        gx.fillOval(x, y, SIZE*2, SIZE*2)
    }
    void renderPath(Graphics gx, Camera camera) {
        // Draw previous locations
        gx.color = new Color(color.r, color.g, color.b, 0.3f)

        // Draw line between all prior turn's locations
        for (int i=1; i < prevCoordinates.size(); i++) {
            Vector2f point = prevCoordinates[i]
            Vector2f prevPoint = prevCoordinates[i-1]
            Vector2f sPos = camera.getScreenPos(
                    camera.getWorldPos(point, gridSize)
            )
            gx.drawOval(
                    sPos.x - SIZE as int,
                    sPos.y - SIZE as int,
                    SIZE*2, SIZE*2
            )
            // Draw line between this and the previous point
            Vector2f firstLineCoord = camera.getScreenPos(prevPoint, gridSize)
            Vector2f secondLineCoord = camera.getScreenPos(point, gridSize)
            gx.drawLine(
                    firstLineCoord.x, firstLineCoord.y,
                    secondLineCoord.x, secondLineCoord.y
            )
        }
        if (prevCoordinates.size() > 0) {
            Vector2f firstLineCoord = camera.getScreenPos(
                    prevCoordinates[prevCoordinates.size()-1],
                    gridSize)
            Vector2f secondLineCoord = camera.getScreenPos(pos, gridSize)
            gx.drawLine(
                    firstLineCoord.x, firstLineCoord.y,
                    secondLineCoord.x, secondLineCoord.y
            )
        }
    }

    /**
     * Sets the player state to crashed
     *
     * @author Riley Steyn
     */
    void crashPlayer() {
        isCrashed = true
    }

    /** Queue of colours to cycle through when getting a new player */
    private static List<Color> colorsCycle
    static { resetColors() /* Set the colours initially */ }

    /**
     * Reset the colour cycle.
     *
     * @author Nelson Crosby
     */
    static void resetColors() {
        colorsCycle = new LinkedList<>([
                Color.blue, Color.red, Color.green, Color.orange,
                Color.cyan, Color.yellow
        ])
    }
    /**
     * Get a new player at the given position.
     * Takes care of choosing a colour as well.
     *
     * @param startX Starting gridX position
     * @param startY Starting gridY position
     * @return The constructed {@link Player} object
     *
     * @author Nelson Crosby
     */
    static Player getNext(int currentPlayersCount, int startX = 0, int startY = 0) {
        Player player = new Player(Track.CELL_WIDTH, startX, startY)

        // Cycle colours
        player.color = colorsCycle[currentPlayersCount % colorsCycle.size()]

        return player
    }
}
