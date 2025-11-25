package starlords.util.randomLoader;

import starlords.util.Utils;

public class WeightedRandomScripted extends WeightedRandomBase{
    public Object maxS, minS,  iS, targetS;
    public double max=0, min=0, i=0, target=0;
    public WeightedRandomScripted(Object max, Object min, Object i, Object target) {
        this.maxS = max;
        this.minS = min;
        this.iS = i;
        this.targetS = target;
    }
    public void setData(Object linkedObject){
        max = Utils.getScriptValueDouble(maxS,linkedObject);
        min = Utils.getScriptValueDouble(minS,linkedObject);
        i = Utils.getScriptValueDouble(iS,linkedObject);
        target = Utils.getScriptValueDouble(targetS,linkedObject);
    }
    @Override
    public int getRandomInt() {
        return getRandomInt(target,i,max,min);
    }

    @Override
    public double getRandom() {
        return getRandom(target,i,max,min);
    }
}
