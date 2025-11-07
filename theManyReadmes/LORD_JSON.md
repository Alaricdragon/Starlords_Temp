a given lord json contains all data related to a given starlord.
please note that each lord needs an entry in the starlords.csv file, to link to the relevant json file.
also note: should you use a 'ScriptOverride' for a given peace of data, please keep in mind that it will not be read outside of the scrip override.
* "name": String. the name of your starlord.
* "fleetName": String. the name of your starlords fleet.
* [only keep this in overriding scripts fo starlords]"isMale": Boolean. wether or not your starlord uses 'male' or 'female' words. use this in the 'ScripOverride' for custom gender(s).
* "personality": String. this determines the dialog, and some small changes in starlords actions. to be overholed. possable options are:
  * "Upstanding"
  * "Martial"
  * "Calculating"
  * "Quarrelsome"
* "lore": String. the description of your starlord in the intel screen
* [moved]"portrait": String. the portrait your starlord uses. By default, the mod will check for .PNGs in `data/graphics/portraits/` of the base game or any mod folder. If your portraits are located in an unusual location, you can specify the path directly e.g. `graphics/my_folder/my_portraits/my_portrait.png`. This portrait must also be registered in any faction config in `data/world/factions`. You may register it under the `starlords_nobility` faction if you want a lord-exclusive portrait.
* "preferredItem": String. The itemID of the prefer item of this starlord. for giving after dates. This will be redone at some point. right now only accepts the following values (unless you use custom dialog. also to be redone):
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
* [moved]"battle_personality": String. is the combat personality of the starlord. possible (base game) values are:
  * "reckless"
  * "aggressive"
  * "steady"
  * "cautious"
  * "timid"
* [moved]"level": Integer. the Level the starlord will appear to be. should match the number of combat skills.
* "ranking": Integer. the 'ranking' of the starlord. should be 0, 1, or 2. To be removed.
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
    * "officers": is a '!!' object [not required]
    * [note] I need a way to make sure ship ratios are possible. in addition to 'a ship variant per b ship variant'.
  * 'ship_Json_Object' is a jsonObject structured as follows:
    * "script": String. [not required] a class path to a '__' class. handles anything you might need a custom script for in this section.
    * "variant": String. [required] the variant ID of the ship.
    * "SMods": is a 'sMods_Json_Object' object [not required]
    * "officers": is a '!!' object [not required]
  * 'sMods_Json_Object' is a boolean were 'false' will force this ship / ships not to have Smods, || is a jsonObject structured as follows:
    * "sModsID": Integer || JSonObject. SModsID is the hullmod you want to SMods. Integer is the 'wieght' of a given Smod being added. JSonObject 
      * "script": String. [required] a classpath to a '__' class. handles the Smod installation, and ship modification.
      * "weight": Integer. [required] is the 'weight' of this Smod compaired to all other options.
  * '!!' is a jsonObject structured as follows:
    * "level": Integer. [not required] the max level of the officer (and current level for starlord. should match the number of skills added in such a case.)
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
    * "isMale": boolean [not required] is if the lord uses male or female pronouns.
    * "name": String || JSonObject [not required, required for lords] is the randomly generated name the lord will use. if a String, is just the name. if a JSonObject, is structred like so:
      * "faction": String [not required] the faction this person will randomly draw there dame from
      * "nameSet": String || JSonObject [not required] if String, the 'personNames' category this person will draw there name from. if a JsonObject, is structured like so:
        * "catagoryID" : Double [required]. were 'categoryID' is the category of a certen name, and Double is the weight of getting this category.
      * "script": String [not required] a classpath to '__' returns the name of the officer / starlord.
* 
* 
* "ScripOverride": JsonObject. This can contain any of the following data, and will allow you to link a script to each section of data for the given starlord.
  * note:
  * "": String. This is the classPath to a __ class