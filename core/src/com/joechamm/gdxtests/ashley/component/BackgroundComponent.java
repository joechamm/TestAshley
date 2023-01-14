package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    BackgroundComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/12/2023 at 10:41 AM
 */
public class BackgroundComponent implements
                                 Component,
                                 Pool.Poolable {
    /** screen units (pixels) to offset the texture by when scrolling **/
    public float bgPixelsOffsetY = 0f;
    /** how fast is this level of background moving in screen units (pixels) **/
    public float bgPixelsScrollSpeed = 0f;

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        bgPixelsOffsetY = 0f;
        bgPixelsScrollSpeed = 0f;
    }
}
