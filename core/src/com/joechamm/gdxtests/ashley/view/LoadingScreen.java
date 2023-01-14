package com.joechamm.gdxtests.ashley.view;

/**
 * File:    LoadingScreen
 * Package: com.joechamm.gdxtests.controls.view
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/1/2023 at 5:10 PM
 */

import static com.badlogic.gdx.scenes.scene2d.Touchable.disabled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.joechamm.gdxtests.ashley.JCGdxTestAshley;

import java.util.Locale;

public class LoadingScreen implements Screen {

    private static final String TAG = LoadingScreen.class.getName ();

    JCGdxTestAshley parent;

    private Stage stage;
    private Skin skin;
    private ProgressBar progressBar;
    private Label countDownLabel;

    // loading stages
    public final int LOAD_IMAGES = 0;
    public final int LOAD_FONTS = 1;
    public final int LOAD_EFFECTS = 2;
    public final int LOAD_SOUNDS = 3;
    public final int LOAD_MUSIC = 4;
    public final int LOAD_EXPLOSIONS = 5;
    public final int LOAD_FINISHED = 6;

    private int currentLoadingStage = LOAD_IMAGES;

    public float countDown = 5f;

    public LoadingScreen ( JCGdxTestAshley jcGdxTestAshley ) {
        Gdx.app.debug ( TAG, "ctor" );

        parent = jcGdxTestAshley;

        stage = new Stage ( new ScreenViewport () );

        loadAssets();
    }

    private void loadAssets () {
        Gdx.app.debug ( TAG, "loadAssets" );

        // load skin
        parent.assetManager.queueAddSkin ();
        parent.assetManager.manager.finishLoading ();

        skin = parent.assetManager.manager.get ( parent.assetManager.skinJson );

        parent.assetManager.queueAddImages ();
        Gdx.app.debug ( TAG, "Loading images..." );
    }

    @Override
    public void show () {

        Gdx.app.debug ( TAG, "show" );

        Table table = new Table();
        table.setFillParent(true);

        Stack stack = new Stack();

        Image image = new Image(skin, "Starscape00-tiled");
        image.setTouchable(disabled);
        image.setScaling(Scaling.fill);
        stack.addActor(image);

        Table table1 = new Table();
        table1.setTouchable(disabled);
        table1.pad(10.0f);
        table1.align(Align.top);

        image = new Image(skin, "title");
        image.setName("titleImage");
        image.setTouchable(disabled);
        image.setScaling(Scaling.fit);
        table1.add(image).padLeft(20.0f).padRight(20.0f).padTop(40.0f).padBottom(10.0f).grow().uniform();

        table1.row();
        progressBar = new ProgressBar(0.0f, 100.0f, 1.0f, false, skin, "tiled-big");
        progressBar.setName("loadingProgressBar");
        table1.add(progressBar).pad(40.0f).growY().uniform();

        table1.row ();
        countDownLabel = new Label ( "loading images...", skin );
        countDownLabel.setName ( "countDownLabel" );
        table1.add (countDownLabel).pad ( 40.0f ).growY ().uniform ();

        stack.addActor(table1);
        table.add(stack);
        stage.addActor(table);

    }

    @Override
    public void render ( float delta ) {

        Gdx.gl.glClearColor ( 0, 0, 0, 1 );
        Gdx.gl.glClear ( GL20.GL_COLOR_BUFFER_BIT );

        if ( parent.assetManager.manager.update () ) {
            currentLoadingStage++;

            float progBarValue = ((float)currentLoadingStage / (float)LOAD_FINISHED) * 100.0f;

            progressBar.setValue ( progBarValue );

            switch ( currentLoadingStage ) {
                case LOAD_FONTS:
                    Gdx.app.debug ( TAG, "Loading fonts..." );
                    countDownLabel.setText ( "loading fonts..." );
                    parent.assetManager.queueAddFonts ();
                    break;
                case LOAD_EFFECTS:
                    Gdx.app.debug ( TAG, "Loading effects..." );
                    countDownLabel.setText ( "loading effects..." );
                    parent.assetManager.queueAddParticleEffects ();
                    break;
                case LOAD_SOUNDS:
                    Gdx.app.debug ( TAG, "Loading sounds..." );
                    countDownLabel.setText ( "loading sounds..." );
//                    parent.assetManager.queueAddSounds ();
                    if ( parent.audioManager.initSoundEffects () ) {
                        Gdx.app.debug ( TAG, "sound effects initialized" );
                    } else {
                        Gdx.app.error ( TAG, "OH NO! Something went wrong loading sounds." );
                        Gdx.app.exit ();
                    }
                    break;
                case LOAD_MUSIC:
                    Gdx.app.debug ( TAG, "Loading music..." );
                    countDownLabel.setText ( "loading music..." );
//                    parent.assetManager.queueAddMusic ();
                    if ( parent.audioManager.initMusic () ) {
                        Gdx.app.debug ( TAG, "music initialized" );
                        parent.audioManager.startPlayingSong ();
                    } else {
                        Gdx.app.error ( TAG, "OH NO! Something went wrong loading music." );
                        Gdx.app.exit ();
                    }
                    break;
                case LOAD_EXPLOSIONS:
                    Gdx.app.debug ( TAG, "Loading explosions..." );
                    countDownLabel.setText ( "loading explosions..." );
                    parent.assetManager.queueAddExplosionImages ();
                    break;
                case LOAD_FINISHED:
                    Gdx.app.debug ( TAG, "Finished loading..." );
                    break;
            }

            if ( currentLoadingStage >= LOAD_FINISHED ) {
                String str = String.format ( Locale.getDefault (), "starting in %d seconds...",
                                                  (int) Math.floor ( (float) countDown ) );

                countDownLabel.setText ( str.subSequence ( 0, str.length () - 1 ) );

                countDown -= delta;
                currentLoadingStage = LOAD_FINISHED;
                if(countDown < 0f) {
                    parent.changeScreen ( JCGdxTestAshley.MENU );
                }
            }
        }

  //      stage.act (delta);
        stage.act ( Math.min ( Gdx.graphics.getDeltaTime (), 1 / 30f ) );
        stage.draw ();
    }

    @Override
    public void resize ( int width, int height ) {
        Gdx.app.debug ( TAG, "resize dimensions: " + width + "x" + height );
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
    }

    @Override
    public void dispose () {
        Gdx.app.debug ( TAG, "dispose" );
        stage.dispose ();
    }

}
