package starlords.generator.dataBuilders;

import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.ScriptedValues.SV_Object;
import starlords.util.ScriptedValues.SV_Object_Code;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.fleetCompasition.FleetCompositionData;
import starlords.util.memoryUtils.Compressed.MemCompressedHolder;
import starlords.util.memoryUtils.Compressed.MemCompressedMasterList;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;
import starlords.util.memoryUtils.Compressed.MemCompressedPrimeSetterUtils;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.*;

public class availableShips implements LordBaseDataBuilder {
    /*
    * todo: so this class is complecated, at least compaired to others.
    *       first, it needs to hold a special function called 'getUseableVarients' that returns an object holding all usable variants this generation sequence is allowed to use.
    *       second, I need to do one of the following:
    *       -> if shouldGenerate, I need to make the next few relevant functions run.
    *           ---I also need to get all possible available ships and place then inside of the data holder.---.
    *           ---this should be the same object as 'getUsableVariants. effectively being a set of ships and there fleet roles.---
    *       -> if loadJson, it should create the inter lord fleet composition data --->> right now <<----.
    *           (but only the ships that are being used. other things, like officers come from other functions).
    *
    *
    * */
    @Override
    public boolean shouldGenerate(JSONObject json) {
        return false;
    }

    @Override
    public void lordJSon(JSONObject json, Lord lord) {

    }

    @Override
    public void generate(Lord lord) {

    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        //this should save the fleet composition data.

        //now the lord has a stored fleetCompositionData for FLEET_COMBAT.
        MemCompressedPrimeSetterUtils mem = MemCompressedPrimeSetterUtils.getHolder(KEY_LORD);
        mem.setObject(FLEET_COMBAT, linkedObject -> new FleetCompositionData());

    }

    @Override
    public void saveLord(Lord lord) {

    }

    @Override
    public void loadLord(Lord lord) {

    }
}
