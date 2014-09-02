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
    float cameraX = 0
    float cameraY = 0

    float cameraSpeed = 1

    // Pre-calculated view variables
    float halfWidth // Holds the furthest away the camera can see from height cameraZ

    // Holds current game instance variables
    Track currentTrack

    // Constructor
    Camera(GameContainer gc, Track track, float ar){
        currentTrack = track
        aspectRatio = ar
        halfWidth = Math.ceil(gc.getWidth() / 2)
    }

    // Rendering code
    Color black = new Color(0, 0, 0)
    Color white = new Color(255, 255, 255)
    void render(Graphics gx) {
        renderTrack(gx)
    }
    void renderTrack(Graphics gx) {
        // Holds corners of world view
        int worldX1 = cameraX-halfWidth
        int worldY1 = cameraY-halfWidth
        int worldX2 = cameraX+halfWidth
        int worldY2 = cameraY+halfWidth

        // Corners of track currently in view
        int trackX1 = Math.floor(worldX1 / currentTrack.CELL_WIDTH)
        int trackY1 = Math.floor(worldY1 / currentTrack.CELL_WIDTH)
        int trackX2 = Math.ceil(worldX2 / currentTrack.CELL_WIDTH)
        int trackY2 = Math.ceil(worldY2 / currentTrack.CELL_WIDTH)

        // Viewport boundary-checking
        if (trackX1 < 0) {trackX1 = 0}
        if (trackY1 < 0) {trackY1 = 0}
        if (trackX2 > currentTrack.width) {trackX2 = currentTrack.width}
        if (trackY2 > currentTrack.height) {trackY2 = currentTrack.height}

        int emptySpaceOnX = (trackX1 * currentTrack.CELL_WIDTH) - worldX1
        int emptySpaceOnY = (trackY1 * currentTrack.CELL_WIDTH) - worldY1

        // Get tiles to draw
        char[][] grid = currentTrack.getTilesInView(trackX1, trackY1, trackX2, trackY2)

        // Actually draw tiles
        for (int y = 0; y < grid.length; y++) {
            char[] row = grid[y]
            for (int x = 0; x < row.length; x++) {
                char cell = row[x]
                // Get colour with which to draw colour
                switch (cell) {
                    case Track.OUT_OF_BOUNDS:
                        gx.color = black
                        break
                    case Track.ON_TRACK:
                        gx.color = white
                        break
                }
                int drawX = (x * currentTrack.CELL_WIDTH) - cameraX + emptySpaceOnX
                int drawY = (y * currentTrack.CELL_WIDTH) - cameraY + emptySpaceOnY
                gx.fillRect(drawX, drawY, currentTrack.CELL_WIDTH, currentTrack.CELL_WIDTH)
            }
        }
    }

    // Movement methods
    void moveX(int direction) {
        cameraX += (direction * cameraSpeed)
    }
    void moveY(int direction) {
        cameraY -= (direction * cameraSpeed)
    }
}
