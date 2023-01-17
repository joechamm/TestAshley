package com.joechamm.gdxtests.ashley.entity;

import static com.badlogic.gdx.math.MathUtils.radiansToDegrees;
import static com.joechamm.gdxtests.ashley.Constants.PIXELS_PER_METER;
import static com.joechamm.gdxtests.ashley.Constants.PIXELS_TO_METERS;
import static com.joechamm.gdxtests.ashley.Constants.PLAYER_STARTING_LIVES;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.joechamm.gdxtests.ashley.component.AnimationComponent;
import com.joechamm.gdxtests.ashley.component.BackgroundComponent;
import com.joechamm.gdxtests.ashley.component.BoundaryComponent;
import com.joechamm.gdxtests.ashley.component.CollisionComponent;
import com.joechamm.gdxtests.ashley.component.EnemyComponent;
import com.joechamm.gdxtests.ashley.component.EnemyOwnedComponent;
import com.joechamm.gdxtests.ashley.component.LaserCannonComponent;
import com.joechamm.gdxtests.ashley.component.LaserComponent;
import com.joechamm.gdxtests.ashley.component.Mappers;
import com.joechamm.gdxtests.ashley.component.MovementComponent;
import com.joechamm.gdxtests.ashley.component.PlayerComponent;
import com.joechamm.gdxtests.ashley.component.PlayerInputComponent;
import com.joechamm.gdxtests.ashley.component.PlayerOwnedComponent;
import com.joechamm.gdxtests.ashley.component.ShieldComponent;
import com.joechamm.gdxtests.ashley.component.ShipComponent;
import com.joechamm.gdxtests.ashley.component.StateComponent;
import com.joechamm.gdxtests.ashley.component.TakesLaserDamageComponent;
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

    public static final String TAG = EntityFactory.class.getName ();

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

    public Entity createLaser ( float spawnPixelsX, float spawnPixelsY,
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

        movementComponent.velocityMeters.setAngleRad ( laserCannon.headingRads );
        movementComponent.velocityMeters.scl ( pixelsToMeters ( laserCannon.laserMovementSpeed ) );

        stateComponent.set ( StateComponent.STATE_SHOOTING );

        transformComponent.screenPositionPixels.set ( spawnPixelsX, spawnPixelsY, 0f );
        transformComponent.scalePixels.x = laserWidthPixels / laserTex.getRegionWidth ();
        transformComponent.scalePixels.y = laserHeightPixels / laserTex.getRegionHeight ();
        transformComponent.rotation = laserCannon.headingRads * radiansToDegrees;

        textureComponent.region = laserTex;

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

    public Entity createShield ( Entity shieldOwner, boolean ownerTypePlayer, float shieldStrength, float shieldRadiusPixels,
                                  float offsetPixelsX, float offsetPixelsY, TextureRegion shieldTex ) {
        Entity shieldEntity = engine.createEntity ();

        BoundaryComponent boundaryComponent = engine.createComponent ( BoundaryComponent.class );
        ShieldComponent shieldComponent = engine.createComponent ( ShieldComponent.class );
        StateComponent stateComponent = engine.createComponent ( StateComponent.class );
        TakesLaserDamageComponent takesLaserDamageComponent = engine.createComponent ( TakesLaserDamageComponent.class );
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
                    .add ( takesLaserDamageComponent )
                    .add ( textureComponent )
                    .add ( transformComponent )
                    .add ( typeComponent );

        if ( ownerTypePlayer ) {
            shieldEntity.add ( engine.createComponent ( PlayerOwnedComponent.class ) );
        } else {
            shieldEntity.add ( engine.createComponent ( EnemyOwnedComponent.class ) );
        }

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
        PlayerOwnedComponent playerOwnedComponent = engine.createComponent ( PlayerOwnedComponent.class );
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
        shipComponent.shield = createShield ( playerShipEntity, true,
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
                        .add ( playerOwnedComponent )
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
        EnemyOwnedComponent enemyOwnedComponent = engine.createComponent ( EnemyOwnedComponent.class );
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
        shipComponent.shield = createShield ( enemyShipEntity, false,
                                              shieldStrength, shieldRadiusPixels,
                                              0f, 0f, shieldTexture );

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
                       .add ( enemyOwnedComponent )
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

    public Entity setPlayerKeyboardInput ( Entity playerShipEntity, int playerIndex ) {

        try {
            if ( playerShipEntity == null ) {
                throw new GdxRuntimeException ( "setPlayerKeyboardInput called on null player ship entity" );
            }

            PlayerInputComponent playerInputComponent = Mappers.playerInputCM.get ( playerShipEntity );
            if ( playerInputComponent == null ) {
                playerInputComponent = engine.createComponent ( PlayerInputComponent.class );
            }

            playerInputComponent.inputType = PlayerInputComponent.Type.INPUT_TYPE_KEYBOARD;
            playerInputComponent.playerIndex = Math.max ( 0, playerIndex );
            playerInputComponent.playerShip = playerShipEntity;
            playerInputComponent.playerController = null;
            playerShipEntity.add ( playerInputComponent );

            return playerShipEntity;

        } catch ( GdxRuntimeException e ) {
            Gdx.app.error ( TAG, e.getMessage (), e );
            return null;
        }

    }

    public Entity setPlayerTouchInput(Entity playerShipEntity, int playerIndex) {
        try {
            if ( playerShipEntity == null ) {
                throw new GdxRuntimeException ( "setPlayerTouchInput called on null player ship entity" );
            }

            PlayerInputComponent playerInputComponent = Mappers.playerInputCM.get ( playerShipEntity );
            if ( playerInputComponent == null ) {
                playerInputComponent = engine.createComponent ( PlayerInputComponent.class );
            }

            playerInputComponent.inputType = PlayerInputComponent.Type.INPUT_TYPE_TOUCH;
            playerInputComponent.playerIndex = Math.max ( 0, playerIndex );
            playerInputComponent.playerShip = playerShipEntity;
            playerInputComponent.playerController = null;
            playerShipEntity.add ( playerInputComponent );

            return playerShipEntity;

        } catch ( GdxRuntimeException e ) {
            Gdx.app.error ( TAG, e.getMessage (), e );
            return null;
        }
    }

    public Entity setPlayerGamepadInput ( Entity playerShipEntity, int index, Controller controller ) {
        try {
            if ( playerShipEntity == null ) {
                throw new GdxRuntimeException ( "setPlayerGamepadInput called on null player ship entity" );
            }

            PlayerInputComponent playerInputComponent = Mappers.playerInputCM.get ( playerShipEntity );
            if ( playerInputComponent == null ) {
                playerInputComponent = engine.createComponent ( PlayerInputComponent.class );
            }

            int playerIndex = index;
            Controller playerController = controller;

            // check to see if we need to acquire the gamepad first // TODO: make sure we're grabbing the right one
            if ( playerController == null ) {
                // if the index passed is -1 we grab the current controller, otherwise try for the one at the indicated index
                if ( playerIndex < 0 ) {
                    playerController = Controllers.getCurrent ();
                } else {
                    playerController = Controllers.getControllers ().get ( playerIndex );
                }
            }

            // make sure we were able to get a controller and throw an exception if not
            if ( playerController == null ) {
                throw new GdxRuntimeException ( "setPlayerGamepadInput called, but no controllers are available!" );
            }

            if ( playerController.supportsPlayerIndex () ) {
                // make sure player index is valid, then set the controller's player index
                if ( playerIndex < 0 ) {
                    if ( playerController.getPlayerIndex () == Controller.PLAYER_IDX_UNSET ) {
                        playerIndex = 0;
                    } else {
                        playerIndex = playerController.getPlayerIndex ();
                    }
                }

                playerController.setPlayerIndex ( playerIndex );
            }

            playerInputComponent.playerIndex = Math.max ( 0, playerIndex );
            playerInputComponent.inputType = PlayerInputComponent.Type.INPUT_TYPE_GAMEPAD;
            playerInputComponent.playerShip = playerShipEntity;
            playerInputComponent.playerController = playerController;

            playerShipEntity.add ( playerInputComponent );

            return playerShipEntity;

        } catch ( GdxRuntimeException e ) {
            Gdx.app.error ( TAG, e.getMessage (), e );
            return null;
        }
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
