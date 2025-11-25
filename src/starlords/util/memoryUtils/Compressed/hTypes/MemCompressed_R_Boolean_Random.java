package starlords.util.memoryUtils.Compressed.hTypes;

import starlords.util.Constants;
import starlords.util.Utils;

public class MemCompressed_R_Boolean_Random extends MemCompressed_R_Boolean_Base{
    private Object odds;
    public MemCompressed_R_Boolean_Random(Object data){
        this.odds = data;
    }

    @Override
    public boolean getRandom(Object linkedObject) {
        Object script = Utils.isScript(odds);
        if (script != null) return ((MemCompressed_R_Boolean_Base)script).getRandom(linkedObject);
        return Utils.rand.nextDouble()<=(Double) odds;
    }
}
