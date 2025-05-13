/**
 * Handles initialization of the game world, entities, and components
 */
public class GameInitializer {
    private ECS ecs;
    
    public GameInitializer(ECS ecs) {
        this.ecs = ecs;
    }
    
    public void initializeGame() {
        // Create room entities and their components
        initializeRooms();
        
        // Create and initialize player
        initializePlayer();
        
        // Create game state entities
        initializeGameEntities();
        
        // Display initial message
        displayWelcomeMessage();
    }
    
    private void initializeRooms() {
        // Commons
        Entity commons = createRoom("Commons");
        addRoomComponents(commons, 
            "The ship's commons, a small lounge area.",
            "The commons is a modest lounge at the heart of KY-25B. Worn cushions line a bench along the port wall, and a small table is bolted to the deck. A window offers a breathtaking view of Starbase Omicron's docking entry, its metallic arms glinting against the black void."
        );
        addExit(commons, "aft", "Engine");
        addExit(commons, "fore", "Hall");
        addRoomFixedItem(commons, "window");
        
        // Engine
        Entity engine = createRoom("Engine");
        addRoomComponents(engine,
            "The engine room, filled with machinery.",
            "The engine room thrums with the pulse of KY-25B's fusion drive. Pipes and conduits snake across the walls, and a control panel flickers with status lights. A heavy wrench lies abandoned near the reactor housing."
        );
        addExit(engine, "down", "Cargo");
        addExit(engine, "fore", "Commons");
        addRoomItem(engine, "wrench");
        
        // Cargo
        Entity cargo = createRoom("Cargo");
        addRoomComponents(cargo,
            "The cargo bay, dimly lit and cluttered.",
            "The cargo bay is a shadowy hold below the engine room. Crates are lashed to the deck, their contents rattling faintly with the ship's vibrations. A dusty tarp covers something bulky in the corner."
        );
        addExit(cargo, "up", "Engine");
        addRoomItem(cargo, "tarp");
        
        // Hall
        Entity hall = createRoom("Hall");
        addRoomComponents(hall,
            "A narrow hallway connecting key areas.",
            "This narrow hallway runs fore to aft, its walls lined with conduit panels. Doors branch off to port and starboard, leading to the stateroom and ship locker, while the bridge lies ahead."
        );
        addExit(hall, "aft", "Commons");
        addExit(hall, "fore", "Bridge");
        addExit(hall, "port", "Stateroom");
        addExit(hall, "starboard", "Ship Locker");
        
        // Stateroom
        Entity stateroom = createRoom("Stateroom");
        addRoomComponents(stateroom,
            "A cozy stateroom with a bunk.",
            "The stateroom is a tight but comfortable retreat. A bunk is tucked against the wall, a folded blanket atop it. A small window reveals the starbase's silhouette, framed by distant stars."
        );
        addExit(stateroom, "starboard", "Hall");
        addRoomFixedItem(stateroom, "window");
        
        // Ship Locker
        Entity shipLocker = createRoom("Ship Locker");
        addRoomComponents(shipLocker,
            "A cramped locker room for gear.",
            "The ship locker is a utilitarian space crammed with EVA suits and tools. A spare oxygen tank leans against the bulkhead, its gauge glowing faintly."
        );
        addExit(shipLocker, "port", "Hall");
        addExit(shipLocker, "starboard", "Airlock");
        addRoomItem(shipLocker, "vac-suit");
        addRoomItem(shipLocker, "tank");
        
        // Airlock
        Entity airlock = createRoom("Airlock");
        addRoomComponents(airlock,
            "The airlock, ready for EVA.",
            "The airlock is a stark chamber with reinforced walls. A control panel blinks beside the outer hatch, ready to cycle into the void."
        );
        addExit(airlock, "port", "Ship Locker");
        addExit(airlock, "out", "outside");
        
        // Bridge
        Entity bridge = createRoom("Bridge");
        addRoomComponents(bridge,
            "The bridge, command center of KY-25B.",
            "The bridge is KY-25B's nerve center. A wide window dominates the forward bulkhead, showcasing Starbase Omicron's docking arms against the void. The pilot's chair faces a console studded with controls and a comms unit."
        );
        addExit(bridge, "aft", "Hall");
        addRoomFixedItem(bridge, "console");
        addRoomFixedItem(bridge, "window");
        
        // Outside
        Entity outside = createRoom("outside");
        addRoomComponents(outside,
            "Outside the ship, floating in space.",
            "You float weightless outside KY-25B, the stars endless around you. The ship's hull gleams faintly in the starlight."
        );
        addExit(outside, "in", "Airlock");
    }
    
    private Entity createRoom(String roomId) {
        Entity room = ecs.createEntity(roomId);
        ecs.addComponent(room, new PositionComponent(roomId));
        return room;
    }
    
    private void addRoomComponents(Entity room, String shortDesc, String longDesc) {
        ecs.addComponent(room, new DescriptionComponent(shortDesc, longDesc));
        ecs.addComponent(room, new ExitsComponent());
        ecs.addComponent(room, new ItemsComponent());
        ecs.addComponent(room, new FixedItemsComponent());
    }
    
    private void addExit(Entity room, String direction, String destination) {
        ExitsComponent exits = ecs.getComponent(room, ExitsComponent.class);
        exits.addExit(direction, destination);
    }
    
    private void addRoomItem(Entity room, String item) {
        ItemsComponent items = ecs.getComponent(room, ItemsComponent.class);
        items.addItem(item);
    }
    
    private void addRoomFixedItem(Entity room, String fixedItem) {
        FixedItemsComponent fixedItems = ecs.getComponent(room, FixedItemsComponent.class);
        fixedItems.addFixedItem(fixedItem);
    }
    
    private void initializePlayer() {
        Entity player = ecs.createEntity("player");
        ecs.addComponent(player, new PositionComponent("Commons"));
        ecs.addComponent(player, new InventoryComponent(10));
        ecs.addComponent(player, new EquipmentComponent());
    }
    
    private void initializeGameEntities() {
        // Create docking state entity
        Entity dockingEntity = ecs.createEntity("docking");
        ecs.addComponent(dockingEntity, new DockingComponent());
        
        // Create airlock state entity
        Entity airlockEntity = ecs.createEntity("airlock");
        ecs.addComponent(airlockEntity, new AirlockStateComponent());
    }
    
    private void displayWelcomeMessage() {
        System.out.println("You are aboard the starship KY-25B, floating in the void outside Starbase Omicron's docking entry.");
        System.out.println("The hum of the engines vibrates faintly through the hull.");
        
        // Display starting room
        RenderSystem render = ecs.getSystem(RenderSystem.class);
        if (render != null) {
            render.displayRoom("Commons", false);
        }
    }
}
