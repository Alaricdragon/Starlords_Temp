package starlords.util.weights;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class UpgradeWeights {
    private HashMap<String,ItemWeight> AIWeights = new HashMap<>();
    private HashMap<String,Double> costWeights = new HashMap<>();
    private HashMap<String,Boolean> enabled = new HashMap<>();
}
