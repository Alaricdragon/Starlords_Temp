most inputted values in starlords .csv files and .json files can be formed as a script.
please keep in mind that many such scripts can have inputted valuables. in such cases, inputted variables can also be scripts.
this can be done by the following values:
* '!' acts as an 'ignore' tool. this is mostly usefull when inputting strings you want to hold data.
  * '!~' ignores the ~ in this context (not a script)
  * '!~~' ignores the ~~ in the context (not a shorthand script)
  * '!:' ignores the : in this context (so you can have : in strings)
  * '!!' ignores the ! in this context (so it will not trigger any other 'ignore' commands)
* ':' acts as a divider between data.
  * WARNING: This means that ANY static string (not formed from a script) that uses : in it needs to use !: instead, or : and everything past it will be ignored!
* '~' this represents a script. the rest of this value should link to one of the following, depending on the data you want to input.
  * boolean: starlords/util/ScriptedValues/SV_Boolean.java
  * double: starlords/util/ScriptedValues/SV_Double.java
  * String: starlords/util/ScriptedValues/SV_String.java
  * Object: starlords/util/ScriptedValues/SV_Object.java
* '~~' this means that the rest of this value is a shorthand to more commonly used data types. (also note: I use shorthand for explaining variables. b = boolean. d = double. s = string. o = object) examples are as follows:
  * for boolean:
    * "B_R": returns a boolean value if a random number is less then 'd0' should look like: '~~B_R:d0'
      * d0: a value between 0 and 1
    * "B_!": returns !b0. should look like '~~B_!:b0'
      * b0: the inputted boolean you want to invert.
    * "B_C": acts as a system to allow for conditional input data. should be structured as: '~~B_C:d0:b10:s10:b11'
      * d0: the number of values held in this function (including conditions). (cannot use linkedobjects)
      * b10: the fist boolean value you are comparing
      * s10: is a string value that must be one of 5 possible strings. each string is used to compare the boolean to the right and the left of itself. the Strings, (and there functions) are:
        * AND  : both booleans must be true to return true
        * OR   : one boolean must be true to return true
        * NAND : both booleans must be false to return true
        * NOR  : one boolean must be false to return true
        * XOR  : one boolean must be true, and one must be false to return true
      * b11: the second boolean you are comparing.
      * examples: '~~B_C:3:true:OR:false' (true). '~~B_C:5:false:OR:true:AND:true' (true). '~~B_C:7:false:OR:ture:AND:true:XOR:true' (false)
    * "B_B": if the first boolean is true, returns the first value. if false, returns the second value. should look like '~~B_B:b0:b1:b2'
      * b0: if this is true, returns b1. if false returns b2
      * b1: 
      * b2: 
    * "B_B2": a list of values were the the first value with a 'true' condition will be selected. should look like '~~B_B2:d0:b10:b11'
      * d0: the number of values this list holds (cannot use linked objects).
      * b10: the condition for returning a given value.
      * b11: the value returned from a giving conditions
      * examples: '~~B_B2:4:false:true:true:false' (returns false (from second check)). '~~B_B2:6:false:true:true:true:true:false' (returns true (from second check)). '~~B_B2:8:false:false:true:false:true:true:true:true' (returns false (from second check))
    * "B_Faction": returns true if the linked object's 'faction' is equal to the inputted string. always returns false if the linked object is not a Lord. should look like '~~B_Faction:s0'
      * s0: the faction ID you want this lord to be.
    * "B_Culture": returns true if the linked object's 'culture' is equal to the inputted string. always returns false if the linked object is not a Lord. should look like '~~B_Faction:s0'
      * s0: the faction ID you want this lord to be.
    * "B_S_Manufacture": 
      * a
  * for double:
    * "D_R": a random value between 'd0' and 'd1'. should look like: '~~D_Rd0:d1'
      * d0: the min random value
      * d1: the max random value
    * "D_WR": a weighted random equation should look like: '~~D_WR:d0:d1:d2:d3' were:
      * d0: the max value of this random.
      * d1: the min value of this random.
      * d2: the target value of this random.
      * d3: the standard deviation of this random (in effect, 64.2% of items are within 1 sd of the target. 27.2% of items are between 1 and 2 sd, and 4.2% of items are between 2 and 3 sd)
    * "D_LR": a list of values were one is selected based on weight. should look like: '~~D_LR:d0:d10:d11'
      * d0: the number of values this list holds. (cannot use linkedobjects)
      * d10: the 'value' of this item in the list.
      * d11: the 'weight' of this value in the list
      * keep in mind, d10 and d11 can repeat themselves as many times as required.
      * examples: '~~D_LR:4:1:10:2:5' (10/15 1, 5/15 5). '~~D_LR:10:1:10:2:5:3:15:4:20:5:25' (10/85 1, 5/85 2, 15/85 3, 20/85 4, 25/85 5)
    * "D_B": if the first boolean is true, returns the first value. if false, returns the second value. should look like '~~B_B:b0:d1:d2'
      * b0: if this is true, returns d1. if false returns d2
      * d1:
      * d2:
    * "D_B2": a list of values were the the first value with a 'true' condition will be selected. should look like '~~D_B2:d0:b10:d11'
      * d0: the number of values this list holds (cannot use linked objects).
      * b10: the condition for returning a given value.
      * d11: the value returned from a giving conditions
      * examples: '~~D_B2:4:false:25:true:15' (returns 15 (from second check)). '~~D_B2:6:false:4:true:99:true:125' (returns 99 (from second check)). '~~D_B2:8:false:5:true:-222:true:999:true:1' (returns -222 (from second check))
    * "D_S_Weapons": gets the number of weapons / fitted mounts of the inputted type and size. only works in instances were a ship variant is part of the linked object  should look like: '~~D_S_Weapons:s0:s1:s2:s3'
      * s0: if you are looking at the weapon, mount, or both. can be: 'MOUNT', 'WEAPON', 'BOTH'
        * if you are looking at 'both', both the mount and weapon needs to be the same size and type to be added to the count of wepons.
      * s1: the weapon / mount type that is being looked at. can be: 'ENERGY', 'BALLISTIC', 'MISSILES', 'HYBRID', 'SYNERGY', 'COMPOSITE', 'UNIVERSAL', 'ANY_BALLISTIC', 'ANY_MISSILE', 'ANY_ENERGY', 'ANY'. (for mounts only): 'BUILT_IN, 'DECORATIVE', 'LAUNCH_BAY', 'STATION_MODULE', 'SYSTEM'
      * s2: the size of the weapon / mount that is being looked at. can be: 'SMALL', 'MEDIUM', 'LARGE', 'ANY'
      * s3: the ammo 'type' of the weapon. can be 'ANY', 'NO_AMMO', 'ANY_AMMO', 'RECHARGE', 'NO_RECHARGE'
      * example: '~~D_S_Weapons:BOTH:ENERGY:SMALL:NO_RECHARGE' (this would return the number of small energy weapons mounted in small energy mounts on the ship that have recharging ammo)
    * "D_S_WeaponsOP": gets amount of weapon OP from weapons / fitted mounts of the inputted type and size. only works in instances were a ship variant is part of the linked object  should look like: '~~D_S_WeaponsOP:s0:s1:s2:s3'
      * s0: if you are looking at the weapon, mount, or both. can be: 'MOUNT', 'WEAPON', 'BOTH'
        * if you are looking at 'both', both the mount and weapon needs to be the same size and type to be added to the count of wepons.
      * s1: the weapon / mount type that is being looked at. can be: 'ENERGY', 'BALLISTIC', 'MISSILES', 'HYBRID', 'SYNERGY', 'COMPOSITE', 'UNIVERSAL', 'ANY_BALLISTIC', 'ANY_MISSILE', 'ANY_ENERGY', 'ANY'. (for mounts only): 'BUILT_IN, 'DECORATIVE', 'LAUNCH_BAY', 'STATION_MODULE', 'SYSTEM'
      * s2: the size of the weapon / mount that is being looked at. can be: 'SMALL', 'MEDIUM', 'LARGE', 'ANY'
      * s3: the ammo 'type' of the weapon. can be 'ANY', 'NO_AMMO', 'ANY_AMMO', 'RECHARGE', 'NO_RECHARGE'
      * example: '~~D_S_WeaponsOP:WEAPON:MISSILES:SMALL:ANY_AMMO' (this would return the amount of opp spent on small missile weapons that have ammo mounted on the ship.)
  * for String:
    * "S_LR": a list of values were one is selected based on weight. should look like: '~~S_LR:d0:S10:d11'
      * d0: the number of values this list holds. (cannot use linkedobjects)
      * S10: the 'value' of this item in the list.
      * d11: the 'weight' of this value in the list
      * keep in mind, d10 and d11 can repeat themselves as many times as required.
      * examples: '~~S_LR:4:cat:10:bird:5' (10/15 cat, 5/15 bird). '~~S_LR:10:cat:10:bird:5:dog:15:bear:20:frog:25' (10/85 cat, 5/85 bird, 15/85 dog, 20/85 bear, 25/85 frog)
    * S_FactionPortrait: a path to a random portrait from the inputted faction. should look like: 'S_FactPort:s0:d1'
      * s0: the faction ID you want to get a portrait from.
      * d1: the 'gender'. set to 0 for any, set to 1 for male, set to 2 for female. rounds down.
    * "S_B": if the first boolean is true, returns the first value. if false, returns the second value. should look like '~~B_B:b0:s1:s2'
      * b0: if this is true, returns s1. if false returns s2
      * s1:
      * s2:
    * "S_B2": a list of values were the the first value with a 'true' condition will be selected. should look like '~~S_B2:d0:b10:s11'
      * d0: the number of values this list holds (cannot use linked objects).
      * b10: the condition for returning a given value.
      * s11: the value returned from a giving conditions
      * examples: '~~S_B2:4:false:one:true:two' (returns two (from second check)). '~~S_B2:6:false:cat:true:dog:true:car' (returns dog (from second check)). '~~S_B2:8:false:fish:true:ice cream:true:power:true:cuddles' (returns ice cream (from second check))
  * for object:


* operators:
  * some types have 'operators'. said operators can be placed between two values of the same type. they can also be chained together.
  * for boolean: (to get a 'true' output)
    * "AND"   both values must be true. should look like: 'b0:AND:b1'
    * "OR"    any value must be true. should look like: 'b0:OR:b1'
    * "!AND"  both value must be false. should look like: 'b0:!AND:b1'
    * "!OR"   any value must be true. should look like: 'b0:!OR:b1'
    * "XOR"   one value must be true, and one false. should look like: 'b0:XOR:b1'
  * for double:
    * "+"     adds the two values. should look like: 'd0:+:d1'
    * "-"     substracts the two values. should look like: 'd0:-:d1'
    * "/"     devides the two values. should look like: 'd0:/:d1'
    * "*"     multiplys the two values. should look like 'd0:\*:d1'
    * "^"     runs math.power(d0,d1). should look like 'd0:^:d1'
  * for string:
    * "+"     adds two strings together. should look like: 's0:+:s1'
  * additional context:
    * operators can be chained. so 'b0:AND:b1:OR:b2:AND:b3' works.
    * operators cannot be a scripted value. as a resalt, having a rar string of any operator value does not work.

sometimes in CSV files and TheManyReadmes you will find references to 'LinkedObjects'.
'LinkedObjects' are just references to some relevant object, and can be checked each time a 'script' attempts to computer a value.
more explanations will be available one a case by case basis.


lastly, most scripts are only ran once per whatever they are attached to. Exceptions will be explained.



the following files are exsmpt form this system (for now):
  * faction.json
  * default_dialog_options.json
  * dialog.json
  * starlord_settings.json
  * starlords_factionSettings.json
  * starlord_LifeAndDeathSettings.json
  * starlord_generaterSettings.json