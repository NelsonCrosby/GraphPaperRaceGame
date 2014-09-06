package io.github.nelsoncrosby.gprg.entity

import org.newdawn.slick.Graphics

/**
 *
 */
abstract class Entity {
    abstract void update(int delta)
    abstract void render(Graphics gx, Camera camera)
}
