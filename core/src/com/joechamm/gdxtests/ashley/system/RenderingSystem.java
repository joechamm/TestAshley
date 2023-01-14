package com.joechamm.gdxtests.ashley.system;

import static com.badlogic.gdx.math.MathUtils.*;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.joechamm.gdxtests.ashley.component.Mappers;
import com.joechamm.gdxtests.ashley.component.TextureComponent;
import com.joechamm.gdxtests.ashley.component.TransformComponent;

import java.util.Comparator;

/**
 * File:    RenderingSystem
 * Package: com.joechamm.gdxtests.ashley.system
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 11:29 PM
 */
public class RenderingSystem extends SortedIteratingSystem {
    public static final String TAG = RenderingSystem.class.getName ();

    private boolean shouldRender = true;

    public static final float PPM = 16.0f; // sets the amount of pixels each meter of box2d objects contains

    // this gets the height and width of our camera frustum bassed off the width and height of the screen and our pixel per meter ratio
    static final float FRUSTUM_WIDTH = Gdx.graphics.getWidth () / PPM;
    static final float FRUSTUM_HEIGHT = Gdx.graphics.getHeight () / PPM;

    public static final float PIXELS_TO_METERS = 1.0f / PPM; // ratio for converting pixels to meters

    // static method to get screen width in meters
    private static Vector2 meterDimensions = new Vector2 ();
    private static Vector2 pixelDimensions = new Vector2 ();

    public static Vector2 getScreenSizeInMeters () {
        meterDimensions.set ( Gdx.graphics.getWidth () * PIXELS_TO_METERS,
                              Gdx.graphics.getHeight () * PIXELS_TO_METERS );
        return meterDimensions;
    }

    // static method to get screen size in pixels
    public static Vector2 getScreenSizeInPixels () {
        pixelDimensions.set ( Gdx.graphics.getWidth (), Gdx.graphics.getHeight () );
        return pixelDimensions;
    }

    // convenience method to convert pixels to meters
    public static float PixelsToMeters ( float pixelValue ) {
        return pixelValue * PIXELS_TO_METERS;
    }

    private SpriteBatch batch; // reference to our spritebatch
    private Array<Entity> renderQueue; // an array used to allow sorting of images allowing us to draw images on top of each other
    private Comparator<Entity> comparator; // a comparator to sort images based on the z position of the transformComponent
    private OrthographicCamera cam; // reference to our camera

    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family     The family of entities iterated over in this System
     * @param comparator The comparator to sort the entities
     */
    RenderingSystem ( Family family, Comparator<Entity> comparator, SpriteBatch batch ) {
        super ( family, comparator );
        this.batch = batch;

        renderQueue = new Array<> ();

        this.comparator = comparator;

        cam = new OrthographicCamera ( FRUSTUM_WIDTH, FRUSTUM_HEIGHT );
        cam.position.set ( FRUSTUM_WIDTH / 2f, FRUSTUM_HEIGHT / 2f, 0f );
    }

    public RenderingSystem ( SpriteBatch batch ) {
        this ( Family.all ( TransformComponent.class, TextureComponent.class ).get (), new ZComparator (), batch );
    }

    @Override
    public void update ( float deltaTime ) {
        super.update ( deltaTime );

        // sort the renderQueue based on z index
        renderQueue.sort ( comparator );

        // update camera and sprite batch
        cam.update ();
        batch.setProjectionMatrix ( cam.combined );
        batch.enableBlending ();

        if ( shouldRender ) {
            batch.begin ();

            // loop through each entity in our render queue
            for ( Entity entity : renderQueue ) {

                TextureComponent tex = Mappers.textCM.get ( entity );
                TransformComponent t = Mappers.tranCM.get ( entity );

                if ( tex.region == null || t.isHidden ) {
                    continue;
                }

                float width = tex.region.getRegionWidth ();
                float height = tex.region.getRegionHeight ();

                float originX = width / 2f;
                float originY = height / 2f;

                batch.draw ( tex.region,
                             t.screenPositionPixels.x - originX + tex.offsetX,
                             t.screenPositionPixels.y - originY + tex.offsetY,
                             originX, originY,
                             width, height,
                             PixelsToMeters ( t.scalePixels.x ), PixelsToMeters ( t.scalePixels.y ),
                             radiansToDegrees * t.rotation );

            }

            batch.end ();
        }

        renderQueue.clear ();
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
        renderQueue.add ( entity );
    }

    /**
     * Convenience method to get camera
     * @return current camera
     */
    public OrthographicCamera getCamera () {
        return cam;
    }

    private static class ZComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity e1, Entity e2) {
            return (int) Math.signum ( Mappers.tranCM.get ( e1 ).screenPositionPixels.z - Mappers.tranCM.get ( e2 ).screenPositionPixels.z );
        }
    }
}
