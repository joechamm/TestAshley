package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    LaserCannonComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/11/2023 at 2:33 PM
 *
 * The laser cannon component is what tells the entity what type of laser it fires... not sure how well this will work
 */
public class LaserCannonComponent implements
                                  Component,
                                  Pool.Poolable {

    /** how long to wait in seconds before cannon can fire another laser **/
    public float laserRechargeTime = 0f;
    public float timeSinceLastShot = 0f;

    /** laser movement speed in pixels **/
    public float laserMovementSpeed = 0f;   // how fast do its lasers move (apparently not 299,792,458 m/s in this game, lol)
    /** entities that collide with the laser will lose the damage amount from their shield strength/hull points **/
    public float laserDamage = 0f;

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        laserRechargeTime = 0f;
        timeSinceLastShot = 0f;
        laserMovementSpeed = 0f;
        laserDamage = 0f;
    }
}
