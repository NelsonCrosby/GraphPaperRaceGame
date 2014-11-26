package io.github.nelsoncrosby.gprg.track

import io.github.nelsoncrosby.gprg.entity.Camera
import io.github.nelsoncrosby.gprg.entity.Entity
import org.newdawn.slick.Color
import org.newdawn.slick.Graphics
import org.newdawn.slick.Image
import org.newdawn.slick.geom.Vector2f

/**
 * Track grid
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 * @author Riley Steyn (github/RSteyn)
 */
class Track {
    /** The width of each cell on the screen (px) */
    static final int CELL_WIDTH = 12;

    // Track properties
    Image imgTrack
    Vector2f size
    Queue<Vector2f> startLocations

    Color offTrack = new Color(0, 100, 0, 255)
    @SuppressWarnings("GroovyUnusedDeclaration")
    Color onTrack = Color.white
    Color startLine = Color.black

    /**
     * Convenience constructor for simply passing an image.
     * Better for fast iterative development
     *
     * @param trackName Name for the track
     *
     * @author Riley Steyn
     * @author Nelson Crosby
     */
    Track(String trackName) {
        String imgPath = "${trackName}.track.png"
        imgTrack = new Image(
                getClass().getResourceAsStream(imgPath), imgPath, false
        )
        imgTrack.setFilter(Image.FILTER_NEAREST)
        size = new Vector2f(imgTrack.width, imgTrack.height)

        startLocations = new LinkedList<>()
        for (x in 0..imgTrack.width-1) {
            for (y in 0..imgTrack.height-1) {
                Color checking = imgTrack.getColor(x, y)
                if (checking == startLine)
                    startLocations.offer(new Vector2f(x, y))
            }
        }

        // The first one isn't in a good spot, get rid of it
        startLocations.poll()
    }

    /**
     * Check if an entity is within the track bounds
     *
     * @param entity Entity to check
     * @return <code>true</code> if not all surrounding tiles are
     *         <code>offTrack</code>
     *
     * @author Nelson Crosby
     * @author Riley Steyn
     */
    boolean isOnTrack(Entity entity) {
        return imgTrack.getColor(entity.gridX, entity.gridY) != offTrack ||
                imgTrack.getColor(entity.gridX, entity.gridY-1) != offTrack ||
                imgTrack.getColor(entity.gridX-1, entity.gridY) != offTrack ||
                imgTrack.getColor(entity.gridX-1, entity.gridY-1) != offTrack
    }

    /**
     * Draw the track on the screen.
     *
     * @param gx Graphics context to draw to
     * @param camera Camera context to determine relative positions
     *
     * @author Riley Steyn
     */
    void render(Graphics gx, Camera camera) {
        gx.drawImage(imgTrack, -camera.position.x, -camera.position.y,
                (imgTrack.width*CELL_WIDTH)-camera.position.x as int,
                (imgTrack.height*CELL_WIDTH)-camera.position.y as int,
                0, 0, imgTrack.width, imgTrack.height)
    }
}
