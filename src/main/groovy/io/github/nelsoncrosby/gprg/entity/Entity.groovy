package io.github.nelsoncrosby.gprg.entity

import org.newdawn.slick.Graphics

/**
 * Something that can be displayed on the screen
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 */
abstract class Entity {
    /**
     * Perform changes to the entity
     *
     * @param delta Milliseconds since last called
     */
    abstract void update(int delta)
    /**
     * Render entity to the screen
     *
     * @param gx Graphics context to draw to
     * @param camera Camera context to determine relative positions
     */
    abstract void render(Graphics gx, Camera camera)
}
