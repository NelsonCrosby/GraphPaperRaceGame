package io.github.nelsoncrosby.gprg

import groovy.util.logging.Log
import org.newdawn.slick.Input
import org.newdawn.slick.KeyListener

/**
 * Class for simplifying the way input works
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 */
@Log
class BoundInput implements KeyListener {
    private Input input
    private Map<Integer, Closure> pollBindings
    private Map<Integer, Closure> eventBindings

    /**
     * @param input Input context
     * @param pollActions Actions to be made available to properties file
     *
     * @author Nelson Crosby
     */
    BoundInput(Input input,
               Map<String, Closure> pollActions,
               Map<String, Closure> eventActions) {
        this.input = input
        input.addKeyListener(this)

        this.pollBindings = [:]
        this.eventBindings = [:]
        Properties bindProp = new Properties()

        String propertyName = 'io.github.nelsoncrosby.gprg.inputBindings'
        if (System.hasProperty(propertyName))
            bindProp.load(new FileReader(propertyName))
        else
            bindProp.load(getClass().getResourceAsStream('input.properties'))

        for (String action : bindProp.stringPropertyNames()) {
            log.config "Keybind $action for ${bindProp[action]}"
            int key = Input[bindProp[action] as String] as int
            if (pollActions.containsKey(key))
                pollBindings[key] = pollActions[action]
            else if (eventActions.containsKey(key))
                eventBindings[key] = eventActions[action]
        }
    }

    /**
     * Test for currently active inputs
     *
     * @param delta Time since last update (sent to binding)
     *
     * @author Nelson Crosby
     */
    void test(int delta) {
        for (int key : pollBindings.keySet()) {
            if (input.isKeyDown(key))
                pollBindings[key](delta)
        }
    }

    /**
     * Notification that a key was pressed
     *
     * @param key The key code that was pressed (@see org.newdawn.slick.Input)
     * @param c The character of the key that was pressed
     *
     * @author Nelson Crosby
     */
    @Override
    void keyPressed(int key, char c) {
        if (eventBindings.containsKey(key)) {
            eventBindings[key]()
        }
    }

    /**
     * Notification that a key was released
     *
     * @param key The key code that was released (@see org.newdawn.slick.Input)
     * @param c The character of the key that was released
     */
    @Override
    void keyReleased(int key, char c) {}

    /**
     * Set the input that events are being sent from
     *
     * @param input The input instance sending events
     */
    @Override
    void setInput(Input input) {}

    /**
     * Check if this input listener is accepting input
     *
     * @return True if the input listener should recieve events
     */
    @Override
    boolean isAcceptingInput() {
        return false
    }

    /**
     * Notification that all input events have been sent for this frame
     */
    @Override
    void inputEnded() {}

    /**
     * Notification that input is about to be processed
     */
    @Override
    void inputStarted() {}
}
