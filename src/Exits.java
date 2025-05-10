import java.util.HashMap;
import java.util.Map;

/**
 * COMPONENT class for exits in the ECS pattern
 * 
 * This component maps direction strings (e.g., "north", "south") to room names,
 * defining the valid exits from a room entity.
 */
public class Exits {
    Map<String, String> exits = new HashMap<>();
}
