package com.joechamm.gdxtests.ashley.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.joechamm.gdxtests.ashley.JCGdxTestAshley;
import com.joechamm.gdxtests.ashley.asset.JCGdxAssetManager;

/**
 * File:    JCGdxAudioManager
 * Package: com.joechamm.gdxtests.controls.audio
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/1/2023 at 3:19 PM
 */
public class JCGdxAudioManager {
    private static final String TAG = JCGdxAudioManager.class.getName ();

    // SINGLETON
    private static JCGdxAudioManager thisInstance;

    // Access to parent and resources
    private JCGdxTestAshley parent;
    private JCGdxAssetManager assetManager;

    // SOUND INDEX
    public static final int LASER_01_SNDIDX = 0;
    public static final int LASER_02_SNDIDX = 1;
    public static final int LOSE_SNDIDX = 2;
    public static final int SHIELD_UP_SNDIDX = 3;
    public static final int SHIELD_DOWN_SNDIDX = 4;
    public static final int TWOTONE_SNDIDX = 5;
    public static final int ZAP_SNDIDX = 6;
    public static final int EXPLOSION_01_SNDIDX = 7;
    public static final int SNDIDX_COUNT = 8;

    // MUSIC INDEX
    public static final int ROLEPLAYING_MUSIDX = 0;
    public static final int MUSIDX_COUNT = 1;

    // SOUNDS
    private final Array<Sound> sounds = new Array<Sound> ();

    // Music Handles
    private final Array<Music> songs = new Array<Music> ();

    // MUSIC
    private Music playingSong;

    // initialization state
    private boolean initialized = false;
    private boolean musicInitialized = false;
    private boolean soundInitialized = false;
    private boolean preferencesInitialized = false;

    // Volume
    public float soundEffectsVolume = 1f;
    public float musicVolume = 1f;
    public boolean soundEffectsOn = true;
    public boolean musicOn = true;


    private JCGdxAudioManager ( JCGdxTestAshley jcGdxTestAshley ) {
        parent = jcGdxTestAshley;
        assetManager = parent.assetManager;
        sounds.setSize ( SNDIDX_COUNT );
        songs.setSize ( MUSIDX_COUNT );
    }

    public static JCGdxAudioManager getInstance ( JCGdxTestAshley jcGdxTestControls ) {
        if ( thisInstance == null ) {
            thisInstance = new JCGdxAudioManager ( jcGdxTestControls );
        } else {
            thisInstance.parent = jcGdxTestControls;
            thisInstance.assetManager = jcGdxTestControls.assetManager;
        }

        return thisInstance;
    }

    /**
     * Initializes the music/sound on/off and volume levels from the game preferences
     * NOTE: this should be called before initSoundEffects and initMusic are called
     */
    public boolean initPrefs () {
        if ( preferencesInitialized ) return true;

        musicOn = parent.getPreferences ().isMusicEnabled ();
        soundEffectsOn = parent.getPreferences ().isSoundEffectsEnabled ();
        musicVolume = parent.getPreferences ().getMusicVolume ();
        soundEffectsVolume = parent.getPreferences ().getSoundVolume ();

        preferencesInitialized = true;
        return true;
    }

    public boolean initSoundEffects () {
        if ( ! preferencesInitialized ) return false;
        if ( soundInitialized ) return true;
        assetManager.queueAddSounds ();
        assetManager.manager.finishLoading ();
        Sound laser01 = assetManager.manager.get ( assetManager.laserSound01, Sound.class );
        Sound laser02 = assetManager.manager.get ( assetManager.laserSound02, Sound.class );
        Sound lose = assetManager.manager.get ( assetManager.loseSound, Sound.class );
        Sound shieldUp = assetManager.manager.get ( assetManager.shieldUpSound, Sound.class );
        Sound shieldDown = assetManager.manager.get ( assetManager.shieldDownSound, Sound.class );
        Sound twoTone = assetManager.manager.get ( assetManager.twoToneSound, Sound.class );
        Sound zap = assetManager.manager.get ( assetManager.zapSound, Sound.class );
        Sound explosion01 = assetManager.manager.get ( assetManager.explosionSound01, Sound.class );
        sounds.set ( LASER_01_SNDIDX, laser01 );
        sounds.set ( LASER_02_SNDIDX, laser02 );
        sounds.set ( LOSE_SNDIDX, lose );
        sounds.set ( SHIELD_UP_SNDIDX, shieldUp );
        sounds.set ( SHIELD_DOWN_SNDIDX, shieldDown );
        sounds.set ( TWOTONE_SNDIDX, twoTone );
        sounds.set ( ZAP_SNDIDX, zap );
        sounds.set ( EXPLOSION_01_SNDIDX, explosion01 );
        soundInitialized = true;
        // if music has already been initialized, set initialized to true
        if ( musicInitialized ) {
            initialized = true;
        }

        return true;
    }

    public boolean initMusic () {
        if ( ! preferencesInitialized ) return false;
        if ( musicInitialized ) return true;

        assetManager.queueAddMusic ();
        assetManager.manager.finishLoading ();
        Music roleplaying = assetManager.manager.get ( assetManager.playingSong, Music.class );
        songs.set ( ROLEPLAYING_MUSIDX, roleplaying );
        playingSong = roleplaying;

        // if sound has already been initialized, set initialized to true
        if ( soundInitialized ) {
            initialized = true;
        }

        musicInitialized = true;
        return true;
    }

    public boolean isInitialized () {
        return initialized;
    }

    public void updateMusicVolume () {
        if ( preferencesInitialized ) {
            musicVolume = parent.getPreferences ().getMusicVolume ();
            playingSong.setVolume ( musicVolume );
        }
    }

    public void updateSoundVolume () {
        if ( preferencesInitialized ) {
            soundEffectsVolume = parent.getPreferences ().getSoundVolume ();
        }
    }

    public void updateMusicOn () {
        if ( preferencesInitialized ) {
            boolean enabled = parent.getPreferences ().isMusicEnabled ();
            // is music enabled?
            if ( enabled ) {
                // did this just change?
                if ( ! musicOn ) {
                    musicOn = true;
                    startPlayingSong ();
                }
            } else {
                musicOn = false;
                stopPlayingSong ();
            }
        }
    }

    public void updateSoundEffectsOn () {
        if ( preferencesInitialized ) {
            soundEffectsOn = parent.getPreferences ().isSoundEffectsEnabled ();
        }
    }

    private long playSound ( Sound sound, float volume, float pitch, float pan ) {
        // play sound at volume, pitch, and pan then return the id
        // we assume that this only gets called when soundsOn is true
        return sound.play ( volume, pitch, pan );
    }

    private long playSound ( Sound sound, float volume, float pitch ) {
        return sound.play ( volume, pitch, 0 );
    }

    private long playSound ( Sound sound, float volume ) {
        return sound.play ( volume );
    }

    private long playSound ( Sound sound ) {
        return sound.play ();
    }

    private void playMusic ( Music song, float volume, float position, boolean looping ) {
        // make sure we're not playing anything right now
        stopPlayingSong ();
        // make song the playingSong
        playingSong = song;
        // set the volume, looping, and position
        playingSong.setVolume ( volume );
        playingSong.setLooping ( looping );
        playingSong.setPosition ( position );
        // since this is a private method, we'll assume it only gets called when musicOn is true
        playingSong.play ();
    }

    private void playMusic ( Music song, float volume, boolean looping ) {
        playMusic ( song, volume, 0f, looping );
    }

    private void playMusic ( Music song, float volume ) {
        playMusic ( song, volume, true );
    }

    private void playMusic ( Music song ) {
        playMusic ( song, 0.5f );
    }

    // PUBLIC METHODS

    /**
     * Plays the current song 'playingSong' at volume 'musicVolume' if 'musicOn' is true
     */
    public void startPlayingSong () {
        if ( musicOn && playingSong != null ) {
            playMusic ( playingSong, musicVolume );
        }
    }

    /**
     * Stops the current song 'playingSong'
     */
    public void stopPlayingSong () {
        if ( playingSong != null ) {
            playingSong.stop ();
        }
    }

    /**
     * Sets the 'playingSong' member
     *
     * @param songIdx the index of the song to set
     */
    public void setPlayingSong ( int songIdx ) {
        stopPlayingSong ();

        switch ( songIdx ) {
            case ROLEPLAYING_MUSIDX:
                playingSong = songs.get ( ROLEPLAYING_MUSIDX );
                break;
            default:
                Gdx.app.error ( TAG, "Invalid song index: " + songIdx );
                break;
        }

        if ( musicOn ) {
            startPlayingSong ();
        }
    }

    public long playSoundEffect ( int soundEffectIdx ) {
        if ( ! soundEffectsOn ) {
            return - 1;
        }

        switch ( soundEffectIdx ) {
            case LASER_01_SNDIDX:
                return playSound ( sounds.get ( LASER_01_SNDIDX ), soundEffectsVolume );
            case LASER_02_SNDIDX:
                return playSound ( sounds.get ( LASER_02_SNDIDX ), soundEffectsVolume );
            case SHIELD_UP_SNDIDX:
                return playSound ( sounds.get ( SHIELD_UP_SNDIDX ), soundEffectsVolume );
            case SHIELD_DOWN_SNDIDX:
                return playSound ( sounds.get ( SHIELD_DOWN_SNDIDX ), soundEffectsVolume );
            case TWOTONE_SNDIDX:
                return playSound ( sounds.get ( TWOTONE_SNDIDX ), soundEffectsVolume );
            case ZAP_SNDIDX:
                return playSound ( sounds.get ( ZAP_SNDIDX ), soundEffectsVolume );
            case EXPLOSION_01_SNDIDX:
                return playSound ( sounds.get ( EXPLOSION_01_SNDIDX ), soundEffectsVolume );
            default:
                Gdx.app.error ( TAG, "Invalid sound effect id: " + soundEffectIdx );
                return - 1;
        }
    }

    // helper functions
    public long playFirstLaserSound () {
        return playSoundEffect ( LASER_01_SNDIDX );
    }

    public long playSecondLaserSound () {
        return playSoundEffect ( LASER_02_SNDIDX );
    }

    public long playShieldUpSound () {
        return playSoundEffect ( SHIELD_UP_SNDIDX );
    }

    public long playShieldDownSound () {
        return playSoundEffect ( SHIELD_DOWN_SNDIDX );
    }

    public long playTwoToneSound () {
        return playSoundEffect ( TWOTONE_SNDIDX );
    }

    public long playZapSound () {
        return playSoundEffect ( ZAP_SNDIDX );
    }

    public long playFirstExplosionSound() {
        return playSoundEffect ( EXPLOSION_01_SNDIDX );
    }

    public void dispose () {
        for ( Sound sound : sounds ) {
            sound.dispose ();
        }


        for ( Music music : songs ) {
            music.dispose ();
        }

        soundInitialized = false;
        musicInitialized = false;
        initialized = false;
    }
}
