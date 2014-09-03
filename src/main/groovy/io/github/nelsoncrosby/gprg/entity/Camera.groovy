package io.github.nelsoncrosby.gprg.entity

import io.github.nelsoncrosby.gprg.track.Track
import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics

/**
 * Created by rileysteyn on 1/09/14.
 */
class Camera {
    // Hold essential view variables
    static final int FOV = 45 // Degrees in Field-Of-View
    float aspectRatio // Fairly self-explanatory

    // Hold positions of camera in world space
    float cameraX = 160
    float cameraY = 160

    float cameraSpeed = 1

    // Pre-calculated view variables
    float halfWidth // Half the width of the window
    float halfHeight // Half the height of the window

    // Holds current game instance variables
    Track currentTrack

    // Constructor
    Camera(GameContainer gc, Track track, float ar){
        currentTrack = track
        aspectRatio = ar
        halfWidth = Math.ceil(gc.getWidth() / 2)
        halfHeight = Math.ceil(gc.getHeight() / 2)
    }

    // Rendering code
    Color black = new Color(100, 100, 0)
    Color white = new Color(255, 255, 255)
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
        for (x in 0..currentTrack.width-1) {
            column = currentTrack.grid[x]
            for (y in 0..column.length-1) {
                char cell = column[y]
                // Get colour with which to draw colour
                switch (cell) {
                    case Track.OUT_OF_BOUNDS:
                        gx.color = black
                        break
                    case Track.ON_TRACK:
                        gx.color = white
                        break
                }
                drawX = (x * currentTrack.CELL_WIDTH) - cameraX + halfWidth
                drawY = (y * currentTrack.CELL_WIDTH) - cameraY + halfHeight
                gx.fillRect(drawX, drawY, currentTrack.CELL_WIDTH, currentTrack.CELL_WIDTH)
            }
        }
    }

    // Movement methods
    void moveX(int direction, float dt) {
        cameraX += (direction * cameraSpeed * dt)
    }
    void moveY(int direction, float dt) {
        cameraY -= (direction * cameraSpeed * dt)
    }
}
