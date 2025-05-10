/**
 * COMPONENT class for docking system state in the ECS pattern
 * 
 * This component tracks the state of the docking process with the starbase,
 * including whether docking has been requested, initiated, which pad is assigned,
 * and whether the ship is in space or docked.
 */
public class Docking {
    boolean requested = false;
    boolean initiated = false;
    int pad = -1;
    boolean inSpace = true;
}
