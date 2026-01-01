package starlords.util.ScriptedValues;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.characters.FullName;

public class SV_S_FactionPortrait implements SV_String{
    private SV_String factionID;
    private SV_Double type;
    @Override
    public void init(ScriptedValueController value) {
        factionID = value.getNextString();
        type = value.getNextDouble();
    }

    @Override
    public String getValue(Object linkedObject) {
        FactionAPI a  = Global.getSector().getFaction(factionID.getValue(linkedObject));
        int b = (int) type.getValue(linkedObject);
        FullName.Gender c = switch (b) {
            case 0 -> FullName.Gender.ANY;
            case 1 -> FullName.Gender.MALE;
            default -> FullName.Gender.FEMALE;
        };
        return a.getPortraits(c).pick();
    }
}
