package io.github.nelsoncrosby.gprg.track

import io.github.nelsoncrosby.gprg.entity.Camera
import org.newdawn.slick.Color
import org.newdawn.slick.Graphics
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
    /**
     * Characters used in track files
     */
    static final char OUT_OF_BOUNDS = 'X',
                      ON_TRACK = ' '
    /**
     * The width of each cell on the screen (px)
     */
    static final int CELL_WIDTH = 12;

    // Track properties
    char[][] grid
    Vector2f size

    /**
     * Convenience constructor for reading from a file.
     *
     * @param file The file to open
     *
     * @author Nelson Crosby
     */
    Track(File file) {
        this(new FileReader(file))
    }
    /**
     * Convenience constructor for reading from an InputStream.
     *
     * @param istream The InputStream to read from
     *
     * @author Nelson Crosby
     */
    Track(InputStream istream) {
        this(new InputStreamReader(istream))
    }
    /**
     * Read a track from a Reader.
     * Each new character is a new cell, and each row is separated by a
     *  new-line.
     *
     * @param input Where to read from
     *
     * @author Nelson Crosby
     */
    Track(Reader input) {
        List<String> lines = input.readLines()
        // Populate grid with characters
        grid = new char[lines[0].size()][lines.size()]
        size = new Vector2f(grid.length, grid[0].length)

        for (y in 0..size.y-1) {
            for (x in 0..size.x-1) {
                grid[x][y] = lines[y].charAt(x)
            }
        }
    }

    Color offTrack = new Color(0, 100, 0)
    Color onTrack = new Color(255, 255, 255)
    /**
     * Draw the track on the screen.
     *
     * @param gx Graphics context to draw to
     * @param camera Camera context to determine relative positions
     *
     * @author Riley Steyn
     */
    void render(Graphics gx, Camera camera) {
        int drawX
        int drawY
        char[] column
        // -1 for zero-based again
        for (x in 0..size.x-1) {
            column = grid[x]
            for (y in 0..column.length-1) {
                char cell = column[y]
                // Get colour with which to draw colour
                switch (cell) {
                    case OUT_OF_BOUNDS:
                        gx.color = offTrack
                        break
                    case ON_TRACK:
                        gx.color = onTrack
                        break
                }
                drawX = (x*CELL_WIDTH) - camera.position.x
                drawY = (y*CELL_WIDTH) - camera.position.y
                gx.fillRect(drawX, drawY, CELL_WIDTH, CELL_WIDTH)
            }
        }
    }

    /**
     * This method culls tiles, returning all tiles visible in
     * viewport defined by corner arguments.  This is to optimise later, if necessary,
     * or for the minimap.
     * Ensure calling method does not attempt to access
     * coordinates off the grid, or IndexOutOfRangeExceptions will occur.
     *
     * @param camera Camera context to determine relative positions
     *
     * @author Riley Steyn
     */
    char[][] getTilesInView(Camera camera) {
        Vector2f size = camera.halfSize * 2

        // Must -1 a lot because of zero-based arrays
        char[][] returnGrid = new char[size.x+1][size.y+1]
        int gridX
        int gridY
        for (x in 0..size.x) {
            for (y in 0..size.y) {
                gridX = x + camera.position.x - camera.halfSize.x
                gridY = y + camera.position.y - camera.halfSize.y
                returnGrid[x][y] = grid[gridX-1][gridY-1]
            }
        }
        return returnGrid
    }

    /**
     * Method basically prints the grid in the form it is in
     * in the .txt file.  Really only useful for testing culling.
     * Doesn't work at the moment, only prints full map.
     *
     * @author Riley Steyn
     */
    void printGrid() {
        // First transpose grid (swap coordinates) for printing
        char[][] newGrid = new char[size.y][size.x]
        for (x in 0..size.x-1) {
            for (y in 0..size.y-1) {
                newGrid[y][x] = grid[x][y]
            }
        }
        // Now go through each row, printing it
        for (row in newGrid) {
            for (c in row) {
                print(c)
            }
            print('\n')
        }
    }
}
