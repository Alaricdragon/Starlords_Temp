package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.generator.LordGenerator;
import starlords.person.Lord;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.memoryUtils.Compressed.MemCompressedPrimeSetterUtils;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.KEY_LORD;

public class LordPersonality implements LordBaseDataBuilder {
    /*todo:
            1) (done) create lord personality (should just be random for now.)
            2) get flagship
                if combat fleet was generated: use memory saved in shipCompositionCombat for flagship
                if combat fleet was not generated: use a random LordFlagshipGenerator with all fleet ships as input.
            3) combat personality (should just use random for now)
            4) lord fav food (should just use random for now) (this needs an upgrade when I get to the next dialog upgrade)
            5) LordGender: should be male / female or a special script. that script can replace the gender data for a given lord.
            5) lord looks and name: I should have code for this. use entincity if available.
            6) fleet name: upgrade this with a CSV file. just for possable fleet names. I could have specal code to get the faction the lord started as to get the fleet name.
                           -also have code that lets the fleet name be limited to one or whatever number of lords.
            7) lordCombatSkills: (I need to have some type of 'possable combat skill' csv file. with scripts to determin best combat skills)
            8) lordFleetSkills: (I need a csv file with lord skills, there odds, and a class that has a activated / disactivated function just for that)



    */
    /*todo: when I final change the dialog, remove the 'LordPersonality' class in its current form. it should not be.

    */
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        return !json.has("personality");
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        String a = new ScriptedValueController(json.getString("personality")).getNextString().getValue(lord);
        starlords.person.LordPersonality personality = starlords.person.LordPersonality.valueOf(a.toUpperCase());
        lord.getMemory().setCompressed_Object(Lord.MEMKEY_Personality,personality);
    }

    @Override
    public void generate(Lord lord) {
        String a = LordGenerator.getPersonalities()[LordGenerator.getValueFromWeight(LordGenerator.getPersonalityRatio())];
        starlords.person.LordPersonality personality = starlords.person.LordPersonality.valueOf(a.toUpperCase());
        lord.getMemory().setCompressed_Object(Lord.MEMKEY_Personality,personality);

    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        MemCompressedPrimeSetterUtils mem = MemCompressedPrimeSetterUtils.getHolder(KEY_LORD);
        mem.setObject(Lord.MEMKEY_Personality,linkedObject -> "");
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
