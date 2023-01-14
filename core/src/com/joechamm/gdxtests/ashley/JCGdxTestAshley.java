package com.joechamm.gdxtests.ashley;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.joechamm.gdxtests.ashley.view.GameScreen;
import com.joechamm.gdxtests.ashley.view.LoadingScreen;
import com.joechamm.gdxtests.ashley.view.MenuScreen;
import com.joechamm.gdxtests.ashley.view.PreferencesScreen;

import com.joechamm.gdxtests.ashley.asset.JCGdxAssetManager;
import com.joechamm.gdxtests.ashley.audio.JCGdxAudioManager;

import java.util.Random;

public class JCGdxTestAshley extends Game {

	public static final String TAG = JCGdxTestAshley.class.getName ();

	// game screens
	private MenuScreen menuScreen;
	private LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private GameScreen gameScreen;

	// game screen indices
	public static final int MENU = 0;
	public static final int PREFERENCES = 1;
	public static final int APPLICATION = 2;
	public static final int CREDITS = 3;

	// preferences
	private AppPreferences preferences;

	// asset manager
	public JCGdxAssetManager assetManager = new JCGdxAssetManager ();

	// audio manager
	public JCGdxAudioManager audioManager = null;

	// handle input
	private InputMultiplexer inputMultiplexer;

	// TODO: move random to "GameLogic" type class
	public static Random random = new Random ();

	public void changeScreen(int screen) {
		Gdx.app.debug ( TAG, "changeScreen" );

		switch ( screen ) {
			case MENU:
				Gdx.app.debug ( TAG, "MENU" );
				if (menuScreen == null) menuScreen = new MenuScreen ( this);
				this.setScreen ( menuScreen );
				break;
			case PREFERENCES:
				Gdx.app.debug ( TAG, "PREFERENCES" );
				if(preferencesScreen == null) preferencesScreen = new PreferencesScreen ( this );
				this.setScreen ( preferencesScreen );
				// TODO
				break;
			case APPLICATION:
				Gdx.app.debug ( TAG, "APPLICATION" );
				if(gameScreen == null) gameScreen = new GameScreen ( this );
				this.setScreen ( gameScreen );
				// TODO
				break;
			case CREDITS:
				Gdx.app.debug ( TAG, "CREDITS" );
				// TODO
				break;
		}
	}

	@Override
	public void create () {
		Gdx.app.setLogLevel ( Application.LOG_DEBUG );

		switch ( Gdx.app.getType () ) {
			case Android:
				break;
			case Desktop:
				Gdx.graphics.setWindowedMode ( 510, 960 );
				break;
			default:
				break;
		}

		//	Gdx.graphics.setWindowedMode ( 1020, 1920 );

		Gdx.app.debug ( TAG, "create" );

		preferences = new AppPreferences ();
		audioManager = JCGdxAudioManager.getInstance ( this );
		if(! audioManager.initPrefs ()) {
			Gdx.app.error ( TAG, "OH NO! failed to initialize audio preferences." );
			Gdx.app.exit ();
		}

		inputMultiplexer = new InputMultiplexer ();
		Gdx.input.setInputProcessor ( inputMultiplexer );

		loadingScreen = new LoadingScreen ( this );
		setScreen ( loadingScreen );
	}

	@Override
	public void dispose () {
		// TODO
		assetManager.manager.dispose ();
		audioManager.dispose ();
	}

	public AppPreferences getPreferences() {
		Gdx.app.debug ( TAG, "getPreferences" );
		return this.preferences;
	}
}
