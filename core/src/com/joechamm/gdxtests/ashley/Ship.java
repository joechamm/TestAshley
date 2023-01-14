package com.joechamm.gdxtests.ashley;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * File:    Ship
 * Package: com.joechamm.gdxtests.controls
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/3/2023 at 12:15 AM
 */
public abstract class Ship {

    public static final String TAG = Ship.class.getName ();

    // Ship characteristics
    public float movementSpeed; // world units per second
    public int shield;

    // position & dimension
    public Rectangle boundingBox;

    // graphics
    TextureRegion shipTextureRegion, shieldTextureRegion, laserTextureRegion;

    // laser information
    float laserWidth, laserHeight;
    float laserMovementSpeed;
    float timeBetweenShots;
    float timeSinceLastShot = 0;

    public Ship ( float xCenter, float yCenter,
                  float width, float height,
                  float movementSpeed, int shield,
                  float laserWidth, float laserHeight,
                  float laserMovementSpeed, float timeBetweenShots,
                  TextureRegion shipTextureRegion,
                  TextureRegion shieldTextureRegion,
                  TextureRegion laserTextureRegion
    ) {

        /// DEBUGGING
        Gdx.app.debug ( TAG, "ctor" );
        try {
            if ( null == shipTextureRegion ) {
                throw new GdxRuntimeException ( "null ship texture region" );
            }
            if ( null == shieldTextureRegion ) {
                throw new GdxRuntimeException ( "null shield texture region" );
            }
            if ( null == laserTextureRegion ) {
                throw new GdxRuntimeException ( "null laser texture region" );
            }
        } catch ( GdxRuntimeException e ) {
            Gdx.app.error ( TAG, e.getMessage (), e );
            Gdx.app.exit ();
        }
        /// END DEBUGGING

        this.movementSpeed = movementSpeed;
        this.shield = shield;

        this.boundingBox = new Rectangle ( xCenter - width / 2, yCenter - height / 2, width, height );
        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
        this.shipTextureRegion = shipTextureRegion;
        this.shieldTextureRegion = shieldTextureRegion;
        this.laserTextureRegion = laserTextureRegion;
    }

    public void update(float deltaTime) {

        timeSinceLastShot += deltaTime;
    }

    public boolean canFireLaser() {
        return (timeSinceLastShot - timeBetweenShots >= 0);
    }

    public abstract Laser[] fireLasers();

    public void translate(float xChange, float yChange) {
        boundingBox.setPosition ( boundingBox.x + xChange, boundingBox.y + yChange );
    }

    public void draw ( Batch batch ) {
        batch.draw ( shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if(shield > 0) {
            batch.draw ( shieldTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height );
        }
    }

    public boolean intersects ( Rectangle otherRectangle ) {
        return boundingBox.overlaps ( otherRectangle );
    }

    public boolean hitAndCheckDestroyed ( Laser laser ) {
        if ( shield > 0 ) {
            shield--;
            return false;
        }
        return true;
    }
}
