package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    TextureComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 9:01 PM
 */
public class TextureComponent implements
                              Component,
                              Pool.Poolable {

    /** texture region to draw with offsets in screen units (pixels) **/
    public TextureRegion region = null;
    public float offsetX = 0f;
    public float offsetY = 0f;

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        region = null;
        offsetX = 0f;
        offsetY = 0f;
    }
}
