package com.joechamm.gdxtests.ashley.system;

import static com.joechamm.gdxtests.ashley.Constants.PIXELS_PER_METER;
import static com.joechamm.gdxtests.ashley.Constants.PIXELS_TO_METERS;
import static com.joechamm.gdxtests.ashley.Constants.WORLD_HEIGHT_METERS;
import static com.joechamm.gdxtests.ashley.Constants.WORLD_WIDTH_METERS;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.joechamm.gdxtests.ashley.component.BoundaryComponent;
import com.joechamm.gdxtests.ashley.component.Mappers;
import com.joechamm.gdxtests.ashley.component.TransformComponent;

/**
 * File:    BoundaryUpdateSystem
 * Package: com.joechamm.gdxtests.ashley.system
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/13/2023 at 5:08 PM
 */
public class BoundaryUpdateSystem extends IteratingSystem {

    public static final String TAG = BoundaryUpdateSystem.class.getName ();

    /** hold the boundary for our world in game units (meters) here **/
    private final Rectangle worldBoundsMeters = new Rectangle ();
    /** hold the boundary for our world in screen units (pixels) here **/
    private final Rectangle worldBoundsPixels = new Rectangle ();

    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family The family of entities iterated over in this System
     */
    BoundaryUpdateSystem ( Family family ) {
        super ( family );
    }

    public BoundaryUpdateSystem() {
        this ( Family.all ( BoundaryComponent.class, TransformComponent.class ).get () );
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

        TransformComponent xfrm = Mappers.tranCM.get ( entity );
        BoundaryComponent bounds = Mappers.bounCM.get ( entity );

        bounds.boundingBoxPixels.x = xfrm.screenPositionPixels.x - 0.5f * bounds.boundingBoxPixels.width;
        bounds.boundingBoxPixels.y = xfrm.screenPositionPixels.y - 0.5f * bounds.boundingBoxPixels.height;
    }
}
