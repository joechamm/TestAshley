package com.joechamm.gdxtests.ashley.util.quadtree;

import com.badlogic.gdx.math.MathUtils;
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

    private final Pool<QuadTreeNode<T>> pool;

    QuadTree ( Rectangle rootBounds, T rootValue ) {
        pool = new Pool<QuadTreeNode<T>> ( 1, maxNodesInPool () ) {
            @Override
            protected QuadTreeNode<T> newObject () {
                return new QuadTreeNode<> ();
            }
        };

        root = pool.obtain ();
        root.parent = null;
        root.bounds = new Rectangle ( rootBounds );
        root.value = rootValue;
    }

    private int maxNodesInPool() {
        // 4 children per node, so 4^MAX_DEPTH + 4^(MAX_DEPTH - 1) + ... + 4^2 + 4^1 + 4^0... or
        // (2^2)^(MAX_DEPTH) + ... + 1 = 1 + 2^2 + 2^4 + ... + 2^(2i) + ... + 2^(2 * MAX_DEPTH) or
        // sum 2^2i for i = 0 to MAX_DEPTH = (4^(MAX_DEPTH + 1) - 1) / 3 = ((2^(2 * (MAX_DEPTH + 1)) - 1) /3
        final int fourToNPlusOne =  1 << (2 * MAX_DEPTH + 2);
        return (fourToNPlusOne - 1) / 3;
    }

    public void insert(Rectangle bounds, T value) {

    }




}
