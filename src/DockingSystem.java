import java.util.Random;

/**
 * System that handles docking procedures
 */
public class DockingSystem implements Esystem {
    private ECS ecs;
    private Random random;
    private static final String PLAYER = "player";
    
    public DockingSystem(ECS ecs) {
        this.ecs = ecs;
        this.random = new Random();
    }

    public void requestDocking() {
        Entity player = ecs.createEntity(PLAYER);
        PositionComponent position = ecs.getComponent(player, PositionComponent.class);
        
        if (!position.room.equals("Bridge")) {
            System.out.println("You can only request docking from the bridge.");
            return;
        }
        
        DockingComponent docking = ecs.getComponent(ecs.createEntity("docking"), DockingComponent.class);
        if (docking.requested) {
            System.out.println("Docking already requested.");
            return;
        }
        
        System.out.println("You lean into the comms unit and key the mic.");
        System.out.println("KY-25B: 'Starbase Omicron, this is KY-25B requesting docking clearance. Over.'");
        System.out.println("...static crackles...");
        
        // Assign a random docking pad
        docking.pad = random.nextInt(5) + 1;
        System.out.println("Starbase Omicron: 'KY-25B, this is Omicron Control. Clearance granted. Proceed to docking pad " + 
                         docking.pad + ". Maintain approach vector. Out.'");
        
        docking.requested = true;
    }
    
    public void initiateDocking() {
        Entity player = ecs.createEntity(PLAYER);
        PositionComponent position = ecs.getComponent(player, PositionComponent.class);
        DockingComponent docking = ecs.getComponent(ecs.createEntity("docking"), DockingComponent.class);
        
        if (!position.room.equals("Bridge")) {
            System.out.println("You can only initiate docking from the bridge.");
            return;
        }
        
        if (!docking.requested) {
            System.out.println("You need to request docking clearance first.");
            return;
        }
        
        if (docking.initiated) {
            System.out.println("Docking sequence already complete.");
            return;
        }
        
        // Perform docking sequence
        System.out.println("You grip the controls and align KY-25B with pad " + docking.pad + ".");
        System.out.println("The ship hums as thrusters fire, nudging you toward the starbase.");
        System.out.println("The docking arms loom larger in the window, guiding you in.");
        System.out.println("A soft thud reverberates as magnetic clamps engage.");
        System.out.println("Starbase Omicron: 'KY-25B, docking complete. Welcome aboard.'");
        
        docking.initiated = true;
        docking.inSpace = false;
        
        // Update room descriptions to reflect docked state
        RenderSystem renderSystem = ecs.getSystem(RenderSystem.class);
        if (renderSystem != null) {
            renderSystem.updateRoomDescriptions();
        }
    }
}
