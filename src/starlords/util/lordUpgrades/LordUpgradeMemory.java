package starlords.util.lordUpgrades;

import lombok.Getter;

import java.util.HashMap;

public class LordUpgradeMemory {
    /*
    * how this works:
    * a controller is held in Upgrade Controller that remembers the ID of each and every upgrade and upgrade data between sessions.
    * if a new data is added / removed, all lord memory's are reorganized to reflect that.
    * this was done to prevent many many strings from being stored in starlords (lower memory use. it could get quite intense.)*/
    @Getter
    private HashMap<Integer,Double> aiUpgradeWeight = new HashMap<>();
    @Getter
    private HashMap<Integer,Double> costUpgradeWeight = new HashMap<>();
}
