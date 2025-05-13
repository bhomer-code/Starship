import java.util.Collection;

/**
 * System that handles entity movement between rooms
 */
public class MovementSystem implements Esystem {
    private ECS ecs;
    private static final String PLAYER = "player";
    private static final String OUTSIDE = "outside";
    
    public MovementSystem(ECS ecs) {
        this.ecs = ecs;
    }
    
    public void moveEntity(Entity entity, String direction) {
        PositionComponent position = ecs.getComponent(entity, PositionComponent.class);
        if (position == null) return;
        
        // Get current room exits
        Entity currentRoom = ecs.createEntity(position.room);
        ExitsComponent exits = ecs.getComponent(currentRoom, ExitsComponent.class);
        if (exits == null) return;
        
        String newRoomId = exits.getExit(direction);
        if (newRoomId == null || newRoomId.equals(position.room)) {
            // Can't go that way
            return;
        }
        
        // Check special conditions for player movement
        if (entity.getId().equals(PLAYER)) {
            if (!checkPlayerMovementConditions(entity, position.room, newRoomId)) {
                return;
            }
        }
        
        // Update entity position
        ecs.removeComponent(entity, PositionComponent.class);
        ecs.addComponent(entity, new PositionComponent(newRoomId));
        
    }
    
    private boolean checkPlayerMovementConditions(Entity player, String from, String to) {
        // Handle airlock and outside transitions
        DockingComponent docking = ecs.getComponent(ecs.createEntity("docking"), DockingComponent.class);
        EquipmentComponent equipment = ecs.getComponent(player, EquipmentComponent.class);
        AirlockStateComponent airlock = ecs.getComponent(ecs.createEntity("airlock"), AirlockStateComponent.class);
        
        // Check if moving to outside space without suit
        if (to.equals(OUTSIDE) && docking.inSpace && !equipment.isEquipped(EquipmentComponent.EquipmentSlot.VAC_SUIT)) {
            // Handle immediate death
            return false;
        }
        
        // Track turns in airlock without suit
        if (from.equals("Airlock") && !equipment.isEquipped(EquipmentComponent.EquipmentSlot.VAC_SUIT) && docking.inSpace) {
            airlock.incrementTurnsWithoutSuit();
            if (airlock.turnsWithoutSuit >= 2 && !airlock.cycled) {
                // Handle death condition
                return false;
            }
        } else {
            airlock.resetTurnsWithoutSuit();
            airlock.setCycled(false);
        }
        
        return true;
    }
    
}
