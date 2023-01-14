package com.joechamm.gdxtests.ashley;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * File:    Laser
 * Package: com.joechamm.gdxtests.controls
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/3/2023 at 12:17 AM
 */
public class Laser {
    private static final String TAG = Laser.class.getName ();

    // position and dimensions
    public Rectangle boundingBox;

    // laser physical characteristics
    public float movementSpeed; // world units per second

    // graphics
    TextureRegion textureRegion;

    public Laser (
            float xCenter, float yBottom,
            float width, float height,
            float movementSpeed, TextureRegion textureRegion
    ) {
        /// DEBUGGING
        Gdx.app.debug ( TAG, "ctor" );
        try {
            if ( null == textureRegion ) {
                throw new GdxRuntimeException ( "null laser texture region" );
            }
        } catch ( GdxRuntimeException e ) {
            Gdx.app.error ( TAG, e.getMessage (), e );
            Gdx.app.exit ();
        }
        /// END DEBUGGING

        this.boundingBox = new Rectangle ( xCenter - width / 2, yBottom, width, height);

        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
    }

    public void draw ( Batch batch ) {
        batch.draw ( textureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

//    public Rectangle getBoundingBox() {
//        return boundingBox;
//    }

}
