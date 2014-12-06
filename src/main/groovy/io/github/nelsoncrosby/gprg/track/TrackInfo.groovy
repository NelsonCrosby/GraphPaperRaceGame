package io.github.nelsoncrosby.gprg.track

import io.github.nelsoncrosby.gprg.Direction

/**
 * Information about a track.
 * TrackInfo is encoded into a {@code .track} file using the Java Serialization
 *  Format. The file consists of a header written through an ObjectOutputStream,
 *  then a body in PNG format.
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 */
class TrackInfo implements Serializable {
    /**
     * Exception to be thrown when a TrackInfo version is not currently
     *  supported.
     */
    static class VersionNotSupportedException extends Exception {
        VersionNotSupportedException(int version) {
            super("The version $version is not supported")
        }
    }

    private static final long serialVersionUID = 0

    /** TrackInfo version. Used to determine what version to cast to. */
    int version
    static class Version1 extends TrackInfo {
        private static final long serialVersionUID = 1
        /** TrackInfo version */
        int version = 1
        /** The name of the track */
        String trackName
        /** The {@link Direction} the players should start moving in */
        Direction startLineDirection
    }
}
