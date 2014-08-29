package io.github.nelsoncrosby.gridrace

/**
 *
 */
class CONST {
    private static final Properties constants
    static {
        constants = new Properties()
        constants.load(CONST.getResourceAsStream('const.properties'))
    }

    static Object getAt(Object key) {
        return constants.getAt(key)
    }
}
