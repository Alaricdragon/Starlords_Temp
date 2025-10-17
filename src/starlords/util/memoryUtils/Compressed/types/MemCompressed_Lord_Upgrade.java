package starlords.util.memoryUtils.Compressed.types;

import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.WeightedRandom;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

import static starlords.util.Constants.COMPRESSED_ORGANIZER_UPGRADE_KEY;
import static starlords.util.Constants.COMPRESSED_ORGANIZER_UPGRADE_WEIGHT_KEY;

public class MemCompressed_Lord_Upgrade extends MemCompressedOrganizer<Double, WeightedRandom> {
    private String name;
    //upgrade, weight and type name are for finding this data in the json file
    private String upgradeName;
    private String weightName;
    private String typeName;
    public MemCompressed_Lord_Upgrade(String name,String upgradeName,String weightName,String typeName){
        //name structure would be:
        this.name = COMPRESSED_ORGANIZER_UPGRADE_KEY + "NameOfUpgrade_" + COMPRESSED_ORGANIZER_UPGRADE_WEIGHT_KEY +"NameOfWeight";
        this.name = name;
        this.upgradeName = upgradeName;
        this.weightName = weightName;
        this.typeName = typeName;
    }
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
        //this would allow
        for (Lord lord : LordController.getLordsList()) {
            this.repair(lord.getCOMPRESSED_MEMORY().getItem(name),lord);
            //replace starlord data that exists inside of json here. (aka, data that was not default.)
        }
        //todo: remove the saved json here. it is no longer required.
    }
}
