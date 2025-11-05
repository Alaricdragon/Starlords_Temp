package starlords.util.lordUpgrades.types;

import starlords.person.Lord;
import starlords.util.lordUpgrades.UpgradeBase;
import starlords.util.lordUpgrades.UpgradeData;

import java.util.ArrayList;
import java.util.HashMap;

public class AddAICores implements UpgradeBase {
    //todo: make settings respect max number of AI cores in a given fleet.
    //      have a cost list of all AI cores, and also let other mods add other AI cores? (have a json or CSV file or something.
    //      -I would like a CSV file more, I can link a AI core plugin and a script for if it can be added to a given fleet. as well as a script / double for weight of the AI core.
    //
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

    @Override
    public boolean canBeAddedToGame() {
        return true;
    }
}
