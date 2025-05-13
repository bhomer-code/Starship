import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * COMPONENT class for inventory in the ECS pattern
 * 
 * This component tracks the items an entity (typically the player) is carrying.
 * It has a maximum capacity and provides methods for adding and removing items.
 */
public class InventoryComponent implements Component {
    private List<String> items;
    public final int maxCapacity;
    
    public InventoryComponent(int maxCapacity) {
        this.items = new ArrayList<>();
        this.maxCapacity = maxCapacity;
    }
    
    public boolean addItem(String item) {
        if (items.size() < maxCapacity) {
            items.add(item);
            return true;
        }
        return false;
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
    
    public boolean isFull() {
        return items.size() >= maxCapacity;
    }
}
