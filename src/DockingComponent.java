/**
 * COMPONENT class for docking system state in the ECS pattern
 * 
 * This component tracks the state of the docking process with the starbase,
 * including whether docking has been requested, initiated, which pad is assigned,
 * and whether the ship is in space or docked.
 */
public class DockingComponent implements Component {
    public boolean requested;
    public boolean initiated;
    public int pad;
    public boolean inSpace;
    
    public DockingComponent() {
        this.requested = false;
        this.initiated = false;
        this.pad = -1;
        this.inSpace = true;
    }
    
    public boolean isReadyForInitiation() {
        return requested && !initiated;
    }
    
    public boolean isFullyDocked() {
        return initiated && !inSpace;
    }
}
