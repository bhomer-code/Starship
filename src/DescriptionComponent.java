/**
 * COMPONENT class for descriptions in the ECS pattern
 * 
 * This component stores descriptive text for room entities, including both short and long descriptions.
 * It also tracks whether the room has been visited before to control which description to display.
 */
public class DescriptionComponent implements Component {
    public final String shortDesc;
    public final String longDesc;
    public boolean firstVisit;
    
    public DescriptionComponent(String shortDesc, String longDesc) {
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.firstVisit = true;
    }
    
    public String getCurrentDescription(boolean forceLong) {
        if (firstVisit || forceLong) {
            return longDesc;
        }
        return shortDesc;
    }
}
