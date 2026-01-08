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
            B_B: (can, value, value2) (why would someone use this? my got tells me it wll be useful.)
                -can: boolean. if true, returns value. if false, returns value2
                -value: returns if can is true
                -value2 returns if can is false.
            B_B2: (size,can, value) (why would someone use this? my got tells me it wll be useful.)
                -size: the number of can and value values.
                -can: if I can use this value.
                -value: the value I use.

                -can and value can be repeated. this gets the first 'true' value.
        double:
            D_B: (can, value, value2)
                -can: boolean. if true, returns value. if false, returns value2
                -value: returns if can is true
                -value2 returns if can is false.
            D_B2: (size,can, value) (why would someone use this? my got tells me it wll be useful.)
                -size: the number of can and value values.
                -can: if I can use this value.
                -value: the value I use.

                -can and value can be repeated. this gets the first 'true' value.
        string:
            S_B: (can, value, value2)
                -can: boolean. if true, returns value. if false, returns value2
                -value: returns if can is true
                -value2 returns if can is false.
            S_B2: (size,can, value) (why would someone use this? my got tells me it wll be useful.)
                -size: the number of can and value values.
                -can: if I can use this value.
                -value: the value I use.

                -can and value can be repeated. this gets the first 'true' value.

    doubles:
        (not yet done, still need a few more variables.)D_S_Weapons: (inputs: type, size. give number of weapons of this type)
                     (for use, keep in mind I am going to have operators. so that should help. (like '-' or '+' or '*' or so on.))
            -type:
                ANY
                ENERGY
                BALLISTIC
                MISSILES
            size:
                ANY
                ENERGY
                BALLISTIC
                MISSILES

        (done. merged into D_S_Weapons.)D_S_Weapons_Ammo (inputs:ammowType:buildType,type,size)
            -ammoType is:
                ANY
                RELOAD              (amount of 'reloading' ammo held)
                NO_RELOAD           (amount of 'not reloading' ammo held)
                RELOAD_PER_SECOND
            -type:
                ANY
                ENERGY
                BALLISTIC
                MISSILES
            -size:
                ANY
                SMALL
                MID
                LARGE
    strings:

        S_S_DefenseType (shield, phase, or none)

        S_S_Size (returns ship size as string)

    boolean:
        B_S_Manufacture: returns true if data manufacture equals the inputed string.

        B_S_Size inputs:type (returns true if size is one of the following types.)
            -type:
                ANY
                FIGHTER
                FRIGET
                DESTROYER
                CRUSIER
                CAPITAL
            -if ANY, add the following 4 values:
                b0: should be frigate
                b1: should be destroyer
                b2: should be cruiser
                b3: should be capital
        B_S_Hangers: (min,max)
            min: min number of fighter bays
            max: max number of fighter bays


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
