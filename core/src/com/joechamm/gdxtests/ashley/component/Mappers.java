package com.joechamm.gdxtests.ashley.component;

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
    public static final ComponentMapper<AnimationComponent> animationCM = ComponentMapper.getFor ( AnimationComponent.class );
    public static final ComponentMapper<BackgroundComponent> backgroundCM = ComponentMapper.getFor ( BackgroundComponent.class );
    public static final ComponentMapper<BoundaryComponent> boundaryCM = ComponentMapper.getFor ( BoundaryComponent.class );
    public static final ComponentMapper<CollisionComponent> collisionCM = ComponentMapper.getFor ( CollisionComponent.class );
    public static final ComponentMapper<EnemyComponent> enemyCM = ComponentMapper.getFor ( EnemyComponent.class );
    public static final ComponentMapper<EnemyOwnedComponent> enemyOwnedCM = ComponentMapper.getFor ( EnemyOwnedComponent.class );
    public static final ComponentMapper<LaserComponent> laserCM = ComponentMapper.getFor ( LaserComponent.class );
    public static final ComponentMapper<LaserCannonComponent> laserCannonCM = ComponentMapper.getFor ( LaserCannonComponent.class );
    public static final ComponentMapper<MovementComponent> movementCM = ComponentMapper.getFor ( MovementComponent.class );
    public static final ComponentMapper<PlayerComponent> playerCM = ComponentMapper.getFor ( PlayerComponent.class );
    public static final ComponentMapper<PlayerInputComponent> playerInputCM = ComponentMapper.getFor ( PlayerInputComponent.class );
    public static final ComponentMapper<PlayerOwnedComponent> playerOwnedCM = ComponentMapper.getFor ( PlayerOwnedComponent.class );
    public static final ComponentMapper<ShieldComponent> shieldCM = ComponentMapper.getFor ( ShieldComponent.class );
    public static final ComponentMapper<ShipComponent> shipCM = ComponentMapper.getFor ( ShipComponent.class );
    public static final ComponentMapper<StateComponent> stateCM = ComponentMapper.getFor ( StateComponent.class );
    public static final ComponentMapper<TakesLaserDamageComponent> takesLaserDamageCM = ComponentMapper.getFor ( TakesLaserDamageComponent.class );
    public static final ComponentMapper<TextureComponent> textureCM = ComponentMapper.getFor ( TextureComponent.class );
    public static final ComponentMapper<TransformComponent> transformCM = ComponentMapper.getFor ( TransformComponent.class );
    public static final ComponentMapper<TypeComponent> typeCM = ComponentMapper.getFor ( TypeComponent.class );

}
