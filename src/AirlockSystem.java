/**
 * System that handles airlock operations and safety checks
 */
public class AirlockSystem implements Esystem {
    private ECS ecs;
    private static final String PLAYER = "player";
    
    public AirlockSystem(ECS ecs) {
        this.ecs = ecs;
    }
    
    public void cycleAirlock(java.util.Scanner scanner) {
        Entity player = ecs.createEntity(PLAYER);
        PositionComponent position = ecs.getComponent(player, PositionComponent.class);
        
        if (!position.room.equals("Airlock")) {
            System.out.println("You can only cycle the airlock from within it.");
            return;
        }
        
        DockingComponent docking = ecs.getComponent(ecs.createEntity("docking"), DockingComponent.class);
        EquipmentComponent equipment = ecs.getComponent(player, EquipmentComponent.class);
        AirlockStateComponent airlockState = ecs.getComponent(ecs.createEntity("airlock"), AirlockStateComponent.class);
        
        // Check safety conditions
        if (docking.inSpace && !equipment.isEquipped(EquipmentComponent.EquipmentSlot.VAC_SUIT)) {
            handleAirlockDeath(scanner);
            return;
        }
        
        // Safe cycling
        System.out.println("You cycle the airlock. The hatch opens smoothly.");
        
        if (docking.inSpace) {
            System.out.println("You float weightless, the stars endless around you.");
        } else {
            System.out.println("The starbase's gravity pulls you down as you step onto the pad.");
        }
        
        airlockState.setCycled(true);
    }
    
    private void handleAirlockDeath(java.util.Scanner scanner) {
        System.out.println("The outer hatch opens to the void. Silence engulfs you as the air rushes out.");
        System.out.println("Your vision blurs, ice crystals form on your skin, and your lungs burn.");
        System.out.println("You float weightless, the stars spinning around you.");
        System.out.println("You have one chance to survive. Cycle the airlock now, or perish.");
        
        System.out.print("> ");
        String nextInput = scanner.nextLine().trim().toLowerCase();
        
        if (nextInput.equals("cycle airlock")) {
            System.out.println("You slam the controls. The hatch seals, and air floods back in.");
            System.out.println("You collapse, gasping, but alive.");
            
            // Update airlock state
            AirlockStateComponent airlockState = ecs.getComponent(ecs.createEntity("airlock"), AirlockStateComponent.class);
            airlockState.setCycled(true);
        } else {
            System.out.println("The void claims you. Your body drifts among the stars.");
            System.exit(0);
        }
    }
}
