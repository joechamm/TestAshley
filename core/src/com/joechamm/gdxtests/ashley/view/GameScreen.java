package com.joechamm.gdxtests.ashley.view;

/**
 * File:    GameScreen
 * Package: com.joechamm.gdxtests.controls.view
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/3/2023 at 12:14 AM
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.joechamm.gdxtests.ashley.EnemyShip;
import com.joechamm.gdxtests.ashley.Explosion;
import com.joechamm.gdxtests.ashley.JCGdxTestAshley;
import com.joechamm.gdxtests.ashley.Laser;
import com.joechamm.gdxtests.ashley.PlayerShip;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

public class GameScreen implements
                        Screen,
                        InputProcessor,
                        ControllerListener {

    public static final String TAG = GameScreen.class.getName ();

    // orchestrator
    private JCGdxTestAshley parent;

    // controller
//    private Controller controllerPlayer1;
    private boolean useGamepad = false;
    private Controller controllerPlayer1 = null;
    private int controllerPlayerIndex = - 1;
    private int controllerArrayIndex = - 1;

    // screen
    private Camera camera;
    private Viewport viewport;

    // graphics
    private SpriteBatch sb;
    private TextureAtlas atlas;
    private Texture explosionTexture;

    private TextureRegion[] backgrounds;
    private float backgroundHeight; // height of background in World units

    private TextureRegion playerShipTextureRegion, playerShieldTextureRegion,
            enemyShipTextureRegion, enemyShieldtextureRegion,
            playerLaserTextureRegion, enemyLaserTextureRegion;

    // timing
    private float[] backgroundOffsets = {0,0,0,0};
    private float backgroundMaxScrollingSpeed;
    private float timeBetweenEnemySpawns = 3f;
    private float enemySpawnTimer = 0;

    private boolean isPaused = false;

    // world parameters
    private final float WORLD_WIDTH = 72.0f;
    private final float WORLD_HEIGHT = 128.0f;
    private final float TOUCH_MOVEMENT_THRESHOLD = 0.5f;

    // game objects
    private PlayerShip playerShip;
    private LinkedList<EnemyShip> enemyShipList;

    private LinkedList<Laser> playerLaserList;
    private LinkedList<Laser> enemyLaserList;
    private LinkedList<Explosion> explosionList;

    private int score = 0;

    // Heads-Up Display
    BitmapFont font;
    float hudVerticalMargin, hudLeftX, hudRightX, hudCenterX, hudRow1Y, hudRow2Y, hudSectionWidth;

    /// DEBUGGING
//    private DebugControls debugControls;

    public GameScreen( JCGdxTestAshley jcGdxTestAshley ) {
        Gdx.app.debug ( TAG, "ctor" );

        this.parent = jcGdxTestAshley;

        camera = new OrthographicCamera ();
        viewport = new StretchViewport ( WORLD_WIDTH, WORLD_HEIGHT, camera );

        if ( parent.getPreferences ().isGamepadEnabled () ) {
            useGamepad = true;

            try {
                if ( Controllers.getControllers ().isEmpty () ) {
                    throw new GdxRuntimeException ( "NO CONTROLLERS AVAILABLE" );
                }

                Controller currentController = Controllers.getCurrent ();

                if ( currentController != null ) {
                    controllerPlayer1 = currentController;
                    controllerArrayIndex = Controllers.getControllers ().indexOf ( currentController, true );
                    controllerPlayerIndex = controllerPlayer1.getPlayerIndex ();
                } else {
                    controllerPlayer1 = Controllers.getControllers ().first ();
                    controllerArrayIndex = Controllers.getControllers ().indexOf ( controllerPlayer1, true );
                    controllerPlayerIndex = controllerPlayer1.getPlayerIndex ();
                }

                Controllers.clearListeners ();
                Controllers.addListener ( this );

            } catch ( GdxRuntimeException e ) {
                Gdx.app.error ( TAG, e.getLocalizedMessage (), e.getCause () );
            }
        }

        atlas = parent.assetManager.manager.get ( parent.assetManager.gameImages, TextureAtlas.class );

        // setup background
        backgrounds = new TextureRegion[ 4 ];
        backgrounds[ 0 ] = atlas.findRegion ( "Starscape00" );
        backgrounds[ 1 ] = atlas.findRegion ( "Starscape01" );
        backgrounds[ 2 ] = atlas.findRegion ( "Starscape02" );
        backgrounds[ 3 ] = atlas.findRegion ( "Starscape03" );

        backgroundHeight = WORLD_HEIGHT * 2;
        backgroundMaxScrollingSpeed = (float)(WORLD_HEIGHT / 4);

        // initialize texture regions
        playerShipTextureRegion = atlas.findRegion ( "playerShip2_blue" );
        enemyShipTextureRegion = atlas.findRegion ( "enemyRed4" );
        playerShieldTextureRegion = atlas.findRegion ( "shield2" );
        enemyShieldtextureRegion = atlas.findRegion ( "shield1" );
        enemyShieldtextureRegion.flip ( false, true );
        playerLaserTextureRegion = atlas.findRegion ( "laserBlue03" );
        enemyLaserTextureRegion = atlas.findRegion ( "laserRed03" );

 //       explosionTexture = new Texture ( "explosion.png" );
        explosionTexture = parent.assetManager.manager.get ( parent.assetManager.explosionImages, Texture.class );

        // setup game objects
        playerShip = new PlayerShip ( WORLD_WIDTH / 2, WORLD_HEIGHT / 4,
                                      10, 10,
                                      48, 3,
                                      0.4f, 4, 45, 0.5f,
                                      playerShipTextureRegion, playerShieldTextureRegion, playerLaserTextureRegion);

        enemyShipList = new LinkedList<> ();


        playerLaserList = new LinkedList<> ();
        enemyLaserList = new LinkedList<> ();
        explosionList = new LinkedList<> ();

        sb = new SpriteBatch ();

        prepareHUD();
    }

    private void prepareHUD() {
        font = parent.assetManager.manager.get ( parent.assetManager.fontRegTransparent72, BitmapFont.class );

        // Scale the font to fit world
        font.getData ().setScale ( 0.08f );

        // calculate HUD margins, etc...
        hudVerticalMargin = font.getCapHeight () / 2;
        hudLeftX = hudVerticalMargin;
        hudRightX = WORLD_WIDTH * 2 / 3 - hudLeftX;
        hudCenterX = WORLD_WIDTH / 3;
        hudRow1Y = WORLD_HEIGHT - hudVerticalMargin;
        hudRow2Y = hudRow1Y - hudVerticalMargin - font.getCapHeight ();
        hudSectionWidth = WORLD_WIDTH / 3;
    }

    @Override
    public void show () {
        Gdx.app.debug ( TAG, "show" );
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor ();
        inputMultiplexer.addProcessor ( this );
    }

    @Override
    public void render ( float delta ) {

        //       Gdx.gl.glClearColor ( 0, 0, 0, 1 );
        //       Gdx.gl.glClear ( GL20.GL_COLOR_BUFFER_BIT );

        sb.begin ();

        // scrolling background
        renderBackground ( delta );

        detectInput ( delta );
        playerShip.update ( delta );

        spawnEnemyShips ( delta );

        ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator ();
        while ( enemyShipListIterator.hasNext () ) {
            EnemyShip enemyShip = enemyShipListIterator.next ();
            moveEnemy(enemyShip, delta);
            enemyShip.update ( delta );
            enemyShip.draw ( sb );
        }

        // player ships
        playerShip.draw ( sb );

        // lasers
        renderLasers ( delta );

        // detect collisions between lasers and ships
        detectCollisions();

        // explosions
        updateAndRenderExplosions ( delta);

        // hud rendering
        updateAndRenderHUD();

        sb.end ();
    }

    private void updateAndRenderHUD () {
        // render top row labels
        font.draw ( sb, "Score", hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false );
        font.draw ( sb, "Shield", hudCenterX, hudRow1Y, hudSectionWidth, Align.center, false );
        font.draw ( sb, "Lives", hudRightX, hudRow1Y, hudSectionWidth, Align.right, false );

        // render second row values
        font.draw ( sb, String.format ( Locale.getDefault (), "%06d", score ), hudLeftX, hudRow2Y, hudSectionWidth, Align.left, false );
        font.draw ( sb, String.format ( Locale.getDefault (), "%02d", playerShip.shield ), hudCenterX, hudRow2Y, hudSectionWidth, Align.center, false );
        font.draw ( sb, String.format ( Locale.getDefault (), "%02d", playerShip.lives ), hudRightX, hudRow2Y, hudSectionWidth, Align.right, false );
    }

    private void spawnEnemyShips ( float delta ) {
        enemySpawnTimer += delta;

        if ( enemySpawnTimer > timeBetweenEnemySpawns ) {
            enemyShipList.add ( new EnemyShip ( JCGdxTestAshley.random.nextFloat () * (WORLD_WIDTH - 10) + 5, WORLD_HEIGHT - 5,
                                                10, 10,
                                                48, 1,
                                                0.3f, 5, 50, 0.8f,
                                                enemyShipTextureRegion, enemyShieldtextureRegion, enemyLaserTextureRegion));

            enemySpawnTimer -= timeBetweenEnemySpawns;

            // play sound // TODO: need a better sound here
            parent.audioManager.playShieldUpSound ();
        }


    }

    private void moveEnemy ( EnemyShip enemyShip, float delta ) {
        // strategy: determine the max distance the ship can move

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = - enemyShip.boundingBox.x;
        downLimit = (float)WORLD_HEIGHT / 2 - enemyShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        upLimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;

        float xMove = enemyShip.getDirectionVector ().x * enemyShip.movementSpeed * delta;
        float yMove = enemyShip.getDirectionVector ().y * enemyShip.movementSpeed * delta;

        if ( xMove > 0 ) {
            xMove = Math.min ( xMove, rightLimit );
        } else {
            xMove = Math.max ( xMove, leftLimit );
        }

        if ( yMove > 0 ) {
            yMove = Math.min ( yMove, upLimit );
        } else {
            yMove = Math.max ( yMove, downLimit );
        }

        enemyShip.translate ( xMove, yMove );
    }

    private void detectInput(float delta) {
        // keyboard input

        // strategy: determine the max distance the ship can move
        // check each key that matters and move accordingly

        float leftLimit, rightLimit, upLimit, downLimit, xMove, yMove;
        leftLimit = - playerShip.boundingBox.x;
        downLimit = - playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        upLimit = (float)WORLD_HEIGHT / 2 - playerShip.boundingBox.y - playerShip.boundingBox.height;
        xMove = 0.0f;
        yMove = 0.0f;

    /*    if(controllerPlayer1.isPressed ( GameControls.BUTTON_DPAD_RIGHT ) && rightLimit > 0) {
            playerShip.translate ( Math.min ( playerShip.movementSpeed * delta, rightLimit ), 0f );
        }

        if(controllerPlayer1.isPressed ( GameControls.BUTTON_DPAD_UP ) && upLimit > 0) {
            playerShip.translate ( 0f, Math.min ( playerShip.movementSpeed * delta, upLimit ) );
        }

        if(controllerPlayer1.isPressed ( GameControls.BUTTON_DPAD_LEFT ) && leftLimit < 0) {
            playerShip.translate ( Math.max ( - playerShip.movementSpeed * delta, leftLimit ), 0f );
        }

        if(controllerPlayer1.isPressed ( GameControls.BUTTON_DPAD_DOWN ) && downLimit < 0) {
            playerShip.translate ( 0f, Math.max ( - playerShip.movementSpeed * delta, downLimit ) );
        }*/

        if ( useGamepad &&
                controllerPlayer1 != null ) {
            float axisX = controllerPlayer1.getAxis ( controllerPlayer1.getMapping ().axisLeftX );
            float axisY = - controllerPlayer1.getAxis ( controllerPlayer1.getMapping ().axisLeftY );

            xMove = axisX * playerShip.movementSpeed * delta;
            yMove = axisY * playerShip.movementSpeed * delta;
        } else {
            // check for mouse/touch input
            if ( Gdx.input.isTouched () ) {
                // get the screen position of the touch
                float xTouchPixels = Gdx.input.getX ();
                float yTouchPixels = Gdx.input.getY ();

                // convert to world position
                Vector2 touchPoint = new Vector2 ( xTouchPixels, yTouchPixels );
                touchPoint = viewport.unproject ( touchPoint );

                // calculate the x and y differences
                Vector2 playerShipCenter = new Vector2 ( playerShip.boundingBox.x + playerShip.boundingBox.width / 2,
                                                         playerShip.boundingBox.y + playerShip.boundingBox.height / 2 );

                float touchDistance = touchPoint.dst ( playerShipCenter );

                if ( touchDistance > TOUCH_MOVEMENT_THRESHOLD ) {
                    float xTouchDifference = touchPoint.x - playerShipCenter.x;
                    float yTouchDifference = touchPoint.y - playerShipCenter.y;

                    // scale to the maximum speed of the ship
                    xMove = ( xTouchDifference / touchDistance ) * playerShip.movementSpeed * delta;
                    yMove = ( yTouchDifference / touchDistance ) * playerShip.movementSpeed * delta;
                }
            } else {
                if ( Gdx.input.isKeyPressed ( Input.Keys.UP ) ) {
                    yMove = playerShip.movementSpeed * delta;
                }

                if ( Gdx.input.isKeyPressed ( Input.Keys.DOWN ) ) {
                    yMove = - playerShip.movementSpeed * delta;
                }

                if ( Gdx.input.isKeyPressed ( Input.Keys.RIGHT ) ) {
                    xMove = playerShip.movementSpeed * delta;
                }

                if ( Gdx.input.isKeyPressed ( Input.Keys.LEFT ) ) {
                    xMove = - playerShip.movementSpeed * delta;
                }
            }
        }

        if(xMove > 0 &&
                rightLimit > 0) {
            xMove = Math.min ( xMove, rightLimit );
        }

        if ( xMove < 0 &&
                leftLimit < 0 ) {
            xMove = Math.max ( xMove, leftLimit );
        }

        if ( yMove > 0 &&
                upLimit > 0) {
            yMove = Math.min ( yMove, upLimit );
        }

        if ( yMove < 0 &&
                downLimit < 0 ) {
            Math.max ( yMove, downLimit );
        }

        playerShip.translate ( xMove, yMove );

    }

    private void detectCollisions () {
        // for each player laser, check whether it intersects an enemy ship
        ListIterator<Laser> laserListIterator = playerLaserList.listIterator ();
        while ( laserListIterator.hasNext () ) {
            Laser laser = laserListIterator.next ();
            ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator ();
            while ( enemyShipListIterator.hasNext () ) {
                EnemyShip enemyShip = enemyShipListIterator.next ();
                if ( enemyShip.intersects ( laser.boundingBox  ) ) {
                    // contact with enemy ship
                    if ( enemyShip.hitAndCheckDestroyed ( laser) ) {
                        enemyShipListIterator.remove ();
                        explosionList.add (
                                new Explosion ( explosionTexture,
                                                new Rectangle ( enemyShip.boundingBox ),
                                                0.7f ) );
                        // play explosion sound
                        parent.audioManager.playFirstExplosionSound ();
                        score += 100;
                    }
                    laserListIterator.remove ();
                    break;
                }
            }
        }

        // for each enemy laser, check whether it intersects a player ship
        laserListIterator = enemyLaserList.listIterator ();
        while ( laserListIterator.hasNext () ) {
            Laser laser = laserListIterator.next ();
            if ( playerShip.intersects ( laser.boundingBox ) ) {
                // contact with enemy ship
                if ( playerShip.hitAndCheckDestroyed ( laser) ) {
                    explosionList.add ( new Explosion (
                            explosionTexture,
                            new Rectangle ( playerShip.boundingBox ),
                            1.6f
                    ) );

                    playerShip.shield = 10;
                    playerShip.lives--;
                    // play sound
                    parent.audioManager.playFirstExplosionSound ();
                    parent.audioManager.playTwoToneSound ();
                }
                laserListIterator.remove ();
            }
        }
    }

    private void updateAndRenderExplosions ( float delta ) {
        ListIterator<Explosion> explosionListIterator = explosionList.listIterator ();
        while ( explosionListIterator.hasNext () ) {
            Explosion explosion = explosionListIterator.next ();
            explosion.update ( delta );
            if ( explosion.isFinished () ) {
                explosionListIterator.remove ();
            } else {
                explosion.draw ( sb );
            }
        }
    }

    private void renderLasers(float delta) {
        // create new lasers
        // player lasers
        if ( playerShip.canFireLaser () ) {
            Laser[] lasers = playerShip.fireLasers ();
            for ( Laser laser : lasers ) {
                playerLaserList.add ( laser );
            }
            // play laser sound 1
            parent.audioManager.playFirstLaserSound ();
        }
        // enemy lasers
        ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator ();
        while ( enemyShipListIterator.hasNext () ) {
            EnemyShip enemyShip = enemyShipListIterator.next ();
            if ( enemyShip.canFireLaser () ) {
                Laser[] lasers = enemyShip.fireLasers ();
                for ( Laser laser : lasers ) {
                    enemyLaserList.add ( laser );
                }
                // play laser sound 2
                parent.audioManager.playSecondLaserSound ();
            }
        }

        // draw lasers
        // remove old lasers
        ListIterator<Laser> iterator = playerLaserList.listIterator ();
        while ( iterator.hasNext () ) {
            Laser laser = iterator.next ();
            laser.draw ( sb );
            laser.boundingBox.y += laser.movementSpeed * delta;
            if ( laser.boundingBox.y > WORLD_HEIGHT ) {
                iterator.remove ();
            }
        }
        iterator = enemyLaserList.listIterator ();
        while ( iterator.hasNext () ) {
            Laser laser = iterator.next ();
            laser.draw ( sb );
            laser.boundingBox.y -= laser.movementSpeed * delta;
            if ( laser.boundingBox.y + laser.boundingBox.height < 0 ) {
                iterator.remove ();
            }
        }
    }

    private void renderBackground(float delta) {
        backgroundOffsets[0] += delta * backgroundMaxScrollingSpeed / 8;
        backgroundOffsets[1] += delta * backgroundMaxScrollingSpeed / 4;
        backgroundOffsets[2] += delta * backgroundMaxScrollingSpeed / 2;
        backgroundOffsets[3] += delta * backgroundMaxScrollingSpeed;

        for ( int layer = 0; layer < backgroundOffsets.length; layer++ ) {
            if(backgroundOffsets[layer] > WORLD_HEIGHT) {
                backgroundOffsets[layer] = 0;
            }

            sb.draw ( backgrounds[layer], 0, -backgroundOffsets[layer], WORLD_WIDTH, backgroundHeight );
            //          sb.draw ( backgrounds[layer], 0, -backgroundOffsets[layer] + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT );
        }

    }

    @Override
    public void resize ( int width, int height ) {
        Gdx.app.debug ( TAG, "resize: " + width + " x " + height );
        viewport.update ( width, height, true );
        sb.setProjectionMatrix ( camera.combined );
    }

    @Override
    public void pause () {
        Gdx.app.debug ( TAG, "pause" );
        isPaused = true;
    }

    @Override
    public void resume () {
        Gdx.app.debug ( TAG, "resume" );
        isPaused = false;
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
        sb.dispose ();
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

        switch ( keycode ) {
            case Input.Keys.BACKSPACE:
                parent.changeScreen ( JCGdxTestAshley.MENU );
                return true;
            case Input.Keys.ESCAPE:
                Gdx.app.exit ();
                return true;
            case Input.Keys.SPACE:
                if ( isPaused ) {
                    this.resume ();
                } else {
                    this.pause ();
                }
                return true;
            default:
                break;
        }

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
        Gdx.app.debug ( TAG, "connected: " + controller.getName () + "/" + controller.getUniqueId () );
        if ( useGamepad ) {
            if(null == controllerPlayer1) {
                controllerPlayer1 = controller;
                controllerPlayerIndex = controllerPlayer1.getPlayerIndex ();
                controllerArrayIndex = Controllers.getControllers ().indexOf ( controller, true );
                return;
            }

            if ( controller == controllerPlayer1 ) {
                controllerPlayerIndex = controller.getPlayerIndex ();
                controllerArrayIndex = Controllers.getControllers ().indexOf ( controller, true );
                this.resume ();
            }
        }
    }

    /**
     * A {@link Controller} got disconnected.
     *
     * @param controller
     */
    @Override
    public void disconnected ( Controller controller ) {
        Gdx.app.debug ( TAG, "disconnected: " + controller.getName () + "/" + controller.getUniqueId () );
        if ( useGamepad &&
            controllerPlayer1 != null) {
            if(controller == controllerPlayer1) {
                controllerPlayer1 = null;
                controllerPlayerIndex = - 1;
                controllerArrayIndex = - 1;
                this.pause ();
            }
        }
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
        Gdx.app.debug ( TAG, controller.getName () + "/" + controller.getUniqueId () + " pressed " + buttonCode);
        if ( useGamepad &&
                controller == controllerPlayer1 ) {

            final ControllerMapping mapping = controller.getMapping ();

            if ( buttonCode == mapping.buttonBack ) {
                parent.changeScreen ( JCGdxTestAshley.MENU );
                return true;
            }

            if ( buttonCode == mapping.buttonStart ) {
                if ( isPaused ) {
                    this.resume ();
                } else {
                    this.pause ();
                }

                return true;
            }
        }
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
