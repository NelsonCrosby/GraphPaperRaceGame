package io.github.nelsoncrosby.gprg.entity

import io.github.nelsoncrosby.gprg.Direction
import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.Color
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f

import static io.github.nelsoncrosby.gprg.Direction.Axis

/**
 * A "car" on the track
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 */
class Player extends Entity {
    /** The SIZE of the player when drawn */
    static final int SIZE = 6
    /** The current draw colour of the player */
    Color color
    /** Determines how the player is drawn, and whether or not it can move */
    boolean onTrack = true
    /** Holds acceleration of player for each turn */
    Vector2f accel = new Vector2f(0, 0)

    /**
     * Construct to a position on the grid
     *
     * @param gridSize Size (in pixels) of each cell on the screen
     * @param startX Starting gridX position
     * @param startY Starting gridY position
     *
     * @author Nelson Crosby
     */
    Player(int gridSize, int startX, int startY) {
        super(gridSize, startX, startY)
        // TODO: Get rid of this, implement acceleration
        vel = vel.add(new Vector2f(1, 0))
    }

    /**
     * Perform changes to the entity
     *
     * @param delta Milliseconds since last called
     * @param track The currently active track
     *
     * @author Nelson Crosby
     */
    @Override
    void update(int delta, Track track) {}

    /**
     * Update the player to next turn time step
     * @param track The currently active track
     */
    void performTurn(Track track) {
        vel.add(accel)
        accel = new Vector2f(0, 0)
        this.move(vel)
        onTrack = track.isOnTrack(this)
    }

    /**
     * Move the player by current velocity
     *
     * @author Nelson Crosby
     */
    void move(Vector2f v) {
        pos = pos.add(v)
    }

    void accelerate(Direction dir) {
        if (dir.axis == Axis.Y) {
            accel.y += dir.multiplier
            if (accel.y > 1) accel.y = 1
            else if (accel.y < -1) accel.y = -1
        } else if (dir.axis == Axis.X) {
            accel.x = dir.multiplier
            if (accel.x > 1) accel.x = 1
            else if (accel.x < -1) accel.x = -1
        }
    }

    /**
     * Render entity to the screen
     *
     * @param gx Graphics context to draw to
     * @param screenPos Actual position of the entity on the screen
     *
     * @author Nelson Crosby
     */
    @Override
    void render(Graphics gx, Vector2f screenPos) {
        gx.color = color
        float x1 = screenPos.x - SIZE
        float x2 = screenPos.x + SIZE
        float y1 = screenPos.y - SIZE
        float y2 = screenPos.y + SIZE
        if (onTrack) {
            // Draw player as circle
            gx.drawOval(x1, y1, SIZE*2, SIZE*2)
        } else {
            // Crashed, draw a cross
            gx.drawLine(x1, y1, x2, y2)  // Draw \ line
            gx.drawLine(x2, y1, x1, y2)  // Swap x to draw / line
        }
    }


    /** Queue of colours to cycle through when getting a new player */
    private static Queue<Color> colorsCycle = new LinkedList<>([
            Color.blue, Color.red, Color.green
    ])
    /**
     * Get a new player at the given position
     * Takes care of choosing a colour as well.
     *
     * @param gridSize Size (in pixels) of each cell on the screen
     * @param startX Starting gridX position
     * @param startY Starting gridY position
     * @return The constructed <code>Player</code> object
     *
     * @author Nelson Crosby
     */
    static Player getNext(int startX = 0, int startY = 0) {
        Player player = new Player(Track.CELL_WIDTH, startX, startY)

        // Cycle colours
        player.color = colorsCycle.poll()
        colorsCycle.offer(player.color)

        return player
    }
}
