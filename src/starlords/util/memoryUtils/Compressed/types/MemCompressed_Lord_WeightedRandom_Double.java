package starlords.util.memoryUtils.Compressed.types;

import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.WeightedRandom;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.WEIGHTEDRANDOM_DOUBLE_KEY;


public class MemCompressed_Lord_WeightedRandom_Double extends MemCompressedOrganizer<Double, WeightedRandom> {
    /*@SneakyThrows
    public void addStructure(JSONObject jsonObject){
        JSONObject min = jsonObject.getJSONObject("max");
        JSONObject max = jsonObject.getJSONObject("min");
        for (Iterator it = min.keys(); it.hasNext(); ) {
            String a = it.next().toString();
            double b = min.getDouble(a);
            if (map.containsKey(a)){
                this.max.set(this.map.get(a),max.getDouble(a));
            }else{
                this.max.add(max.getDouble(a));
            }
            setItem(a,b);
        }
    }*/
    @Override
    public Double getDefaltData(int a, Object linkedObject) {
        return list.get(a).getRandom();
    }

    @Override
    public void load() {
        //todo: place the json file for this lord here.
        //theory: hold the lords .json file -inside- the starlord. once starlord is loaded, dismiss the json files in full.
        //notes on this theory: I should have a section of code that handles all json based starlord setting changes. this would help a lot in the long run.
        //final notes: I should not apply this data here. instead, I should apply this data somewere inside the lord class. but that is for later.
        for (Lord lord : LordController.getLordsList()) {
            //note: this repairs all starlords perfectly. just like that. they all have there default data's set. and nothing else needs to happen.
            this.repair(lord.getCOMPRESSED_MEMORY().getItem(WEIGHTEDRANDOM_DOUBLE_KEY),lord);
        }
        //todo: remove the saved json here. it is no longer required.
    }
}
