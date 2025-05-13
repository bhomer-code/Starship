import java.util.HashMap;
import java.util.Map;

/**
 * COMPONENT class for equipment/wearing state in the ECS pattern
 * 
 * This component tracks what equipment an entity (typically the player) is wearing.
 * It supports multiple equipment slots (like armor, weapon, suit, etc.)
 */
public class EquipmentComponent implements Component {
    private Map<EquipmentSlot, String> equipped;
    
    public EquipmentComponent() {
        equipped = new HashMap<>();
    }
    
    public void equipItem(EquipmentSlot slot, String item) {
        equipped.put(slot, item);
    }
    
    public void unequipItem(EquipmentSlot slot) {
        equipped.remove(slot);
    }
    
    public String getEquippedItem(EquipmentSlot slot) {
        return equipped.get(slot);
    }
    
    public boolean isEquipped(EquipmentSlot slot) {
        return equipped.containsKey(slot);
    }
    
    public enum EquipmentSlot {
        VAC_SUIT,
        WEAPON,
        TOOL
    }
}
