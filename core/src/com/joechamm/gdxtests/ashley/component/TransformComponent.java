package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    TransformComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 9:01 PM
 *
 * I think we want the transform component units to be screen units (pixels) for fast use in our rendering system, but need to investigate
 * TODO
 */
public class TransformComponent implements
                                Component,
                                Pool.Poolable {

    // TODO: this doesn't seem correct to me... it seems like we want the position/scale in meters then convert
    /** holds the position of the entity in screen units (pixels)
     * the z-component is used for background/foreground ordering when rendering **/
    public final Vector3 screenPositionPixels = new Vector3 ();
    /** holds the scale in screen units (pixels) **/
    public final Vector2 scalePixels = new Vector2 ( 1.0f, 1.0f);
    /** CCW rotation applied to draw(TextureRegion,...) call in degrees (SpriteBatch uses degrees) **/
    public float rotation = 0f;
    /** should this entity be drawn? // TODO: make invisible component instead **/
    public boolean isHidden = false;

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        scalePixels.set ( 1f, 1f );
        screenPositionPixels.set ( 0f, 0f, 0f );
        rotation = 0f;
        isHidden = false;
    }
}
