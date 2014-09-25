package io.github.nelsoncrosby.gprg.entity

import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f

/**
 * Something that can be displayed on the screen
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 */
abstract class Entity {
    /** Location on the grid */
    int gridX, gridY
    /** Size (in pixels) of each cell on the screen */
    int gridSize

    /**
     * Construct to a position on the grid
     *
     * @param gridSize Size (in pixels) of each cell on the screen
     * @param startX Starting gridX position
     * @param startY Starting gridY position
     *
     * @author Nelson Crosby
     */
    Entity(int gridSize = 1, int startX = 0, int startY = 0) {
        this.gridSize = gridSize
        this.gridX = startX
        this.gridY = startY
    }

    /**
     * Construct to a position on the map
     *
     * @param gridSize Size (in pixels) of each cell on the screen
     * @param startPos Starting map position
     *
     * @author Nelson Crosby
     */
    Entity(int gridSize, Vector2f startPos) {
        this.gridSize = gridSize
        this.pos = startPos
    }

    /**
     * Pixel-based position on the map
     * Getter for <code>pos</code>
     *
     * @return <code>pos</pos>
     *
     * @author Nelson Crosby
     */
    Vector2f getPos() {
        return new Vector2f(gridX * gridSize, gridY * gridSize)
    }

    /**
     * Pixel-based position on the map
     * Setter for <code>pos</code>
     *
     * @param newPos New <code>pos</code>
     *
     * @author Nelson Crosby
     */
    void setPos(Vector2f newPos) {
        gridX = newPos.x / gridSize
        gridY = newPos.y / gridSize
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
