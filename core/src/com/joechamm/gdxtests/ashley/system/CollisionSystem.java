package com.joechamm.gdxtests.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Bits;
import com.joechamm.gdxtests.ashley.component.CollisionComponent;
import com.joechamm.gdxtests.ashley.component.LaserComponent;
import com.joechamm.gdxtests.ashley.component.Mappers;
import com.joechamm.gdxtests.ashley.component.PlayerComponent;
import com.joechamm.gdxtests.ashley.component.TypeComponent;

import com.badlogic.gdx.utils.QuadTreeFloat;

/**
 * File:    CollisionSystem
 * Package: com.joechamm.gdxtests.ashley.system
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 11:29 PM
 */
public class CollisionSystem extends IteratingSystem {

    public static final String TAG = CollisionSystem.class.getName ();

    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family The family of entities iterated over in this System
     */
    CollisionSystem ( Family family ) {
        super ( family );
    }

    public CollisionSystem() {
        this ( Family.all ( CollisionComponent.class ).get () );
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
        // get collision for this entity
        CollisionComponent collisionComponent = Mappers.collCM.get ( entity );
        // get collided entity
        Entity collidedEntity = collisionComponent.collisionEntity;

        TypeComponent thisType = entity.getComponent ( TypeComponent.class );



        // TODO

   /*     // Do player collisions
        if ( thisType.type == TypeComponent.PLAYER_SHIP ) {
            PlayerComponent playerComponent = Mappers.playCM.get ( entity );
            if ( collidedEntity != null ) {
                TypeComponent collidedType = collidedEntity.getComponent ( TypeComponent.class );
                if(collidedType != null) {
                    switch ( collidedType.type ) {
                        case TypeComponent.ENEMY_SHIP:
                            // TODO: should player ship be able to do this here?
                            Gdx.app.debug ( TAG, "player collided with enemy ship" );
                            break;
                        case TypeComponent.LASER:
                            LaserComponent laserComponent = Mappers.laseCM.get ( collidedEntity );
                            if ( laserComponent.owner == LaserComponent.Owner.ENEMY ) {

                            }

                    }
                }
            }
        }*/

    }
}
