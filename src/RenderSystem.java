import java.util.Collection;

/**
 * System that handles rendering/displaying game information
 */
public class RenderSystem implements Esystem {
    private ECS ecs;
    private static final String PLAYER = "player";
    
    public RenderSystem(ECS ecs) {
        this.ecs = ecs;
    }
    
    public void displayRoom(String roomId, boolean forceLongDescription) {
        Entity room = ecs.createEntity(roomId);
        DescriptionComponent desc = ecs.getComponent(room, DescriptionComponent.class);
        ExitsComponent exits = ecs.getComponent(room, ExitsComponent.class);
        
        if (desc != null) {
            // Show description
            if (forceLongDescription || desc.firstVisit) {
                System.out.println(desc.longDesc);
                desc.firstVisit = false;
            } else {
                System.out.println(desc.shortDesc);
            }
        }
        
        // Show exits
        if (exits != null) {
            System.out.print("Exits: ");
            for (String direction : exits.getDirections()) {
                System.out.print(direction + " ");
            }
            System.out.println();
        }
    }
    
    public void displayInventory() {
        Entity player = ecs.createEntity(PLAYER);
        InventoryComponent inventory = ecs.getComponent(player, InventoryComponent.class);
        EquipmentComponent equipment = ecs.getComponent(player, EquipmentComponent.class);
        
        if (inventory == null || inventory.getItems().isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.print("You are carrying: ");
            System.out.println(String.join(", ", inventory.getItems()));
        }
        
        if (equipment != null && equipment.isEquipped(EquipmentComponent.EquipmentSlot.VAC_SUIT)) {
            System.out.println("You are wearing a vac-suit.");
        }
    }
    
    // Update room descriptions based on docking state
    public void updateRoomDescriptions() {
        DockingComponent docking = ecs.getComponent(ecs.createEntity("docking"), DockingComponent.class);
        
        // Update airlock description
        Entity airlock = ecs.createEntity("Airlock");
        DescriptionComponent airlockDesc = ecs.getComponent(airlock, DescriptionComponent.class);
        if (airlockDesc != null) {
            String newDesc = docking.inSpace ?
                "The airlock is a stark chamber with reinforced walls. A control panel blinks beside the outer hatch, ready to cycle into the void." :
                "The airlock is connected to the starbase, its outer hatch open to a pressurized corridor.";
            ecs.removeComponent(airlock, DescriptionComponent.class);
            ecs.addComponent(airlock, new DescriptionComponent(airlockDesc.shortDesc, newDesc));
        }
        
        // Update outside description
        Entity outside = ecs.createEntity("outside");
        DescriptionComponent outsideDesc = ecs.getComponent(outside, DescriptionComponent.class);
        if (outsideDesc != null) {
            String newDesc = docking.inSpace ?
                "You float weightless outside KY-25B, the stars endless around you. The ship's hull gleams faintly in the starlight." :
                "You stand on the starbase's docking pad, feeling the centrifugal gravity. The interior of the starbase bustles with activity.";
            ecs.removeComponent(outside, DescriptionComponent.class);
            ecs.addComponent(outside, new DescriptionComponent(outsideDesc.shortDesc, newDesc));
        }
        
        // Update windowed rooms - Commons, Stateroom, Bridge
        updateWindowedRoom("Commons", docking);
        updateWindowedRoom("Stateroom", docking);
        updateWindowedRoom("Bridge", docking);
    }
    
    private void updateWindowedRoom(String roomId, DockingComponent docking) {
        Entity room = ecs.createEntity(roomId);
        DescriptionComponent desc = ecs.getComponent(room, DescriptionComponent.class);
        if (desc == null) return;
        
        String newDesc = "";
        switch (roomId) {
            case "Commons":
                newDesc = docking.inSpace ?
                    "The commons is a modest lounge at the heart of KY-25B. Worn cushions line a bench along the port wall, and a small table is bolted to the deck. A window offers a breathtaking view of Starbase Omicron's docking entry, its metallic arms glinting against the black void." :
                    "The commons is a modest lounge at the heart of KY-25B. Worn cushions line a bench along the port wall, and a small table is bolted to the deck. The window frames the interior of the starbase, bustling with activity.";
                break;
            case "Stateroom":
                newDesc = docking.inSpace ?
                    "The stateroom is a tight but comfortable retreat. A bunk is tucked against the wall, a folded blanket atop it. A small window reveals the starbase's silhouette, framed by distant stars." :
                    "The stateroom is a tight but comfortable retreat. A bunk is tucked against the wall, a folded blanket atop it. Through the stateroom window, you can see the curved expanse of the interior of the starbase.";
                break;
            case "Bridge":
                newDesc = docking.inSpace ?
                    "The bridge is KY-25B's nerve center. A wide window dominates the forward bulkhead, showcasing Starbase Omicron's docking arms against the void. The pilot's chair faces a console studded with controls and a comms unit." :
                    "The bridge is KY-25B's nerve center. A wide window dominates the forward bulkhead, showing the interior of the starbase, bustling with activity. The pilot's chair faces a console studded with controls and a comms unit.";
                break;
            default:
                return;
        }
        
        ecs.removeComponent(room, DescriptionComponent.class);
        ecs.addComponent(room, new DescriptionComponent(desc.shortDesc, newDesc));
    }
}
