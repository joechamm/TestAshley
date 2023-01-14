package com.joechamm.gdxtests.ashley;

import static com.joechamm.gdxtests.ashley.Constants.ANIMATION_EXPLOSION_COL_COUNT;
import static com.joechamm.gdxtests.ashley.Constants.ANIMATION_EXPLOSION_FRAMETIME;
import static com.joechamm.gdxtests.ashley.Constants.ANIMATION_EXPLOSION_ROW_COUNT;
import static com.joechamm.gdxtests.ashley.Constants.PLAYER_STARTING_LIVES;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.joechamm.gdxtests.ashley.asset.JCGdxAssetManager;
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
import com.joechamm.gdxtests.ashley.util.TextureHelper;

/**
 * File:    LevelFactory
 * Package: com.joechamm.gdxtests.ashley
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/11/2023 at 10:48 AM
 */
public class LevelFactory {

    public static final String TAG = LevelFactory.class.getName ();
    // the player
    public Entity playerEntity;
    // asset manager
    public JCGdxAssetManager assetManager;
    // ECS engine
    private PooledEngine engine;
    // game images atlas
    private TextureAtlas atlas;
    // ship textures
    private TextureRegion playerShipTex;
    private TextureRegion enemyShipTex;
    // laser textures
    private TextureRegion playerLaserTex;
    private TextureRegion enemyLaserTex;
    // shield textures
    private TextureRegion playerShieldTex;
    private TextureRegion enemyShieldTex;
    // animation textures
    private Texture explosionTexture;

    // Animations
    private Animation<TextureRegion> shipNormalAnimation;
    private Animation<TextureRegion> shipShootingAnimation;
    private Animation<TextureRegion> shipHitAnimation;
    private Animation<TextureRegion> shipExplodingAnimation;

    public LevelFactory ( PooledEngine engine, JCGdxAssetManager assetManager ) {
        this.engine = engine;
        this.assetManager = assetManager;
        this.atlas = assetManager.manager.get ( assetManager.gameImages, TextureAtlas.class );
        this.explosionTexture = assetManager.manager.get ( assetManager.explosionImages, Texture.class );


    }

    private Entity createBackground(float width, float height, float scrollSpeed, TextureRegion backgroundTexture) {

        Entity bgEntity = engine.createEntity ();

        BackgroundComponent bgCom = engine.createComponent ( BackgroundComponent.class );
        BoundaryComponent boundaryComponent = engine.createComponent ( BoundaryComponent.class );
        MovementComponent movementComponent = engine.createComponent ( MovementComponent.class );
        TransformComponent transformComponent = engine.createComponent ( TransformComponent.class );
        TextureComponent textureComponent = engine.createComponent ( TextureComponent.class );
        TypeComponent typeComponent = engine.createComponent ( TypeComponent.class );

        boundaryComponent.boundingBoxPixels.setSize ( width, height );

        movementComponent.velocityMeters.set ( 0f, scrollSpeed );

        textureComponent.region = backgroundTexture;
        textureComponent.offsetX = 0f;
        textureComponent.offsetY = 0f;

        typeComponent.type = TypeComponent.BACKGROUND_LAYER;

        bgEntity.add ( bgCom )
                .add ( boundaryComponent )
                .add ( movementComponent )
                .add ( transformComponent )
                .add ( textureComponent )
                .add ( typeComponent );

        engine.addEntity ( bgEntity );

        return bgEntity;
    }

    private void initAnimations() {
        // TODO: normal, shooting, hit animations

        shipExplodingAnimation = new Animation<> ( ANIMATION_EXPLOSION_FRAMETIME,
                                                   TextureHelper.spriteSheetToFrames ( explosionTexture,
                                                                                       ANIMATION_EXPLOSION_COL_COUNT,
                                                                                       ANIMATION_EXPLOSION_ROW_COUNT ) );
    }

    public Entity createEnemyShip(float xCenter, float yCenter,
                                  float shipWidth, float shipHeight, float shipHullPoints,
                                  float shipMovementSpeed, float shieldStrength, float shieldRadius,
                                  float laserDamage, float laserMovementSpeed, float laserRechargeTime,
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
        animationComponent.animations.put ( StateComponent.STATE_EXPLODING, shipExplodingAnimation );
        // TODO: add other animations

        boundaryComponent.boundingBoxPixels.setSize ( shipWidth, shipHeight );

        enemyComponent.enemyType = EnemyComponent.Type.ENEMY_SHIP_RANDOM_MOVER;
        enemyComponent.directionChangeFrequency = directionChangeFrequency;

        laserCannonComponent.laserDamage = laserDamage;
        laserCannonComponent.laserRechargeTime = laserRechargeTime;
        laserCannonComponent.laserMovementSpeed = laserMovementSpeed;

        shipComponent.maxSpeed = shipMovementSpeed;
        shipComponent.shipType = ShipComponent.Type.ENEMY_SHIP;
        shipComponent.hullPoints = shipHullPoints;
        shipComponent.shield = createShield ( enemyShipEntity, shieldStrength, shieldRadius, 0f, 0f, shieldTexture );

        stateComponent.set ( StateComponent.STATE_NORMAL );

        textureComponent.region = shipTexture;

        transformComponent.screenPositionPixels.set ( xCenter, yCenter, 1f );
        transformComponent.scalePixels.set ( shipWidth, shipHeight );

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

    public Entity createPlayerShip(float xCenter, float yCenter,
                                   float shipWidth, float shipHeight, float shipHullPoints,
                                   float shipMovementSpeed, float shieldStrength, float shieldRadius,
                                   float laserDamage, float laserMovementSpeed, float laserRechargeTime,
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
        animationComponent.animations.put ( StateComponent.STATE_EXPLODING, shipExplodingAnimation );
        // TODO: add other animations

        boundaryComponent.boundingBoxPixels.width = shipWidth;
        boundaryComponent.boundingBoxPixels.height = shipHeight;

        laserCannonComponent.laserDamage = laserDamage;
        laserCannonComponent.laserRechargeTime = laserRechargeTime;
        laserCannonComponent.laserMovementSpeed = laserMovementSpeed;

        playerComponent.score = 0;
        playerComponent.livesLeft = PLAYER_STARTING_LIVES;

        shipComponent.maxSpeed = shipMovementSpeed;
        shipComponent.shipType = ShipComponent.Type.PLAYER_SHIP;
        shipComponent.hullPoints = shipHullPoints;
        shipComponent.shield = createShield ( playerShipEntity, shieldStrength, shieldRadius, 0f, 0f, shieldTex );

        stateComponent.set ( StateComponent.STATE_NORMAL );

        textureComponent.region = shipTex;

        transformComponent.screenPositionPixels.set ( xCenter, yCenter, 1f );
        transformComponent.scalePixels.set ( shipWidth, shipHeight );

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

    private Entity createShield ( Entity shieldOwner,
                                  float shieldStrength, float shieldRadius,
                                  float offsetX, float offsetY,
                                  TextureRegion shieldTex ) {
        Entity shieldEntity = engine.createEntity ();

        BoundaryComponent boundaryComponent = engine.createComponent ( BoundaryComponent.class );
        ShieldComponent shieldComponent = engine.createComponent ( ShieldComponent.class );
        TextureComponent textureComponent = engine.createComponent ( TextureComponent.class );
        TransformComponent transformComponent = engine.createComponent ( TransformComponent.class );
        TypeComponent typeComponent = engine.createComponent ( TypeComponent.class );

        boundaryComponent.boundingBoxPixels.setSize ( shieldRadius * 2f );

        shieldComponent.strength = shieldStrength;
        shieldComponent.radius = shieldRadius;
        shieldComponent.owner = shieldOwner;

        textureComponent.region = shieldTex;
        textureComponent.offsetX = offsetX;
        textureComponent.offsetY = offsetY;

        typeComponent.type = TypeComponent.SHIELD;

        shieldEntity.add ( boundaryComponent )
                    .add ( shieldComponent )
                    .add ( textureComponent )
                    .add ( transformComponent )
                    .add ( typeComponent );

        engine.addEntity ( shieldEntity );

        return shieldEntity;
    }

    private Entity createLaser(float spawnX, float spawnY,
                               float laserSpeed, float laserHeadingRad, float laserDamage,
                               float laserWidth, float laserHeight,
                               TextureRegion laserTex, LaserComponent.Owner laserOwner) {

        Entity laserEntity = engine.createEntity ();

        BoundaryComponent boundaryComponent = engine.createComponent ( BoundaryComponent.class );
        CollisionComponent collisionComponent = engine.createComponent ( CollisionComponent.class );
        LaserComponent laserComponent = engine.createComponent ( LaserComponent.class );
        MovementComponent movementComponent = engine.createComponent ( MovementComponent.class );
        StateComponent stateComponent = engine.createComponent ( StateComponent.class );
        TextureComponent textureComponent = engine.createComponent ( TextureComponent.class );
        TransformComponent transformComponent = engine.createComponent ( TransformComponent.class );
        TypeComponent typeComponent = engine.createComponent ( TypeComponent.class );

        boundaryComponent.boundingBoxPixels.setSize ( laserWidth, laserHeight );

        laserComponent.damage = laserDamage;
        laserComponent.owner = laserOwner;
        laserComponent.movementSpeed = laserSpeed;

        movementComponent.velocityMeters.setAngleRad ( laserHeadingRad );
        movementComponent.velocityMeters.scl ( laserSpeed );

        stateComponent.set ( StateComponent.STATE_NORMAL );

        textureComponent.region = laserTex;

        transformComponent.screenPositionPixels.set ( spawnX, spawnY, 0f );
        transformComponent.scalePixels.set ( laserWidth, laserHeight );
        transformComponent.rotation = laserHeadingRad;

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

    public Entity createEnemyLaser( float spawnX, float spawnY,
                                    float laserSpeed, float laserHeadingRad, float laserDamage,
                                    float laserWidth, float laserHeight,
                                    TextureRegion laserTex ) {
        return createLaser ( spawnX, spawnY, laserSpeed, laserHeadingRad, laserDamage, laserWidth, laserHeight, laserTex,
                             LaserComponent.Owner.ENEMY );
    }

    public Entity createPlayerLaser( float spawnX, float spawnY,
                                     float laserSpeed, float laserHeadingRad, float laserDamage,
                                     float laserWidth, float laserHeight,
                                     TextureRegion laserTex ) {

        return createLaser ( spawnX, spawnY, laserSpeed, laserHeadingRad, laserDamage, laserWidth, laserHeight, laserTex,
                             LaserComponent.Owner.PLAYER );
    }

}
