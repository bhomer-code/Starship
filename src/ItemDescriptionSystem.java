/**
 * System that handles item descriptions in a context-aware manner
 * 
 * This system manages the display of item descriptions based on context
 * such as the item's current location or state.
 */
public class ItemDescriptionSystem implements Esystem {
    private final ECS ecs;
    
    public ItemDescriptionSystem(ECS ecs) {
        this.ecs = ecs;
    }
    
    /**
     * Gets the description of an item based on its current context
     * @param itemEntityId The entity ID of the item
     * @param context The context (e.g., room name) for the description
     * @return The context-sensitive description or a default message
     */
    public String getItemDescription(String itemEntityId, String context) {
        Entity itemEntity = ecs.createEntity(itemEntityId);
        ItemDescriptionComponent descComponent = ecs.getComponent(itemEntity, ItemDescriptionComponent.class);
        
        if (descComponent == null) {
            return "ids:You see nothing special about it.";
        }
        
        return descComponent.getDescription(context);
    }
    
    /**
     * Gets the description of an item in a specific room, considering dynamic state
     * @param itemName The name of the item
     * @param roomName The name of the room
     * @return The context-sensitive description
     */
    public String getItemInRoomDescription(String itemName, String roomName) {
        String itemEntityId = roomName + "_" + itemName;
        DockingComponent docking = ecs.getComponent(ecs.createEntity("docking"), DockingComponent.class);
        if (itemName.equals("window")) {
        	itemEntityId += docking.inSpace ? "_space" : "_docked"; 
        }
        
        Entity itemEntity = ecs.createEntity(itemEntityId);
        ItemDescriptionComponent descComponent = ecs.getComponent(itemEntity, ItemDescriptionComponent.class);
        
        if (descComponent != null) {
            // Check for dynamic state modifiers
            String description = descComponent.getDescription(roomName);
            
            return description;
        }
        
        // Fall back to simple item entity if room-specific doesn't exist
        itemEntity = ecs.createEntity(itemName);
        descComponent = ecs.getComponent(itemEntity, ItemDescriptionComponent.class);
        
        if (descComponent != null) {
            return descComponent.getDescription(roomName);
        }
        
        return "You see nothing special about it.";
    }
    
}