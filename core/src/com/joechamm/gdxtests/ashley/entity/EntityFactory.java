package com.joechamm.gdxtests.ashley.entity;

import static com.badlogic.gdx.math.MathUtils.radiansToDegrees;
import static com.joechamm.gdxtests.ashley.Constants.PIXELS_PER_METER;
import static com.joechamm.gdxtests.ashley.Constants.PIXELS_TO_METERS;
import static com.joechamm.gdxtests.ashley.Constants.PLAYER_STARTING_LIVES;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.joechamm.gdxtests.ashley.component.AnimationComponent;
import com.joechamm.gdxtests.ashley.component.BackgroundComponent;
import com.joechamm.gdxtests.ashley.component.BoundaryComponent;
import com.joechamm.gdxtests.ashley.component.CollisionComponent;
import com.joechamm.gdxtests.ashley.component.EnemyComponent;
import com.joechamm.gdxtests.ashley.component.LaserCannonComponent;
import com.joechamm.gdxtests.ashley.component.LaserComponent;
import com.joechamm.gdxtests.ashley.component.MovementComponent;
import com.joechamm.gdxtests.ashley.component.PlayerComponent;
import com.joechamm.gdxtests.ashley.component.ShieldComponent;
import com.joechamm.gdxtests.ashley.component.ShipComponent;
import com.joechamm.gdxtests.ashley.component.StateComponent;
import com.joechamm.gdxtests.ashley.component.TextureComponent;
import com.joechamm.gdxtests.ashley.component.TransformComponent;
import com.joechamm.gdxtests.ashley.component.TypeComponent;

/**
 * File:    EntityFactory
 * Package: com.joechamm.gdxtests.ashley.entity
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/14/2023 at 12:30 PM
 */
public class EntityFactory {

    // ECS ENGINE
    private PooledEngine engine;

    public EntityFactory ( PooledEngine engine ) {
        this.engine = engine;
    }

    public Entity createBackground( float widthPixels, float heightPixels,
                                    float scrollSpeedPixels, float zIndex,
                                    TextureRegion bgTexture) {

        Entity bgEntity = engine.createEntity ();

        BackgroundComponent bgCom = engine.createComponent ( BackgroundComponent.class );
        TransformComponent xfrmCom = engine.createComponent ( TransformComponent.class );
        TextureComponent texCom = engine.createComponent ( TextureComponent.class );
        TypeComponent typeCom = engine.createComponent ( TypeComponent.class );

        bgCom.bgPixelsScrollSpeed = scrollSpeedPixels;
        bgCom.bgPixelsOffsetY = 0f;

        // start at lower left corner of screen
        xfrmCom.screenPositionPixels.set ( 0f, 0f, zIndex );
        // scale the transform by width / texWidth, height / texHeight where width/height are the screen dimensions in pixels
        xfrmCom.scalePixels.x = widthPixels / bgTexture.getRegionWidth ();
        xfrmCom.scalePixels.y = heightPixels / bgTexture.getRegionHeight ();

        texCom.region = bgTexture;

        typeCom.type = TypeComponent.BACKGROUND_LAYER;

        // bgEntity.flags = 0; // TODO

        bgEntity.add ( bgCom )
                .add ( xfrmCom )
                .add ( texCom )
                .add ( typeCom );

        engine.addEntity ( bgEntity );

        return bgEntity;
    }

    public Entity createLaser ( float spawnPixelsX, float spawnPixelsY, float spawnHeadingRads,
                                float laserWidthPixels, float laserHeightPixels,
                                LaserComponent.Owner ownerType, LaserCannonComponent laserCannon,
                                TextureRegion laserTex ) {

        Entity laserEntity = engine.createEntity ();

        BoundaryComponent boundaryComponent = engine.createComponent ( BoundaryComponent.class );
        CollisionComponent collisionComponent = engine.createComponent ( CollisionComponent.class );
        LaserComponent laserComponent = engine.createComponent ( LaserComponent.class );
        MovementComponent movementComponent = engine.createComponent ( MovementComponent.class );
        StateComponent stateComponent = engine.createComponent ( StateComponent.class );
        TextureComponent textureComponent = engine.createComponent ( TextureComponent.class );
        TransformComponent transformComponent = engine.createComponent ( TransformComponent.class );
        TypeComponent typeComponent = engine.createComponent ( TypeComponent.class );

        boundaryComponent.boundingBoxPixels.setSize ( laserWidthPixels, laserHeightPixels );

        laserComponent.movementSpeed = laserCannon.laserMovementSpeed;
        laserComponent.damage = laserCannon.laserDamage;
        laserComponent.owner = ownerType;

        movementComponent.velocityMeters.setAngleRad ( spawnHeadingRads );
        movementComponent.velocityMeters.scl ( pixelsToMeters ( laserCannon.laserMovementSpeed ) );

        stateComponent.set ( StateComponent.STATE_SHOOTING );

        transformComponent.screenPositionPixels.set ( spawnPixelsX, spawnPixelsY, 0f );
        transformComponent.scalePixels.x = laserWidthPixels / laserTex.getRegionWidth ();
        transformComponent.scalePixels.y = laserHeightPixels / laserTex.getRegionHeight ();
        transformComponent.rotation = spawnHeadingRads * radiansToDegrees;

        typeComponent.type = TypeComponent.LASER;

        laserEntity.add ( boundaryComponent )
                   .add ( collisionComponent )
                   .add ( laserComponent )
                   .add ( movementComponent )
                   .add ( stateComponent )
                   .add ( textureComponent )
                   .add ( transformComponent )
                   .add ( typeComponent );

        engine.addEntity ( laserEntity );

        return laserEntity;
    }

    public Entity createShield ( Entity shieldOwner, float shieldStrength, float shieldRadiusPixels,
                                  float offsetPixelsX, float offsetPixelsY, TextureRegion shieldTex ) {
        Entity shieldEntity = engine.createEntity ();

        BoundaryComponent boundaryComponent = engine.createComponent ( BoundaryComponent.class );
        ShieldComponent shieldComponent = engine.createComponent ( ShieldComponent.class );
        StateComponent stateComponent = engine.createComponent ( StateComponent.class );
        TextureComponent textureComponent = engine.createComponent ( TextureComponent.class );
        TransformComponent transformComponent = engine.createComponent ( TransformComponent.class );
        TypeComponent typeComponent = engine.createComponent ( TypeComponent.class );

        boundaryComponent.boundingBoxPixels.setSize ( shieldRadiusPixels * 2f );

        shieldComponent.strength = shieldStrength;
        shieldComponent.radius = pixelsToMeters ( shieldRadiusPixels );
        shieldComponent.owner = shieldOwner;

        stateComponent.set ( StateComponent.STATE_NORMAL );

        textureComponent.region = shieldTex;
        textureComponent.offsetX = offsetPixelsX;
        textureComponent.offsetY = offsetPixelsY;

        typeComponent.type = TypeComponent.SHIELD;

        shieldEntity.add ( boundaryComponent )
                    .add ( shieldComponent )
                    .add ( stateComponent )
                    .add ( textureComponent )
                    .add ( transformComponent )
                    .add ( typeComponent );

        engine.addEntity ( shieldEntity );

        return shieldEntity;
    }

    public Entity createPlayerShip (float xCenterPixels, float yCenterPixels,
                                    float shipWidthPixels, float shipHeightPixels,
                                    float shipHullPoints, float shipSpeedMeters,
                                    float shieldStrength, float shieldRadiusPixels,
                                    float laserDamage, float laserSpeedPixels, float laserRechargeTime,
                                    TextureRegion shipTex, TextureRegion shieldTex) {
        Entity playerShipEntity = engine.createEntity ();

        AnimationComponent animationComponent = engine.createComponent ( AnimationComponent.class );
        BoundaryComponent boundaryComponent = engine.createComponent ( BoundaryComponent.class );
        CollisionComponent collisionComponent = engine.createComponent ( CollisionComponent.class );
        LaserCannonComponent laserCannonComponent = engine.createComponent ( LaserCannonComponent.class );
        MovementComponent movementComponent = engine.createComponent ( MovementComponent.class );
        PlayerComponent playerComponent = engine.createComponent ( PlayerComponent.class );
        ShipComponent shipComponent = engine.createComponent ( ShipComponent.class );
        StateComponent stateComponent = engine.createComponent ( StateComponent.class );
        TextureComponent textureComponent = engine.createComponent ( TextureComponent.class );
        TransformComponent transformComponent = engine.createComponent ( TransformComponent.class );
        TypeComponent typeComponent = engine.createComponent ( TypeComponent.class );

        /// ANIMATION ///
        // TODO  animationComponent.animations.put ( StateComponent.STATE_EXPLODING, shipExplodingAnimation );
        // TODO: add other animations

        boundaryComponent.boundingBoxPixels.setSize ( shipWidthPixels, shipHeightPixels );

        laserCannonComponent.laserDamage = laserDamage;
        laserCannonComponent.laserRechargeTime = laserRechargeTime;
        laserCannonComponent.laserMovementSpeed = laserSpeedPixels;

        playerComponent.score = 0;
        playerComponent.livesLeft = PLAYER_STARTING_LIVES;

        shipComponent.maxSpeed = shipSpeedMeters;
        shipComponent.shipType = ShipComponent.Type.PLAYER_SHIP;
        shipComponent.hullPoints = shipHullPoints;
        shipComponent.shield = createShield ( playerShipEntity,
                                              shieldStrength, shieldRadiusPixels,
                                              0f, 0f, shieldTex );

        stateComponent.set ( StateComponent.STATE_NORMAL );

        textureComponent.region = shipTex;

        transformComponent.screenPositionPixels.set ( xCenterPixels, yCenterPixels, 1f );
        transformComponent.scalePixels.x = shipWidthPixels / shipTex.getRegionWidth ();
        transformComponent.scalePixels.y = shipHeightPixels / shipTex.getRegionHeight ();

        typeComponent.type = TypeComponent.PLAYER_SHIP;

        playerShipEntity.add ( animationComponent )
                        .add ( boundaryComponent )
                        .add ( collisionComponent )
                        .add ( laserCannonComponent )
                        .add ( movementComponent )
                        .add ( playerComponent )
                        .add ( shipComponent )
                        .add ( stateComponent )
                        .add ( textureComponent )
                        .add ( transformComponent )
                        .add ( typeComponent );

        engine.addEntity ( playerShipEntity );

        return playerShipEntity;
    }

    public Entity createEnemyShip(float xCenterPixels, float yCenterPixels,
                                  float shipWidthPixels, float shipHeightPixels,
                                  float shipHullPoints, float shipSpeedMeters,
                                  float shieldStrength, float shieldRadiusPixels,
                                  float laserDamage, float laserSpeedPixels, float laserRechargeTime,
                                  float directionChangeFrequency,
                                  TextureRegion shipTexture, TextureRegion shieldTexture) {

        Entity enemyShipEntity = engine.createEntity ();

        AnimationComponent animationComponent = engine.createComponent ( AnimationComponent.class );
        BoundaryComponent boundaryComponent = engine.createComponent ( BoundaryComponent.class );
        CollisionComponent collisionComponent = engine.createComponent ( CollisionComponent.class );
        EnemyComponent enemyComponent = engine.createComponent ( EnemyComponent.class );
        LaserCannonComponent laserCannonComponent = engine.createComponent ( LaserCannonComponent.class );
        MovementComponent movementComponent = engine.createComponent ( MovementComponent.class );
        ShipComponent shipComponent = engine.createComponent ( ShipComponent.class );
        StateComponent stateComponent = engine.createComponent ( StateComponent.class );
        TextureComponent textureComponent = engine.createComponent ( TextureComponent.class );
        TransformComponent transformComponent = engine.createComponent ( TransformComponent.class );
        TypeComponent typeComponent = engine.createComponent ( TypeComponent.class );

        /// ANIMATION ///
        //animationComponent.animations.put ( StateComponent.STATE_EXPLODING, shipExplodingAnimation );
        // TODO: add other animations

        boundaryComponent.boundingBoxPixels.setSize ( shipWidthPixels, shipHeightPixels );

        enemyComponent.enemyType = EnemyComponent.Type.ENEMY_SHIP_RANDOM_MOVER;
        enemyComponent.directionChangeFrequency = directionChangeFrequency;

        laserCannonComponent.laserDamage = laserDamage;
        laserCannonComponent.laserRechargeTime = laserRechargeTime;
        laserCannonComponent.laserMovementSpeed = laserSpeedPixels;

        shipComponent.maxSpeed = shipSpeedMeters;
        shipComponent.shipType = ShipComponent.Type.ENEMY_SHIP;
        shipComponent.hullPoints = shipHullPoints;
        shipComponent.shield = createShield ( enemyShipEntity, shieldStrength, shieldRadiusPixels, 0f, 0f, shieldTexture );

        stateComponent.set ( StateComponent.STATE_NORMAL );

        textureComponent.region = shipTexture;

        transformComponent.screenPositionPixels.set ( xCenterPixels, yCenterPixels, 1f );
        transformComponent.scalePixels.x = shipWidthPixels / shipTexture.getRegionWidth ();
        transformComponent.scalePixels.y = shipHeightPixels / shipTexture.getRegionHeight ();

        typeComponent.type = TypeComponent.ENEMY_SHIP;

        enemyShipEntity.add ( animationComponent )
                       .add ( boundaryComponent )
                       .add ( collisionComponent )
                       .add ( enemyComponent )
                       .add ( laserCannonComponent )
                       .add ( movementComponent )
                       .add ( shipComponent )
                       .add ( stateComponent )
                       .add ( textureComponent )
                       .add ( transformComponent )
                       .add ( typeComponent );

        engine.addEntity ( enemyShipEntity );

        return enemyShipEntity;
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



}
