package starlords.util.memoryUtils.Compressed_outdated.dataTypes;

import starlords.util.ScriptedValues.SV_Double_Code;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedOrganizer;

@Deprecated
public class MemCompressed_DoubleScript extends MemCompressedOrganizer<Double, SV_Double_Code> {
    @Override
    public Double getDefaltData(int a, Object linkedObject) {
        return list.get(a).getValue(linkedObject);
    }
}
