package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    StateComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 9:00 PM
 *
 * The StateComponent is used for tracking the state of an entity for the purposes of animimation/effects
 */
public class StateComponent implements
                            Component,
                            Pool.Poolable {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_SHOOTING = 1;
    public static final int STATE_HIT = 2;
    public static final int STATE_EXPLODING = 3;
    public static final int STATE_DEAD = 4;

    private int currentState = STATE_NORMAL;
    public float time = 0.0f;
    public boolean isLooping = true;

    public void set(int newState) {
        currentState = newState;
        time = 0.0f;
    }

    public int get() {
        return currentState;
    }

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        currentState = STATE_NORMAL;
        time = 0.0f;
        isLooping = true;
    }
}
