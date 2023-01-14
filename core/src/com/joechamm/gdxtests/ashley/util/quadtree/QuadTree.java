package com.joechamm.gdxtests.ashley.util.quadtree;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    QuadTree
 * Package: com.joechamm.gdxtests.ashley.util.quadtree
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/14/2023 at 6:09 PM
 */
public class QuadTree<T> {

    public static int MAX_DEPTH = 10;

    private QuadTreeNode root;

    private Pool<QuadTreeNode<T>> pool;

    QuadTree ( Rectangle rootBounds ) {
        pool = new Pool<QuadTreeNode<T>> () {
            @Override
            protected QuadTreeNode<T> newObject () {
                return null;
            }
        }

    }

    public void insert(Rectangle bounds, T value) {

    }




}
