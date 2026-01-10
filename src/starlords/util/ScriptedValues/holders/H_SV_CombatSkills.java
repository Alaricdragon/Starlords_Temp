package starlords.util.ScriptedValues.holders;

import com.fs.starfarer.api.characters.PersonAPI;
import starlords.person.Lord;
import starlords.util.fleetCompasition.ShipCompositionData;

public class H_SV_CombatSkills {
    /*
    new types required:
        (done) operators:

    calculations:
        boolean:
        double:
        string:

    doubles:
    strings:
        S_S_DefenseType (shield, phase, or none)
        S_S_Size (returns ship size as string)
    boolean:

        B_S_Ship: returns true if all the ship criteria is meet.
            NOTICE: I am going to make each of this a different class, because there is no need for them all to be once class????
            (done) size:
                ANY
                FIGHTER
                FRIGET
                DESTROYER
                CRUSIER
                CAPITAL
            (done) type:
                ANY
                CARRIER
                WARSHIP
                PHASE SHIP
                CIV SHIP
            (done) manufacturer:
                ANY
                -should match the manufacture ID-
            defenseType:
                ANY
                SHIELD_ANY
                SHIELD_FRONT
                SHIELD_OMI
                PHASE
                DAMPER_FIELD
                CANISTER FLAX
            hanges: (might move this to its own section.)
                min, max.
            burn:
                min, max
            combat speed:
                min, max.
            manoeuvrability:
                min, max.

        B_S_System
            idSizes: the number of IDs that are accepted.
            id[]: the system ID
            charges:
                min, max
            range:
                min, max
            rechargeRate
                min, max


    ...
    this will all be very completed. will I even use the giving data more then once?
    like, this will be useful more then once right? RIGHT?!?!?!


    */
    public Lord lord;
    public PersonAPI person;
    public ShipCompositionData shipCompositionData;
    public H_SV_CombatSkills(Lord lord, PersonAPI person, ShipCompositionData data){
        this.lord = lord;
        this.person = person;
        this.shipCompositionData = data;
    }
}
