package com.joechamm.gdxtests.ashley;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * File:    PlayerShip
 * Package: com.joechamm.gdxtests.controls
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/3/2023 at 12:15 AM
 */
public class PlayerShip extends Ship {

    public static final String TAG = PlayerShip.class.getName ();

    public int lives;

    public PlayerShip (
            float xCenter, float yCenter,
            float width, float height,
            float movementSpeed, int shield,
            float laserWidth, float laserHeight,
            float laserMovementSpeed,
            float timeBetweenShots,
            TextureRegion shipTextureRegion,
            TextureRegion shieldTextureRegion,
            TextureRegion laserTextureRegion
    ) {
        super ( xCenter, yCenter, width, height, movementSpeed, shield, laserWidth, laserHeight, laserMovementSpeed, timeBetweenShots,
                shipTextureRegion, shieldTextureRegion, laserTextureRegion );
        lives = 3;
    }

    @Override
    public Laser[] fireLasers () {
        Laser[] laser = new Laser[2];
        laser[ 0 ] = new Laser ( boundingBox.x + boundingBox.width * 0.07f, boundingBox.y + boundingBox.height * 0.45f,
                                 laserWidth, laserHeight,
                                 laserMovementSpeed, laserTextureRegion );

        laser[ 1 ] = new Laser ( boundingBox.x + boundingBox.width * 0.93f, boundingBox.y + boundingBox.height * 0.45f,
                                 laserWidth, laserHeight,
                                 laserMovementSpeed, laserTextureRegion );

        timeSinceLastShot = 0;

        return laser;
    }
}
