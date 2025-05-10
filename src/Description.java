/**
 * COMPONENT class for descriptions in the ECS pattern
 * 
 * This component stores descriptive text for room entities, including both short and long descriptions.
 * It also tracks whether the room has been visited before to control which description to display.
 */
public class Description {
    String shortDesc;
    String longDesc;
    boolean firstVisit = true;
    
    Description(String shortDesc, String longDesc) {
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }
}
