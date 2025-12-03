package starlords.generator.dataBuilders;

import com.fs.starfarer.api.impl.campaign.ids.Factions;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.memoryUtils.Compressed.MemCompressedPrimeSetterUtils;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.KEY_LORD;

public class culture implements LordBaseDataBuilder {
    @Override
    public boolean shouldGenerate(JSONObject json) {
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
            lord.getMemory().setCompressed_String("culture",a);
            return;
        }
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
        lord.getMemory().setCompressed_String("culture",a);
    }

    @Override
    public void generate(Lord lord) {
        //todo: make it so I can generate with 'other' caltures from a givin faction dictionary.
        //      something I could do, is add a 'factions' csv file early, and make each one have a 'sub factions' data.
        //      the 'sub factions' data would be used here, allowing for random generation of starlord fleet types.
        //FactionAPI fac = Global.getSector().getFaction(lord.getMemory().getCompressedString("faction"));
        //fac.getDoctrine();
        //fac.getFactionSpec();
        //fac.getRestrictToVariants();
        lord.getMemory().setCompressed_String("culture",lord.getMemory().getCompressed_String("faction"));
    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        MemCompressedPrimeSetterUtils mem = MemCompressedPrimeSetterUtils.getHolder(KEY_LORD);
        mem.setString("culture",linkedObject -> "");
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
