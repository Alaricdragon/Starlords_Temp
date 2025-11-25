package starlords.util.memoryUtils.Compressed.hTypes;

import com.fs.starfarer.api.Global;
import starlords.util.Utils;

import java.util.ArrayList;

public class MemCompressed_R_Double_Random extends MemCompressed_R_Double_Base{
    private Object min;
    Object max;
    public MemCompressed_R_Double_Random(Object min, Object max){
        this.max = max;
        this.min = min;
    }

    @Override
    public double getRandom(Object linkedObject) {
        double minT = 0;
        double maxT = 0;
        Object script = Utils.isScript(min);
        if (script != null){
            minT = ((MemCompressed_R_Double_Base)script).getRandom(linkedObject);
        }else{
            minT = (Double) this.min;
        }
        script = Utils.isScript(max);
        if (script != null){
            maxT = ((MemCompressed_R_Double_Base)script).getRandom(linkedObject);
        }else{
            maxT = (Double) this.max;
        }
        if (maxT <= minT) return minT;
        maxT = maxT - minT;
        return (Utils.rand.nextDouble() * maxT) + minT;
    }
}
