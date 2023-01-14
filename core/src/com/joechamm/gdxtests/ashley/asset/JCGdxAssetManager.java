package com.joechamm.gdxtests.ashley.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

/**
 * File:    JCGdxAssetManager
 * Package: com.joechamm.gdxtests.controls.asset
 * Project: TestControls
 *
 * @author joechamm
 * Created  1/1/2023 at 3:20 PM
 */
public class JCGdxAssetManager {
    private static final String TAG = JCGdxAssetManager.class.getName ();

    public final AssetManager manager = new AssetManager ();

    // Textures
    public final String gameImages = "output/images/game.atlas";
//    public final String loadingImages = "images/loading.atlas";
    public final String explosionImages = "output/images/explosion.png";

    // Sounds
    public final String laserSound01 = "output/sounds/sfx_laser1.ogg";
    public final String laserSound02 = "output/sounds/sfx_laser2.ogg";
    public final String loseSound = "output/sounds/sfx_lose.ogg";
    public final String shieldDownSound = "output/sounds/sfx_shieldDown.ogg";
    public final String shieldUpSound = "output/sounds/sfx_shieldUp.ogg";
    public final String twoToneSound = "output/sounds/sfx_twoTone.ogg";
    public final String zapSound = "output/sounds/sfx_zap.ogg";
    public final String explosionSound01 = "output/sounds/sfx_explosion1.ogg";

    // Music
    public final String playingSong = "output/music/music_rolemusic.mp3";

    // Skin
    public final String skinJson = "output/skin/controls-clean-crispy/controls-clean-crispy.json";
    public final String skinAtlas = "output/skin/controls-clean-crispy/controls-clean-crispy.atlas";

    // Fonts
    public final String ttfRegFontFilename = "output/fonts/fontrg.ttf";
//    public final String ttfRegFontSizeSmall = "regSmall.ttf";
//    public final String ttfRegFontSizeMed = "regMed.ttf";
//    public final String ttfRegFontSizeLarge = "regLarge.ttf";
//    public final String otfEOTGRegFontFilename = "fonts/EdgeOfTheGalaxyRegular-OVEa6.otf";
//    public final String fntEOTGReg72ptFontFilename = "fonts/EdgeOfTheGalaxyRegular_72pt.fnt";
    public final String fontRegOpaque72 = "fontRegOpaque72.ttf";
    public final String fontRegTransparent72 = "fontRegTransparent72.ttf";

    // Particle Effects
//    public final String smokeEffect = "particles/smoke.pe";
//    public final String waterEffect = "particles/water.pe";
//    public final String fireEffect = "particles/fire.pe";

    public void queueAddImages () {
        manager.load ( gameImages, TextureAtlas.class );
    }

    // TODO: use atlas for animations
    public void queueAddExplosionImages() {
        manager.load ( explosionImages, Texture.class );
    }

//    public void queueAddLoadingImages () {
//       manager.load ( loadingImages, TextureAtlas.class );
//    }

    public void queueAddSounds () {
        manager.load ( laserSound01, Sound.class );
        manager.load ( laserSound02, Sound.class );
        manager.load ( loseSound, Sound.class );
        manager.load ( shieldUpSound, Sound.class );
        manager.load ( shieldDownSound, Sound.class );
        manager.load ( twoToneSound, Sound.class );
        manager.load ( zapSound, Sound.class );
        manager.load ( explosionSound01, Sound.class );
    }

    public void queueAddMusic () {
        manager.load ( playingSong, Music.class );
    }

    public void queueAddSkin () {
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter ( skinAtlas );
        manager.load ( skinJson, Skin.class, params );
    }

    public void queueAddFonts() {
        FileHandleResolver resolver = new InternalFileHandleResolver ();
        manager.setLoader ( FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader ( resolver ) );
        manager.setLoader ( BitmapFont.class, ".ttf", new FreetypeFontLoader ( resolver ) );

        FreetypeFontLoader.FreeTypeFontLoaderParameter regOpaqueFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter ();
        regOpaqueFontParameter.fontFileName = ttfRegFontFilename;
        regOpaqueFontParameter.fontParameters.size = 72;
        regOpaqueFontParameter.fontParameters.borderWidth = 3.6f;
        regOpaqueFontParameter.fontParameters.color = new Color ( 1, 1, 1, 1 );
        regOpaqueFontParameter.fontParameters.borderColor = new Color ( 0, 0, 0, 1 );
        manager.load ( fontRegOpaque72, BitmapFont.class, regOpaqueFontParameter );

        FreetypeFontLoader.FreeTypeFontLoaderParameter regTransparentFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter ();
        regTransparentFontParameter.fontFileName = ttfRegFontFilename;
        regTransparentFontParameter.fontParameters.size = 72;
        regTransparentFontParameter.fontParameters.borderWidth = 3.6f;
        regTransparentFontParameter.fontParameters.color = new Color (1, 1, 1, 0.3f);
        regTransparentFontParameter.fontParameters.borderColor = new Color ( 0, 0, 0, 0.3f );
        manager.load ( fontRegTransparent72, BitmapFont.class, regTransparentFontParameter );
    }

 /*   // Recipe pulled from https://github.com/libgdx/libgdx/blob/master/tests/gdx-tests/src/com/badlogic/gdx/tests/extensions/FreeTypeFontLoaderTest.java
    public void queueAddFonts () {

        // TODO: move all of the font generation to separate method
        // set the loaders for the generator and the fonts themselves
        FileHandleResolver resolver = new InternalFileHandleResolver ();
        manager.setLoader ( FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader ( resolver ) );
        manager.setLoader ( BitmapFont.class, ".ttf", new FreetypeFontLoader ( resolver ) );

        // load to fonts via the generator (implicitely done by the FreetypefontLoader).
        // Note: you MUST specify a FreetypeFontGenerator defining the ttf font file name and the size
        // of the font to be generated. The names of the fonts are arbitrary and are not pointing
        // to a file on disk (but must end with the font's file format '.ttf')!
        FreetypeFontLoader.FreeTypeFontLoaderParameter size1Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter ();
        size1Params.fontFileName = ttfRegFontFilename;
        size1Params.fontParameters.size = 16;
        manager.load ( ttfRegFontSizeSmall, BitmapFont.class, size1Params );

        FreetypeFontLoader.FreeTypeFontLoaderParameter size2Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter ();
        size2Params.fontFileName = ttfRegFontFilename;
        size2Params.fontParameters.size = 24;
        manager.load ( ttfRegFontSizeMed, BitmapFont.class, size2Params );

        FreetypeFontLoader.FreeTypeFontLoaderParameter size3Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter ();
        size3Params.fontFileName = ttfRegFontFilename;
        size3Params.fontParameters.size = 32;
        manager.load ( ttfRegFontSizeLarge, BitmapFont.class, size3Params );

        // Add Hiero generated fnt font EdgeOfTheGalaxy
        manager.load ( fntEOTGReg72ptFontFilename, BitmapFont.class );

    }*/

    public void queueAddParticleEffects () {

//        ParticleEffectLoader.ParticleEffectParameter pep = new ParticleEffectLoader.ParticleEffectParameter ();
//        pep.atlasFile = "images/game.atlas";
//        manager.load ( smokeEffect, ParticleEffect.class, pep );
//        manager.load ( waterEffect, ParticleEffect.class, pep );
//        manager.load ( fireEffect, ParticleEffect.class, pep );
    }
}
