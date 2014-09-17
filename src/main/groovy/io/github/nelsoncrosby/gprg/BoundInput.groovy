package io.github.nelsoncrosby.gprg

import org.newdawn.slick.Input

/**
 *
 */
class BoundInput {
    private Input input
    private Map<Integer, Closure> bindings

    BoundInput(Input input, Map<String, Closure> actions) {
        this.input = input

        this.bindings = [:]
        Properties bindProp = new Properties()
        bindProp.load(getClass().getResourceAsStream('bindings.properties'))

        for (String action : bindProp.stringPropertyNames()) {
            int key = Input[bindProp[action] as String] as int
            bindings[key] = actions[action]
        }
    }

    void test(int delta) {
        for (int key : bindings.keySet()) {
            if (input.isKeyDown(key))
                bindings[key](delta)
        }
    }
}
