import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * COMPONENT class for fixed items in the ECS pattern
 * 
 * This component maintains a list of fixed items present in a room entity.
 * Items can be examined or interacted with by the player, but not picked up.
 */
public class FixedItemsComponent implements Component {
    private List<String> fixedItems;
    
    public FixedItemsComponent() {
        fixedItems = new ArrayList<>();
    }
    
    public void addFixedItem(String fixedItem) {
        fixedItems.add(fixedItem);
    }
    
    public boolean hasItem(String item) {
        return fixedItems.contains(item);
    }
    
    public List<String> getItems() {
        return Collections.unmodifiableList(fixedItems);
    }
}
