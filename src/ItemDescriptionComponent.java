import java.util.Map;
import java.util.HashMap;

/**
 * COMPONENT class for context-sensitive item descriptions in the ECS pattern
 * 
 * This component allows items to have different descriptions based on their location
 * or state. It stores multiple descriptions keyed by context (e.g., room name).
 */
public class ItemDescriptionComponent implements Component {
    private final Map<String, String> contextDescriptions;
    private final String defaultDescription;
    
    public ItemDescriptionComponent(String defaultDescription) {
        this.defaultDescription = defaultDescription;
        this.contextDescriptions = new HashMap<>();
    }
    
    public void addContextDescription(String context, String description) {
        contextDescriptions.put(context, description);
    }
    
    public String getDescription(String context) {
        return contextDescriptions.getOrDefault(context, defaultDescription);
    }
    
    public String getDefaultDescription() {
        return defaultDescription;
    }
}
