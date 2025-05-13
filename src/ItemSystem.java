import java.util.Collection;

/**
 * System that handles inventory and item interaction
 */
public class ItemSystem implements Esystem {
    private ECS ecs;
    private static final String PLAYER = "player";
    
    public ItemSystem(ECS ecs) {
        this.ecs = ecs;
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
        
        if ((roomItems == null && fixedRoomItems == null) || (!roomItems.hasItem(itemName) && !fixedRoomItems.hasItem(itemName))) {
            System.out.println("Nothing special to see here.");
            return;
        }
        
        // Get description based on item and room
        String description = getItemDescription(itemName, position.room);
        if (!description.isEmpty()) {
            System.out.println(description);
        } else {
            System.out.println("Nothing special to see here.");
        }
    }
    
    private String getItemDescription(String item, String room) {
    	// room-specific descriptions of some fixed and regular items
        switch (item) {
            case "window":
                // Window description depends on room and docking state
                DockingComponent docking = ecs.getComponent(ecs.createEntity("docking"), DockingComponent.class);
                switch (room) {
                    case "Commons":
                        return docking.inSpace ?
                            "The window frames Starbase Omicron's docking entry, a lattice of steel arms glowing faintly against the infinite black." :
                            "The window frames the interior of the starbase, bustling with activity.";
                    case "Stateroom":
                        return docking.inSpace ?
                            "Through the stateroom window, the starbase looms, its silhouette stark against a scattering of stars." :
                            "Through the stateroom window, you can see the curved expanse of the interior of the starbase.";
                    case "Bridge":
                        return docking.inSpace ?
                            "The bridge window offers a commanding view of the starbase's docking arms, poised like a predator in the void." :
                            "The bridge window shows the interior of the starbase, bustling with activity.";
                }
                break;
            case "wrench":
                if (room.equals("Engine")) {
                    return "A hefty wrench, its handle worn smooth from use, rests near the reactor. It's caked with grease.";
                }
                break;
            case "tarp":
                if (room.equals("Cargo")) {
                    return "The tarp is dusty and frayed, draped over a lumpy shape. Peeking beneath reveals a stack of spare parts.";
                }
                break;
            case "tank":
                if (room.equals("Ship Locker")) {
                    return "The oxygen tank is scratched but functional, its gauge showing three-quarters full.";
                }
                break;
            case "console":
                if (room.equals("Bridge")) {
                    return "The console is a maze of switches, dials, and a glowing comms unit, ready to hail the starbase.";
                }
                break;
        }

        return "";
    }
}
