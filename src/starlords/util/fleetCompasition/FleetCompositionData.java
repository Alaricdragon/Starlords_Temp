package starlords.util.fleetCompasition;

import lombok.Getter;
import starlords.util.memoryUtils.Compressed.MemCompressedMasterList;
import starlords.util.memoryUtils.GenericMemory;

import java.util.HashMap;

public class FleetCompositionData {
    //note: data is stored as: ID:value, NOT as VarientID:value.
    @Getter
    private HashMap<String,ShipCompositionData> data = new HashMap<>();
    @Getter
    private HashMap<String,Integer> numShips = new HashMap<>();//this is for storing the number of each ship active in the fleet.
    /*todo: what the hell am I even doing again????
            so, I am trying to get the upgrade scripts to work. doing things like digging out sections in memory and what have you.
            so... thats done. me being here is just confusing me right now.
            what I should do:
            1) go back into LordBaseDataControler and LordBaseDataBuilder and start to get though the functions one by one.
               as I go through them, I can simply upgrade FleetCompositionData and ShipCompositionData
               ...
               ya. lets do that. just gotta look into the functions.


    */

    /*todo: so... how the hell is this even going to work?
            ---NOTE: THI IS ONLY BEING KEEPT BECAUSE IT EXSPLAINS WHY I NEED A MEMORY OBJECT HERE---
            well, I am going to need the following:
            1) A list of available ships.
            in each ship I am going to need:
                1)(done) a special memCompressedData of some type donated to ships.
                   - the reason for this is because of thins like: Smods, Exsotic Technology Data, Officers, and so on.
                   - basically, random things I need stored that I should read later.
                2)(done) a specal memCompressedData that is only held on the fleet:
                    - the reason for this is again because of things like: Smods, Exsotic Technology Data, Officers, and so on.
                    - the only difference is that the data here is held for the fleet, and is applied at a lower priority then normal ships.
                3) a way to tell how many of each ship are required per each of another ship. effectively, having a min / max number of ships based on random bits of data.
                    - for example: min 5 : astral_attack. for each astral_attack, make 5 of this ship at top priority.
                    - for example: max 2 : astral_attack. for each astral_attack, make at most 2 of this ship before it is no longer an option.

     */
    @Getter
    private final GenericMemory Memory;
    public FleetCompositionData(){

        Memory = new GenericMemory(MemCompressedMasterList.KEY_FLEET,this);
    }
    public void addShip(String id, ShipCompositionData data, int ratio){
        this.data.put(id,data);
        this.numShips.put(id,ratio);
    }

    public int getNumberOfShips(String shipID){
        //todo: in order for this function to work, fleet compasition needs to know the number of each ship, by ID, in the fleet.
        /*todo: I could get the ship compasition by doing the following:
        *  1) in 'create ship' upgrade, add a tag to ships determining there ID in this object.
        *  2) read the saved id for the ship, matching it to the saved ID of the data here.*/
        return numShips.getOrDefault(shipID,0);
    }
    public ShipCompositionData getCurrentToBuildShip(){
        double priority = Double.MIN_VALUE;
        ShipCompositionData out = null;
        for (String a : data.keySet()){
            ShipCompositionData ship = data.get(a);
            double p = ship.getPriorityOverriderByMinMax(this,ship.getPriorityToBuild(this),getNumberOfShips(a));
            if (p > priority){
                out = ship;
                priority = p;
            }
        }
        return out;
    }
}
