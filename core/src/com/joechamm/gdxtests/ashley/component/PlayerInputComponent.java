package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    PlayerInputComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/12/2023 at 10:35 AM
 *
 * TODO
 * How is the player going to give us input?
 *
 */
public class PlayerInputComponent implements Component, Pool.Poolable {

    /** how should we poll for player input? **/
    public static enum Type {
        INPUT_TYPE_KEYBOARD,
        INPUT_TYPE_TOUCH,
        INPUT_TYPE_GAMEPAD,
        INPUT_TYPE_NONE
    }

    /** input type **/
    public Type inputType = Type.INPUT_TYPE_NONE;
    /** gamepad controller **/
    public Controller playerController = null;
    /** the player index starting at 0, with -1 to indicate inactive **/
    public int playerIndex = - 1;

    /** the player ship being controlled **/
    public Entity playerShip = null;

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        playerController = null;
        playerIndex = - 1;
        inputType = Type.INPUT_TYPE_NONE;
        playerShip = null;
    }

}
