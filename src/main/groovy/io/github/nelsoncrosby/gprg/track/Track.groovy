package io.github.nelsoncrosby.gprg.track

import org.newdawn.slick.Color
import org.newdawn.slick.Graphics

/**
 *
 */
class Track {
    static final char OUT_OF_BOUNDS = 'X',
                      ON_TRACK = ' '
    static final int CELL_WIDTH = 32;
    char[][] grid

    // Track properties
    int width
    int height

    Track(File file) {
        this(new FileInputStream(file))
    }
    Track(InputStream istream) {
        List<String> lines = istream.readLines()
        // Populate grid with characters
        grid = new char[lines.size()][lines[0].size()]
        width = grid.length
        height = grid[0].length

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = lines[i].charAt(j)
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

    char[][] getTilesInView(int x1, int y1, int x2, int y2) {
        /**
         * Ensure calling method does not attempt to access
         * coordinates off the grid, or IndexExceptions will happen.
         */
        int width = x2-x1
        int height = y2-y1

        char[][] returnGrid = new char[width][height]
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                returnGrid[x][y] = grid[x][y]
            }
        }
        return returnGrid
    }
}
