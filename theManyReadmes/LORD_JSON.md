a given lord json contains all data related to a given starlord.
please note that each lord needs an entry in the starlords.csv file, to link to the relevant json file.
also note: many values can be replaced by scripts. any value that can be will be marked by a [classpath:link] value. if you want a given value to be a classpath, write it as a string, but place a '~' at the beginging, to mark it as a classpath.
* "name": String. the name of your starlord. [classpath:starlords/util/overriders/OverridersString.java]
* "fleetName": String. the name of your starlords fleet. [classpath:starlords/util/overriders/OverridersString.java]
* "personality": String. this determines the dialog, and some small changes in starlords actions. to be overholed. [classpath:starlords/util/overriders/OverridersString.java] possable options are:
  * "Upstanding"
  * "Martial"
  * "Calculating"
  * "Quarrelsome"
* "lore": String. the description of your starlord in the intel screen. [classpath:starlords/util/overriders/OverridersString.java]
* "preferredItem": String. The itemID of the prefer item of this starlord. for giving after dates. This will be redone at some point. [classpath:starlords/util/overriders/OverridersString.java] right now only accepts the following values (unless you use custom dialog. also to be redone):
  * "domestic_goods"
  * "drugs"
  * "lobster"
  * "luxury_goods"
  * "food"
  * "hand_weapons"
  * "alpha_core"
* "fief": String || JSONArray || Integer. The fief the starlord ownes.
  * if a String, should be the markets ID. not the market name.
  * if a JSONArray, should hold a list of markets ID.
  * if a Integer, the starlord will attempt to 'take' free markets at the start of the game. each market is worth size*10 ponits. The starlord will never take more markets then it has ponits.
* [moved. (allowing a classpath for gender for officers could be usefull later.)]"isMale": Boolean. wether or not your starlord uses 'male' or 'female' words. use this in the 'ScripOverride' for custom gender(s).
* [moved]"portrait": String. the portrait your starlord uses. By default, the mod will check for .PNGs in `data/graphics/portraits/` of the base game or any mod folder. If your portraits are located in an unusual location, you can specify the path directly e.g. `graphics/my_folder/my_portraits/my_portrait.png`. This portrait must also be registered in any faction config in `data/world/factions`. You may register it under the `starlords_nobility` faction if you want a lord-exclusive portrait.
* [moved]"battle_personality": String. is the combat personality of the starlord. possible (base game) values are:
  * "reckless"
  * "aggressive"
  * "steady"
  * "cautious"
  * "timid"
* [moved]"level": Integer. the Level the starlord will appear to be. should match the number of combat skills.
* "ranking": Integer. the 'ranking' of the starlord. should be 0, 1, or 2. To be removed.
* "startingFaction": String. the 'Starting faction' of the starlord. [classpath:starlords/util/overriders/OverridersString.java]
* "person": 'person_Json_Object' [required]
* "fleetComposition": JsonObject. holds the following values:
  * "flagship": 'ship_Json_Object'. [required] This is the flagship the lord pilots.
  * "desiredSpeed": Integer. [not required] This is the speed the fleet desires. will add a ship from civFleet_Tug until it reaches this speed.
  * "combatFleet": 'Fleet_Json_Object'. [required] This is all the ships in the fleet.
  * "civFleet_Tug": 'Fleet_Json_Object'. [not required] This is all the ships that count as 'tugs' that this fleet can use.
  * "civFleet_Fuel": 'Fleet_Json_Object'. [not required] This is all the ships that the fleet will try to add when it wants more fuel space.
  * "civFleet_Cargo": 'Fleet_Json_Object'. [not required] This is all the ships that the fleet will try to add when it wants more cargo space.
  * "civFleet_Personal": 'Fleet_Json_Object'. [not required] This is all the ships that the fleet will try to add when it wants more personel space.
  * 'Fleet_Json_Object' is a JsonObject holding the following:
    * "script": String. [not required] a class path to a '__' class. handles what ship to select and when. every function that you dont override there will use the rest of the data here.
    * "ships": a jsonArray [required] holding a list of 'ship_Json_Object' 
    * "SMods": a 'sMods_Json_Object' object [not required] (any Smods inside of the 'ships' folder will be added before the Smods here.)
    * "officers": is a 'person_Json_Object' object [not required]
    * [note] I need a way to make sure ship ratios are possible. in addition to 'a ship variant per b ship variant'.
  * 'ship_Json_Object' is a jsonObject structured as follows:
    * "script": String. [not required] a class path to a '__' class. handles anything you might need a custom script for in this section.
    * "variant": String. [required] the variant ID of the ship.
    * "idOverride": String. replaces the 'variant' of a ship in the context of the 'required' json object. useful if you happen to have more then one ship in your Fleet_Json_Object of the same variantID (something that can happen if you want more then one ship of the same variant with different officers.)
    * "SMods": is a 'sMods_Json_Object' object [not required]
    * "officers": is a JSonArray of 'person_Json_Object' objects [not required]
    * "priority": Double [not required]. is the priority of this ship variant being built. default is zero. a 'Fleet_JSon_Object' will always try to build the ship with the highest priority regardless of context.
    * "required": JSonObject [not required]. is structured as:
      * "variantID": Double. is the 'variantID' that must be in the fleet, and the number of them, per 1 of this ship.
    * "min": JSonObject [not required] is structured as:
      * "variantID/idOverride": double. the number of this ship is less then 'double * variantID', increase the priroity of building this ship to 1000, effectively making this ship produce itself first.
    * "max": JSonObject [not required] is structured as:
      * "variantID/idOverride": double. the number of this ship is equal or greater then 'double * variantID', decrease the priority of building this ship to -1000, effectively making this ship never be produced.
  * 'sMods_Json_Object' is a boolean were 'false' will force this ship / ships not to have Smods, || is a jsonObject structured as follows:
    * "sModsID": Integer || JSonObject. SModsID is the hullmod you want to SMods. Integer is the 'wieght' of a given Smod being added. JSonObject 
      * "script": String. [required] a classpath to a '__' class. handles the Smod installation, and ship modification.
      * "weight": Integer. [required] is the 'weight' of this Smod compaired to all other options.
      * "priority": Integer [not required]. is the 'priority' of this Smods. Smods with a higher priority will always be added first, regardless of any other factor. default value is 0.
  * 'person_Json_Object' is a jsonObject structured as follows:
    * "level": Integer. [not required, required for lords] the max level of the officer (and current level for starlord. should match the number of combat skills added in such a case.)
    * "skills": JsonObject || String [not required] if a String, is a class path to '__'. if a jsonObject, is a list of skills that this officer can have.
      * "ID": Integer. 'ID' is skill ID. Integer is skill level. (_ for normal, _ for elite. some skills will have more levels then that.)
    * "portrait" String || JSonArray [not required] is a string, is the portrait your officer/starlord will use is By default, the mod will check for .PNGs in `data/graphics/portraits/` of the base game or any mod folder. If your portraits are located in an unusual location, you can specify the path directly e.g. `graphics/my_folder/my_portraits/my_portrait.png`. This portrait must also be registered in any faction config in `data/world/factions`. You may register it under the `starlords_nobility` faction if you want a lord-exclusive portrait. if a jsonArray, it holds a list of jsonObjects structured as:
      * "portrait": String [not required] is the path to your portrait, as described aboth. 
      * "script": String [not required] is the script that determines what this portrait is.
      * "faction": String [not required] is the faction that this portrait will be taken from, at random.
      * "limited": int [not required] the max number of officers with the portrait allows to be assined to whatever the '!!' class is being applied to. 
    * "priority": Double. [not required] the 'priority' of this ship / group getting an officer. if set to zero, will always try to add an officer to other ships first. higher values will get officers mush more often. 
    * "battle_personality": String. [not required, required for lords] is the combat personality of the starlord. possible (base game) values are:
      * "reckless"
      * "aggressive"
      * "steady"
      * "cautious"
      * "timid"
    * "isMale": boolean|| String [not required, required for lords] is if the person uses man or women pronouns. If a string, is a class path to '__', allowing for full pronoun control.
    * "name": String || JSonObject [not required, required for lords] is the randomly generated name the lord will use. if a String, is just the name. if a JSonObject, is structred like so:
      * "faction": String [not required] the faction this person will randomly draw there dame from
      * "nameSet": String || JSonObject [not required] if String, the 'personNames' category this person will draw there name from. if a JsonObject, is structured like so:
        * "catagoryID" : Double [required]. were 'categoryID' is the category of a certen name, and Double is the weight of getting this category.
      * "script": String [not required] a classpath to '__' returns the name of the officer / starlord.
    * "tags": String || JSonArray [not required]. if a String, is a classpath to '__'. adds tags to this person. if a JSonArray, is a list of tags to be added to this person.
* "Stats": JSONObject [not required]. a list of 'stats' the starlord happens to have. the stats will be used in many diffrent things. if a given stat does not exsist, it will instead be set useing the 'lords/randoms.csv' file.
  * I need to take a closer look at how this would work.
* 
* 
* 
* 
* "otherObjects": JSonObject [not required]. This is a json object holding data related to so called 'other' lord data. Mostly, it is mods that will want some type of 'other' lord data. each item in the JSonObjects name is the ID of the 'other' data being read. the internal data is what is held within.
  * examples:
    * [PUT EXAMPLE FOR SECOND IN COMMAND ATTRIBUTE DATA HERE]
* 
* "scripOverride": JsonObject [not required] This can contain any of the following data, and will allow you to link a script to each section of data for the given starlord.
  * "upgrades": JsonObject [not required]. The jsonobject is structured as:
    * "upgradeID": String. were 'upgradeID' is the ID of the upgrade (as found in the 'upgrade CSV file), and String is a classpath to 'starlords\util\lordUpgrades\UpgradeBase.java'.
  * "lordGenerator": JsonObject [not required]. the jsonObject is structured as:
    * "generatorID": String. were 'generatorID' is the ID of a generator property (as defined in lordGenerator.json), and String is a classpath to 'starlords\generator\LordBaseDataBuilder.java'
  * [TO DO: this is not yet compleated. I require addition data here.]