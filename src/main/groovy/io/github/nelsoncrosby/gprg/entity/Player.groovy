package io.github.nelsoncrosby.gprg.entity

import io.github.nelsoncrosby.gprg.Direction
import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.Color
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f

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
    boolean onTrack

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
    void update(int delta, Track track) {
        onTrack = track.isOnTrack(this)
    }

    /**
     * Move the player one square in the given direction
     *
     * Calls relevant {@link #moveX} or {@link #moveY}
     *
     * @param direction Direction to move
     *
     * @author Nelson Crosby
     */
    void move(Direction direction) {
        if (direction.axis == Direction.Axis.X)
            moveX(direction.multiplier)
        else
            moveY(direction.multiplier)
    }

    /**
     * Move the player by one square in the given direction left or right.
     *
     * @param direction The direction to move along X
     *
     * @author Nelson Crosby
     */
    void moveX(int direction) {
        if (onTrack)
            gridX += direction
    }

    /**
     * Move the player by one square in the given direction up or down.
     *
     * @param direction The direction to move along Y
     *
     * @author Nelson Crosby
     */
    void moveY(int direction) {
        if (onTrack)
            gridY -= direction
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
