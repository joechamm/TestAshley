package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    EnemyComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 8:59 PM
 */
public class EnemyComponent implements
                            Component,
                            Pool.Poolable {

    /** use an enum for the type of entity this is **/
    public static enum Type {
        ENEMY_SHIP_RANDOM_MOVER,
        ENEMY_SHIP_NONE
    }

    /** should we remove this entity **/
    public boolean isDead = false;
    /** timer for updating the random direction, and the frequency to update **/
    // TODO: move this to its own component/system
    public float timeSinceLastDirectionChange = 0f;
    public float directionChangeFrequency = 0.75f;
    public Type enemyType = Type.ENEMY_SHIP_NONE;

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        isDead = false;
        timeSinceLastDirectionChange = 0f;
        directionChangeFrequency = 0.75f;
        enemyType = Type.ENEMY_SHIP_NONE;
    }
}
