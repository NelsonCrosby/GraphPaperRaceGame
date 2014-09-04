package io.github.nelsoncrosby.gprg.track

import org.newdawn.slick.Color
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f

/**
 *
 */
class Track {
    static final char OUT_OF_BOUNDS = 'X',
                      ON_TRACK = ' '
    static final int CELL_WIDTH = 20;
    char[][] grid

    // Track properties
    Vector2f size

    Track(File file) {
        this(new FileInputStream(file))
    }
    Track(InputStream istream) {
        List<String> lines = istream.readLines()
        // Populate grid with characters
        grid = new char[lines[0].size()][lines.size()]
        size = new Vector2f(grid.length, grid[0].length)

        for (y in 0..size.y-1) {
            for (x in 0..size.x-1) {
                grid[x][y] = lines[y].charAt(x)
            }
        }
    }

    Color black = new Color(0, 0, 0)
    Color white = new Color(255, 255, 255)
    void render(Graphics gx) {
        // Actually draw the thing
        for (int y = 0; y < grid.length; y++) {
            char[] row = grid[y]
            for (int x = 0; x < row.length; x++) {
                char cell = row[x]
                // Get colour with which to draw colour
                switch (cell) {
                    case OUT_OF_BOUNDS:
                        gx.color = black
                        break
                    case ON_TRACK:
                        gx.color = white
                        break
                }
                gx.fillRect(x*CELL_WIDTH, y*CELL_WIDTH, CELL_WIDTH, CELL_WIDTH)
            }
        }
    }
    /**
     * This method culls tiles, returning all tiles visible in
     * viewport defined by corner arguments.  This is to optimise later, if necessary,
     * or for the minimap.
     * Ensure calling method does not attempt to access
     * coordinates off the grid, or IndexOutOfRangeExceptions will occur.
     */
    char[][] getTilesInView(int x1, int y1, int x2, int y2) {
        Vector2f size = new Vector2f(x2-x1, y2-y1)

        // Must -1 a lot because of zero-based arrays
        char[][] returnGrid = new char[size.x+1][size.y+1]
        int gridX
        int gridY
        for (x in 0..size.x) {
            for (y in 0..size.y) {
                gridX = x+x1
                gridY = y+y1
                returnGrid[x][y] = grid[gridX-1][gridY-1]
            }
        }
        return returnGrid
    }

    void printGrid() {
        /**
         * Method basically prints the grid in the form it is in
         * in the .txt file.  Really only useful for testing culling.
         * Doesn't work at the moment, only prints full map.
         */
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
