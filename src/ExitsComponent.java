import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * COMPONENT class for exits in the ECS pattern
 * 
 * This component maps direction strings (e.g., "north", "south") to room names,
 * defining the valid exits from a room entity.
 */
public class ExitsComponent implements Component {
    private Map<String, String> exits;
    
    public ExitsComponent() {
        exits = new HashMap<>();
    }
    
    public void addExit(String direction, String destination) {
        exits.put(direction, destination);
    }
    
    public String getExit(String direction) {
        return exits.get(direction);
    }
    
    public Set<String> getDirections() {
        return exits.keySet();
    }
    
    public boolean hasExit(String direction) {
        return exits.containsKey(direction);
    }
}
