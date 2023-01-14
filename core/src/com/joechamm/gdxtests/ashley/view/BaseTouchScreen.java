package com.joechamm.gdxtests.ashley.view;

import static com.joechamm.gdxtests.ashley.Constants.CONTROLS_HEIGHT;
import static com.joechamm.gdxtests.ashley.Constants.VIEWPORT_CONTROLS_HEIGHT;
import static com.joechamm.gdxtests.ashley.Constants.VIEWPORT_CONTROLS_WIDTH;
import static com.joechamm.gdxtests.ashley.Constants.VIEWPORT_HEIGHT;
import static com.joechamm.gdxtests.ashley.Constants.VIEWPORT_WIDTH;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.joechamm.gdxtests.ashley.JCGdxTestAshley;

/**
 * File:    BaseTouchScreen
 * Package: com.joechamm.gdxtests.controls.view
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/4/2023 at 7:09 PM
 */
public abstract class BaseTouchScreen extends BaseScreen {

    public static final String TAG = BaseTouchScreen.class.getName ();

    protected Stage controlStage;
    protected Table controlTable;

    public BaseTouchScreen ( JCGdxTestAshley jcGdxTestAshley ) {
        super ( jcGdxTestAshley );
    }

    // run during initialize method
    public void initializeControlArea() {
        controlStage = new Stage ( new FitViewport ( VIEWPORT_CONTROLS_WIDTH, VIEWPORT_CONTROLS_HEIGHT ) );
        controlTable = new Table ();
        controlTable.setFillParent ( true );
        controlStage.addActor ( controlTable );
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     * Set up InputMultiplexer here, in case screen is reactivated at a later time
     */
    @Override
    public void show () {
        super.show ();
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor ();
        inputMultiplexer.addProcessor ( controlStage );
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render ( float delta ) {
        // act methods
        uiStage.act (delta);
        mainStage.act (delta);
        controlStage.act (delta);

        // defined by user
        update ( delta );

        // Clear the screen
        Gdx.gl.glClearColor ( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear ( GL20.GL_COLOR_BUFFER_BIT );

        // set the drawing regions to draw the main stage and uiStage
        Gdx.gl.glViewport ( 0, CONTROLS_HEIGHT, VIEWPORT_WIDTH, VIEWPORT_HEIGHT );
        mainStage.draw ();
        uiStage.draw ();

        Gdx.gl.glViewport ( 0, 0, VIEWPORT_CONTROLS_WIDTH, VIEWPORT_CONTROLS_HEIGHT );
        controlStage.draw ();
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     * Screen class and Stages no longer process input.
     * Other InputProcessors must be removed manually.
     */
    @Override
    public void hide () {
        super.hide ();
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor ();
        inputMultiplexer.removeProcessor ( controlStage );
    }
}
