import java.util.HashMap;
import java.util.Map;

/**
 * ENTITY base class for the ECS pattern
 * 
 * In a proper ECS, entities are just unique IDs. Here we'll use strings for simplicity,
 * but this class could be extended to use UUIDs or type-safe IDs in a more robust implementation.
 */
public class Entity {
    private final String id;
    
    public Entity(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Entity)) return false;
        Entity other = (Entity) obj;
        return id.equals(other.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return "Entity{" + id + "}";
    }
}
