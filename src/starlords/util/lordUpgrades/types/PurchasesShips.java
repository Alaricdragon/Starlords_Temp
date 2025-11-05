package starlords.util.lordUpgrades.types;

import starlords.person.Lord;
import starlords.util.lordUpgrades.UpgradeBase;
import starlords.util.lordUpgrades.UpgradeData;

import java.util.ArrayList;
import java.util.HashMap;

public class PurchasesShips implements UpgradeBase {
    /*
    * todo: make it so this respects things like max number of ships, and max DP of ships.
     *      - remember that civ ships and military ships will need to be divided here (and some ships will count as both)
    *       make it so this can respect what ships a given starlord wants -now-. (I will need to rebuild fleet building before I can compleat this, wont I?)
    *
    *
    * */
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
