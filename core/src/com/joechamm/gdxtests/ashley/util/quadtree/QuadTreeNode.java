package com.joechamm.gdxtests.ashley.util.quadtree;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    QuadTreeNode
 * Package: com.joechamm.gdxtests.ashley.util
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/14/2023 at 5:43 PM
 */
public class QuadTreeNode<T> implements Pool.Poolable {

    /** the bounds for this quad **/
    public Rectangle bounds = null;
    /** value stored here **/
    public T value = null;
    /** this node's parent, null if this is the root **/
    public QuadTreeNode parent = null;
    /** this node's children **/
    /** north east **/
    public QuadTreeNode ne = null;
    /** north west **/
    public QuadTreeNode nw = null;
    /** south west **/
    public QuadTreeNode sw = null;
    /** south east **/
    public QuadTreeNode se = null;

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        bounds = null;
        value = null;
        parent = null;
        ne = null;
        nw = null;
        sw = null;
        se = null;
    }
}
