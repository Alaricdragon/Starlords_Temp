package starlords.util.memoryUtils.Stats;

import lombok.Getter;
import starlords.util.math.StarLord_MutableStat;
import starlords.util.memoryUtils.Stats.types.*;

import java.util.HashMap;
import java.util.Random;

public class StatsRandomOrganizer {
    //@Getter
    //private static final int maxSeed = 1000000000;
    /*
    todo:
        I am the forever confused.
        so, what am I not doing this in full:
        in short, it would (proboly) create lag.
        starsector defalt storge can hold 268,435,456 doubles.
        if starlords lords uses 1% of this:
        2,684,354 doubles
        1000 * 1000 = 1,000,000 doubles. that is assuming 1000 lords all with 1000 values.
        ...
        2,000,000 values for hashmaps.
        (note: more reilisic version would be 250*500 = 100,000+25,000= 125,000) 250,000 values for hashmaps. still a lot but...
        I am fine not to do generative values.
        -under the worst possible case, I can change the values to 'hide' in memory in combat, coming out after combat.
        -
        the seed idea was really cool though.


        I need to completely redesign this.
        first, each bit of data needs to be a Mutible Stat.
        I need to build the data around this.
        secondly... It might be possable to compress data into near nothing with this methood of compressed data, but the Mutible stats would need to be constantly recalculated.
        I need to determin if its worth it, but this function, the random data organizer, needs to serve that methood. it cannot be something I just attach. It just cant.
        ...
        2 possabilitys exsist:
        1) I do hashmaps.
            pros:
                mush faster to code.
                less confusion (hopefully) (in my mind of philosophy.)
            cons:
                the idea of having 'to mush data' in starlords forever hanging over my head.
        2) I do StatsRandomOrganizer
            pros:
                generative data would reduce memory storge by as mush as 90%. proboly closer to 75%.
            cons:
                -every single time- I want to get data, I will need to reclaculate. would case lag.
        -
        ..what was my objective again?
        to reduce lag...
        not memory. that is just a bonus. something nice to have.
        this would not reduce lag unless I really fuck up.
        so, farther consideration:

    */
    /*
    so the data here: is it better? and it needs to be a lot better then hashmaps to use
    well, in terms of storge: yes. yes it is better.
    in terms of not storge: ???? who knows????
    it will cost processing power each time I want to get a value. like it might cost a bit.
    for now, let us think of it with 5 diffrent contexts:
        AI - Stragic (what do I do now)
            -this runs rarely. maybe once every few in game weeks.
            -BUT: there would need to be quite a few stats to be recreated.
        AI - Tatical (there are 20 people at locations a-z, and my allies are here and bla bla bla. what do I do right now?)
            -this runs rapidly. there is no way I could, or should recreate the data every time.
        Politics
            -this would run maybe once a week. at most.
            -once a month at least.
            -I would only need to recalculate relevant data.
        Upgrades
            -this can run as short as once a week, or as long as once every 6 months.
            -diffently worth it.
    handled very very carefully, and this should be worth it.
    please note: things like factions likely want to keep there data forever. lords will want to have seprate data.


    data storge calculation changes:
        a = starlords.
        b = data ponits per lord
        c = % of data that is not compressed.
        eq for hashmaps: a * b * 2
        eq for this?:    b*2 + (a*b*2)*c
        so: a=10000,b=10000,c=0.25
        hashmap: 200,000,000
        this:    20,000 + 50,000,000 = 50,020,000
        difference: 75% less memory usage held.
        disadvantage: increase data getting complexity.
        so: a=10000,b=10000,c=0.05
        hashmap: 200,000,000
        this:    20,000 + 10,000,000 = 10,020,000
    */
    //@Getter
    private static HashMap<String, StatsRandomOrganizer> organizers;
    //private HashMap<String, RandomDataReader> data;
    //private HashMap<String, Integer> dataIds;

    //this is randomDataToBeSet. in effect, this is the stat values that will be set to a givin lord.
    //it is stored as a string to allow for scripts to run. it will be stored in better values once started.
    private HashMap<String,String> randomDataToBeSet;
    /*
    private HashMap<String, RandomData_String> data_s;
    private HashMap<String, RandomData_Double> data_d;
    private HashMap<String, RandomData_Int> data_i;
    private HashMap<String, RandomData_Object> data_o;
    private HashMap<String, RandomData_Boolean> data_b;*/

    public StatsRandomOrganizer(String id, String path){
        //load data here (from json path).
        organizers.put(id,this);
    }
    public void setData(StatsHolder holder, Object linkedObject,HashMap<String,String> scripts){
        for (String a : randomDataToBeSet.keySet()){
            String value;
            if (scripts.containsKey(a)) value = scripts.get(a);
            else value = randomDataToBeSet.get(a);
            setSingleItem(holder.data,linkedObject,value);
        }
    }
    private void setSingleItem(HashMap<String, StarLord_MutableStat> data, Object linkedObject, String value){
        /*todo:
            1) I need to change mutible stat to have the folloiwing:
                1) 2 values:
                    1 will be the 'flat mod' (to be added to the base mod)
                    1 will be the 'multi mod' to be multipled to that value.
                2) of note: there will be a 3rd type of input, that when past 1 will do the following:
                    note: 'value'
                    a) a>1
                        mult = 1
                        for(a : values) mult+= (value-1)
                        (multi will be stored, and used just before the normal multi values, but only appied to the base value.)
                    b) a<=1
                        mult = 1
                        for(a : values) mult*=a;
                        (mult will then be stored, because its not required not to be...?)
                3) please keep in mind, do to the very nature of statmods, every item will need to be a hashmap.



                2) 2 arrays of scripts: (might not be eneded)
                    1 will be the 'flat mod'
                    2 will be 'multi mods'.




        */

        String idOfItem="";
        boolean isScript; //this is for if the item is a scrip of a mutible stat. if this is the case, the stat can change when it is called. This is rarely usefull, but should be implmented anyways.
        if (!data.containsKey(idOfItem)){} //create new item here
        StarLord_MutableStat stat = data.get(idOfItem);
        //from here, all I need to do is get the relevant data. it should be simple hopefully.
    }
}
