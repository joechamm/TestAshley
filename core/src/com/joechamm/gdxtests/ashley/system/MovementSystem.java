package com.joechamm.gdxtests.ashley.system;

import static com.joechamm.gdxtests.ashley.Constants.PIXELS_PER_METER;
import static com.joechamm.gdxtests.ashley.Constants.PIXELS_TO_METERS;
import static com.joechamm.gdxtests.ashley.Constants.WORLD_HEIGHT_METERS;
import static com.joechamm.gdxtests.ashley.Constants.WORLD_WIDTH_METERS;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.joechamm.gdxtests.ashley.component.Mappers;
import com.joechamm.gdxtests.ashley.component.MovementComponent;
import com.joechamm.gdxtests.ashley.component.TransformComponent;

/**
 * File:    MovementSystem
 * Package: com.joechamm.gdxtests.ashley.system
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/12/2023 at 10:03 AM
 *
 * The movement system
 */
public class MovementSystem extends IteratingSystem {

    public static final String TAG = MovementSystem.class.getName ();

    /** hold the boundary for our world in game units (meters) here **/
    private final Rectangle worldBoundsMeters = new Rectangle ();
    /** hold the boundary for our world in screen units (pixels) here **/
    private final Rectangle worldBoundsPixels = new Rectangle ();

    /** some vec2 and vec3 so we don't have to keep allocating/deallocating them **/
    private final Vector2 deltaVelocity = new Vector2 ();   // temporary variable for changing the velocity
    private final Vector3 deltaPosition = new Vector3 ();   // temporary variable for changing the position
    private final Vector3 scratchVec3 = new Vector3 ();     // scratch variable

    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family The family of entities iterated over in this System
     */
    MovementSystem ( Family family ) {
        super ( family );
        updateWorldBounds ();
    }

    public MovementSystem() {
        this ( Family.all ( MovementComponent.class, TransformComponent.class ).get () );
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
     * Convert a vec2 from game units to screen units and return for chaining
     * @param vector2 the vector in game units to convert
     * @return the screen units vector
     */
    private Vector2 convertGameToScreen(Vector2 vector2) {
        return vector2.scl ( PIXELS_PER_METER );
    }

    /**
     * Convert the xy coordinates of a vec3 from game units to screen units
     * @param vector3 in meters
     * @return xy coordinates in pixels, z unchanged
     */
    private Vector3 convertGameToScreen ( Vector3 vector3 ) {
        return vector3.scl ( PIXELS_PER_METER, PIXELS_PER_METER, 1.0f );
    }

    /**
     * Convert a vec2 from screen units to game units and return for chaining
     * @param vector2 pixels
     * @return meters
     */
    private Vector2 convertScreenToGame ( Vector2 vector2 ) {
        return vector2.scl ( PIXELS_TO_METERS );
    }

    /**
     * Convert xy coordinates from screen to game units
     * @param vector3 xy in pixels
     * @return xy in meters, z unchanged
     */
    private Vector3 convertScreenToGame ( Vector3 vector3 ) {
        return vector3.scl ( PIXELS_TO_METERS, PIXELS_TO_METERS, 1.0f );
    }

    /**
     * Check if the xy coordinates in game units are within our world bounds rectangle, and if not set them to the edge
     * @param pos xy in meters
     * @return the position closest to xy that's inside the rectangle
     */
    private Vector2 enforceWorldBoundsMeters(Vector2 pos) {
        // check if pos is inside the worldBoundsMeters rectangle
        if ( !worldBoundsMeters.contains ( pos ) ) {
            if ( pos.x < worldBoundsMeters.x ) {
                pos.x = worldBoundsMeters.x;
            } else if ( pos.x > worldBoundsMeters.x + worldBoundsMeters.width ) {
                pos.x = worldBoundsMeters.x + worldBoundsMeters.width;
            }
            if ( pos.y < worldBoundsMeters.y ) {
                pos.y = worldBoundsMeters.y;
            } else if ( pos.y > worldBoundsMeters.y + worldBoundsMeters.height ) {
                pos.y = worldBoundsMeters.y + worldBoundsMeters.height;
            }
        }
        return pos;
    }

    /**
     * Check if the xy coordinates in game units are within our world bounds rectangle, and if not set them to the edge
     * @param pos xy in meters
     * @return the position with x and y updated to the closest values inside the rectangle
     */
    private Vector3 enforceWorldBoundsMeters(Vector3 pos) {
        if ( ! worldBoundsMeters.contains ( pos.x, pos.y ) ) {
            if ( pos.x < worldBoundsMeters.x ) {
                pos.x = worldBoundsMeters.x;
            } else if ( pos.x > worldBoundsMeters.x + worldBoundsMeters.width ) {
                pos.x = worldBoundsMeters.x + worldBoundsMeters.width;
            }
            if ( pos.y < worldBoundsMeters.y ) {
                pos.y = worldBoundsMeters.y;
            } else if ( pos.y > worldBoundsMeters.y + worldBoundsMeters.height ) {
                pos.y = worldBoundsMeters.y + worldBoundsMeters.height;
            }
        }
        return pos;
    }

    /**
     * Check if the xy coordinates in screen units are within our world bounds rectangle, and if not set them to the edge
     * @param pos xy in pixels
     * @return the position closest to xy that's inside the rectangle
     */
    private Vector2 enforceWorldBoundsPixels(Vector2 pos) {
        // check if pos is inside the worldBoundsPixels rectangle
        if ( !worldBoundsPixels.contains ( pos ) ) {
            if ( pos.x < worldBoundsPixels.x ) {
                pos.x = worldBoundsPixels.x;
            } else if ( pos.x > worldBoundsPixels.x + worldBoundsPixels.width ) {
                pos.x = worldBoundsPixels.x + worldBoundsPixels.width;
            }
            if ( pos.y < worldBoundsPixels.y ) {
                pos.y = worldBoundsPixels.y;
            } else if ( pos.y > worldBoundsPixels.y + worldBoundsPixels.height ) {
                pos.y = worldBoundsPixels.y + worldBoundsPixels.height;
            }
        }
        return pos;
    }

    /**
     * Check if the xy coordinates in screen units are within our world bounds rectangle, and if not set them to the edge
     * @param pos xy in pixels
     * @return the position with x and y updated to the closest values inside the rectangle
     */
    private Vector3 enforceWorldBoundsPixels(Vector3 pos) {
        if ( ! worldBoundsPixels.contains ( pos.x, pos.y ) ) {
            if ( pos.x < worldBoundsPixels.x ) {
                pos.x = worldBoundsPixels.x;
            } else if ( pos.x > worldBoundsPixels.x + worldBoundsPixels.width ) {
                pos.x = worldBoundsPixels.x + worldBoundsPixels.width;
            }
            if ( pos.y < worldBoundsPixels.y ) {
                pos.y = worldBoundsPixels.y;
            } else if ( pos.y > worldBoundsPixels.y + worldBoundsPixels.height ) {
                pos.y = worldBoundsPixels.y + worldBoundsPixels.height;
            }
        }
        return pos;
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

        // the position and scale from the 'TransformComponent' is in pixels, so we need to convert the game units to screen units

        final float halfDeltaTimeSquared = 0.5f * deltaTime * deltaTime; // used for position update

        TransformComponent xfrm = Mappers.tranCM.get ( entity );
        MovementComponent mov = Mappers.moveCM.get ( entity );

        // store the current velocity in a scratch variable
        scratchVec3.set ( mov.velocityMeters, 0f );
        // dp = v * dt + 0.5 * a * dt^2
        deltaPosition.set ( mov.accelerationMeters, 0f ).scl ( halfDeltaTimeSquared ).mulAdd ( scratchVec3, deltaTime );

        // update the movement component velocity with v += a * dt
        mov.velocityMeters.mulAdd ( mov.accelerationMeters, deltaTime );
        // TODO: might want to zero the acceleration here

        // convert deltaPosition to screen coordinates, add to our transform component position, and enforce bounds
        enforceWorldBoundsPixels ( xfrm.screenPositionPixels.mulAdd ( deltaPosition, PIXELS_PER_METER ) );

    }
}
