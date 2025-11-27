package starlords.util.memoryUtils.Compressed.types;

import starlords.util.ScriptedValues.SV_Double;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

public class MemCompressed_DoubleScript extends MemCompressedOrganizer<Double, SV_Double> {
    @Override
    public Double getDefaltData(int a, Object linkedObject) {
        return list.get(a).getValue(linkedObject);
    }
}
