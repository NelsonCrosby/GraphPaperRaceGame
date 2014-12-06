package io.github.nelsoncrosby.gprg.entity

import io.github.nelsoncrosby.gprg.Direction
import org.newdawn.slick.GameContainer
import org.newdawn.slick.geom.Vector2f

/**
 * Allows for relative positioning
 *
 * @author Riley Steyn (github/RSteyn)
 * @author Nelson Crosby (github/NelsonCrosby)
 */
class Camera {
    /** Hold positions of camera in world space */
    Vector2f position = new Vector2f(45*12, -15*12)

    private float cameraSpeed = 1
    /** Holds half the (width, height) of screen */
    Vector2f halfSize

    /**
     * Construct the camera
     *
     * @param gc GameContainer context
     *
     * @author Riley Steyn
     */
    Camera(GameContainer gc) {
        halfSize = new Vector2f(
                (float) Math.ceil(gc.getWidth() / 2),
                (float) Math.ceil(gc.getHeight() / 2)
        )
    }

    /**
     * Move the camera the specified direction
     *
     * Calls the appropriate {@link #moveX} and {@link #moveY}
     *
     * @param direction Direction to move
     * @param dt Distance to move
     *
     * @author Nelson Crosby
     */
    void move(Direction direction, float dt) {
        if (direction.axis == Direction.Axis.X)
            moveX(direction.multiplier, dt)
        else
            moveY(direction.multiplier, dt)
    }

    /**
     * Move camera along the <code>x</code> (horizontal) axis
     *
     * @param direction Direction (left/right) to move
     * @param dt Distance to move
     *
     * @author Riley Steyn
     */
    void moveX(int direction, float dt) {
        position.x += (direction * cameraSpeed * dt)
    }

    /**
     * Move camera along the <code>y</code> (vertical) axis
     *
     * @param direction Direction (up/down) to move
     * @param dt Distance to move
     *
     * @author Riley Steyn
     */
    void moveY(int direction, float dt) {
        position.y += (direction * cameraSpeed * dt)
    }

    /**
     * Get the position on the screen for any given position in the world
     *
     * @param fieldPos Position in world space (not grid)
     * @return Position on screen
     *
     * @author Nelson Crosby
     */
    Vector2f getScreenPos(Vector2f fieldPos) {
        return new Vector2f(
                fieldPos.x - position.x as float,
                fieldPos.y - position.y as float
        )
    }
}
