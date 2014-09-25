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

    protected Entity(Vector2f pos = new Vector2f(0f, 0f)) {
        this.pos = pos
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
