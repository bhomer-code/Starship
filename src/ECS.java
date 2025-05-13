/**
 * Main ECS (Entity Component System) manager
 * 
 * This class provides a unified interface to the ECS architecture.
 * It manages entities, components, and systems in one place.
 */
public class ECS {
    private ComponentManager componentManager;
    private SystemManager systemManager;
    private static ECS instance;
    
    private ECS() {
        componentManager = new ComponentManager();
        systemManager = new SystemManager();
    }
    
    public static ECS getInstance() {
        if (instance == null) {
            instance = new ECS();
        }
        return instance;
    }
    
    // Entity management
    public Entity createEntity(String id) {
        return new Entity(id);
    }
    
    // Component management
    public <T extends Component> void addComponent(Entity entity, T component) {
        componentManager.addComponent(entity, component);
    }
    
    public <T extends Component> void removeComponent(Entity entity, Class<T> componentClass) {
        componentManager.removeComponent(entity, componentClass);
    }
    
    public <T extends Component> T getComponent(Entity entity, Class<T> componentClass) {
        return componentManager.getComponent(entity, componentClass);
    }
    
    public <T extends Component> boolean hasComponent(Entity entity, Class<T> componentClass) {
        return componentManager.hasComponent(entity, componentClass);
    }
    
    public java.util.Collection<Entity> getEntitiesWithComponent(Class<? extends Component> componentClass) {
        return componentManager.getEntitiesWithComponent(componentClass);
    }
    
    @SafeVarargs
    public final java.util.Collection<Entity> getEntitiesWithComponents(Class<? extends Component>... componentClasses) {
        return componentManager.getEntitiesWithComponents(componentClasses);
    }
    
    // System management
    public void registerSystem(Esystem system) {
        systemManager.registerSystem(system);
    }
    
    public void unregisterSystem(Esystem system) {
        systemManager.unregisterSystem(system);
    }
    
    public <T extends Esystem> T getSystem(Class<T> systemClass) {
        return systemManager.getSystem(systemClass);
    }
    
    // Update all systems
    public void update(double deltaTime) {
        systemManager.update(deltaTime);
    }
}
