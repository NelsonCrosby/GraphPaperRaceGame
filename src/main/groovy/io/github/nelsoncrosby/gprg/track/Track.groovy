package io.github.nelsoncrosby.gprg.track

import org.newdawn.slick.Graphics

/**
 *
 */
class Track {
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

    void render(Graphics gx) {

    }
}
