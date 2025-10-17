package starlords.util.memoryUtils.Compressed.types;

import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.WeightedRandom;
import starlords.util.memoryUtils.Compressed.MemCompressedHolder;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

public class MemCompressed_Lord_UpgradeOrganizer extends MemCompressedOrganizer<MemCompressedHolder<?>, WeightedRandom> {
    ///!!! NO LONGER USEING
    @Override
    public MemCompressedHolder<?> getDefaltData(int a, Object linkedObject) {
        return null;// defalt data goes into the starlord, NOT the organizer.
    }

    @Override
    public void load() {
        //todo: this need to do the following:
        //1: get a list of all upgrades and add them to itself.
        //2: apply the basic upgrade data to all the upgradecosts, and upgrade....
        //ok, no. just no.
        //it was interesting, but...
        //but then again why would I care......
        /*OK.... OK....
        * so: how would I hold the data inside of this, but also inside of upgradeOrganizers?
        * for the first thing: I have two options:
        * 1) make this hold its own class, holding both the 'cost and weight' of an upgrade in it
        * OR
        * 2) make this hold a single cost or weight, and make a second one to hold the other....
        * OR:
        * 3) -don't- hold anything other then MemCompressor_Lord_Upgrade_Iternal.
        *       organize the names of the class as follows:
        *       "UPGRADE_UPGRADEID_TYPE_NAME"
        *       then just get that data vi organization...?
        *       this holds a few issues with mod-ability, but so long as the data can be organized on use (have some type of master list some were), it should be fine.
        *       in theory.
        *       it is all stored inside of the MemCompressedHolder in the end, and that is my only objective for this data...
        *       they are stats. they need to be organized yes, but this is fine in the end...
        * no... I will go with item one.
        * ok so: I still need to use the MemCompressed (because I can add additional data mid game and don't want data corruption.)
        * so this needs to hold:
        *   class A -> 2 MemCompressed -> a list of Weighted randoms.
        *   on
        */
    }
    @Override
    public void save() {

    }
}
