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

    float version
    static class Version0_1 extends TrackInfo {
        float version = 0.1
        String trackName
        Direction startLineDirection
    }
}
