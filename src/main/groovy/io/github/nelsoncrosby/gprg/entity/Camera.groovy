package io.github.nelsoncrosby.gprg.entity

import io.github.nelsoncrosby.gprg.Direction
import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Image
import org.newdawn.slick.ShapeFill
import org.newdawn.slick.fills.GradientFill
import org.newdawn.slick.geom.Rectangle
import org.newdawn.slick.geom.Shape
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
    float zoom = 1
    static final float DEFAULT_ZOOM_FACTOR = 1.75
    static final float DEFAULT_ZOOM_OUT_FACTOR = 1 / DEFAULT_ZOOM_FACTOR

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
                (worldPos.x * zoom) - position.x as float,
                (worldPos.y * zoom) - position.y as float
        )
    }
    Vector2f getScreenPos(Vector2f gridPos, int gridSize) {
        return getScreenPos(getWorldPos(gridPos, gridSize))
    }

    /**
     * Get world position from grid coordinates
     *
     * @author Riley Steyn
     */
    Vector2f getWorldPos(Vector2f gridPos, int gridSize) {
        return gridPos.copy().scale(gridSize)
    }

    /**
     * Draw an image using {@link Graphics} context {@code gx}, taking zoom
     *  into consideration.
     *
     * Also gives options to scale and crop the image.
     *
     * @param gx {@link Graphics} context to draw to
     * @param image {@link Image} to draw
     * @param dest The destination screen co-ordinates
     * @param scale Optional scale factor. Scaling is multiplicative (
     *              (1,1) is no scaling; (2,2) is scale by factor of 2, etc).
     *              If null, will be set to (1,1). This reference will not be
     *              modified
     * @param crop A {@link Rectangle} (relative to image dimensions) to crop
     *             out.
     *
     * @author Nelson Crosby
     */
    void draw(Graphics gx, Image image, Vector2f dest, Vector2f scale = null, Rectangle crop = null) {
        if (scale == null) {
            // No scaling
            scale = new Vector2f(1, 1)
        }
        if (crop == null) {
            // No cropping
            crop = new Rectangle(0, 0, image.width, image.height)
        }

        // Apply zoom to scale (which will result in zoom being applied to image)
        scale = scale.copy(/* We don't want to modify the passed vector */).scale(zoom)

        // Draw image       Destination
        gx.drawImage(image, dest.x, dest.y,
                /* Destination scale */
                (float) dest.x + (scale.x * image.width), (float) dest.y + (scale.y * image.height),
                /* Apply cropping rectangle */
                crop.x, crop.y, crop.width, crop.height
        )
    }

    /**
     * Zoom in/out by the given factor.
     * Camera maintains the center position.
     *
     * @param factor Zoom factor
     *
     * @author Nelson Crosby
     */
    void adjustZoom(float factor) {
        zoom *= factor
        position.add(halfSize).scale(factor).sub(halfSize)
    }

    /**
     * Reset zoom to 1 (original).
     * Camera maintains the center position.
     *
     * @author Nelson Crosby
     */
    void resetZoom() {
        // Hook into adjustZoom method in order to maintain center position
        adjustZoom(1 / zoom as float)
    }
}
