package starlords.util.memoryUtils.Compressed.hTypes;

import starlords.util.randomLoader.WeightedRandom;
import starlords.util.randomLoader.WeightedRandomScripted;

public class MemCompressed_R_Double_WeightedRandom extends MemCompressed_R_Double_Base {
    private WeightedRandomScripted random;
    public MemCompressed_R_Double_WeightedRandom(WeightedRandomScripted random){
        this.random = random;
    }

    @Override
    public double getRandom(Object linkedObject) {
        random.setData(linkedObject);
        return random.getRandom();
    }
}
