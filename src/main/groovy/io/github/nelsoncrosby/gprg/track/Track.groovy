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

    Track(File file) {
        this(new FileInputStream(file))
    }
    Track(InputStream istream) {
        List<String> lines = istream.readLines()
        // Populate grid with characters
        grid = new char[lines.size()][lines[0].size()]
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = lines[i].charAt(j)
            }
        }
    }

    Color black = new Color(0, 0, 0)
    Color white = new Color(255, 255, 255)
    void render(Graphics gx) {
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
}
