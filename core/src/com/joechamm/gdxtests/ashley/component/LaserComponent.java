package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    LaserComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 9:00 PM
 */
public class LaserComponent implements
                            Component,
                            Pool.Poolable {

    /** specify if this laser damages enemies, players, or none **/
    public static enum Owner {
        ENEMY,
        PLAYER,
        NONE
    }

    /** we set 'isDead' to true when we want to remove the entity **/
    // TODO: check for overlap with entities having more than one component with 'isDead'
    public boolean isDead = false;      // should we dispose of this? set to true when laser hits shield/ship or goes off screen
    /** movementSpeed of the laser in game units (meters) **/
    public float movementSpeed = 0f;    // how fast does this concentrated beam of light travel?
    /** damage done by the laser to the entity (ship/shield) it hits **/
    public float damage = 0f;           // how much damage will this laser do?
    public Owner owner = Owner.NONE;    // is this a player laser, or enemy laser?

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        isDead = false;
        owner = Owner.NONE;
        movementSpeed = 0f;
        damage = 0f;
    }
}
