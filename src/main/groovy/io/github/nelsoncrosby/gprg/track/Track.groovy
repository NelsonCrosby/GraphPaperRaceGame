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
// There's some weird warnings coming out of here that make no sense.
@SuppressWarnings(["GroovyAssignabilityCheck", "GrUnresolvedAccess"])
class Track {
    /** The width of each cell on the screen (px) */
    static final int CELL_WIDTH = 12;

    // Track properties
    Image imgTrack
    Vector2f size

    Color offTrack = new Color(0, 100, 0, 255)
    Color onTrack = new Color(255, 255, 255, 255)

    /**
     * Convenience constructor for simply passing an image.
     * Better for fast iterative development
     *
     * @param img
     *
     * @author Riley Steyn
     */
    Track(String trackName) {
        String imgPath = "${trackName}.track.png"
        imgTrack = new Image(
                getClass().getResourceAsStream(imgPath), imgPath, false
        )
        imgTrack.setFilter(Image.FILTER_NEAREST)
        size = new Vector2f(imgTrack.width, imgTrack.height)
    }

    /**
     * Check if an entity is within the track bounds
     *
     * @param entity Entity to check
     * @return <code>true</code> if all surrounding tiles are
     *         <code>onTrack</code>
     *
     * @author Nelson Crosby
     * @author Riley Steyn
     */
    boolean isOnTrack(Entity entity) {
        return imgTrack.getColor(entity.gridX, entity.gridY) == onTrack ||
                imgTrack.getColor(entity.gridX, entity.gridY-1) == onTrack ||
                imgTrack.getColor(entity.gridX-1, entity.gridY) == onTrack ||
                imgTrack.getColor(entity.gridX-1, entity.gridY-1) == onTrack
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
