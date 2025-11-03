package starlords.util.lordUpgrades.types;

import starlords.person.Lord;
import starlords.util.lordUpgrades.UpgradeBase;
import starlords.util.lordUpgrades.UpgradeData;

import java.util.ArrayList;
import java.util.HashMap;

public class Upgrade_Officers implements UpgradeBase {
    @Override
    public ArrayList<String> getAIModifiers(Lord lord, UpgradeData data) {
        return null;
    }

    @Override
    public ArrayList<String> getCostModifiers(Lord lord, UpgradeData data) {
        return null;
    }

    @Override
    public ArrayList<String> getWeightModifiers(Lord lord, UpgradeData data) {
        return null;
    }

    @Override
    public double getWeight(Lord lord, UpgradeData data, HashMap<String, Double> modifiers) {
        return 0;
    }

    @Override
    public double getAIWeight(Lord lord, HashMap<String, Double> modifiers) {
        return 0;
    }

    @Override
    public double getCost(Lord lord, UpgradeData data, HashMap<String, Double> modifiers) {
        return 0;
    }

    @Override
    public void applyUpgrade(Lord lord, UpgradeData data, double creditsSpent, HashMap<String, Double> costModifiers) {

    }

    @Override
    public boolean canPreformUpgrade(Lord lord, UpgradeData data) {
        return false;
    }
}
