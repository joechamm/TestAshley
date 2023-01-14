package com.joechamm.gdxtests.ashley.view;

/**
 * File:    PreferencesScreen
 * Package: com.joechamm.gdxtests.controls.view
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/1/2023 at 10:22 PM
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.joechamm.gdxtests.ashley.JCGdxTestAshley;

public class PreferencesScreen implements
                               Screen,
                               InputProcessor,
                               ControllerListener {

    public static final String TAG = PreferencesScreen.class.getName ();

    JCGdxTestAshley parent;

    Stage stage;
    Skin skin;

    public PreferencesScreen ( JCGdxTestAshley jcGdxTestAshley ) {
        Gdx.app.debug ( TAG, "ctor" );
        parent = jcGdxTestAshley;

        stage = new Stage ( new ScreenViewport () );
    }

    @Override
    public void show () {

  //      stage = new Stage ( new ScreenViewport () );
        Gdx.app.debug ( TAG, "show" );

        stage.clear ();
  //      Gdx.input.setInputProcessor ( stage );
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor ();
        inputMultiplexer.addProcessor ( stage );
        inputMultiplexer.addProcessor ( this );

        stage.setDebugAll ( true );

        skin = parent.assetManager.manager.get ( parent.assetManager.skinJson );

        Table table = new Table();
        table.setFillParent(true);

        Stack stack = new Stack();

        Image image = new Image(skin, "Starscape00");
  //      image.setTouchable(disabled);
        image.setScaling(Scaling.fill);
        stack.addActor(image);

        Table table1 = new Table();
        table1.setName("prefBaseTable");
   //     table1.setTouchable(disabled);
        table1.pad(5.0f);

        Label label = new Label("PREFERENCES", skin);
        label.setName("prefSceneLabel");
        label.setAlignment(Align.center);
        label.setColor(skin.getColor("CYAN"));
        table1.add(label).pad(5.0f).fill(true).uniform().colspan(3);

        table1.row();
        Table table2 = new Table();
        table2.setName("prefSliderTable");
 //       table2.setTouchable(enabled);

        label = new Label("Volume", skin);
        label.setAlignment(Align.center);
        table2.add(label).pad(5.0f).align(Align.top).colspan(3);

        table2.row();
        label = new Label("Sound Effects", skin);
        label.setAlignment(Align.center);
        table2.add(label).pad(5.0f).spaceRight(5.0f).align(Align.right);

        final Slider volumeSoundSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin, "default-horizontal");
        volumeSoundSlider.setName("volumeSoundSlider");
        table2.add(volumeSoundSlider).pad(5.0f).colspan(2);

        table2.row();
        label = new Label("Music", skin);
        table2.add(label).pad(5.0f).spaceRight(5.0f).align(Align.right);

        final Slider volumeMusicSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin, "default-horizontal");
        volumeMusicSlider.setName("volumeMusicSlider");
        table2.add(volumeMusicSlider).pad(5.0f).colspan(2);
        table1.add(table2).pad(5.0f).fill(true).uniform();

        table1.row();
        table2 = new Table();
        table2.setName("prefCheckboxTable");
 //       table2.setTouchable(enabled);

        label = new Label("Enable", skin);
        label.setName("enabledLabel");
        table2.add(label).pad(5.0f).uniform().colspan(2);

        table2.row();
        final CheckBox soundCheckBox = new CheckBox(" Sound Effects", skin);
        soundCheckBox.setName("soundCheckbox");
   //     soundCheckBox.setChecked(true);
        soundCheckBox.setColor(skin.getColor("WHITE"));
        table2.add(soundCheckBox).pad(5.0f).uniform();

        final CheckBox musicCheckbox = new CheckBox(" Music", skin);
        musicCheckbox.setName("musicCheckbox");
  //      musicCheckbox.setChecked(true);
        table2.add(musicCheckbox).pad(5.0f).uniform();
        table1.add(table2).pad(5.0f).fill(true).uniform();
        table1.row();

        // Add the game controls method table
        table2 = new Table ();
        table2.setName ( "prefRadioButtonTable" );

        label = new Label ( "Controls Method", skin );
        label.setName ( "controlsMethodLabel" );
        table2.add (label).pad ( 5.0f ).uniform ().align ( Align.top );

        table2.row ();

        // setup our radio buttons
        final CheckBox radioCheckboxControlsMethodTouch = new CheckBox ( "Touch", skin, "radio" );
        radioCheckboxControlsMethodTouch.setChecked ( true );
        radioCheckboxControlsMethodTouch.addListener ( new EventListener () {
            @Override
            public boolean handle ( Event event ) {
                Gdx.app.debug ( TAG, "radioCheckboxControlsMethodTouch handle event" );
                if ( radioCheckboxControlsMethodTouch.isChecked () ) {
                    parent.getPreferences ().setControlsMethodTouch ();
                }
                return false;
            }
        } );

        final CheckBox radioCheckboxControlsMethodGamepad = new CheckBox ( "Gamepad", skin, "radio" );
        radioCheckboxControlsMethodGamepad.setChecked ( false );
        radioCheckboxControlsMethodGamepad.addListener ( new EventListener () {
            @Override
            public boolean handle ( Event event ) {
                Gdx.app.debug ( TAG, "radioCheckboxControlsMethodGamepad handle event" );
                if ( radioCheckboxControlsMethodGamepad.isChecked () ) {
                    parent.getPreferences ().setControlsMethodGamepad ();
                }
                return false;
            }
        } );

        final ButtonGroup<CheckBox> controlsMethodButtonGroup = new ButtonGroup<>();
        controlsMethodButtonGroup.add ( radioCheckboxControlsMethodTouch );
        controlsMethodButtonGroup.add ( radioCheckboxControlsMethodGamepad );
        controlsMethodButtonGroup.setMinCheckCount ( 1 );
        controlsMethodButtonGroup.setMaxCheckCount ( 1 );

        table2.add ( radioCheckboxControlsMethodTouch ).pad ( 5.0f ).uniform ().align ( Align.left );
        table2.add ( radioCheckboxControlsMethodGamepad ).pad ( 5.0f ).uniform ().align ( Align.right );

        table1.add(table2).pad(5.0f).fill(true).uniform();
        table1.row();

        Container container = new Container();
        container.setName("backButtonContainer");

        final TextButton backButton = new TextButton("Back", skin);
        backButton.setName("backTextButton");
        container.setActor(backButton);
        table1.add(container).pad(5.0f).fill(true).uniform();
        stack.addActor(table1);
        table.add(stack);
        stage.addActor(table);

        // set values and add listeners
        volumeSoundSlider.setValue ( parent.getPreferences ().getSoundVolume () );
        volumeSoundSlider.addListener ( new EventListener () {
            @Override
            public boolean handle ( Event event ) {
                Gdx.app.debug ( TAG, "volumeSoundSlider handle event" );
                parent.getPreferences ().setSoundVolume ( volumeSoundSlider.getValue () );
                parent.audioManager.updateSoundVolume ();
                return false;
            }
        } );

        volumeMusicSlider.setValue ( parent.getPreferences ().getMusicVolume () );
        volumeMusicSlider.addListener ( new EventListener () {
            @Override
            public boolean handle ( Event event ) {
                Gdx.app.debug ( TAG, "volumeMusicSlider handle event" );
                parent.getPreferences ().setMusicVolume ( volumeMusicSlider.getValue () );
                parent.audioManager.updateMusicVolume ();
                return false;
            }
        } );

        soundCheckBox.setChecked ( parent.getPreferences ().isSoundEffectsEnabled () );
        soundCheckBox.addListener ( new EventListener () {
            @Override
            public boolean handle ( Event event ) {
                Gdx.app.debug ( TAG, "soundCheckBox handle event" );
                boolean enabled = soundCheckBox.isChecked ();
                parent.getPreferences ().setSoundEffectsEnabled ( enabled );
                parent.audioManager.updateSoundEffectsOn ();
                return false;
            }
        } );

        musicCheckbox.setChecked ( parent.getPreferences ().isMusicEnabled () );
        musicCheckbox.addListener ( new EventListener () {
            @Override
            public boolean handle ( Event event ) {
                Gdx.app.debug ( TAG, "musicCheckbox handle event" );
                boolean enabled = musicCheckbox.isChecked ();
                parent.getPreferences ().setMusicEnabled ( enabled );
                parent.audioManager.updateMusicOn ();
                return false;
            }
        } );

        backButton.addListener ( new ChangeListener () {
            @Override
            public void changed ( ChangeEvent event, Actor actor ) {
                Gdx.app.debug ( TAG, "backButton handle event" );
                parent.changeScreen ( JCGdxTestAshley.MENU );
            }
        } );

    }

    @Override
    public void render ( float delta ) {

        Gdx.gl.glClearColor ( 0, 0, 0, 1 );
        Gdx.gl.glClear ( GL20.GL_COLOR_BUFFER_BIT );

//        stage.act (delta);
        stage.act ( Math.min ( Gdx.graphics.getDeltaTime (), 1 / 30f ) );
        stage.draw ();

    }

    @Override
    public void resize ( int width, int height ) {
        Gdx.app.debug ( TAG, "resize" );
        stage.getViewport ().update ( width, height, true );
    }

    @Override
    public void pause () {
        Gdx.app.debug ( TAG, "pause" );
    }

    @Override
    public void resume () {
        Gdx.app.debug ( TAG, "resume" );
    }

    @Override
    public void hide () {
        Gdx.app.debug ( TAG, "hide" );
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor ();
        inputMultiplexer.removeProcessor ( this );
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
