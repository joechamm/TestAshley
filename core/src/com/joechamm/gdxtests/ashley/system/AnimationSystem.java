package com.joechamm.gdxtests.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.joechamm.gdxtests.ashley.component.AnimationComponent;
import com.joechamm.gdxtests.ashley.component.Mappers;
import com.joechamm.gdxtests.ashley.component.StateComponent;
import com.joechamm.gdxtests.ashley.component.TextureComponent;

/**
 * File:    AnimationSystem
 * Package: com.joechamm.gdxtests.ashley.system
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 11:31 PM
 */
public class AnimationSystem extends IteratingSystem {

    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family The family of entities iterated over in this System
     */
    AnimationSystem ( Family family ) {
        super ( family );
    }

    public AnimationSystem() {
        this ( Family.all ( TextureComponent.class,
                            AnimationComponent.class,
                            StateComponent.class ).get () );
    }

    /**
     * This method is called on every entity on every update call of the EntitySystem. Override this to implement your system's
     * specific processing.
     *
     * @param entity    The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity ( Entity entity, float deltaTime ) {
        AnimationComponent animationComponent = Mappers.animCM.get ( entity );
        StateComponent stateComponent = Mappers.statCM.get ( entity );

        if ( animationComponent.animations.containsKey ( stateComponent.get () ) ) {
            TextureComponent textureComponent = Mappers.textCM.get ( entity );
            textureComponent.region = (TextureRegion) animationComponent.animations.get ( stateComponent.get () )
                                                                                   .getKeyFrame ( stateComponent.time,
                                                                                                  stateComponent.isLooping );
        }

        stateComponent.time += deltaTime;
    }
}
