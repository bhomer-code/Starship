import java.util.List;
import java.util.ArrayList;

/**
 * COMPONENT class for items in the ECS pattern
 * 
 * This component maintains a list of items present in a room entity.
 * Items can be picked up, examined, or interacted with by the player.
 */
public class Items {
    List<String> items = new ArrayList<>();
}
