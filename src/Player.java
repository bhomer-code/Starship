import java.util.List;
import java.util.ArrayList;

/**
 * COMPONENT class for player state in the ECS pattern
 * 
 * This component tracks the player's inventory and whether they are wearing the vac-suit.
 * Note: In a more robust ECS, this might be split into separate Inventory and Equipment components.
 */
public class Player {
    List<String> inventory = new ArrayList<>();
    boolean wearingSuit = false;
}
