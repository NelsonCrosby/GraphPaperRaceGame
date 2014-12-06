import io.github.nelsoncrosby.gprg.Direction
import io.github.nelsoncrosby.gprg.track.TrackInfo
import io.github.nelsoncrosby.utils.StreamUtils

// Use for reading lines
def stdin = new BufferedReader(new InputStreamReader(System.in))

// Read info
print 'Track name: '; def trackName = stdin.readLine()
print 'Start line direction: '; def startDirection = stdin.readLine()

// Populate trackInfo
def trackInfo = new TrackInfo.Version0_1()

trackInfo.trackName = trackName

startDirection = startDirection.toUpperCase()
// Get the direction enum
switch (startDirection) {
    case Direction.UP.name():
        trackInfo.startLineDirection = Direction.UP
        break
    case Direction.DOWN.name():
        trackInfo.startLineDirection = Direction.DOWN
        break
    case Direction.LEFT.name():
        trackInfo.startLineDirection = Direction.LEFT
        break
    case Direction.RIGHT.name():
        trackInfo.startLineDirection = Direction.RIGHT
        break
    default:
        println 'Not a valid start line direction'
        assert false // Kill script
}

// Get body PNG
print 'Track PNG: '; def trackPng = stdin.readLine()
def pngFile = new File(trackPng)
if (!pngFile.exists()) {
    // Ensure PNG exists
    println "Track doesn't exist"
    assert false // Kill script
}

// Get the destination file
print 'Destination file: '; def destPath = stdin.readLine()
def dest = new ObjectOutputStream(new FileOutputStream(destPath))

// Write header
dest.writeObject(trackInfo)
// Copy body from pngFile
StreamUtils.copyStreams(new FileInputStream(pngFile), dest)

println 'Done!'
