/**
 * COMPONENT class for position in the ECS pattern
 * 
 * This component stores the current room/location for an entity.
 * It's used by the player entity to track their current position
 * and by room entities to define their own position identifier.
 */
public class PositionComponent implements Component {
    public final String room;
    
    public PositionComponent(String room) { 
        this.room = room; 
    }
}
