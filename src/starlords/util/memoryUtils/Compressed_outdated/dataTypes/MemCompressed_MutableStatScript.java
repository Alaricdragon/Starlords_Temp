package starlords.util.memoryUtils.Compressed_outdated.dataTypes;

import starlords.util.ScriptedValues.SV_MutableStat_Code;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.math.StarLord_MutableStat;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedHolder;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedOrganizer;

@Deprecated
public class MemCompressed_MutableStatScript extends MemCompressedOrganizer<StarLord_MutableStat, SV_MutableStat_Code> {
    @Override
    public StarLord_MutableStat getDefaltData(int a, Object linkedObject) {
        return list.get(a).getValue(linkedObject);
    }

    @Override
    public void repair(MemCompressedHolder sur, Object linkedObject) {
        super.repair(sur, linkedObject);
        /*for (String a : map.keySet()){
            //if (!(list.get(map.get(a)) instanceof StarLord_MutableStat)) continue;
            StarLord_MutableStat stat = (StarLord_MutableStat) sur.getItem(a);
            String id = stat.getCompressedID(a, ScriptedValueController.TYPE_MUTABLE_STAT_BASE);//"TypeHeldPastThisOne(I dont yet have acsess to)_"+"StatModOrganizer_"+a;
            MemCompressedHolder<?> holder = stat.getDynamicFlatCompressedMods();
            MemCompressedMasterList.getMemory().get(id).repair(holder,linkedObject);


            id = stat.getCompressedID(a, ScriptedValueController.TYPE_MUTABLE_STAT_FLAT);//"TypeHeldPastThisOne(I dont yet have acsess to)_"+"StatModOrganizer_"+a;
            holder = stat.getFlatCompressedMods();
            MemCompressedMasterList.getMemory().get(id).repair(holder,linkedObject);

            id = stat.getCompressedID(a, ScriptedValueController.TYPE_MUTABLE_STAT_MULTI);//"TypeHeldPastThisOne(I dont yet have acsess to)_"+"StatModOrganizer_"+a;
            holder = stat.getMultiCompressedMods();
            MemCompressedMasterList.getMemory().get(id).repair(holder,linkedObject);
        }*/
    }

}
