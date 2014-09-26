package io.github.nelsoncrosby.gprg.entity

import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.Color
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f

/**
 * A "car" on the track
 */
class Player extends Entity {
    Color color
    boolean onTrack

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

    void moveX(int direction) {
        if (onTrack)
            gridX += direction
    }

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


    private static List<Color> colorsCycle = [
            Color.blue, Color.red, Color.green
    ]
    private static int currentColor = 0
    static Player getNext(int gridSize = 1, int startX = 0, int startY = 0) {
        Player player = new Player(gridSize, startX, startY)

        if (currentColor >= colorsCycle.size())
            currentColor = 0
        player.color = colorsCycle[currentColor]
        currentColor++

        return player
    }
}
