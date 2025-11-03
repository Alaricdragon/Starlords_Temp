package starlords.util.memoryUtils.Compressed.hTypes;

import starlords.util.WeightedRandom;

public class MemCompressed_Random_Double_WeightedRandom extends MemCompressed_Random_Double_Base {
    private WeightedRandom random;
    public MemCompressed_Random_Double_WeightedRandom(WeightedRandom random){
        this.random = random;
    }

    @Override
    public double getRandom(Object linkedObject) {
        return random.getRandom();
    }
}
