package com.joechamm.gdxtests.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * File:    Mappers
 * Package: com.joechamm.gdxtests.ashley.component
 * Project: TestAshley
 *
 * @author joechamm
 * Created  1/10/2023 at 11:21 PM
 */
public class Mappers {
    public static final ComponentMapper<AnimationComponent> animCM = ComponentMapper.getFor ( AnimationComponent.class );
    public static final ComponentMapper<BackgroundComponent> bgCM = ComponentMapper.getFor ( BackgroundComponent.class );
    public static final ComponentMapper<BoundaryComponent> bounCM = ComponentMapper.getFor ( BoundaryComponent.class );
    public static final ComponentMapper<CollisionComponent> collCM = ComponentMapper.getFor ( CollisionComponent.class );
    public static final ComponentMapper<EnemyComponent> enemCM = ComponentMapper.getFor ( EnemyComponent.class );
    public static final ComponentMapper<LaserComponent> laseCM = ComponentMapper.getFor ( LaserComponent.class );
    public static final ComponentMapper<LaserCannonComponent> lcCM = ComponentMapper.getFor ( LaserCannonComponent.class );
    public static final ComponentMapper<MovementComponent> moveCM = ComponentMapper.getFor ( MovementComponent.class );
    public static final ComponentMapper<PlayerComponent> playCM = ComponentMapper.getFor ( PlayerComponent.class );
    public static final ComponentMapper<PlayerInputComponent> piCM = ComponentMapper.getFor ( PlayerInputComponent.class );
    public static final ComponentMapper<ShieldComponent> shieCM = ComponentMapper.getFor ( ShieldComponent.class );
    public static final ComponentMapper<ShipComponent> shipCM = ComponentMapper.getFor ( ShipComponent.class );
    public static final ComponentMapper<StateComponent> statCM = ComponentMapper.getFor ( StateComponent.class );
    public static final ComponentMapper<TextureComponent> textCM = ComponentMapper.getFor ( TextureComponent.class );
    public static final ComponentMapper<TransformComponent> tranCM = ComponentMapper.getFor ( TransformComponent.class );
    public static final ComponentMapper<TypeComponent> typeCM = ComponentMapper.getFor ( TypeComponent.class );

}
