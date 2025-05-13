import java.util.Scanner;

/**
 * System that handles user input and command processing
 */
public class CommandSystem implements Esystem {
    private ECS ecs;
    private Scanner scanner;
    private static final String PLAYER = "player";
    private String lastInput = "";
    
    public CommandSystem(ECS ecs) {
        this.ecs = ecs;
        this.scanner = new Scanner(System.in);
    }
    
    public void processCommand() {
        System.out.print("> ");
        String input = scanner.nextLine().trim().toLowerCase();
        lastInput = input;
        
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";
        
        switch (command) {
            case "go":
                handleMove(argument);
                break;
            case "look":
                handleLook();
                break;
            case "examine":
                handleExamine(argument);
                break;
            case "request":
                handleRequest(argument);
                break;
            case "initiate":
                handleInitiate(argument);
                break;
            case "a":
            case "aft":
                handleMove("aft");
                break;
            case "f":
            case "fore":
                handleMove("fore");
                break;
            case "p":
            case "port":
                handleMove("port");
                break;
            case "s":
            case "starboard":
                handleMove("starboard");
                break;
            case "u":
            case "up":
                handleMove("up");
                break;
            case "d":
            case "down":
                handleMove("down");
                break;
            case "o":
            case "out":
                handleMove("out");
                break;
            case "in":
                handleMove("in");
                break;
            case "i":
            case "inventory":
                handleInventory();
                break;
            case "get":
            case "take":
                handleTake(argument);
                break;
            case "wear":
            case "don":
                handleWear(argument);
                break;
            case "remove":
            case "doff":
                handleRemove(argument);
                break;
            case "cycle":
                handleCycle(argument);
                break;
            case "quit":
                handleQuit();
                break;
            default:
                System.out.println("Unknown command. Try: go, look, examine, request, initiate, quit");
        }
    }
    
    private void handleMove(String direction) {
        MovementSystem movement = ecs.getSystem(MovementSystem.class);
        Entity player = ecs.createEntity(PLAYER);
        
        if (movement != null) {
            String oldRoom = ecs.getComponent(player, PositionComponent.class).room;
            movement.moveEntity(player, direction);
            String newRoom = ecs.getComponent(player, PositionComponent.class).room;
            
            if (!newRoom.equals(oldRoom)) {
                // Room changed, display it
                RenderSystem render = ecs.getSystem(RenderSystem.class);
                if (render != null) {
                    render.displayRoom(newRoom, false);
                }
            } else {
                System.out.println("You can't go that way.");
            }
        }
    }
    
    private void handleLook() {
        Entity player = ecs.createEntity(PLAYER);
        PositionComponent position = ecs.getComponent(player, PositionComponent.class);
        
        if (position != null) {
            RenderSystem render = ecs.getSystem(RenderSystem.class);
            if (render != null) {
                render.displayRoom(position.room, true);
            }
        }
    }
    
    private void handleExamine(String item) {
        ItemSystem inventory = ecs.getSystem(ItemSystem.class);
        if (inventory != null) {
            inventory.examineItem(item);
        }
    }
    
    private void handleRequest(String argument) {
        if (argument.equals("docking")) {
            DockingSystem docking = ecs.getSystem(DockingSystem.class);
            if (docking != null) {
                docking.requestDocking();
            }
        } else {
            System.out.println("Request what? Try 'request docking' from the bridge.");
        }
    }
    
    private void handleInitiate(String argument) {
        if (argument.equals("docking")) {
            DockingSystem docking = ecs.getSystem(DockingSystem.class);
            if (docking != null) {
                docking.initiateDocking();
            }
        } else {
            System.out.println("Initiate what? Try 'initiate docking' from the bridge.");
        }
    }
    
    private void handleInventory() {
        RenderSystem render = ecs.getSystem(RenderSystem.class);
        if (render != null) {
            render.displayInventory();
        }
    }
    
    private void handleTake(String item) {
        ItemSystem inventory = ecs.getSystem(ItemSystem.class);
        if (inventory != null) {
            inventory.takeItem(item);
        }
    }
    
    private void handleWear(String item) {
        ItemSystem inventory = ecs.getSystem(ItemSystem.class);
        if (inventory != null) {
            inventory.wearItem(item);
        }
    }
    
    private void handleRemove(String item) {
        ItemSystem inventory = ecs.getSystem(ItemSystem.class);
        if (inventory != null) {
            inventory.removeItem(item);
        }
    }
    
    private void handleCycle(String argument) {
        if (argument.equals("airlock")) {
            AirlockSystem airlock = ecs.getSystem(AirlockSystem.class);
            if (airlock != null) {
                airlock.cycleAirlock(scanner);
            }
        } else {
            System.out.println("Cycle what? Try 'cycle airlock' from within the airlock.");
        }
    }
    
    private void handleQuit() {
        System.out.println("Shutting down systems. Goodbye, Commander.");
        System.exit(0);
    }
    
    public String getLastInput() {
        return lastInput;
    }
}
