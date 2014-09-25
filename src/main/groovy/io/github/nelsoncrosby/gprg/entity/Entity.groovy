package io.github.nelsoncrosby.gprg.entity

import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f

/**
 * Something that can be displayed on the screen
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 */
abstract class Entity {
    Vector2f pos
    int gridSize

    /**
     * @param gridSize Size (in pixels) of each cell on the screen
     * @param pos Starting position for the entity
     *
     * @author Nelson Crosby
     */
    Entity(int gridSize = 1, Vector2f pos = new Vector2f(0f, 0f)) {
        this.gridSize = gridSize
        this.pos = pos
    }

    /**
     * X position on the grid
     * Getter for <code>gridX</code>
     *
     * @return <code>gridX</code>
     *
     * @author Nelson Crosby
     */
    int getGridX() {
        return pos.x / gridSize
    }

    /**
     * Y position on the grid
     * Getter for <code>gridY</code>
     *
     * @return <code>gridY</code>
     *
     * @author Nelson Crosby
     */
    int getGridY() {
        return pos.y / gridSize
    }

    /**
     * X position on the grid
     * Setter for <code>gridX</code>
     *
     * @param newX New <code>gridX</code>
     *
     * @author Nelson Crosby
     */
    void setGridX(int newX) {
        pos.x = newX * gridSize
    }

    /**
     * Y position on the grid
     * Setter for <code>gridY</code>
     *
     * @param newY New <code>gridY</code>
     *
     * @author Nelson Crosby
     */
    void setGridY(int newY) {
        pos.y = newY * gridSize
    }

    /**
     * Perform changes to the entity
     *
     * @param delta Milliseconds since last called
     */
    abstract void update(int delta)

    /**
     * Render entity to the screen
     * Calls the abstract method, giving actual screen co-ordinates
     *
     * @param gx Graphics context to draw to
     * @param actualPos Camera object to determine actual position
     *
     * @author Nelson Crosby
     */
    void render(Graphics gx, Camera camera) {
        render(gx, camera.getScreenPos(pos))
    }

    /**
     * Render entity to the screen
     *
     * @param gx Graphics context to draw to
     * @param screenPos Actual position of the entity on the screen
     */
    protected abstract void render(Graphics gx, Vector2f screenPos)
}
