package com.joechamm.gdxtests.ashley;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * File:    Explosion
 * Package: com.joechamm.gdxtests.controls
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/3/2023 at 12:15 AM
 */
public class Explosion {
    private static final String TAG = Explosion.class.getName ();

    private Animation<TextureRegion> explosionAnimation;
    private float explosionTimer;

    private Rectangle boundingBox;

    public Explosion ( Texture texture, Rectangle boundingBox, float totalAnimationTime ) {

        /// DEBUGGING
        Gdx.app.debug ( TAG, "ctor" );
        try {
            if ( null == texture ) {
                throw new GdxRuntimeException ( "null explosion texture" );
            }
        } catch ( GdxRuntimeException e ) {
            Gdx.app.error ( TAG, e.getMessage (), e );
            Gdx.app.exit ();
        }
        /// END DEBUGGING

        this.boundingBox = boundingBox;

        // split texture
        TextureRegion[][] textureRegion2D = TextureRegion.split ( texture, 64, 64 );

        // convert texture to 1D array
        TextureRegion[] textureRegion1D = new TextureRegion[16];
        int index = 0;
        for ( int i = 0; i < 4; i++ ) {
            for ( int j = 0; j < 4; j++ ) {
                textureRegion1D[index++] = textureRegion2D[i][j];
            }
        }

        explosionAnimation = new Animation<TextureRegion> ( totalAnimationTime / 16, textureRegion1D );
        explosionTimer = 0;
    }

    public void update(float delta) {
        explosionTimer += delta;
    }

    public void draw ( SpriteBatch batch ) {
        batch.draw ( explosionAnimation.getKeyFrame ( explosionTimer ),
                     boundingBox.x,
                     boundingBox.y,
                     boundingBox.width,
                     boundingBox.height );

    }

    public boolean isFinished() {
        return explosionAnimation.isAnimationFinished ( explosionTimer );
    }
}
