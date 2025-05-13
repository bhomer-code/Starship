/**
 * SYSTEM base interface for the ECS pattern
 * 
 * Systems contain the game logic and operate on entities with specific sets of components.
 * They should be stateless and operate only on the data in components.
 */
public interface Esystem {
    
    /**
     * Priority for system execution order (lower values execute first)
     */
    default int getPriority() {
        return 0;
    }
}
