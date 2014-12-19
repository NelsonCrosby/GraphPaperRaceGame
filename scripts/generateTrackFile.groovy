import groovy.swing.SwingBuilder
import io.github.nelsoncrosby.gprg.Direction
import io.github.nelsoncrosby.gprg.track.TrackInfo
import io.github.nelsoncrosby.utils.StreamUtils

import javax.swing.*
import javax.swing.filechooser.FileFilter

/* Track file generator script
 * Uses a Swing GUI to get info about the track from the user and produce a
 *  .track file for use in GPRG
 */

// GUI for getting info
new SwingBuilder().edt {
    // Suffix for dialog titles
    def titleSuffix = 'GPRG Track file creator'

    // Variables used
    JFrame trackInfoFrame, trackPngFrame, trackDestFrame
    def trackInfo = new TrackInfo.Version1()
    File pngFile, destFile
    // Frame for getting track info
    trackInfoFrame = frame(
            title: "Track Info | $titleSuffix",
            size: [380, 130],
            show: false,
            defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
            locationRelativeTo: null) {
        tableLayout(cellpadding: 3) {
            // Track name - use text input
            tr {
                td {label(text: 'Track name:')}
                td {trackNameField = textField(columns: 20)}
            }
            // Start direction - use drop-down
            tr {
                td {label(text: 'Start line direction:')}
                td {startDirBox = comboBox(items: Direction.enumConstants)}
            }
            // OK button
            tr {
                td(colspan: 2, colfill: 2) {
                    button(text: 'OK', actionPerformed: {
                        // Hide this frame
                        trackInfoFrame.setVisible(false)
                        // Set track info stuff
                        trackInfo.trackName = trackNameField.text
                        trackInfo.startLineDirection = startDirBox.selectedItem
                        // Show next frame
                        trackPngFrame.setVisible(true)
                    })
                }
            }
        }
    }
    // Frame for getting PNG
    trackPngFrame = frame(
            title: "Track PNG | $titleSuffix",
            size: [505, 385],
            show: false,
            defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
            locationRelativeTo: null) {
        flowLayout()
        label(text: 'Track PNG:')
        JFileChooser pngFileChooser
        // FileChooser to select PNG
        pngFileChooser = fileChooser(
                fileFilter: [
                        getDescription: {'PNG file'},
                        accept: {
                            it ==~ /.+\.png/ || it.isDirectory()
                        }
                ] as FileFilter,
                currentDirectory: new File('.').absoluteFile,
                actionPerformed: {
                    // Hide this frame
                    trackPngFrame.setVisible(false)
                    // Store PNG file
                    pngFile = pngFileChooser.selectedFile
                    // Show next frame
                    trackDestFrame.setVisible(true)
                }
        )
    }
    // Frame for getting destination
    trackDestFrame = frame(
            title: "Destination | $titleSuffix",
            size: [505, 385],
            show: false,
            defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
            locationRelativeTo: null) {
        flowLayout()
        label(text: 'Destination:')
        JFileChooser destFileChooser
        // FileChooser to select destination
        destFileChooser = fileChooser(
                currentDirectory: new File('.').absoluteFile,
                actionPerformed: {
                    // Hide this frame
                    trackDestFrame.setVisible(false)
                    // Store dest file
                    destFile = destFileChooser.selectedFile

                    def png = new FileInputStream(pngFile)
                    def dest = new ObjectOutputStream(new FileOutputStream(destFile))
                    // Write track info
                    dest.writeInt(1)
                    dest.writeObject(trackInfo)
                    // Write PNG data
                    StreamUtils.copyStreams(png, dest)

                    // Exit by destroying frames
                    trackInfoFrame.dispose()
                    trackPngFrame.dispose()
                    trackDestFrame.dispose()
                }
        )
    }
    trackInfoFrame.setVisible(true)
}
