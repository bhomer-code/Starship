import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Central manager for ECS components
 * 
 * This class manages the mapping between entities and their components.
 * It provides a type-safe way to add, remove, and query components.
 */
public class ComponentManager {
    private Map<Class<? extends Component>, Map<Entity, ? extends Component>> componentMaps;
    
    public ComponentManager() {
        componentMaps = new HashMap<>();
    }
    
    /**
     * Add a component to an entity
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> void addComponent(Entity entity, T component) {
        Map<Entity, T> map = (Map<Entity, T>) componentMaps.computeIfAbsent(
            component.getClass(), 
            k -> new HashMap<>()
        );
        map.put(entity, component);
    }
    
    /**
     * Remove a component from an entity
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> void removeComponent(Entity entity, Class<T> componentClass) {
        Map<Entity, T> map = (Map<Entity, T>) componentMaps.get(componentClass);
        if (map != null) {
            map.remove(entity);
        }
    }
    
    /**
     * Get a component from an entity
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Entity entity, Class<T> componentClass) {
        Map<Entity, T> map = (Map<Entity, T>) componentMaps.get(componentClass);
        if (map != null) {
            return map.get(entity);
        }
        return null;
    }
    
    /**
     * Check if an entity has a specific component
     */
    public <T extends Component> boolean hasComponent(Entity entity, Class<T> componentClass) {
        Map<Entity, T> map = (Map<Entity, T>) componentMaps.get(componentClass);
        return map != null && map.containsKey(entity);
    }
    
    /**
     * Get all entities that have a specific component
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> Collection<Entity> getEntitiesWithComponent(Class<T> componentClass) {
        Map<Entity, T> map = (Map<Entity, T>) componentMaps.get(componentClass);
        if (map != null) {
            return map.keySet();
        }
        return new java.util.ArrayList<>();
    }
    
    /**
     * Get all entities that have ALL specified components
     */
    @SafeVarargs
    public final Collection<Entity> getEntitiesWithComponents(Class<? extends Component>... componentClasses) {
        if (componentClasses.length == 0) {
            return new java.util.ArrayList<>();
        }
        
        Collection<Entity> result = getEntitiesWithComponent(componentClasses[0]);
        
        for (int i = 1; i < componentClasses.length; i++) {
            final Class<? extends Component> currentClass = componentClasses[i];
            result = result.stream()
                .filter(entity -> hasComponent(entity, currentClass))
                .collect(Collectors.toList());
        }
        
        return result;
    }
}
