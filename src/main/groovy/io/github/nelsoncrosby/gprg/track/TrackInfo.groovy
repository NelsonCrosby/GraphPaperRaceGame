package io.github.nelsoncrosby.gprg.track

import io.github.nelsoncrosby.gprg.Direction

/**
 *
 */
class TrackInfo implements Serializable {
    static class VersionNotSupportedException extends Exception {
        VersionNotSupportedException(float version) {
            super("The version $version is not supported")
        }
    }

    private static final long serialVersionUID = 0

    int version
    static class Version1 extends TrackInfo {
        private static final long serialVersionUID = 1
        int version = 1
        String trackName
        Direction startLineDirection
    }
}
