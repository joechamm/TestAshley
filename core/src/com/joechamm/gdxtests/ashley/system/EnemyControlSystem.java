package com.joechamm.gdxtests.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.joechamm.gdxtests.ashley.component.EnemyComponent;
import com.joechamm.gdxtests.ashley.component.LaserCannonComponent;
import com.joechamm.gdxtests.ashley.component.Mappers;
import com.joechamm.gdxtests.ashley.component.MovementComponent;
import com.joechamm.gdxtests.ashley.component.PlayerComponent;
import com.joechamm.gdxtests.ashley.component.ShipComponent;
import com.joechamm.gdxtests.ashley.component.StateComponent;
import com.joechamm.gdxtests.ashley.component.TransformComponent;

/**
 * File:    EnemyControlSystem
 * Package: com.joechamm.gdxtests.ashley.system
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/12/2023 at 10:04 AM
 */
public class EnemyControlSystem extends IteratingSystem {
    public static final String TAG = EnemyControlSystem.class.getName ();

    /** how accurate are the enemies shooting? **/
    private float enemyAimRangeRads = MathUtils.HALF_PI;

    /** scratch variable **/
    private final Vector2 scratchVec2 = new Vector2 ();

    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family The family of entities iterated over in this System
     */
    EnemyControlSystem ( Family family ) {
        super ( family );
    }

    public EnemyControlSystem() {
        this ( Family.all ( EnemyComponent.class ).get () );
    }

    /**
     * This method is called on every entity on every update call of the EntitySystem. Override this to implement your system's
     * specific processing.
     *
     * @param entity    The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity ( Entity entity, float deltaTime ) {

        EnemyComponent enemyComponent = Mappers.enemyCM.get ( entity );
        if ( enemyComponent.isDead ) {
            // nothing to do but remove the enemy
            getEngine ().removeEntity ( entity );
            return;
        }

        switch ( enemyComponent.enemyType ) {
            case ENEMY_SHIP_RANDOM_MOVER:
                handleRandomMover ( entity, deltaTime );
                break;
            case ENEMY_SHIP_NONE:
                break;
            default:
                break;
        }
    }

    private void handleRandomMover(Entity entity, float deltaTime) {
        EnemyComponent enemyComponent = Mappers.enemyCM.get ( entity );
        LaserCannonComponent laserCannonComponent = Mappers.laserCannonCM.get ( entity );

        enemyComponent.timeSinceLastDirectionChange += deltaTime;
        laserCannonComponent.timeSinceLastShot += deltaTime;
        if ( enemyComponent.timeSinceLastDirectionChange > enemyComponent.directionChangeFrequency ) {
            // get the ship component to determine the max speed of the ship
            ShipComponent shipComponent = Mappers.shipCM.get ( entity );
            // get the movement component so we can set a new direction
            MovementComponent movementComponent = Mappers.movementCM.get ( entity );

            movementComponent.velocityMeters.setToRandomDirection ().scl ( shipComponent.maxSpeed );

            enemyComponent.timeSinceLastDirectionChange -= enemyComponent.directionChangeFrequency;
        }

        // should we fire?
        if ( laserCannonComponent.timeSinceLastShot > laserCannonComponent.laserRechargeTime ) {
            // hopefully we just have one of these right now
            Entity playerShip = getEngine ().getEntitiesFor ( Family.all( PlayerComponent.class ).get () ).first ();

            TransformComponent playerShipTransform = Mappers.transformCM.get ( playerShip );
            TransformComponent enemyShipTransform = Mappers.transformCM.get ( entity );
            StateComponent stateComponent = Mappers.stateCM.get(entity);

            stateComponent.set ( StateComponent.STATE_SHOOTING );

            // get the difference vector enemy to player
            scratchVec2.set ( playerShipTransform.screenPositionPixels.x - enemyShipTransform.screenPositionPixels.x,
                              playerShipTransform.screenPositionPixels.y - enemyShipTransform.screenPositionPixels.y );

            laserCannonComponent.headingRads = getEnemyAimRads ( scratchVec2.angleRad () );
            laserCannonComponent.justFired = true;

        }
    }

    private float getEnemyAimRads ( float enemyToPlayerHeading ) {

        // the enemy's aim is bad at first so we get a random heading offset from the true direction
        float aimRads = MathUtils.randomTriangular ( enemyToPlayerHeading - enemyAimRangeRads, enemyToPlayerHeading + enemyAimRangeRads,
                                                     enemyToPlayerHeading );
        // now that the enemy has fired though, let's make that random range a little smaller // TODO: find a better way to aim here
        enemyAimRangeRads *= 0.99f;

        return aimRads;
    }
}
