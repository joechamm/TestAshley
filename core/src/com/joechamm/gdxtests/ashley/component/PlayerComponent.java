package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * File:    PlayerComponent
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 9:00 PM
 */
public class PlayerComponent implements
                             Component,
                             Pool.Poolable {
    /** some player stuff here like lives and score... probably gonna change this, but right now it's also what tells us the entity is a player **/
    public int livesLeft = 3;
    public int score = 0;

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    @Override
    public void reset () {
        livesLeft = 3;
        score = 0;
    }
}
