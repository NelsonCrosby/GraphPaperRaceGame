package io.github.nelsoncrosby.gprg.track

import io.github.nelsoncrosby.gprg.Direction
import io.github.nelsoncrosby.gprg.entity.Camera
import io.github.nelsoncrosby.gprg.entity.Entity
import org.newdawn.slick.Color
import org.newdawn.slick.Graphics
import org.newdawn.slick.Image
import org.newdawn.slick.geom.Vector2f

/**
 * Grid-based track that can be raced on
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 * @author Riley Steyn (github/RSteyn)
 */
class Track {
    /** The width of each cell on the screen (px) */
    static final int CELL_WIDTH = 12;
    private static final Vector2f CELL_WIDTH_VECT = new Vector2f(CELL_WIDTH, CELL_WIDTH)

    // Track properties
    Image trackDef
    Vector2f size
    Deque<Vector2f> startLocations
    TrackInfo.Version1 info
    boolean drawGrid = false
    void toggleGrid() {drawGrid = !drawGrid}

    Color offTrack = new Color(0, 100, 0, 255)
    @SuppressWarnings("GroovyUnusedDeclaration")
    Color onTrack = Color.white
    Color startLine = Color.black

    /**
     * Load track from {@code .track} file read from {@code sourceFile}.
     *
     * @param sourceFile The file containing a {@code .track} file
     *
     * @throws TrackInfo.VersionNotSupportedException Version found is not
     *              supported by the current version of {@link Track}
     *
     * @author Nelson Crosby
     */
    Track(File sourceFile) throws TrackInfo.VersionNotSupportedException {
        this(sourceFile.name, new FileInputStream(sourceFile))
    }

    /**
     * Convenience constructor to load a {@code .track} file from jar resources
     *  relative to {@code loadClass}
     *
     * @param trackName Base track name
     * @param loadClass Class to use for relative jar resources
     *
     * @throws TrackInfo.VersionNotSupportedException Version found is not
     *              supported by the current version of {@link Track}
     *
     * @author Nelson Crosby
     */
    Track(String trackName, Class loadClass)
            throws TrackInfo.VersionNotSupportedException
    {
        this(trackName, loadClass.getResourceAsStream("${trackName}.track"))
    }

    /**
     * Load track from {@code .track} file read from {@code source}.
     *
     * @param trackName Base track name (gotten from file)
     * @param source {@link InputStream} encoded in {@code .track} to read from
     *
     * @throws TrackInfo.VersionNotSupportedException Version found is not
     *              supported by the current version of {@link Track}
     *
     * @author Riley Steyn
     * @author Nelson Crosby
     */
    Track(String trackName, InputStream source)
            throws TrackInfo.VersionNotSupportedException
    {
        ObjectInputStream trackInfoStream = new ObjectInputStream(source)

        // Get the track info version
        def trackInfoVersion = trackInfoStream.readInt()
        // Get the track info
        switch (trackInfoVersion) {
            case 1: info = trackInfoStream.readObject() as TrackInfo.Version1; break
            default:
                throw new TrackInfo.VersionNotSupportedException(trackInfoVersion)
        }

        // Parse track image
        trackDef = new Image(trackInfoStream /* Use remainder of bytes */,
                "${trackName}.png", false /* Not flipped */)
        trackDef.setFilter(Image.FILTER_NEAREST)  // Stops image blurring
        size = new Vector2f(trackDef.width, trackDef.height)

        this.startLocations = generateStartLocations()
    }

    /**
     * Populate {@link #startLocations} with grid positions.
     * Takes the logic out of the constructor for tidiness.
     *
     * @author Nelson Crosby
     * @author Riley Steyn
     */
    private Queue<Vector2f> generateStartLocations() {
        def startLocations = new LinkedList<Vector2f>()
        // Populate startLocations based on black-coloured pixels
        for (x in 0..trackDef.width - 1) /* Iterate through X grid */ {
            for (y in 0..trackDef.height - 1) /* Iterate through Y grid */ {
                Color checking = trackDef.getColor(x, y)
                if (checking == startLine) {
                    def pos = new Vector2f(x, y)
                    if (info.startLineDirection == Direction.RIGHT) {
                        pos.x += 1
                    } else if (info.startLineDirection == Direction.DOWN) {
                        pos.y += 1
                    }
                    // Add pos to the queue
                    startLocations.offer(pos)
                }
            }
        }
        // The first one isn't in a good spot, get rid of it
        startLocations.poll()

        return startLocations
    }

    /**
     * Check if an entity is within the track bounds
     *
     * @param entity Entity to check
     * @return {@code true} if not all surrounding tiles are
     *         {@code offTrack}
     *
     * @author Nelson Crosby
     * @author Riley Steyn
     */
    boolean isOnTrack(Entity entity) {
        Vector2f pos = entity.gridPos
        if (pos.x < 0 || pos.x > trackDef.width ||
                pos.y < 0 || pos.y > trackDef.height) {
            // Illegal state - outside of track bounds
            return false
        }
        return trackDef.getColor(pos.x as int, pos.y as int) != offTrack ||
                trackDef.getColor(pos.x as int, pos.y-1 as int) != offTrack ||
                trackDef.getColor(pos.x-1 as int, pos.y as int) != offTrack ||
                trackDef.getColor(pos.x-1 as int, pos.y-1 as int) != offTrack
    }

    /**
     * Draw the track on the screen.
     *
     * @param gx {@link Graphics} context to draw to
     * @param camera {@link Camera} context to determine relative positions
     *
     * @author Riley Steyn
     */
    void render(Graphics gx, Camera camera) {
        camera.draw(gx, trackDef, camera.position.negate(), CELL_WIDTH_VECT)
        if (drawGrid) {
            gx.color = new Color(0, 0, 0, 0.1f)
            for (int i = 1; i < trackDef.width; i++) {
                Vector2f pos1 = camera.getScreenPos(new Vector2f(i, 0), CELL_WIDTH)
                Vector2f pos2 = camera.getScreenPos(new Vector2f(i, trackDef.height), CELL_WIDTH)
                gx.drawLine(pos1.x, pos1.y, pos2.x, pos2.y)
            }
            for (int i = 1; i < trackDef.height; i++) {
                Vector2f pos1 = camera.getScreenPos(new Vector2f(0, i), CELL_WIDTH)
                Vector2f pos2 = camera.getScreenPos(new Vector2f(trackDef.width, i), CELL_WIDTH)
                gx.drawLine(pos1.x, pos1.y, pos2.x, pos2.y)
            }
        }
    }
}
