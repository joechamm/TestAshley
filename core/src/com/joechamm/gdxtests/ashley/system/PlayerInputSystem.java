package com.joechamm.gdxtests.ashley.system;

import static com.joechamm.gdxtests.ashley.Constants.*;
import static com.joechamm.gdxtests.ashley.Constants.MAX_ACCELERATION_LINEAR_METERS_SQ;
import static com.joechamm.gdxtests.ashley.Constants.MAX_SPEED_ANGULAR_RAD;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.joechamm.gdxtests.ashley.Constants;
import com.joechamm.gdxtests.ashley.component.LaserCannonComponent;
import com.joechamm.gdxtests.ashley.component.Mappers;
import com.joechamm.gdxtests.ashley.component.MovementComponent;
import com.joechamm.gdxtests.ashley.component.PlayerInputComponent;
import com.joechamm.gdxtests.ashley.component.ShipComponent;
import com.joechamm.gdxtests.ashley.component.StateComponent;
import com.joechamm.gdxtests.ashley.component.TransformComponent;

/**
 * File:    PlayerInputSystem
 * Package: com.joechamm.gdxtests.ashley.system
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/12/2023 at 10:37 AM
 */
public class PlayerInputSystem extends IteratingSystem {

    public static final String TAG = PlayerInputSystem.class.getName ();

    /** scratch variables **/
    private final Vector2 scrathVec2 = new Vector2 ();
    private float deltaRads = 0f;
    private boolean fireLaser = false;

    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family The family of entities iterated over in this System
     */
    public PlayerInputSystem ( Family family ) {
        super ( family );
    }

    public PlayerInputSystem() {
        this ( Family.all ( PlayerInputComponent.class ).get () );
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
        PlayerInputComponent playerInputComponent = Mappers.playerInputCM.get ( entity );

        if ( playerInputComponent == null ||
                playerInputComponent.inputType == PlayerInputComponent.Type.INPUT_TYPE_NONE) {
            return;
        }

        switch ( playerInputComponent.inputType ) {
            case INPUT_TYPE_KEYBOARD:
                handleKeyInput ( entity, deltaTime );
                break;
            case INPUT_TYPE_TOUCH:
                handleTouchInput ( entity, deltaTime );
                break;
            case INPUT_TYPE_GAMEPAD:
                handleGamepadInput ( entity, deltaTime, playerInputComponent.playerController );
                break;
            default:
                break;
        }

    }

    private void handleKeyInput(Entity entity, float deltaTime) {
        // TODO

        LaserCannonComponent laserCannonComponent = Mappers.laserCannonCM.get ( entity );
        MovementComponent movementComponent = Mappers.movementCM.get ( entity );
        StateComponent stateComponent = Mappers.stateCM.get ( entity );
        TransformComponent transformComponent = Mappers.transformCM.get ( entity );

        // get the left axis values
        //float axisX = controller.getAxis ( controller.getMapping ().axisLeftX );
        //float axisY = controller.getAxis ( controller.getMapping ().axisLeftY );

        float axisX = 0f;
        float axisY = 0f;

        // poll the keyboard for left/right
        if ( Gdx.input.isKeyPressed ( Input.Keys.LEFT ) ) {
            axisX = - 1f;
        } else if ( Gdx.input.isKeyPressed ( Input.Keys.RIGHT ) ) {
            axisX = 1f;
        }

        // poll the keyboard for up/down
        if ( Gdx.input.isKeyPressed ( Input.Keys.UP ) ) {
            axisY = 1f;
        } else if ( Gdx.input.isKeyPressed ( Input.Keys.DOWN ) ) {
            axisY = - 1f;
        }

        // poll the keyboard for space pressed
        boolean fireButtonPressed = Gdx.input.isKeyPressed ( Input.Keys.SPACE );

        // get the current heading
        float headingRads = transformComponent.rotation * MathUtils.degreesToRadians;

        // calculate the new heading
        deltaRads = MAX_SPEED_ANGULAR_RAD * axisX * deltaTime;

        headingRads = Math.max ( Math.min ( headingRads + deltaRads, MathUtils.PI2 ), 0f );

        // make the laser cannon point in the same direction
        laserCannonComponent.headingRads = headingRads;

        // check if the y axis is beyond the threshold and set the acceleration vector in the direction of the new heading
        if ( Math.abs ( axisY ) >= ZERO_THRESHOLD ) {

            float accelMag = axisY * MAX_ACCELERATION_LINEAR_METERS;
            movementComponent.accelerationMeters.set ( 1f, 0f ).setAngleRad ( headingRads ).scl ( accelMag );

        } else {
            movementComponent.accelerationMeters.setZero ();
        }

        // check to see if we need to fire the laser cannon
        laserCannonComponent.timeSinceLastShot += deltaTime;
        if ( laserCannonComponent.timeSinceLastShot > laserCannonComponent.laserRechargeTime &&
                stateComponent.get () == StateComponent.STATE_NORMAL &&
                fireButtonPressed ) {

            laserCannonComponent.justFired = true;
            stateComponent.set ( StateComponent.STATE_SHOOTING );

        }
    }

    private void handleTouchInput(Entity entity, float deltaTime) {
        // TODO
        LaserCannonComponent laserCannonComponent = Mappers.laserCannonCM.get ( entity );
        MovementComponent movementComponent = Mappers.movementCM.get ( entity );
        StateComponent stateComponent = Mappers.stateCM.get ( entity );
        TransformComponent transformComponent = Mappers.transformCM.get ( entity );

        float axisX = 0f;
        float axisY = 0f;
        boolean fireButtonPressed = false;

        if ( Gdx.input.isTouched () ) {
            int dxPixels = Gdx.input.getDeltaX ();
            int dyPixels = Gdx.input.getDeltaY ();
            int screenWidthPixels = Gdx.graphics.getWidth ();
            int screenHeightPixels = Gdx.graphics.getHeight ();

            axisX = (float)dxPixels / screenWidthPixels;
            axisY = (float)dyPixels / screenHeightPixels;
            fireButtonPressed = true;
        }

        if ( Gdx.input.isButtonPressed ( Input.Buttons.RIGHT ) ||
                Gdx.input.isButtonPressed ( Input.Buttons.MIDDLE ) ||
                Gdx.input.isButtonPressed ( Input.Buttons.LEFT ) ||
                Gdx.input.isKeyPressed ( Input.Keys.SPACE ) ) {
            fireButtonPressed = true;
        }


        // get the current heading
        float headingRads = transformComponent.rotation * MathUtils.degreesToRadians;

        // calculate the new heading
        deltaRads = MAX_SPEED_ANGULAR_RAD * axisX * deltaTime;

        headingRads = Math.max ( Math.min ( headingRads + deltaRads, MathUtils.PI2 ), 0f );

        // make the laser cannon point in the same direction
        laserCannonComponent.headingRads = headingRads;

        // check if the y axis is beyond the threshold and set the acceleration vector in the direction of the new heading
        if ( Math.abs ( axisY ) >= ZERO_THRESHOLD ) {

            float accelMag = axisY * MAX_ACCELERATION_LINEAR_METERS;
            movementComponent.accelerationMeters.set ( 1f, 0f ).setAngleRad ( headingRads ).scl ( accelMag );

        } else {
            movementComponent.accelerationMeters.setZero ();
        }

        // check to see if we need to fire the laser cannon
        laserCannonComponent.timeSinceLastShot += deltaTime;
        if ( laserCannonComponent.timeSinceLastShot > laserCannonComponent.laserRechargeTime &&
                stateComponent.get () == StateComponent.STATE_NORMAL &&
                fireButtonPressed ) {

            laserCannonComponent.justFired = true;
            stateComponent.set ( StateComponent.STATE_SHOOTING );

        }
    }

    private void handleGamepadInput(Entity entity, float deltaTime, Controller controller) {
        // TODO

        LaserCannonComponent laserCannonComponent = Mappers.laserCannonCM.get ( entity );
        MovementComponent movementComponent = Mappers.movementCM.get ( entity );
        StateComponent stateComponent = Mappers.stateCM.get ( entity );
        TransformComponent transformComponent = Mappers.transformCM.get ( entity );

        // get the left axis values
        float axisX = controller.getAxis ( controller.getMapping ().axisLeftX );
        float axisY = controller.getAxis ( controller.getMapping ().axisLeftY );
        // get the state of the fire button
        boolean fireButtonPressed = controller.getButton ( controller.getMapping ().buttonA );
        // get the current heading
        float headingRads = transformComponent.rotation * MathUtils.degreesToRadians;

        // calculate the new heading
        deltaRads = MAX_SPEED_ANGULAR_RAD * axisX * deltaTime;

        headingRads = Math.max ( Math.min ( headingRads + deltaRads, MathUtils.PI2 ), 0f );

        // make the laser cannon point in the same direction
        laserCannonComponent.headingRads = headingRads;

        // check if the y axis is beyond the threshold and set the acceleration vector in the direction of the new heading
        if ( Math.abs ( axisY ) >= ZERO_THRESHOLD ) {

            float accelMag = axisY * MAX_ACCELERATION_LINEAR_METERS;
            movementComponent.accelerationMeters.set ( 1f, 0f ).setAngleRad ( headingRads ).scl ( accelMag );

        } else {
            movementComponent.accelerationMeters.setZero ();
        }

        // check to see if we need to fire the laser cannon
        laserCannonComponent.timeSinceLastShot += deltaTime;
        if ( laserCannonComponent.timeSinceLastShot > laserCannonComponent.laserRechargeTime &&
                stateComponent.get () == StateComponent.STATE_NORMAL &&
                fireButtonPressed ) {

            laserCannonComponent.justFired = true;
            stateComponent.set ( StateComponent.STATE_SHOOTING );

        }

    }
}
