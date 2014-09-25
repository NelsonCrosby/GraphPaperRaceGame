package io.github.nelsoncrosby.gprg.entity

import org.newdawn.slick.Color
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f

/**
 * A "car" on the track
 */
class Player extends Entity {
    Color color

    Player(int gridSize, int startX, int startY) {
        super(gridSize, startX, startY)
    }

    /**
     * Perform changes to the entity
     *
     * @param delta Milliseconds since last called
     */
    @Override
    void update(int delta) {

    }

    void moveX(int direction) {
        gridX += direction
    }

    void moveY(int direction) {
        gridY -= direction
    }

    /**
     * Render entity to the screen
     *
     * @param gx Graphics context to draw to
     * @param screenPos Actual position of the entity on the screen
     */
    @Override
    void render(Graphics gx, Vector2f screenPos) {
        gx.color = color
        float x1 = screenPos.x - 5
        float x2 = screenPos.x + 5
        float y1 = screenPos.y - 5
        float y2 = screenPos.y + 5
        // Draw \ line
        gx.drawLine(x1, y1, x2, y2)
        // Swap x to draw / line
        gx.drawLine(x2, y1, x1, y2)
    }
}
