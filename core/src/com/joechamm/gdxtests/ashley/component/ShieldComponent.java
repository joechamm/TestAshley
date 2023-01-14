package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    ShieldComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 9:00 PM
 *
 * Some of our ships will have shields. The shields absorb damage the owner of the shield (the ship) would normally take.
 * Still need to make some decisions on things like recharge, radius, etc...
 * TODO
 */
public class ShieldComponent implements
                             Component,
                             Pool.Poolable {

    /** we set 'isDead' to true when the entity should be removed **/
    public boolean isDead = false;  // should we be disposing of this?
    /** current strength of the shield drops to 0, it should be removed **/
    public float strength = 0f;     // strength left of the shield // TODO: should we recharge
    /** radius of the shield in game units (meters)... not sure if this makes sense, but i always think of shields as circular like, so... **/
    public float radius = 1.0f;     // radius of the shield from the center of its owner
    public Entity owner;            // the ship/other that this shield is protecting

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        isDead = false;
        strength = 0f;
        radius = 1.0f;
        owner = null;
    }
}
