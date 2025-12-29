package starlords.generator.dataBuilders;

import com.fs.starfarer.api.impl.campaign.ids.Factions;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedPrimeSetterUtils;

import static starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList.KEY_LORD;

public class culture implements LordBaseDataBuilder {
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        return !json.has("culture") && !json.has("faction");
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        if (json.has("culture")) {
            String a = new ScriptedValueController(json.getString("culture")).getNextString().getValue(lord);
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
            lord.setCulture(a);
            return;
        }
        lord.setCulture(lord.getFaction().getId());
    }

    @Override
    public void generate(Lord lord) {
        //todo: make it so I can generate with 'other' cultures from a given faction dictionary.
        //      something I could do, is add a 'factions' csv file early, and make each one have a 'sub factions' data.
        //      the 'sub factions' data would be used here, allowing for random generation of starlord fleet dataTypes.
        //FactionAPI fac = Global.getSector().getFaction(lord.getMemory().getCompressedString("faction"));

        lord.setCulture(lord.getFaction().getId());
    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        //this data is set directly stored in the Lord class.
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
