import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages and executes all systems in the ECS
 * 
 * This class handles the registration and execution of systems.
 * Systems are executed in priority order each frame/tick.
 */
public class SystemManager {
    private List<Esystem> systems;
    private boolean needsSort;
    
    public SystemManager() {
        systems = new CopyOnWriteArrayList<>();
        needsSort = false;
    }
    
    /**
     * Register a system
     */
    public void registerSystem(Esystem system) {
        systems.add(system);
        needsSort = true;
    }
    
    /**
     * Unregister a system
     */
    public void unregisterSystem(Esystem system) {
        systems.remove(system);
    }
    
    /**
     * Update all systems
     */
    public void update(double deltaTime) {
        // Sort systems by priority if needed
        if (needsSort) {
            systems.sort(Comparator.comparingInt(Esystem::getPriority));
            needsSort = false;
        }
        
    }
    
    /**
     * Get a system of a specific type
     */
    @SuppressWarnings("unchecked")
    public <T extends Esystem> T getSystem(Class<T> systemClass) {
        for (Esystem system : systems) {
            if (systemClass.isInstance(system)) {
                return (T) system;
            }
        }
        return null;
    }
}
