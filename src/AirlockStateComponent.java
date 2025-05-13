/**
 * COMPONENT class for airlock state in the ECS pattern
 * 
 * This component tracks the state of the airlock, including how many turns
 * the player has been in the airlock without a suit (for calculating death)
 * and whether the airlock has been cycled.
 */
public class AirlockStateComponent implements Component {
    public int turnsWithoutSuit;
    public boolean cycled;
    
    public AirlockStateComponent() {
        this.turnsWithoutSuit = 0;
        this.cycled = false;
    }
    
    public void incrementTurnsWithoutSuit() {
        turnsWithoutSuit++;
    }
    
    public void resetTurnsWithoutSuit() {
        turnsWithoutSuit = 0;
    }
    
    public void setCycled(boolean cycled) {
        this.cycled = cycled;
    }
    
    public boolean isDangerousState() {
        return turnsWithoutSuit >= 2 && !cycled;
    }
}
