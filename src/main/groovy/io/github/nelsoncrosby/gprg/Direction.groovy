package io.github.nelsoncrosby.gprg

/**
 * A 2d direction on X and Y
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 */
enum Direction {
    UP, DOWN, LEFT, RIGHT

    private int multiplier
    /** Gets the axis multiplier for the current direction */
    int getMultiplier() {return multiplier}

    /**
     * 2d axes
     *
     * @author Nelson Crosby
     */
    static enum Axis {
        X, Y
    }
    private Axis axis
    /** Gets the axis to move along */
    Axis getAxis() {return axis}

    static {
        // Set the axis multipliers
        UP.multiplier = -1
        DOWN.multiplier = 1
        LEFT.multiplier = -1
        RIGHT.multiplier = 1

        // Set the axes
        UP.axis = Axis.Y
        DOWN.axis = Axis.Y
        LEFT.axis = Axis.X
        RIGHT.axis = Axis.X
    }
}
