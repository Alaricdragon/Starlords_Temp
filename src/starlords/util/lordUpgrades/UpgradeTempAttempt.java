package starlords.util.lordUpgrades;

import com.fs.starfarer.api.Global;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import starlords.person.Lord;

public class UpgradeTempAttempt implements UpgradeBase{
    @Override
    public void init(JSONObject json) {

    }

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
