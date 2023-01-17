package com.joechamm.gdxtests.ashley.system;

import static com.joechamm.gdxtests.ashley.Constants.*;
import static com.joechamm.gdxtests.ashley.Constants.ENEMY_LASER_IMAGE_HEIGHT_PIXELS;
import static com.joechamm.gdxtests.ashley.Constants.ENEMY_LASER_IMAGE_WIDTH_PIXELS;
import static com.joechamm.gdxtests.ashley.Constants.ENEMY_LASER_SCALE;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.joechamm.gdxtests.ashley.Constants;
import com.joechamm.gdxtests.ashley.asset.AssetNames;
import com.joechamm.gdxtests.ashley.asset.JCGdxAssetManager;
import com.joechamm.gdxtests.ashley.component.BoundaryComponent;
import com.joechamm.gdxtests.ashley.component.LaserCannonComponent;
import com.joechamm.gdxtests.ashley.component.LaserComponent;
import com.joechamm.gdxtests.ashley.component.Mappers;
import com.joechamm.gdxtests.ashley.component.ShipComponent;
import com.joechamm.gdxtests.ashley.component.StateComponent;
import com.joechamm.gdxtests.ashley.component.TransformComponent;
import com.joechamm.gdxtests.ashley.component.TypeComponent;
import com.joechamm.gdxtests.ashley.entity.EntityFactory;

/**
 * File:    LaserCannonSystem
 * Package: com.joechamm.gdxtests.ashley.system
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/16/2023 at 2:46 PM
 *
 * system to spawn lasers when requested
 */
public class LaserCannonSystem extends IteratingSystem {

    public static final String TAG = LaserCannonSystem.class.getName ();

    private EntityFactory entityFactory;
    private JCGdxAssetManager assetManager;

    // scratch variables
    private final Vector2 spawnPixelsPivotVec = new Vector2 ();
    private final Vector2 leftSpawnPixelsVec = new Vector2 ();
    private final Vector2 rightSpawnPixelsVec = new Vector2 ();

    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family The family of entities iterated over in this System
     */
    public LaserCannonSystem ( Family family,
                               EntityFactory ef,
                               JCGdxAssetManager assetManager) {
        super ( family );
        this.entityFactory = ef;
        this.assetManager = assetManager;
    }

    public LaserCannonSystem(EntityFactory ef, JCGdxAssetManager assetManager) {
        this ( Family.all ( LaserCannonComponent.class ).get (), ef, assetManager );
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

        if ( Mappers.laserCannonCM.get ( entity ).justFired ) {
            fireLaserCannon ( entity, deltaTime );
        }
    }

    private void fireLaserCannon ( Entity entity, float deltaTime ) {
        TypeComponent typeComponent = Mappers.typeCM.get ( entity );

        switch ( typeComponent.type ) {
            case TypeComponent.PLAYER_SHIP:
                handlePlayerShip ( entity, deltaTime );
                break;
            case TypeComponent.ENEMY_SHIP:
                handleEnemyShip ( entity, deltaTime );
                break;
            default:
                break;
        }

    }

    private void handleEnemyShip ( Entity entity, float time ) {
        LaserCannonComponent laserCannonComponent = Mappers.laserCannonCM.get ( entity );
        BoundaryComponent boundaryComponent = Mappers.boundaryCM.get ( entity );
        StateComponent stateComponent = Mappers.stateCM.get ( entity );
        TransformComponent transformComponent = Mappers.transformCM.get ( entity );

        // set the pivot point to the location of the ship
        spawnPixelsPivotVec.set ( transformComponent.screenPositionPixels.x, transformComponent.screenPositionPixels.y );
        // set the left and right spawn points, then rotate them about the pivot to match the orientation of the ship
        leftSpawnPixelsVec.set (
                transformComponent.screenPositionPixels.x + ENEMY_LASER_SPAWN_BOUND_OFFSET_X_PCT * boundaryComponent.boundingBoxPixels.width,
                transformComponent.screenPositionPixels.y + ENEMY_LASER_SPAWN_BOUND_OFFSET_Y_PCT * boundaryComponent.boundingBoxPixels.height );

        rightSpawnPixelsVec.set (
                transformComponent.screenPositionPixels.x + ( 1.0f - ENEMY_LASER_SPAWN_BOUND_OFFSET_X_PCT ) * boundaryComponent.boundingBoxPixels.width,
                transformComponent.screenPositionPixels.y + ENEMY_LASER_SPAWN_BOUND_OFFSET_Y_PCT * boundaryComponent.boundingBoxPixels.height );

        leftSpawnPixelsVec.rotateAroundDeg ( spawnPixelsPivotVec, transformComponent.rotation );
        rightSpawnPixelsVec.rotateAroundDeg ( spawnPixelsPivotVec, transformComponent.rotation );

        float laserWidthPixels = ENEMY_LASER_IMAGE_WIDTH_PIXELS * ENEMY_LASER_SCALE;
        float laserHeightPixels = ENEMY_LASER_IMAGE_HEIGHT_PIXELS * ENEMY_LASER_SCALE;
        // grab the laser texture
        TextureRegion laserTex = assetManager.manager.get ( AssetNames.ENEMY_LASER, TextureRegion.class );
        // spawn the lasers
        entityFactory.createLaser ( leftSpawnPixelsVec.x, leftSpawnPixelsVec.y, laserWidthPixels, laserHeightPixels,
                                    LaserComponent.Owner.ENEMY, laserCannonComponent, laserTex );

        entityFactory.createLaser ( rightSpawnPixelsVec.x, rightSpawnPixelsVec.y, laserWidthPixels, laserHeightPixels,
                                    LaserComponent.Owner.ENEMY, laserCannonComponent, laserTex );
        // reset the laser cannon
        laserCannonComponent.justFired = false;
        laserCannonComponent.timeSinceLastShot = 0f;

        // set the ship state to normal
        stateComponent.set ( StateComponent.STATE_NORMAL );
    }

    private void handlePlayerShip ( Entity entity, float time ) {
        LaserCannonComponent laserCannonComponent = Mappers.laserCannonCM.get ( entity );
        BoundaryComponent boundaryComponent = Mappers.boundaryCM.get ( entity );
        StateComponent stateComponent = Mappers.stateCM.get ( entity );
        TransformComponent transformComponent = Mappers.transformCM.get ( entity );

        // set the pivot point to the location of the ship
        spawnPixelsPivotVec.set ( transformComponent.screenPositionPixels.x, transformComponent.screenPositionPixels.y );
        // set the left and right spawn points, then rotate them about the pivot to match the orientation of the ship
        leftSpawnPixelsVec.set (
                transformComponent.screenPositionPixels.x + PLAYER_LASER_SPAWN_BOUND_OFFSET_X_PCT * boundaryComponent.boundingBoxPixels.width,
                transformComponent.screenPositionPixels.y + PLAYER_LASER_SPAWN_BOUND_OFFSET_Y_PCT * boundaryComponent.boundingBoxPixels.height );

        rightSpawnPixelsVec.set (
                transformComponent.screenPositionPixels.x + ( 1.0f - PLAYER_LASER_SPAWN_BOUND_OFFSET_X_PCT ) * boundaryComponent.boundingBoxPixels.width,
                transformComponent.screenPositionPixels.y + PLAYER_LASER_SPAWN_BOUND_OFFSET_Y_PCT * boundaryComponent.boundingBoxPixels.height );

        leftSpawnPixelsVec.rotateAroundDeg ( spawnPixelsPivotVec, transformComponent.rotation );
        rightSpawnPixelsVec.rotateAroundDeg ( spawnPixelsPivotVec, transformComponent.rotation );

        float laserWidthPixels = PLAYER_LASER_IMAGE_WIDTH_PIXELS * PLAYER_LASER_SCALE;
        float laserHeightPixels = PLAYER_LASER_IMAGE_HEIGHT_PIXELS * PLAYER_LASER_SCALE;
        // grab the laser texture
        TextureRegion laserTex = assetManager.manager.get ( AssetNames.PLAYER_LASER, TextureRegion.class );
        // spawn the lasers
        entityFactory.createLaser ( leftSpawnPixelsVec.x, leftSpawnPixelsVec.y, laserWidthPixels, laserHeightPixels,
                                         LaserComponent.Owner.PLAYER, laserCannonComponent, laserTex );

        entityFactory.createLaser ( rightSpawnPixelsVec.x, rightSpawnPixelsVec.y, laserWidthPixels, laserHeightPixels,
                                    LaserComponent.Owner.PLAYER, laserCannonComponent, laserTex );
        // reset the laser cannon
        laserCannonComponent.justFired = false;
        laserCannonComponent.timeSinceLastShot = 0f;

        // set the ship state to normal
        stateComponent.set ( StateComponent.STATE_NORMAL );
    }
}
