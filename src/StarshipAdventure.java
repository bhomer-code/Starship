/**
 * Starship Adventure Game - Improved ECS Architecture
 * 
 * This is an improved implementation using proper ECS (Entity-Component-System) patterns:
 * 
 * ENTITIES (E): Unique IDs representing game objects
 * - Rooms ("Commons", "Bridge", etc.)
 * - Player ("player")
 * - Game state objects ("docking", "airlock")
 * 
 * COMPONENTS (C): Pure data holders implementing Component interface
 * - PositionComponent, DescriptionComponent, InventoryComponent, etc.
 * - Components have no behavior, only data
 * 
 * SYSTEMS (S): Logic and behavior implementing System interface
 * - MovementSystem, RenderSystem, InventorySystem, etc.
 * - Systems operate on entities with specific components
 * 
 * Key improvements over the original:
 * - Type-safe component management
 * - Clear separation of concerns
 * - Reusable, extensible systems
 * - Better encapsulation and maintainability
 * 
 * Runtime experience remains identical to the original implementation.
 */
public class StarshipAdventure {
    public static void main(String[] args) {
        // Initialize the ECS framework
        ECS ecs = ECS.getInstance();
        
        // Register all systems
        registerSystems(ecs);
        
        // Initialize the game world
        GameInitializer initializer = new GameInitializer(ecs);
        initializer.initializeGame();
        
        // Start the game loop
        gameLoop(ecs);
    }
    
    private static void registerSystems(ECS ecs) {
        // Register systems in order of dependency
        ecs.registerSystem(new RenderSystem(ecs));
        ecs.registerSystem(new MovementSystem(ecs));
        ecs.registerSystem(new ItemDescriptionSystem(ecs)); // Handles context-sensitive descriptions
        ecs.registerSystem(new ItemSystem(ecs));
        ecs.registerSystem(new DockingSystem(ecs));
        ecs.registerSystem(new AirlockSystem(ecs));
        ecs.registerSystem(new CommandSystem(ecs));
    }
    
    private static void gameLoop(ECS ecs) {
        CommandSystem commandSystem = ecs.getSystem(CommandSystem.class);
        
        if (commandSystem == null) {
            System.err.println("Failed to initialize command system!");
            return;
        }
        
        // Main game loop
        while (true) {
            // Process player input
            commandSystem.processCommand();
            
            // Update all systems (though most are event-driven)
            ecs.update(0.0); // Delta time not used in this text-based game
        }
    }
}
