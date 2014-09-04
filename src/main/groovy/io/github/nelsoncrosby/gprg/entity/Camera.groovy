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
    // Hold essential view variables
    static final int FOV = 45 // Degrees in Field-Of-View
    float aspectRatio

    // Hold positions of camera in world space
    Vector2f cameraPos = new Vector2f(160, 160)

    float cameraSpeed = 1

    // Pre-calculated view variables
    Vector2f halfSize

    // Holds current game instance variables
    Track currentTrack

    Camera(GameContainer gc, Track track, float ar) {
        currentTrack = track
        aspectRatio = ar
        halfSize = new Vector2f(
                (float) Math.ceil(gc.getWidth() / 2),
                (float) Math.ceil(gc.getHeight() / 2)
        )
    }

    // Rendering code
    Color offTrack = new Color(100, 100, 0)
    Color onTrack = new Color(255, 255, 255)
    void render(Graphics gx) {
        renderTrack(gx)
    }
    void renderTrack(Graphics gx) {

        // Actually draw tiles
        currentTrack.printGrid()
        int drawX
        int drawY
        char[] column
        // -1 for zero-based again
        for (x in 0..currentTrack.size.x-1) {
            column = currentTrack.grid[x]
            for (y in 0..column.length-1) {
                char cell = column[y]
                // Get colour with which to draw colour
                switch (cell) {
                    case Track.OUT_OF_BOUNDS:
                        gx.color = offTrack
                        break
                    case Track.ON_TRACK:
                        gx.color = onTrack
                        break
                }
                drawX = (x * currentTrack.CELL_WIDTH) -
                        cameraPos.x + halfSize.x
                drawY = (y * currentTrack.CELL_WIDTH) -
                        cameraPos.y + halfSize.y
                gx.fillRect(drawX, drawY,
                        currentTrack.CELL_WIDTH, currentTrack.CELL_WIDTH)
            }
        }
    }

    // Movement methods
    void moveX(int direction, float dt) {
        cameraPos.x += (direction * cameraSpeed * dt)
    }
    void moveY(int direction, float dt) {
        cameraPos.y -= (direction * cameraSpeed * dt)
    }
}
