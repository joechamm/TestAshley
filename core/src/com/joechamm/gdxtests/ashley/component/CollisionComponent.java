package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    CollisionComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 8:59 PM
 */
public class CollisionComponent implements
                                Component,
                                Pool.Poolable {
    /** reference to the other entity **/
    public Entity collisionEntity = null;


    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        collisionEntity = null;
    }
}
