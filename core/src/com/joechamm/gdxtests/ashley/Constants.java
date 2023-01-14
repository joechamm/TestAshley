package com.joechamm.gdxtests.ashley;

import com.badlogic.gdx.math.MathUtils;

/**
 * File:    Constants
 * Package: com.joechamm.gdxtests.controls
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/4/2023 at 6:29 PM
 */
public class Constants {

    public static final String TAG = Constants.class.getName ();

    public static final float TOUCH_MOVEMENT_THRESHOLD = 0.5f;

    public static final int VIEWPORT_WIDTH = 800;
    public static final int VIEWPORT_HEIGHT = 600;

    public static final int CONTROLS_WIDTH = VIEWPORT_WIDTH;
    public static final int CONTROLS_HEIGHT = 200;

    public static final int VIEWPORT_CONTROLS_WIDTH = Math.max ( VIEWPORT_WIDTH, CONTROLS_WIDTH);
    public static final int VIEWPORT_CONTROLS_HEIGHT = VIEWPORT_HEIGHT + CONTROLS_HEIGHT;

    public static final float MAX_SPEED_LINEAR_METERS = 2.0f; // max speed an entity can go in m/s
    public static final float MAX_SPEED_LINEAR_METERS_SQ = MAX_SPEED_LINEAR_METERS * MAX_SPEED_LINEAR_METERS; // linear speed squared
    public static final float MAX_ACCELERATION_LINEAR_METERS = 5.0f; // max acceleration in m/s^2
    public static final float MAX_ACCELERATION_LINEAR_METERS_SQ = MAX_ACCELERATION_LINEAR_METERS * MAX_ACCELERATION_LINEAR_METERS;
    public static final float MAX_SPEED_ANGULAR_DEG = 50f; // max turning speed in deg/s
    public static final float MAX_SPEED_ANGULAR_RAD = MAX_SPEED_ANGULAR_DEG * MathUtils.degreesToRadians;
    public static final float MAX_ACCELERATION_ANGULAR_DEG = 5f; // max turning acceleration in deg/s^2
    public static final float MAX_ACCELERATION_ANGULAR_RAD = MAX_ACCELERATION_ANGULAR_DEG * MathUtils.degreesToRadians;
    public static final float ZERO_THRESHOLD = 0.1f; // how accurate should checks be for target location checks
    public static final float BOUNDING_RADIUS = 1.0f; // minimum radius size for a circle required to cover whole object
    public static final float WORLD_WIDTH_METERS = 5.0f;
    public static final float WORLD_HEIGHT_METERS = 5.0f;
    public static final float PIXELS_PER_METER = 16.0f;
    public static final float PIXELS_TO_METERS = 1.0f / PIXELS_PER_METER;

    public static final int ANIMATION_EXPLOSION_ROW_COUNT = 4;
    public static final int ANIMATION_EXPLOSION_COL_COUNT = 4;
    public static final int ANIMATION_EXPLOSION_FRAME_COUNT = ANIMATION_EXPLOSION_COL_COUNT * ANIMATION_EXPLOSION_ROW_COUNT;

    public static final float ANIMATION_EXPLOSION_RUNTIME = 0.7f; // 0.7 second long
    public static final float ANIMATION_EXPLOSION_FRAMETIME = ANIMATION_EXPLOSION_RUNTIME / ANIMATION_EXPLOSION_FRAME_COUNT;

    public static final int PLAYER_STARTING_LIVES = 3;
    public static final float PLAYER_STARTING_HULL_POINTS = 100.0f;

}
