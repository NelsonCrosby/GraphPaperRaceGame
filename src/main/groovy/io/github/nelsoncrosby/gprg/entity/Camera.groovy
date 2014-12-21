package io.github.nelsoncrosby.gprg.entity

import io.github.nelsoncrosby.gprg.Direction
import org.newdawn.slick.GameContainer
import org.newdawn.slick.geom.Vector2f

/**
 * Allows for relative positioning.
 *
 * @author Riley Steyn (github/RSteyn)
 * @author Nelson Crosby (github/NelsonCrosby)
 */
class Camera {
    /** Hold positions of camera in world space */
    Vector2f position
    /** The current speed of the camera (px/ms) */
    private float cameraSpeed = 1
    /** Holds half the (width, height) of screen */
    Vector2f halfSize

    /**
     * Construct the camera.
     *
     * @param gc {@link GameContainer} context (for getting width/height)
     *
     * @author Riley Steyn
     */
    Camera(GameContainer gc, Vector2f position) {
        halfSize = new Vector2f(
                (float) Math.ceil(gc.getWidth() / 2),
                (float) Math.ceil(gc.getHeight() / 2)
        )
        this.position = position
    }

    /**
     * Move the camera the specified direction for the given time.
     *
     * @param direction {@link Direction} to move
     * @param dt Time to move (ms)
     *
     * @author Nelson Crosby
     * @author Riley Steyn
     */
    void move(Direction direction, float dt) {
        if (direction.axis == Direction.Axis.X)
            position.x += (direction.multiplier * cameraSpeed * dt)
        else
            position.y += (direction.multiplier * cameraSpeed * dt)
    }

    /**
     * Get the position on the screen for any given position in the world.
     *
     * @param fieldPos Position in world space (not grid)
     * @return Position on screen
     *
     * @author Nelson Crosby
     */
    Vector2f getScreenPos(Vector2f worldPos) {
        return new Vector2f(
                worldPos.x - position.x as float,
                worldPos.y - position.y as float
        )
    }

    /** Get world position from grid coordinates
     *
     * @author Riley Steyn
     */
    Vector2f getWorldPos(Vector2f gridPos, int gridSize) {
        return gridPos.copy().scale(gridSize)
    }
}
