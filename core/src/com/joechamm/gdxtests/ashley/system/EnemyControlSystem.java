package com.joechamm.gdxtests.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.joechamm.gdxtests.ashley.component.EnemyComponent;
import com.joechamm.gdxtests.ashley.component.Mappers;
import com.joechamm.gdxtests.ashley.component.MovementComponent;
import com.joechamm.gdxtests.ashley.component.ShipComponent;

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

        EnemyComponent enemyComponent = Mappers.enemCM.get ( entity );
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
        EnemyComponent enemyComponent = Mappers.enemCM.get ( entity );

        enemyComponent.timeSinceLastDirectionChange += deltaTime;
        if ( enemyComponent.timeSinceLastDirectionChange > enemyComponent.directionChangeFrequency ) {
            // get the ship component to determine the max speed of the ship
            ShipComponent shipComponent = Mappers.shipCM.get ( entity );
            // get the movement component so we can set a new direction
            MovementComponent movementComponent = Mappers.moveCM.get ( entity );

            movementComponent.velocityMeters.setToRandomDirection ().scl ( shipComponent.maxSpeed );

            enemyComponent.timeSinceLastDirectionChange -= enemyComponent.directionChangeFrequency;
        }
    }
}
