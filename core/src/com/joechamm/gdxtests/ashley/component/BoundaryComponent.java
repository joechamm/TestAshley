package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    BoundaryComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 9:24 PM
 */
public class BoundaryComponent implements
                               Component,
                               Pool.Poolable {

    /** entity's bounding box in screen units (pixels) **/
    public final Rectangle boundingBoxPixels = new Rectangle ( 0f, 0f, 0f, 0f);

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        boundingBoxPixels.set ( 0f, 0f, 0f, 0f );
    }
}
