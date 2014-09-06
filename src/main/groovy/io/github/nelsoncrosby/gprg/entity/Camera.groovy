package io.github.nelsoncrosby.gprg.entity

import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f

/**
 * Created by rileysteyn on 1/09/14.
 */
class Camera {
    // Hold positions of camera in world space
    Vector2f position = new Vector2f(0, 0)

    float cameraSpeed = 1
    Vector2f halfSize  // Holds half the (width, height) of screen
    Camera(GameContainer gc) {
        halfSize = new Vector2f(
                (float) Math.ceil(gc.getWidth() / 2),
                (float) Math.ceil(gc.getHeight() / 2)
        )
    }

    // Movement methods
    void moveX(int direction, float dt) {
        position.x += (direction * cameraSpeed * dt)
    }
    void moveY(int direction, float dt) {
        position.y -= (direction * cameraSpeed * dt)
    }
}
