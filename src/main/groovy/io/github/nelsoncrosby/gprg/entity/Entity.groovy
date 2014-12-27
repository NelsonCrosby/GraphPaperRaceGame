package io.github.nelsoncrosby.gprg.entity

import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f

/**
 * Something that can be displayed on the screen
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 * @author Riley Steyn (github/RSteyn)
 */
abstract class Entity {
    /** Current position in grid */
    Vector2f gridPos
    /** Current velocity in grid-units */
    Vector2f vel
    /** Size (in pixels) of each cell on the screen */
    int gridSize

    /**
     * Construct to a position on the grid.
     *
     * @param gridSize Size (in pixels) of each cell on the screen
     * @param startX Starting gridX position
     * @param startY Starting gridY position
     *
     * @author Nelson Crosby
     * @author Riley Steyn
     */
    Entity(int gridSize = 1, int startX = 0, int startY = 0) {
        this.gridSize = gridSize
        this.gridPos = new Vector2f(startX, startY)
        this.vel = new Vector2f(0, 0)
    }

    /**
     * Get world position from grid coordinates
     *
     * @author Riley Steyn
     */
    static Vector2f getWorldPos(Vector2f gridPos, int gridSize) {
        gridPos.copy().scale(gridSize)
    }

    Vector2f getWorldPos() {
        getWorldPos(gridPos, gridSize)
    }

    /**
     * Perform changes to the entity.
     *
     * @param delta Milliseconds since last called
     * @param track The currently active track
     * @return {@code true} if the game should restart
     */
    abstract boolean update(int delta, Track track)

    /**
     * Render entity to the screen.
     * Calls the abstract method, giving actual screen co-ordinates.
     *
     * @param gx {@link Graphics} context to draw to
     * @param actualPos Camera object to determine actual position
     *
     * @author Nelson Crosby
     */
    void render(Graphics gx, Camera camera) {
        render(gx, camera.getScreenPos(gridPos.copy().scale(gridSize)))
    }

    /**
     * Render entity to the screen.
     *
     * @param gx {@link Graphics} context to draw to
     * @param screenPos Actual position of the entity on the screen
     */
    protected abstract void render(Graphics gx, Vector2f screenPos)
}
