package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    AnimationComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 8:59 PM
 */
public class AnimationComponent implements
                                Component,
                                Pool.Poolable {
    public IntMap<Animation> animations = new IntMap<> ();

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        animations = new IntMap<> ();
    }
}
