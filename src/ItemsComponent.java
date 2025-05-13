import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * COMPONENT class for items in the ECS pattern
 * 
 * This component maintains a list of items present in a room entity.
 * Items can be picked up, examined, or interacted with by the player.
 */
public class ItemsComponent implements Component {
    private List<String> items;
    
    public ItemsComponent() {
        items = new ArrayList<>();
    }
    
    public void addItem(String item) {
        items.add(item);
    }
    
    public boolean removeItem(String item) {
        return items.remove(item);
    }
    
    public boolean hasItem(String item) {
        return items.contains(item);
    }
    
    public List<String> getItems() {
        return Collections.unmodifiableList(items);
    }
}
