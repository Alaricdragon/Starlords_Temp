package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.Global;
import starlords.person.Lord;

public class DialogRule_hostileFaction extends DialogRule_Base {
    boolean hostile;
    public DialogRule_hostileFaction(boolean hostile){
        this.hostile = hostile;
    }

    @Override
    public boolean condition(Lord lord) {
        boolean hostile = lord.getFaction().isHostileTo(Global.getSector().getPlayerFleet().getFaction());
        return this.hostile == hostile;
    }
}
