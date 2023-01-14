package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    ShipComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 9:55 PM
 */
public class ShipComponent implements
                           Component,
                           Pool.Poolable {

    public static enum Type {
        PLAYER_SHIP,
        ENEMY_SHIP,
        NONE
    }


    public boolean isDead = false;      // should we be disposing of this?
    public float hullPoints = 0f;       // how much damage can this ship take without shields?
    /** maximum speed of the ship in game units (m / s)... might just need maxSpeedSquared since it makes more sense in the update calculation **/
    public float maxSpeed = 0f;         // what's the maximum speed of movement
    public Type shipType = Type.NONE;   // what type of ship is this? friend or foe?
    public Entity shield;               // the ship's shield... // TODO: probably need better implementation here

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        isDead = false;
        hullPoints = 0f;
        maxSpeed = 0f;
        shipType = Type.NONE;
        shield = null;
    }
}
