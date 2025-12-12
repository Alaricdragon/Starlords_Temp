package starlords.util.ScriptedValues;

import com.fs.starfarer.api.combat.MutableStat;
import starlords.util.math.StarLord_MutableStat;

public interface SV_MutableStat_Code {
    StarLord_MutableStat getValue(Object linkedObject);
}
