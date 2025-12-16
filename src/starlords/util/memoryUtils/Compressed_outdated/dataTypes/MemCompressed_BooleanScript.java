package starlords.util.memoryUtils.Compressed_outdated.dataTypes;

import starlords.util.ScriptedValues.SV_Boolean_Code;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedOrganizer;

@Deprecated
public class MemCompressed_BooleanScript extends MemCompressedOrganizer<Boolean, SV_Boolean_Code> {
    @Override
    public Boolean getDefaltData(int a, Object linkedObject) {
        return list.get(a).getValue(linkedObject);
    }
}
