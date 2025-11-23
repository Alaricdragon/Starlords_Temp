package starlords.generator;

import com.fs.starfarer.api.util.Pair;
import org.json.JSONObject;
import starlords.person.Lord;

public interface LordBaseDataBuilder {
    //IMPORTANT: I dont need a store data function, I can do that with just this function in lordJSON or generate. there is no need for the additional complexity.
    boolean shouldGenerate(JSONObject json);
    void lordJSon(JSONObject json, Lord lord);
    void generate(Lord lord);
    void prepareStorgeInMemCompressedOrganizer();//in case I ever forget what this is for: this is for preparing a 'default' data in MemCompressedOrganizer. this is so I dont need an additional entry in the 'randoms' file (as the random data is being preset in the generater data. (that or I could put it in randoms. and it could be generated right then. but in this case, the order is VERY IMPORTANT, so I require this function to compensate.)).

    //todo: issue with this concept: some lords will want to save using different methods.
    //      the best solution to this would to have a stored 'replaced scripts' class on starlords.
    //      the issue with this is the possibility of a lot of random data being saved, but it should be smaller then a 'connected memory'. maybe if I fill it with nulls.
    //      note: a null does in fact use memory. better to hashMap that one.
    //      so I require an 'overriding scrips' class organizer thing.
    void saveLord(Lord lord);
    void loadLord(Lord lord);
    //or I could go into MemCompressedOrganizer and make it 'repair' in order. but that would be really really really hard to do. I do not want to do it.
}
