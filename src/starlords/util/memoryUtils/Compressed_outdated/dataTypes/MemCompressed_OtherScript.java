package starlords.util.memoryUtils.Compressed_outdated.dataTypes;

import starlords.util.ScriptedValues.SV_Object_Code;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedOrganizer;

@Deprecated
public class MemCompressed_OtherScript extends MemCompressedOrganizer<Object, SV_Object_Code> {
    @Override
    public Object getDefaltData(int a, Object linkedObject) {
        return list.get(a).getValue(linkedObject);
    }
}
