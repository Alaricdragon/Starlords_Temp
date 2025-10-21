package starlords.util.lordUpgrades.types;

import starlords.person.Lord;
import starlords.util.lordUpgrades.UpgradeBase;
import starlords.util.lordUpgrades.UpgradeData;

public class Upgrade_Ships implements UpgradeBase {
    @Override
    public double getWeight(Lord lord, UpgradeData data) {
        return 0;
    }

    @Override
    public double getAIWeight(Lord lord) {
        return 0;
    }

    @Override
    public int getCost(Lord lord, UpgradeData data) {
        return 0;
    }

    @Override
    public void applyUpgrade(Lord lord, UpgradeData data, int creditsSpent) {

    }

    @Override
    public boolean canPreformUpgrade(Lord lord, UpgradeData data) {
        return false;
    }

    @Override
    public boolean hasEnoughCreditsForUpgrade(Lord lord, UpgradeData data) {
        return false;
    }
}
