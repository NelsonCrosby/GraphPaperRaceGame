package io.github.nelsoncrosby.gprg

/**
 *
 */
enum Direction {
    UP, DOWN, LEFT, RIGHT

    private int multiplier
    /** Gets the axis multiplier for the current direction */
    int getMultiplier() {return multiplier}

    static enum Axis {
        X, Y
    }
    private Axis axis
    /** Gets the axis to move along */
    Axis getAxis() {return axis}

    static {
        UP.multiplier = 1
        DOWN.multiplier = -1
        LEFT.multiplier = -1
        RIGHT.multiplier = 1

        UP.axis = Axis.Y
        DOWN.axis = Axis.Y
        LEFT.axis = Axis.X
        RIGHT.axis = Axis.X
    }
}
