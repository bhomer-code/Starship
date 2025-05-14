import java.util.Collection;

/**
 * System that handles inventory and item interaction
 */
public class ItemSystem implements Esystem {
    private ECS ecs;
    private ItemDescriptionSystem itemDescriptionSystem;
    private static final String PLAYER = "player";
    
    public ItemSystem(ECS ecs) {
        this.ecs = ecs;
        this.itemDescriptionSystem = new ItemDescriptionSystem(ecs);
    }

    public boolean takeItem(String itemName) {
        Entity player = ecs.createEntity(PLAYER);
        PositionComponent position = ecs.getComponent(player, PositionComponent.class);
        InventoryComponent inventory = ecs.getComponent(player, InventoryComponent.class);
        
        if (position == null || inventory == null) {
            return false;
        }
        
        // Check if item exists in current room
        Entity currentRoom = ecs.createEntity(position.room);
        ItemsComponent roomItems = ecs.getComponent(currentRoom, ItemsComponent.class);
        
        if (roomItems == null || !roomItems.hasItem(itemName)) {
            System.out.println("You can't take that.");
            return false;
        }
        
        // Check if inventory has space
        if (inventory.isFull()) {
            System.out.println("Your inventory is full.");
            return false;
        }
        
        // Transfer item from room to inventory
        if (inventory.addItem(itemName)) {
            roomItems.removeItem(itemName);
            System.out.println("You take the " + itemName + ".");
            return true;
        }
        
        return false;
    }
    
    public boolean wearItem(String itemName) {
        Entity player = ecs.createEntity(PLAYER);
        InventoryComponent inventory = ecs.getComponent(player, InventoryComponent.class);
        EquipmentComponent equipment = ecs.getComponent(player, EquipmentComponent.class);
        
        if (inventory == null || equipment == null) {
            return false;
        }
        
        // Check for vac-suit specifically
        if (itemName.equals("vac-suit")) {
            if (!inventory.hasItem("vac-suit")) {
                System.out.println("You don't have a vac-suit to wear.");
                return false;
            }
            
            if (equipment.isEquipped(EquipmentComponent.EquipmentSlot.VAC_SUIT)) {
                System.out.println("You're already wearing a vac-suit.");
                return false;
            }
            
            equipment.equipItem(EquipmentComponent.EquipmentSlot.VAC_SUIT, "vac-suit");
            System.out.println("You don the vac-suit.");
            return true;
        }
        
        System.out.println("You can't wear that.");
        return false;
    }
    
    public boolean removeItem(String itemName) {
        Entity player = ecs.createEntity(PLAYER);
        EquipmentComponent equipment = ecs.getComponent(player, EquipmentComponent.class);
        
        if (equipment == null) {
            return false;
        }
        
        // Check for vac-suit specifically
        if (itemName.equals("vac-suit")) {
            if (!equipment.isEquipped(EquipmentComponent.EquipmentSlot.VAC_SUIT)) {
                System.out.println("You're not wearing a vac-suit.");
                return false;
            }
            
            equipment.unequipItem(EquipmentComponent.EquipmentSlot.VAC_SUIT);
            System.out.println("You remove the vac-suit.");
            return true;
        }
        
        System.out.println("You're not wearing that.");
        return false;
    }
    
    public void examineItem(String itemName) {
        Entity player = ecs.createEntity(PLAYER);
        PositionComponent position = ecs.getComponent(player, PositionComponent.class);
        
        if (position == null) {
            return;
        }
        
        // Check if item is in the current room
        Entity currentRoom = ecs.createEntity(position.room);
        ItemsComponent roomItems = ecs.getComponent(currentRoom, ItemsComponent.class);
        FixedItemsComponent fixedRoomItems = ecs.getComponent(currentRoom, FixedItemsComponent.class);
        
        boolean itemExists = false;
        if (roomItems != null && roomItems.hasItem(itemName)) {
            itemExists = true;
        }
        if (fixedRoomItems != null && fixedRoomItems.hasItem(itemName)) {
            itemExists = true;
        }
        
        if (!itemExists) {
            System.out.println("Nothing special to see here.");
            return;
        }
        
        // Get description using the new ItemDescriptionSystem
        String description = itemDescriptionSystem.getItemInRoomDescription(itemName, position.room);
        System.out.println(description);
    }

}
