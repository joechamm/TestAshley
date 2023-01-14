package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    MovementComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 11:24 PM
 */
public class MovementComponent implements
                               Component,
                               Pool.Poolable {

    /** velocity in game units (meters per second) of the entity **/
    public final Vector2 velocityMeters = new Vector2 ();
    /** acceleration in game units (meters per second per second) of the entity **/
    public final Vector2 accelerationMeters = new Vector2 ();

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        velocityMeters.set ( 0f, 0f );
        accelerationMeters.set ( 0f, 0f );
    }
}
