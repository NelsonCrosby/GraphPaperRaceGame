package io.github.nelsoncrosby.gprg

import org.newdawn.slick.Input

/**
 * Class for simplifying the way input works
 *
 * @author Nelson Crosby (github/NelsonCrosby)
 */
class BoundInput {
    private Input input
    private Map<Integer, Closure> keyBindings

    /**
     * @param input Input context
     * @param actions Actions to be made available to properties file
     *
     * @author Nelson Crosby
     */
    BoundInput(Input input, Map<String, Closure> actions) {
        this.input = input

        this.keyBindings = [:]
        Properties bindProp = new Properties()
        bindProp.load(getClass().getResourceAsStream('bindings.properties'))

        for (String action : bindProp.stringPropertyNames()) {
            int key = Input[bindProp[action] as String] as int
            keyBindings[key] = actions[action]
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
        for (int key : keyBindings.keySet()) {
            if (input.isKeyDown(key))
                keyBindings[key](delta)
        }
    }
}
