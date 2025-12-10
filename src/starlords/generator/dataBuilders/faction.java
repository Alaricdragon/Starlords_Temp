package starlords.generator.dataBuilders;

import com.fs.starfarer.api.impl.campaign.ids.Factions;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.memoryUtils.Compressed.MemCompressedPrimeSetterUtils;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.KEY_LORD;

public class faction implements LordBaseDataBuilder {
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        return (json == null || !json.has("faction"));
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        String a = new ScriptedValueController(json.getString("faction")).getNextString().getValue(lord);
        a = switch (a.toLowerCase()) {
            case "hegemony" -> Factions.HEGEMONY;
            case "sindrian_diktat" -> Factions.DIKTAT;
            case "tritachyon" -> Factions.TRITACHYON;
            case "persean" -> Factions.PERSEAN;
            case "luddic_church" -> Factions.LUDDIC_CHURCH;
            case "pirates" -> Factions.PIRATES;
            case "luddic_path" -> Factions.LUDDIC_PATH;
            default -> a;
        };

        //lord.getLordAPI().setFaction(json.getString("faction"));
        //please keep in mind, this is not changed on defection -yet-. it will need to be later though.
        lord.getMemory().setCompressed_String(Lord.MEMKEY_Faction,a);
    }

    @Override
    public void generate(Lord lord) {
        //todo: make it so the generator sets this before hand.
    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        //this might be needed some day, but its not for now.
        MemCompressedPrimeSetterUtils mem = MemCompressedPrimeSetterUtils.getHolder(KEY_LORD);
        mem.setString(Lord.MEMKEY_Faction,linkedObject -> "");
    }

    @Override
    public void saveLord(Lord lord) {

    }

    @Override
    public void loadLord(Lord lord) {

    }

    @Override
    public boolean shouldRepair(Lord lord, JSONObject json) {
        return false;
    }
}
