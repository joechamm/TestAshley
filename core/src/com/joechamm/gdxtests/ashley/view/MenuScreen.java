package com.joechamm.gdxtests.ashley.view;

/**
 * File:    MenuScreen
 * Package: com.joechamm.gdxtests.controls.view
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/1/2023 at 1:21 PM
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.joechamm.gdxtests.ashley.JCGdxTestAshley;

public class MenuScreen implements
                        Screen,
                        InputProcessor,
                        ControllerListener {

    private static final String TAG = MenuScreen.class.getName ();

    private Stage stage;
    private Skin skin;

    private JCGdxTestAshley parent;

    public MenuScreen( JCGdxTestAshley jcGdxTestAshley ) {
        parent = jcGdxTestAshley;

        stage = new Stage ( new ScreenViewport () );

        skin = parent.assetManager.manager.get ( parent.assetManager.skinJson );
    }

    @Override
    public void show () {

    //    stage = new Stage (new ScreenViewport ());
        Gdx.app.debug ( TAG, "show" );

        stage.clear ();
  //      Gdx.input.setInputProcessor ( stage );
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor ();
        inputMultiplexer.addProcessor ( stage );
        inputMultiplexer.addProcessor ( this );

        stage.setDebugAll ( true );

  //      skin = parent.assetManager.manager.get ( parent.assetManager.skinJson );

        // create buttons and add change listeners
        final TextButton newGameButton = new TextButton("New Game", skin);
        newGameButton.setName("menuNewGameButton");
        newGameButton.addListener ( new ChangeListener () {
            @Override
            public void changed ( ChangeEvent event, Actor actor ) {
                Gdx.app.debug ( TAG, "New Game Pressed..." );
                // TODO: implement
                parent.changeScreen ( JCGdxTestAshley.APPLICATION );
            }
        } );

        final TextButton preferencesButton = new TextButton("Preferences", skin);
        preferencesButton.setName("menuPreferencesButton");
        preferencesButton.addListener ( new ChangeListener () {
            @Override
            public void changed ( ChangeEvent event, Actor actor ) {
                Gdx.app.debug ( TAG, "Preferences Pressed..." );
                parent.changeScreen ( JCGdxTestAshley.PREFERENCES );
                // TODO: implement
            }
        } );

        final TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setName("menuExitButton");
        exitButton.addListener ( new ChangeListener () {
            @Override
            public void changed ( ChangeEvent event, Actor actor ) {
                Gdx.app.debug ( TAG, "Exit Pressed..." );
                Gdx.app.exit ();
                // TODO: implement
            }
        } );

        Table table = new Table();
        table.setFillParent(true);

        Stack stack = new Stack();

        Image image = new Image(skin, "Starscape00");
  //      image.setTouchable(disabled);
        image.setScaling(Scaling.fill);
        stack.addActor(image);

        Table table1 = new Table();
        table1.setName("menuBaseTable");
  //      table1.setTouchable(disabled);
        table1.pad(5.0f);

        image = new Image(skin, "title");
        image.setName("menuTitleImage");
        image.setScaling(Scaling.fit);
        table1.add(image).pad(20.0f).spaceBottom(30.0f).fill(true).align(Align.top).uniformX();

        table1.row();
        Table table2 = new Table();
        table2.setName("menuTable");
        table2.pad(10.0f);


        table2.add(newGameButton).pad(5.0f).fillX().uniformX();

        table2.row();

        table2.add(preferencesButton).pad(5.0f).fillX().uniformX();

        table2.row();

        table2.add(exitButton).pad(5.0f).fillX().uniformX();
        table1.add(table2).fill(true).uniformX();
        stack.addActor(table1);
        table.add(stack);
        stage.addActor(table);

    }

    @Override
    public void render ( float delta ) {

        Gdx.gl.glClearColor ( 0, 0, 0, 1 );
        Gdx.gl.glClear ( GL20.GL_COLOR_BUFFER_BIT );

   //     stage.act (delta);
        stage.act ( Math.min ( Gdx.graphics.getDeltaTime (), 1 / 30f ) );
        stage.draw ();
    }

    @Override
    public void resize ( int width, int height ) {
        Gdx.app.debug ( TAG, "resize dimensions: " + width + "x" + height );
        stage.getViewport ().update ( width, height, true );

    }

    /** @see ApplicationListener#pause() */
    @Override
    public void pause () {
        Gdx.app.debug ( TAG, "pause" );

    }

    /** @see ApplicationListener#resume() */
    @Override
    public void resume () {
        Gdx.app.debug ( TAG, "resume" );

    }

    /** Called when this screen is no longer the current screen for a {@link Game}. */
    @Override
    public void hide () {
        Gdx.app.debug ( TAG, "hide" );
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor ();
        inputMultiplexer.removeProcessor ( this );
        inputMultiplexer.removeProcessor ( stage );
    }

    @Override
    public void dispose () {
        Gdx.app.debug ( TAG, "dispose" );
        stage.dispose ();
    }

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
