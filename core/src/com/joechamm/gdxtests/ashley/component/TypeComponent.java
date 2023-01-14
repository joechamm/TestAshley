package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    TypeComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 9:01 PM
 */
public class TypeComponent implements
                           Component,
                           Pool.Poolable {

    public static final int PLAYER_SHIP = 0;
    public static final int ENEMY_SHIP = 1;
    public static final int LASER = 2;
    public static final int SHIELD = 3;
    public static final int BACKGROUND_LAYER = 4;
    public static final int OTHER = 5;

    public int type = OTHER;

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        type = OTHER;
    }
}
