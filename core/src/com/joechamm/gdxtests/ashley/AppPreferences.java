package com.joechamm.gdxtests.ashley;

/**
 * File:    AppPreferences
 * Package: com.joechamm.gdxtests.controls
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/1/2023 at 1:16 PM
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class AppPreferences {

    public static final String TAG = AppPreferences.class.getName ();

    public static final int CONTROLS_METHOD_TOUCH = 0;
    public static final int CONTROLS_METHOD_GAMEPAD = 1;
    public static final int CONTROLS_METHOD_COUNT = 2;

    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_SOUND_VOL = "sound";
    private static final String PREF_CONTROLS_METHOD = "controls.method";
    private static final String PREFS_NAME = "JCGdxTestControls";

    protected Preferences getPrefs () {
        Gdx.app.debug ( TAG, "getPrefs" );
        return Gdx.app.getPreferences ( PREFS_NAME );
    }

    protected int getControlsMethod() {
        Gdx.app.debug ( TAG, "getControlsMethod" );
        return getPrefs ().getInteger ( PREF_CONTROLS_METHOD, - 1 );
    }

    protected void setControlsMethod(final int method) {
        Gdx.app.debug ( TAG, "setControlsMethod: " + method );
        // store the controls method in our preferences... since this is a protected method, we shouldn't need to make sure method is valid
        getPrefs ().putInteger ( PREF_CONTROLS_METHOD, method );
        getPrefs ().flush ();
    }

    public float getMusicVolume () {
        Gdx.app.debug ( TAG, "getMusicVolume" );
        return getPrefs ().getFloat ( PREF_MUSIC_VOLUME, 0.5f );
    }

    public void setMusicVolume ( float volume ) {
        Gdx.app.debug ( TAG, "setMusicVolume" );
        getPrefs ().putFloat ( PREF_MUSIC_VOLUME, volume );
        getPrefs ().flush ();
    }

    public boolean isSoundEffectsEnabled () {
        Gdx.app.debug ( TAG, "isSoundEffectsEnabled" );
        return getPrefs ().getBoolean ( PREF_SOUND_ENABLED, true );
    }

    public void setSoundEffectsEnabled ( boolean soundEffectsEnabled ) {
        Gdx.app.debug ( TAG, "setSoundEffectsEnabled" );
        getPrefs ().putBoolean ( PREF_SOUND_ENABLED, soundEffectsEnabled );
        getPrefs ().flush ();
    }

    public boolean isMusicEnabled () {
        Gdx.app.debug ( TAG, "isMusicEnabled" );
        return getPrefs ().getBoolean ( PREF_MUSIC_ENABLED, true );
    }

    public void setMusicEnabled ( boolean musicEnabled ) {
        Gdx.app.debug ( TAG, "setMusicEnabled" );
        getPrefs ().putBoolean ( PREF_MUSIC_ENABLED, musicEnabled );
        getPrefs ().flush ();
    }

    public float getSoundVolume () {
        Gdx.app.debug ( TAG, "getSoundVolume" );
        return getPrefs ().getFloat ( PREF_SOUND_VOL, 0.5f );
    }

    public void setSoundVolume ( float volume ) {
        Gdx.app.debug ( TAG, "setSoundVolume" );
        getPrefs ().putFloat ( PREF_SOUND_VOL, volume );
        getPrefs ().flush ();
    }

    public boolean isTouchEnabled() {
        Gdx.app.debug ( TAG, "isTouchEnabled" );
        return CONTROLS_METHOD_TOUCH == getControlsMethod ();
    }

    public boolean isGamepadEnabled() {
        Gdx.app.debug ( TAG, "isGamepadEnabled" );
        return CONTROLS_METHOD_GAMEPAD == getControlsMethod ();
    }

    public void setControlsMethodTouch() {
        Gdx.app.debug ( TAG, "setControlsMethodTouch" );
        setControlsMethod ( CONTROLS_METHOD_TOUCH );
    }

    public void setControlsMethodGamepad() {
        Gdx.app.debug ( TAG, "setControlsMethodGamepad" );
        setControlsMethod ( CONTROLS_METHOD_GAMEPAD );
    }
}
