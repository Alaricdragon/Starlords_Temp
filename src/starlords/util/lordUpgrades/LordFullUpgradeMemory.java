package starlords.util.lordUpgrades;

import lombok.Getter;

import java.util.HashMap;

public class LordFullUpgradeMemory {
    public static final String memoryName = "LordFullUpgradeMemory";
    /*how this works:
    * first of all, the Integer relates to a 'control integer' in the upgrade controller.
    * if a new upgrade is added / removed, all 'full lord memorys' are changed, to make sure the lords upgrade memory stays synced with all other upgrade memory's.
    * */
    @Getter
    private HashMap<Integer,LordUpgradeMemory> Memory = new HashMap<>();
}
