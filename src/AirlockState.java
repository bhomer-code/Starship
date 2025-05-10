/**
 * COMPONENT class for airlock state in the ECS pattern
 * 
 * This component tracks the state of the airlock, including how many turns
 * the player has been in the airlock without a suit (for calculating death)
 * and whether the airlock has been cycled.
 */
public class AirlockState {
    int turnsWithoutSuit = 0;
    boolean cycled = false;
}
