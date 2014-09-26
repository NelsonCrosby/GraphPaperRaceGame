package io.github.nelsoncrosby.gprg.entity

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
     * Move the player by one square in the given direction up or down.
     *
     * @param direction The direction to move.
     *        [<code>CONST.LEFT</code>|<code>CONST.RIGHT</code>]
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
     * @param direction The direction to move.
     *        [<code>CONST.UP</code>|<code>CONST.DOWN</code>]
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
        float x1 = screenPos.x - 5
        float x2 = screenPos.x + 5
        float y1 = screenPos.y - 5
        float y2 = screenPos.y + 5
        if (onTrack) {
            // Draw a cross
            gx.drawLine(x1, y1, x2, y2)         // Draw \ line
            gx.drawLine(x2, y1, x1, y2)         // Swap x to draw / line
        } else {
            // Crashed, draw a rectangle
            gx.drawRect(x1, y1, 10, 10)
        }
    }


    /** List of colours to cycle through when getting a new player */
    private static List<Color> colorsCycle = [
            Color.blue, Color.red, Color.green
    ]
    /** Colour cycle counter */
    private static int currentColor = 0
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
    static Player getNext(int gridSize = 1, int startX = 0, int startY = 0) {
        Player player = new Player(gridSize, startX, startY)

        player.color = colorsCycle[currentColor]
        currentColor++
        if (currentColor >= colorsCycle.size())
            currentColor = 0

        return player
    }
}
