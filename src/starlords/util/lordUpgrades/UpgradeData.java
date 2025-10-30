package starlords.util.lordUpgrades;


import java.util.HashMap;

public class UpgradeData {
    public int timesUpgraded=0;
    public float wealthUsed=0;
    public float wealthAtUpgradeStart=0;
    public float wealthReservedForUpgrades = 0;
    public HashMap<String,Integer> upgrades = new HashMap<>();
    public String lastUpgrade = "";
}
