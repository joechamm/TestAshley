package com.joechamm.gdxtests.ashley.system;

import static com.joechamm.gdxtests.ashley.Constants.PIXELS_PER_METER;
import static com.joechamm.gdxtests.ashley.Constants.PIXELS_TO_METERS;
import static com.joechamm.gdxtests.ashley.Constants.WORLD_HEIGHT_METERS;
import static com.joechamm.gdxtests.ashley.Constants.WORLD_WIDTH_METERS;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.joechamm.gdxtests.ashley.component.BoundaryComponent;
import com.joechamm.gdxtests.ashley.component.CollisionComponent;
import com.joechamm.gdxtests.ashley.component.EnemyOwnedComponent;
import com.joechamm.gdxtests.ashley.component.LaserComponent;
import com.joechamm.gdxtests.ashley.component.Mappers;
import com.joechamm.gdxtests.ashley.component.MovementComponent;
import com.joechamm.gdxtests.ashley.component.PlayerOwnedComponent;
import com.joechamm.gdxtests.ashley.component.ShieldComponent;
import com.joechamm.gdxtests.ashley.component.ShipComponent;
import com.joechamm.gdxtests.ashley.component.StateComponent;
import com.joechamm.gdxtests.ashley.component.TakesLaserDamageComponent;
import com.joechamm.gdxtests.ashley.component.TransformComponent;
import com.joechamm.gdxtests.ashley.component.TypeComponent;

/**
 * File:    LaserSystem
 * Package: com.joechamm.gdxtests.ashley.system
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/13/2023 at 5:33 PM
 */
public class LaserSystem extends IteratingSystem {

    public static final String TAG = LaserSystem.class.getName ();

    /** hold the boundary for our world in game units (meters) here **/
    private final Rectangle worldBoundsMeters = new Rectangle ();
    /** hold the boundary for our world in screen units (pixels) here **/
    private final Rectangle worldBoundsPixels = new Rectangle ();

    public LaserSystem() {
        this ( Family.all ( LaserComponent.class ).get () );
    }

    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family The family of entities iterated over in this System
     */
    LaserSystem ( Family family ) {
        super ( family );
        updateWorldBounds ();
    }

    private void updateWorldBounds() {
        worldBoundsMeters.set ( 0f, 0f, WORLD_WIDTH_METERS, WORLD_HEIGHT_METERS );
        worldBoundsPixels.set ( 0f, 0f, WORLD_WIDTH_METERS * PIXELS_PER_METER, WORLD_HEIGHT_METERS * PIXELS_PER_METER );
    }

    /**
     * convenience function for converting screen units to game units
     * @param px pixels
     * @return meters
     */
    private float pixelsToMeters(float px) {
        return px * PIXELS_TO_METERS;
    }

    /**
     * convenience function for converting game units to screen units
     * @param x meters
     * @return pixels
     */
    private float metersToPixels(float x) {
        return x * PIXELS_PER_METER;
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

        BoundaryComponent thisBoundary = Mappers.bounCM.get ( entity );
        LaserComponent laser = Mappers.laseCM.get ( entity );
        StateComponent thisState = Mappers.statCM.get ( entity );

        // first check if this laser is offscreen
        if ( !worldBoundsPixels.overlaps ( thisBoundary.boundingBoxPixels ) ) {
            // this laser needs to be removed
            getEngine ().removeEntity ( entity );
            return;
        }

        // check if this laser is dead
        if ( laser.isDead ) {
            // remove all lasers that have collided
            getEngine ().removeEntity ( entity );
            return;
        }

        // check if this laser was just fired
        if ( thisState.get () == StateComponent.STATE_SHOOTING ) {
            // change the state and return
            thisState.set ( StateComponent.STATE_NORMAL );
            return;
        }

        if ( laser.owner == LaserComponent.Owner.ENEMY ) {
            handleEnemyLaser ( entity, deltaTime );
        } else if ( laser.owner == LaserComponent.Owner.PLAYER ) {
            handlePlayerLaser ( entity, deltaTime );
        }
    }

    private void handleEnemyLaser(Entity enemyLaserEntity, float deltaTime) {
        BoundaryComponent thisBoundary = Mappers.bounCM.get ( enemyLaserEntity );
        LaserComponent laser = Mappers.laseCM.get ( enemyLaserEntity );

        // we want to check all the entities that take laser damage, (maybe don't need this) is a ship or shield, and is not owned by an enemy
        ImmutableArray<Entity> playerOwnedEntities = getEngine ().getEntitiesFor ( Family.all ( TakesLaserDamageComponent.class ).one (
                ShipComponent.class, ShieldComponent.class ).exclude ( EnemyOwnedComponent.class ).get () );

        for ( Entity otherEntity : playerOwnedEntities ) {
            // first check for collision
            BoundaryComponent otherBoundary = Mappers.bounCM.get ( otherEntity );
            if ( thisBoundary.boundingBoxPixels.overlaps ( otherBoundary.boundingBoxPixels ) ) {
                // did we hit a shield or ship?
                TypeComponent typeComponent = Mappers.typeCM.get ( otherEntity );
                if ( typeComponent.type == TypeComponent.SHIELD ) {
                    ShieldComponent otherShield = Mappers.shieCM.get ( otherEntity );
                    if ( otherShield != null &&
                            !otherShield.isDead ) {
                        // TODO: use radius a little better here
                        otherShield.strength -= laser.damage;
                        if ( otherShield.strength <= 0 ) {
                            /** remove the shield **/
                            // set the shield to dead
                            otherShield.isDead = true;
                            // set the owner's shield to null // TODO: need a better approach here
                            Mappers.shipCM.get ( otherShield.owner ).shield = null;
                            // add the 'TakesLaserDamageComponent' to the shield's owner since the shields went down
                            otherShield.owner.add ( getEngine ().createComponent ( TakesLaserDamageComponent.class ) );
                            // TODO: play the shields down sound
                            // remove the shield component from the ship
                            otherShield.owner.remove ( ShieldComponent.class );
                            // remove the shield from the engine and return
                            getEngine ().removeEntity ( otherEntity );
                        }
                    }
                } else if ( typeComponent.type == TypeComponent.PLAYER_SHIP ) {
                    ShipComponent otherShip = Mappers.shipCM.get ( otherEntity );
                    otherShip.hullPoints -= laser.damage;
                    if ( otherShip.hullPoints <= 0 ) {
                        /** ship is dead **/
                        otherShip.isDead = true;
                    }
                }

                laser.isDead = false;
                getEngine ().removeEntity ( enemyLaserEntity );
                return;
            }
        }
    }

    private void handlePlayerLaser(Entity playerLaserEntity, float deltaTime) {
        BoundaryComponent thisBoundary = Mappers.bounCM.get ( playerLaserEntity );
        LaserComponent laser = Mappers.laseCM.get ( playerLaserEntity );

        // we want to check all the entities that take laser damage, (maybe don't need this) is a ship or shield, and is not owned by an enemy
        ImmutableArray<Entity> enemyOwnedEntities = getEngine ().getEntitiesFor ( Family.all ( TakesLaserDamageComponent.class ).one (
                ShipComponent.class, ShieldComponent.class ).exclude ( PlayerOwnedComponent.class ).get () );

        for ( Entity otherEntity : enemyOwnedEntities ) {
            // first check for collision
            BoundaryComponent otherBoundary = Mappers.bounCM.get ( otherEntity );
            if ( thisBoundary.boundingBoxPixels.overlaps ( otherBoundary.boundingBoxPixels ) ) {
                // did we hit a shield or ship?
                TypeComponent typeComponent = Mappers.typeCM.get ( otherEntity );
                if ( typeComponent.type == TypeComponent.SHIELD ) {
                    ShieldComponent otherShield = Mappers.shieCM.get ( otherEntity );
                    if ( otherShield != null &&
                            !otherShield.isDead ) {
                        // TODO: use radius a little better here
                        otherShield.strength -= laser.damage;
                        if ( otherShield.strength <= 0 ) {
                            /** remove the shield **/
                            // set the shield to dead
                            otherShield.isDead = true;
                            // set the owner's shield to null // TODO: need a better approach here
                            Mappers.shipCM.get ( otherShield.owner ).shield = null;
                            // add the 'TakesLaserDamageComponent' to the shield's owner since the shields went down
                            otherShield.owner.add ( getEngine ().createComponent ( TakesLaserDamageComponent.class ) );
                            // TODO: play the shields down sound
                            // remove the shield component from the ship
                            otherShield.owner.remove ( ShieldComponent.class );
                            // remove the shield from the engine and return
                            getEngine ().removeEntity ( otherEntity );
                        }
                    }
                } else if ( typeComponent.type == TypeComponent.ENEMY_SHIP ) {
                    ShipComponent otherShip = Mappers.shipCM.get ( otherEntity );
                    otherShip.hullPoints -= laser.damage;
                    if ( otherShip.hullPoints <= 0 ) {
                        /** ship is dead **/
                        otherShip.isDead = true;
                        // remove the enemy ship
                        getEngine ().removeEntity ( otherEntity );
                    }
                }

                laser.isDead = false;
                getEngine ().removeEntity ( playerLaserEntity );
                return;
            }
        }
    }
}
