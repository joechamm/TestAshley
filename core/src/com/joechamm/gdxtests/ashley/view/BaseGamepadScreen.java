package com.joechamm.gdxtests.ashley.view;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.joechamm.gdxtests.ashley.JCGdxTestAshley;

/**
 * File:    BaseGamepadScreen
 * Package: com.joechamm.gdxtests.controls.view
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/4/2023 at 10:31 PM
 */
public abstract class BaseGamepadScreen extends BaseScreen
        implements ControllerListener {

    public static final String TAG = BaseGamepadScreen.class.getName ();

    public BaseGamepadScreen ( JCGdxTestAshley jcGdxTestAshley ) {
        super ( jcGdxTestAshley );
        Controllers.clearListeners ();
        Controllers.addListener ( this );
    }

    /**
     * A {@link Controller} got connected.
     *
     * @param controller
     */
    @Override
    public void connected ( Controller controller ) {

    }

    /**
     * A {@link Controller} got disconnected.
     *
     * @param controller
     */
    @Override
    public void disconnected ( Controller controller ) {

    }

    /**
     * A button on the {@link Controller} was pressed. The buttonCode is controller specific. The
     * <code>com.badlogic.gdx.controllers.mapping</code> package hosts button constants for known controllers.
     *
     * @param controller
     * @param buttonCode
     *
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean buttonDown ( Controller controller, int buttonCode ) {
        return false;
    }

    /**
     * A button on the {@link Controller} was released. The buttonCode is controller specific. The
     * <code>com.badlogic.gdx.controllers.mapping</code> package hosts button constants for known controllers.
     *
     * @param controller
     * @param buttonCode
     *
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean buttonUp ( Controller controller, int buttonCode ) {
        return false;
    }

    /**
     * An axis on the {@link Controller} moved. The axisCode is controller specific. The axis value is in the range [-1, 1]. The
     * <code>com.badlogic.gdx.controllers.mapping</code> package hosts axes constants for known controllers.
     *
     * @param controller
     * @param axisCode
     * @param value      the axis value, -1 to 1
     *
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean axisMoved ( Controller controller, int axisCode, float value ) {
        return false;
    }
}
