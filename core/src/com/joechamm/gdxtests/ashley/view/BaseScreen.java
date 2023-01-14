package com.joechamm.gdxtests.ashley.view;

/**
 * File:    BaseScreen
 * Package: com.joechamm.gdxtests.controls.view
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/4/2023 at 6:25 PM
 */

import static com.joechamm.gdxtests.ashley.Constants.VIEWPORT_HEIGHT;
import static com.joechamm.gdxtests.ashley.Constants.VIEWPORT_WIDTH;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.joechamm.gdxtests.ashley.JCGdxTestAshley;

public abstract class BaseScreen implements Screen, InputProcessor {

    public static final String TAG = BaseScreen.class.getName ();

    private JCGdxTestAshley parent;

    protected Stage mainStage;
    protected Stage uiStage;
    protected Table uiTable;

    public BaseScreen ( JCGdxTestAshley jcGdxTestAshley ) {
        this.parent = jcGdxTestAshley;

        mainStage = new Stage ( new FitViewport ( VIEWPORT_WIDTH, VIEWPORT_HEIGHT ) );
        uiStage = new Stage ( new FitViewport ( VIEWPORT_WIDTH, VIEWPORT_HEIGHT ) );

        uiTable = new Table ();
        uiTable.setFillParent ( true );
        uiStage.addActor ( uiTable );

        initialize();
    }

    // Gameloop:
    // (1) process input (discrete handled by listener; continuous in update)
    // (2) update game logic
    // (3) render the graphics

    protected JCGdxTestAshley getParent() {
        return parent;
    }

    public abstract void initialize ();

    public abstract void update(float dt);


    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     *
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown ( int keycode ) {
        return false;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     *
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp ( int keycode ) {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     *
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped ( char character ) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     *
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown ( int screenX, int screenY, int pointer, int button ) {
        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     *
     * @return whether the input was processed
     */
    @Override
    public boolean touchUp ( int screenX, int screenY, int pointer, int button ) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     *
     * @return whether the input was processed
     */
    @Override
    public boolean touchDragged ( int screenX, int screenY, int pointer ) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     *
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved ( int screenX, int screenY ) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amountX the horizontal scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @param amountY the vertical scroll amount, negative or positive depending on the direction the wheel was scrolled.
     *
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled ( float amountX, float amountY ) {
        return false;
    }

    /** Called when this screen becomes the current screen for a {@link Game}.
     * Set up InputMultiplexer here, in case screen is reactivated at a later time
     * */
    @Override
    public void show () {
        InputMultiplexer inputMultiplexer = (InputMultiplexer)Gdx.input.getInputProcessor ();
        inputMultiplexer.addProcessor ( this );
        inputMultiplexer.addProcessor ( uiStage );
        inputMultiplexer.addProcessor ( mainStage );
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render ( float delta ) {

        // act methods
        uiStage.act ( delta );
        mainStage.act (delta);

        // defined by user
        update ( delta );

        // clear the screen
        Gdx.gl.glClearColor ( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear ( GL20.GL_COLOR_BUFFER_BIT );

        // draw the graphics
        mainStage.draw ();
        uiStage.draw ();

    }

    /**
     * @param width
     * @param height
     *
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize ( int width, int height ) {

    }

    /** @see ApplicationListener#pause() */
    @Override
    public void pause () {

    }

    /** @see ApplicationListener#resume() */
    @Override
    public void resume () {

    }

    /** Called when this screen is no longer the current screen for a {@link Game}.
     * Screen class and Stages no longer process input.
     * Other InputProcessors must be removed manually.
     * */
    @Override
    public void hide () {
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor ();
        inputMultiplexer.removeProcessor ( this );
        inputMultiplexer.removeProcessor ( uiStage );
        inputMultiplexer.removeProcessor ( mainStage );
    }

    /** Called when this screen should release all resources. */
    @Override
    public void dispose () {

    }


}
