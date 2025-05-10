import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * ECS (Entity-Component-System) Architecture - Starship Adventure Game
 * 
 * This weak ECS implementation follows the basic pattern:
 * 
 * ENTITIES (E): Unique IDs or names (e.g., "Commons", "player", "Bridge")
 * - Represented as keys in HashMaps
 * - No inherent behavior, just identifiers
 * 
 * COMPONENTS (C): Pure data holders (e.g., Position, Description, Exits)
 * - Store state/data associated with entities
 * - No behavior, just data structures
 * 
 * SYSTEMS (S): Logic and behavior (e.g., move(), displayRoom(), gameLoop())
 * - Operate on entities with specific components
 * - Process game logic, handle input, update state
 * 
 * This implementation mixes ECS with some non-ECS patterns (static methods, some entity-specific logic)
 * but demonstrates the core concept of separating data (components) from behavior (systems).
 */
public class StarshipAdventure {
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    static String input = "";

    // NOTE: Component classes are now in separate files:
    // Position.java, Description.java, Exits.java, Items.java, Player.java, Docking.java, AirlockState.java

    // ============================================
    // ENTITY STORAGE - Maps entities to components
    // ============================================
    // These maps associate entity IDs (strings like "Commons", "player") with their components
    static Map<String, Position> positions = new HashMap<>();          // Entity -> Position component
    static Map<String, Description> descriptions = new HashMap<>();    // Entity -> Description component
    static Map<String, Exits> exits = new HashMap<>();                 // Entity -> Exits component
    static Map<String, Items> roomItems = new HashMap<>();             // Entity -> Items component
    
    // Singleton components (could be improved by making these entities with single instances)
    static Player player = new Player();                               // Player component (improper ECS)
    static Docking docking = new Docking();                           // Docking component (improper ECS)
    static AirlockState airlockState = new AirlockState();             // AirlockState component (improper ECS)

    // Special entities
    static final String PLAYER = "player";
    static final String OUTSIDE = "outside";

    // ============================================
    // SYSTEMS - Game logic and behavior
    // ============================================
    // These static methods operate on entities with specific components
    // to implement game mechanics and behavior

    public static void main(String[] args) {
        // Initialize rooms
        initializeRooms();
        // Initialize player
        positions.put(PLAYER, new Position("Commons"));
        // Initialize outside area
        initializeOutside();

        System.out.println("You are aboard the starship KY-25B, floating in the void outside Starbase Omicron's docking entry.");
        System.out.println("The hum of the engines vibrates faintly through the hull.");
        displayRoom(positions.get(PLAYER).room);
        gameLoop();
    }

    // SYSTEM: Initialize room entities and their components
    static void initializeRooms() {
        // Commons
        positions.put("Commons", new Position("Commons"));
        descriptions.put("Commons", new Description(
            "The ship's commons, a small lounge area.",
            "The commons is a modest lounge at the heart of KY-25B. Worn cushions line a bench along the port wall, and a small table is bolted to the deck. A window offers a breathtaking view of Starbase Omicron's docking entry, its metallic arms glinting against the black void."
        ));
        exits.put("Commons", new Exits());
        exits.get("Commons").exits.put("aft", "Engine");
        exits.get("Commons").exits.put("fore", "Hall");

        // Engine
        positions.put("Engine", new Position("Engine"));
        descriptions.put("Engine", new Description(
            "The engine room, filled with machinery.",
            "The engine room thrums with the pulse of KY-25B's fusion drive. Pipes and conduits snake across the walls, and a control panel flickers with status lights. A heavy wrench lies abandoned near the reactor housing."
        ));
        exits.put("Engine", new Exits());
        exits.get("Engine").exits.put("down", "Cargo");
        exits.get("Engine").exits.put("fore", "Commons");
        roomItems.put("Engine", new Items());
        roomItems.get("Engine").items.add("wrench");

        // Cargo
        positions.put("Cargo", new Position("Cargo"));
        descriptions.put("Cargo", new Description(
            "The cargo bay, dimly lit and cluttered.",
            "The cargo bay is a shadowy hold below the engine room. Crates are lashed to the deck, their contents rattling faintly with the ship's vibrations. A dusty tarp covers something bulky in the corner."
        ));
        exits.put("Cargo", new Exits());
        exits.get("Cargo").exits.put("up", "Engine");
        roomItems.put("Cargo", new Items());
        roomItems.get("Cargo").items.add("tarp");

        // Hall
        positions.put("Hall", new Position("Hall"));
        descriptions.put("Hall", new Description(
            "A narrow hallway connecting key areas.",
            "This narrow hallway runs fore to aft, its walls lined with conduit panels. Doors branch off to port and starboard, leading to the stateroom and ship locker, while the bridge lies ahead."
        ));
        exits.put("Hall", new Exits());
        exits.get("Hall").exits.put("aft", "Commons");
        exits.get("Hall").exits.put("fore", "Bridge");
        exits.get("Hall").exits.put("port", "Stateroom");
        exits.get("Hall").exits.put("starboard", "Ship Locker");

        // Stateroom
        positions.put("Stateroom", new Position("Stateroom"));
        descriptions.put("Stateroom", new Description(
            "A cozy stateroom with a bunk.",
            "The stateroom is a tight but comfortable retreat. A bunk is tucked against the wall, a folded blanket atop it. A small window reveals the starbase's silhouette, framed by distant stars."
        ));
        exits.put("Stateroom", new Exits());
        exits.get("Stateroom").exits.put("starboard", "Hall");

        // Ship Locker
        positions.put("Ship Locker", new Position("Ship Locker"));
        descriptions.put("Ship Locker", new Description(
            "A cramped locker room for gear.",
            "The ship locker is a utilitarian space crammed with EVA suits and tools. A spare oxygen tank leans against the bulkhead, its gauge glowing faintly."
        ));
        exits.put("Ship Locker", new Exits());
        exits.get("Ship Locker").exits.put("port", "Hall");
        exits.get("Ship Locker").exits.put("starboard", "Airlock");
        roomItems.put("Ship Locker", new Items());
        roomItems.get("Ship Locker").items.add("vac-suit");
        roomItems.get("Ship Locker").items.add("tank");

        // Airlock
        positions.put("Airlock", new Position("Airlock"));
        descriptions.put("Airlock", new Description(
            "The airlock, ready for EVA.",
            "The airlock is a stark chamber with reinforced walls. A control panel blinks beside the outer hatch, ready to cycle into the void."
        ));
        exits.put("Airlock", new Exits());
        exits.get("Airlock").exits.put("port", "Ship Locker");
        exits.get("Airlock").exits.put("out", OUTSIDE);

        // Bridge
        positions.put("Bridge", new Position("Bridge"));
        descriptions.put("Bridge", new Description(
            "The bridge, command center of KY-25B.",
            "The bridge is KY-25B's nerve center. A wide window dominates the forward bulkhead, showcasing Starbase Omicron's docking arms against the void. The pilot's chair faces a console studded with controls and a comms unit."
        ));
        exits.put("Bridge", new Exits());
        exits.get("Bridge").exits.put("aft", "Hall");
        roomItems.put("Bridge", new Items());
        roomItems.get("Bridge").items.add("console");
    }

    static void initializeOutside() {
        positions.put(OUTSIDE, new Position(OUTSIDE));
        descriptions.put(OUTSIDE, new Description(
            "Outside the ship, floating in space.",
            "You float weightless outside KY-25B, the stars endless around you. The ship's hull gleams faintly in the starlight."
        ));
        exits.put(OUTSIDE, new Exits());
        exits.get(OUTSIDE).exits.put("in", "Airlock");
    }

    // SYSTEM: Main game loop - handles player input and delegates to other systems
    static void gameLoop() {
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine().trim().toLowerCase();
            processCommand(input);
        }
    }

    // SYSTEM: Process player commands and dispatch to appropriate subsystems
    static void processCommand(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "go":
                move(argument);
                break;
            case "look":
                displayRoom(positions.get(PLAYER).room);
                break;
            case "examine":
                examine(argument);
                break;
            case "request":
                if (argument.equals("docking") && positions.get(PLAYER).room.equals("Bridge")) {
                    requestDocking();
                } else {
                    System.out.println("You can only request docking from the bridge.");
                }
                break;
            case "initiate":
                if (argument.equals("docking") && positions.get(PLAYER).room.equals("Bridge") && docking.requested) {
                    initiateDocking();
                } else if (!docking.requested) {
                    System.out.println("You need to request docking clearance first.");
                } else {
                    System.out.println("You can only initiate docking from the bridge.");
                }
                break;
            case "a":
            case "aft":
                move("aft");
                break;
            case "f":
            case "fore":
                move("fore");
                break;
            case "p":
            case "port":
                move("port");
                break;
            case "s":
            case "starboard":
                move("starboard");
                break;
            case "u":
            case "up":
                move("up");
                break;
            case "d":
            case "down":
                move("down");
                break;
            case "o":
            case "out":
                move("out");
                break;
            case "in":
                move("in");
                break;
            case "i":
            case "inventory":
                showInventory();
                break;
            case "get":
            case "take":
                takeItem(argument);
                break;
            case "wear":
            case "don":
                wearSuit(argument);
                break;
            case "remove":
            case "doff":
                removeSuit(argument);
                break;
            case "cycle":
                if (argument.equals("airlock") && positions.get(PLAYER).room.equals("Airlock")) {
                    cycleAirlock();
                } else {
                    System.out.println("You can only cycle the airlock from within it.");
                }
                break;
            case "quit":
                System.out.println("Shutting down systems. Goodbye, Commander.");
                System.exit(0);
            default:
                System.out.println("Unknown command. Try: go, look, examine, request, initiate, quit");
        }
    }

    // SYSTEM: Handle entity movement between rooms
    static void move(String direction) {
        String currentRoom = positions.get(PLAYER).room;
        Exits roomExits = exits.get(currentRoom);
        String newRoom = roomExits.exits.getOrDefault(direction, currentRoom);

        if (!newRoom.equals(currentRoom)) {
            // Handle airlock and outside transitions
            if (newRoom.equals(OUTSIDE) && docking.inSpace && !player.wearingSuit) {
                handleAirlockDeath();
                return;
            }
            if (currentRoom.equals("Airlock") && !player.wearingSuit && docking.inSpace) {
                airlockState.turnsWithoutSuit++;
                if (airlockState.turnsWithoutSuit == 1) {
                    System.out.println("You step into the airlock without a suit. The air is thin, and you feel lightheaded.");
                } else if (airlockState.turnsWithoutSuit >= 2 && !airlockState.cycled) {
                    handleAirlockDeath();
                    return;
                }
            } else {
                airlockState.turnsWithoutSuit = 0;
                airlockState.cycled = false;
            }
            positions.get(PLAYER).room = newRoom;
            updateRoomDescriptions();
            displayRoom(newRoom);
        } else {
            System.out.println("You can't go that way.");
        }
    }

    // SYSTEM: Update window views based on docking state 
    static void updateRoomDescriptions() {
        // Update airlock description based on docking state
        if (docking.inSpace) {
            descriptions.get("Airlock").longDesc = "The airlock is a stark chamber with reinforced walls. A control panel blinks beside the outer hatch, ready to cycle into the void.";
        } else {
            descriptions.get("Airlock").longDesc = "The airlock is connected to the starbase, its outer hatch open to a pressurized corridor.";
        }

        // Update outside description based on docking state
        if (docking.inSpace) {
            descriptions.get(OUTSIDE).longDesc = "You float weightless outside KY-25B, the stars endless around you. The ship's hull gleams faintly in the starlight.";
        } else {
            descriptions.get(OUTSIDE).longDesc = "You stand on the starbase's docking pad, feeling the centrifugal gravity. The interior of the starbase bustles with activity.";
        }

        // Update windowed rooms
        String commonsDesc = docking.inSpace ?
            "The commons is a modest lounge at the heart of KY-25B. Worn cushions line a bench along the port wall, and a small table is bolted to the deck. A window offers a breathtaking view of Starbase Omicron's docking entry, its metallic arms glinting against the black void." :
            "The commons is a modest lounge at the heart of KY-25B. Worn cushions line a bench along the port wall, and a small table is bolted to the deck. The window frames the interior of the starbase, bustling with activity.";
        descriptions.get("Commons").longDesc = commonsDesc;

        String stateroomDesc = docking.inSpace ?
            "The stateroom is a tight but comfortable retreat. A bunk is tucked against the wall, a folded blanket atop it. A small window reveals the starbase's silhouette, framed by distant stars." :
            "The stateroom is a tight but comfortable retreat. A bunk is tucked against the wall, a folded blanket atop it. Through the stateroom window, you can see the curved expanse of the interior of the starbase.";
        descriptions.get("Stateroom").longDesc = stateroomDesc;

        String bridgeDesc = docking.inSpace ?
            "The bridge is KY-25B's nerve center. A wide window dominates the forward bulkhead, showcasing Starbase Omicron's docking arms against the void. The pilot's chair faces a console studded with controls and a comms unit." :
            "The bridge is KY-25B's nerve center. A wide window dominates the forward bulkhead, showing the interior of the starbase, bustling with activity. The pilot's chair faces a console studded with controls and a comms unit.";
        descriptions.get("Bridge").longDesc = bridgeDesc;
    }

    // SYSTEM: Render room description and exits
    static void displayRoom(String room) {
        Description desc = descriptions.get(room);
        if (desc.firstVisit || input.equals("look")) {
            System.out.println(desc.longDesc);
            desc.firstVisit = false;
        } else {
            System.out.println(desc.shortDesc);
        }
        System.out.print("Exits: ");
        exits.get(room).exits.keySet().forEach(exit -> System.out.print(exit + " "));
        System.out.println();
    }

    // SYSTEM: Render inventory contents
    static void showInventory() {
        if (player.inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.print("You are carrying: ");
            System.out.println(String.join(", ", player.inventory));
        }
        if (player.wearingSuit) {
            System.out.println("You are wearing a vac-suit.");
        }
    }

    // SYSTEM: Move items to player's inventory
    static void takeItem(String item) {
        String currentRoom = positions.get(PLAYER).room;
        Items items = roomItems.getOrDefault(currentRoom, new Items());
        if (items.items.contains(item)) {
            if (player.inventory.size() < 10) {
                player.inventory.add(item);
                items.items.remove(item);
                System.out.println("You take the " + item + ".");
            } else {
                System.out.println("Your inventory is full.");
            }
        } else {
            System.out.println("You can't take that.");
        }
    }
    
    // SYSTEM: Update state of player to wearing vac-suit, if possible
    static void wearSuit(String item) {
        if (item.equals("vac-suit") && !player.wearingSuit) {
            if (player.inventory.contains("vac-suit")) {
                player.wearingSuit = true;
                System.out.println("You don the vac-suit.");
            } else {
                System.out.println("You don't have a vac-suit to wear.");
            }
        } else if (player.wearingSuit) {
            System.out.println("You're already wearing a vac-suit.");
        } else {
            System.out.println("You can't wear that.");
        }
    }

    // SYSTEM: Update state of player to not wearing vac-suit
    static void removeSuit(String item) {
        if (item.equals("vac-suit") && player.wearingSuit) {
            player.wearingSuit = false;
            System.out.println("You remove the vac-suit.");
        } else {
            System.out.println("You're not wearing a vac-suit.");
        }
    }

    // SYSTEM: Render detailed long description of item
    static void examine(String item) {
        String currentRoom = positions.get(PLAYER).room;
        String longDesc = "";
        switch (item) {
            case "window":
                if (currentRoom.equals("Commons")) {
                    longDesc = docking.inSpace ?
                        "The window frames Starbase Omicron's docking entry, a lattice of steel arms glowing faintly against the infinite black." :
                        "The window frames the interior of the starbase, bustling with activity.";
                } else if (currentRoom.equals("Stateroom")) {
                    longDesc = docking.inSpace ?
                        "Through the stateroom window, the starbase looms, its silhouette stark against a scattering of stars." :
                        "Through the stateroom window, you can see the curved expanse of the interior of the starbase.";
                } else if (currentRoom.equals("Bridge")) {
                    longDesc = docking.inSpace ?
                        "The bridge window offers a commanding view of the starbase's docking arms, poised like a predator in the void." :
                        "The bridge window shows the interior of the starbase, bustling with activity.";
                }
                break;
            case "wrench":
                if (currentRoom.equals("Engine") && roomItems.get("Engine").items.contains("wrench"))
                    longDesc = "A hefty wrench, its handle worn smooth from use, rests near the reactor. It's caked with grease.";
                break;
            case "tarp":
                if (currentRoom.equals("Cargo") && roomItems.get("Cargo").items.contains("tarp"))
                    longDesc = "The tarp is dusty and frayed, draped over a lumpy shape. Peeking beneath reveals a stack of spare parts.";
                break;
            case "tank":
                if (currentRoom.equals("Ship Locker") && roomItems.get("Ship Locker").items.contains("tank"))
                    longDesc = "The oxygen tank is scratched but functional, its gauge showing three-quarters full.";
                break;
            case "console":
                if (currentRoom.equals("Bridge") && roomItems.get("Bridge").items.contains("console"))
                    longDesc = "The console is a maze of switches, dials, and a glowing comms unit, ready to hail the starbase.";
                break;
        }
        if (!longDesc.isEmpty()) {
            System.out.println(longDesc);
        } else {
            System.out.println("Nothing special to see here.");
        }
    }

    // SYSTEM: Change docking state to allow initiation of docking
    static void requestDocking() {
        if (docking.requested) {
            System.out.println("Docking already requested.");
            return;
        }
        System.out.println("You lean into the comms unit and key the mic.");
        System.out.println("KY-25B: 'Starbase Omicron, this is KY-25B requesting docking clearance. Over.'");
        System.out.println("...static crackles...");
        docking.pad = random.nextInt(5) + 1;
        System.out.println("Starbase Omicron: 'KY-25B, this is Omicron Control. Clearance granted. Proceed to docking pad " + docking.pad + ". Maintain approach vector. Out.'");
        docking.requested = true;
    }

    //SYSTEM: Initiate docking state
    static void initiateDocking() {
        if (docking.initiated) {
            System.out.println("Docking sequence already complete.");
            return;
        }
        System.out.println("You grip the controls and align KY-25B with pad " + docking.pad + ".");
        System.out.println("The ship hums as thrusters fire, nudging you toward the starbase.");
        System.out.println("The docking arms loom larger in the window, guiding you in.");
        System.out.println("A soft thud reverberates as magnetic clamps engage.");
        System.out.println("Starbase Omicron: 'KY-25B, docking complete. Welcome aboard.'");
        docking.initiated = true;
        docking.inSpace = false;
        updateRoomDescriptions();
    }

    //SYSTEM: Cycle air lock with effects based on various states
    static void cycleAirlock() {
        if (docking.inSpace && !player.wearingSuit) {
            handleAirlockDeath();
        } else {
            System.out.println("You cycle the airlock. The hatch opens smoothly.");
            if (docking.inSpace) {
                System.out.println("You float weightless, the stars endless around you.");
            } else {
                System.out.println("The starbase's gravity pulls you down as you step onto the pad.");
            }
        }
    }
    
    //SYSTEM: Handle dangerous airlock usage
    static void handleAirlockDeath() {
        System.out.println("The outer hatch opens to the void. Silence engulfs you as the air rushes out.");
        System.out.println("Your vision blurs, ice crystals form on your skin, and your lungs burn.");
        System.out.println("You float weightless, the stars spinning around you.");
        System.out.println("You have one chance to survive. Cycle the airlock now, or perish.");
        System.out.print("> ");
        String nextInput = scanner.nextLine().trim().toLowerCase();
        if (nextInput.equals("cycle airlock")) {
            System.out.println("You slam the controls. The hatch seals, and air floods back in.");
            System.out.println("You collapse, gasping, but alive.");
            airlockState.cycled = true;
        } else {
            System.out.println("The void claims you. Your body drifts among the stars.");
            System.exit(0);
        }
    }
}
