# Star Lords

Star Lords attempts to bring the world of [Starsector](https://fractalsoftworks.com/) to life with many character-centric features. No longer will your only allies be nameless faction fleets who you'll meet once and never again. The core mechanic is the addition of dozens of Lords to the game, each with their own personality, fleet composition, backstory, and agendas.

![](example/starlords_readme1.PNG)

Lords will roam the map alongside the player-- raiding, trading, feasting, expanding, and more. Lords follow their own economic system, requiring them to physically visit their fiefs to levy taxes and purchase new ships. They operate persistently and without requiring any interference from the player. When the Marshal raises the banners, the Lords will muster to war in organized interplanetary campaigns of unprecedented scale.

![](example/starlords_readme5.PNG)

A Lord's behavior is heavily dependent on their personality and interpersonal relations with other Lords. Inter-lord relations are fully modeled, culminating in a brand new political system where Lords will politick, scheme, form alliances, and backstab while jockeying for wealth and influence within the realm.

![](example/starlords_readme3.PNG)

Curry favor with Lords by completing their quests, fighting alongside them in battle, or supporting their political agenda. Form a core of close allies and leverage their support to seize de-facto power in your faction's political system. Or convince them to join your own faction and support your claim to unite the sector.

Star Lords is heavily inspired by campaign mechanics of Mount and Blade. The goal is to eventually add all major campaign mechanics from Warband. This mod aims to have minimal side-effects on the game. There are no changes to in-battle gameplay, and no direct effects on campaign balance aside from the actions of the Lords themselves.

### Disclaimer
This mod is currently in early development. Expect plenty of bugs, crashes, questionable balancing, etc. Please report any crash logs or balance feedback to the forums. Thank you for your cooperation.

### Installation
Download the latest release [here](https://github.com/Deluth-git/Starlords/releases) and unzip in your Starsector mod folder.

This mod may be added to existing savegames, but may not be removed after being added. Make sure to back up your save game first in case of unexpected issues.

### Full Feature List
* Adds __48 unique Lords__ spread among all the base major factions.
* Adds fief system, where each market is a fiefdom which can be owned by a Lord. 
* Custom __Economic system__ for Lords
  * Lords collect taxes from their own fiefs and participate in business ventures in friendly markets. 
  * Income is used to expand their fleet, hire marines, and maintain existing ships.
* Custom fleet constructor for Lords, which allows each Lord to have their own __distinctive fleet composition__.
* Lords are __active on the map__, collecting income and waging war.
  * Lord actions are heavily dependent on Lord's personality and relations with other Lords
  * Lord's officer corps slowly level up from fighting in battles
* __Player-Lord Interactions__- If a Lord trusts you sufficiently, they may follow the player's military orders, offer quests, participate in scheming, and share sensitive intel about their operations.
* __Feast system__- Take a reprieve from braving the sector and join the lords of the realm in a night of feasting and revelry. Feasts are an excellent chance to meet all the lords and build rapport with them.
* __Campaign system__- Campaigns may be started by a faction's appointed marshal. All lords of the realm will gather to launch a grand invasion into enemy territory or defend against an enemy's invasion. If Nexerelin is enabled, lords may also use the Nexerelin ground combat system to capture markets.
* __Defection system__- Lords who are dissatisfied with their faction may defect to another. Or you can speed along the process and persuade or bribe Lords to join your own faction as your subordinates.
* __Prisoner system__- Lords may be captured in battle and either ransomed for credits or released for future goodwill.
* __Political system__- The cornerstone of all lord relations, the new political system is where all Lords of the realm gather to propose and vote on legislation. Appoint new marshals, squabble for newly conquered fiefs, conduct foreign policy, and more, as long as you can control the political situation.



### Major Planned Features
Most of these are inspired by Mount & Blade.
* Continued Lord AI improvement and optimization.
* Political marriage/courtship system for forming marriage alliances.
* Expanded subterfuge system involving scheming with friendly lords to increase your status in the realm or discredit mutual rivals.
* "Freelancer" system for enlisting in a Lord's army and fighting as a common pilot.
* Flesh out feast system, with feast tournaments as friendly competition with fellow Lords
* Better integration with Nexerelin invasions and base game crises.
*  Custom questlines for certain lords, e.g. allowing AI-sympathizing lords to field [REDACTED] fleets.


### Dependencies/Compatibility
This mod has no dependencies, though it's recommended to play with [Nexerelin](https://github.com/Histidine91/Nexerelin/tree/master) for planet capture mechanics. This mod should work with faction/ship/weapon mods which don't impact base campaign mechanics. Any kind of mod that only affects in-battle gameplay is also fine. All specific compatibilities are not yet tested.


### Adding your own Lords
If you're a modder or just want to put your own characters into the game, all you have to do is add another entry to the [lords.json](https://github.com/Deluth-git/Starlords/blob/master/data/lords/lords.json) file. A few notes:
* "portrait" should be a valid image. By default, the mod will check for .PNGs in `data/graphics/portraits/` of the base game or any mod folder. If your portraits are located in an unusual location, you can specify the path directly e.g. `graphics/my_folder/my_portraits/my_portrait.png`. This portrait must also be registered in any faction config in `data/world/factions`. You may register it under the `starlords_nobility` faction if you want a lord-exclusive portrait.
* "faction" should be a valid [faction id](https://fractalsoftworks.com/starfarer.api/constant-values.html#com.fs.starfarer.api.impl.campaign.ids.Factions.DIKTAT)
* "fief" should be a valid market id or null
* "ranking" is the lord's rank, which affects their political weight and base income. It should be between 0 and 2, where 2 is highest.
* "customSkills" adds a valid skill to the lord upon generation, which can be used to give specific fleet or piloting modifiers to the character. Each skill must have a value correlated to its level e.g. "field_modulation": 2 for elite-level field modulation on creation
* "customFleetSMods" sets which custom hullmods should be built into the Lord's fleet. Any number can be selected. The value of each hullmod indicates the odds of the hullmod being selected when applying a smod to a given ship. default value is 100. 
* "customLordSMods" sets which custom hullmods should be built into the Lord's flagship. Any number can be selected. The value of each hullmod indicates the odds of the hullmod being selected when applying a smod to the flagship. default value is 100. 
* "fleetForceCustomSMods" set this as false to allow the 'customFleetSMods' to be used alongside the normally selected SMods. if set to true, or unset, a given ship will attempt to add every hullmod in 'customFleetSMods' first
* "flagshipForceCustomSMods"  set this as false to allow the 'customLordSMods' to be used alongside the normally selected SMods. if set to true, or unset, the flahship will attempt to add every hullmod in 'customLordSMods' first
* "preferredItem" can be any of `domestic_goods`, `food`, `luxury_goods`, `drugs`, `hand_weapons`, `alpha_core`, or `lobster`.
* "executiveOfficers" for custom second in command officer layouts. It is a json object where the id is the officer's aptitude, and the value a list of his chosen skills. Skills will continue to be added at random until the executive officers are fully leveled up if they are not specified. 
* "dialogOverride" for custom lord dialogs. Is is a json object were the id is the dialog id (matched to the id of a object in the dialog.json) the value is a list of rules. lord will attempt to use the matching dialogs lines, if and only if, the rules in this object are met (ignoring the rules stored in the dialog.json's object.). for data on rules, see 'Adding Custom dialog to lords' -> "rules".
* Flagship and ship preferences must contain valid ship variant ids. You can find these under the `/data/variants` folder of `starsector-core` or any mod directories.
* Faction and fief will be automatically converted to lower case. Ship variants are case-sensitive.

After that, your lord should be created automatically upon starting a new game.

### Adding Custom SMods to lords
If you're a modder, or just someone who likes S-Mods you might want to expand on the number of S-Mods are available in the generic starlords S-Mod pool. All you have to do is add another entry to the [SMods.json](https://github.com/Deluth-git/Starlords/blob/master/data/lords/SMods.json) file. A few notes:
* "rules" is each requirement that must be met before this set of S-Mods can be added to a given ships pull. every condition must be met for this to happen. conditions are as follows:
  * "hullmods" is the hullmods this set of Smods requires to meet requirements. Set to true for whitelist, and false to blacklist. To meet requirements, a ship must have at least one 'true' hullmod (if any are created in this rule), and no 'false' hullmods
  * "manufacture" is the manufactures this set requires to meet requirements. set to true for whitelist, and false for blacklist. To meet requirements, a ship must have a manufacture of one of the 'true'  manufacture (if any are created in this rule), and must not have a manufacture of the 'false' manufactures.
  * "lordTags" is the starlord tags this set requires to meet requirements. set to true for whitelist, and false for blacklist. To meet requirements, a lord must have a tag of one of the 'true' tag (if any are created in this rule), and must not have a tag of the 'false' tags.
  * "system" is the shipSystem this set requires to meet requirements. set to true for whitelist, and false for blacklist. To meet requirements, a ship must have a system of one of the 'true' systems (if any are created in this rule), and must not have a system of the 'false' systems.
  * "startingFaction" is the starting faction required to meet requirements. starting faction is the faction a lord was part of when they first spawned. set to true for whitelist, and false for blacklist. To meet requirements, a lord must have a starting faction of one of the 'true' factions (if any are created in this rule), and must not have a stating faction of the 'false' factions.
  * "currentFaction" is the current faction required to meet requirements. set to true for whitelist, and false for blacklist. To meet requirements, a lord must have a current faction of one of the 'true' factions (if any are created in this rule), and must not have a current faction of the 'false' factions.
  * "hullID" is the hull required to meet requirements. set to true for whitelist, and false for blacklist. To meet requirements, a ship must have a hull id of one of the 'true' hulls (if any are created in this rule), and must not have a hull id of the 'false' hulls.
  * "defenseType" is the defense type this set requires to meet requirements. set 'true' to all defense types you want this modification to effect. 
    * "NONE"
    * "PHASE"
    * "FRONT"
    * "OMNI" 
  * "fighterBays" is the number of fighter bays this set requires to meet requirements. set between a "min" and "max" value. to get all ships with fighter bays, set "min" to one. to get all ships without figher bays, set "max" to 0
  * "size" is the size this set requires to meet requirements. options are:
    * "FRIGATE"
    * "DESTROYER" 
    * "CRUISER"
    * "CAPITAL_SHIP"
* "S-Mods" are the S-Mods that you want to have present when the groups rules are met. so mods must contain a hull mod ID, and a integer. this value is the amount of weight the Smod has in the pull. default value should be 100


### Adding Custom dialog to lords
If you're a modder, or just someone who loves to write dialog for every starlord in your lords.json, you might want to create custom dialog lines with custom conditions for your starlords. All you have to do is add another entry to the [dialog.json](https://github.com/Deluth-git/Starlords/blob/master/data/lords/dialog.json) file. A few notes:
* "priority" is the priority of this dialog. should have a value of at least 1 the dialog with the highest priority that has all its "rule" reuqirements met will be used in any instance, unless the lord you are talking to has a at least one valid "dialogOverride"
* "rules" is each requirement that must be met for a set of dialog to be used by a given starlord. every condition must be met for this to happen. conditions are as follows:
  * "relationWithPlayer" is the relationship range that this lord must have with a player to meet requirements. set between a "min" and "max" value. range must be between -100 and 100.
  * "startingFaction" is the starting faction required to meet requirements. starting faction is the faction a lord was part of when they first spawned. set to true for whitelist, and false for blacklist. To meet requirements, a lord must have a starting faction of one of the 'true' factions (if any are created in this rule), and must not have a stating faction of the 'false' factions.
  * "currentFaction" is the current faction required to meet requirements. set to true for whitelist, and false for blacklist. To meet requirements, a lord must have a current faction of one of the 'true' factions (if any are created in this rule), and must not have a current faction of the 'false' factions.
  * "isMarriedToPlayer" if set to true, the lord must be married to the player to meet requirements. if set to false, the lord must not be married to the player to meet requirements
  * "willEngage" if set to true, the lord must be willing to attack the player to meet requirements. if set to false, they must be unwilling to attack to meet requirements
  * "hostileFaction" if set to true, the lord must be part of a faction hostile player to meet requirements. if set to false, the lord must not be part of a faction hostile player to meet requirements 
  * "isAtFeast" if set to true, the lord must be at a feast to meet requirements. if set to false, the lord must not be at a feast to meet requirements
  * "playerSubject" if set to true, the lord must be part of the player faction (and as such, the player must be there ruler) to meet requirements. if set to false, the lord must not be part of the player faction to meet requirements.
  * "playerFactionMarital" if set to true, the player must be both part of the lords faction, and the marital of the lord to meet requirements. if set to false, must ether not be in the same faction, or the player must not be the martal to meet requirements
  * "lordFactionMarital" if set to true, the player must be both part of the lords faction, and the lord must be the marital to meet requirements. if set to false, must ether not be in the same faction, or the lord must not be the martal to meet requirements
  * "???playerLord???" (not compleateing this line, because I dont know if this is a thing. more data required)if set to true, the lord must be the lord of the player (and as such, the player must be part of the lords faction, and ) to meet requirements
* "lines" is the dialog lines for every line a starlord speaks. you can also input a number of custom markers into your dialog that will be replaced with data automaticly. the markers are as follows:
  * "%PLAYER_FACTION_NAME"
  * "%PLAYER_NAME" 
  * "%PLAYER_GENDER_MAN_OR_WOMEN"
  * "%PLAYER_GENDER_HE_OR_SHE"
  * "%PLAYER_GENDER_HIM_OR_HER"
  * "%PLAYER_GENDER_HIS_OR_HER" player "his" or "her"
  * "%PLAYER_GENDER_NAME" player gender
  * "%PLAYER_FLAGSHIP_HULLNAME" player flagship ship hull name (return "nothing" if the player has no flagship)
  * "%PLAYER_FLAGSHIP_NAME" player flagship name (returns "nothing" if the player has no flagship)

  * "%LORD_FACTION_NAME" 
  * "%LORD_STARTING_FACTION_NAME"
  * "%LORD_NAME"
  * "%LORD_GENDER_MAN_OR_WOMEN"
  * "%LORD_GENDER_HE_OR_SHE"
  * "%LORD_GENDER_HIM_OR_HER"
  * "%LORD_GENDER_HIS_OR_HER"
  * "%LORD_GENDER_NAME"
  * "%LORD_FLAGSHIP_HULLNAME" lord flagship ship hull name (return "nothing" if the lord has no flagship)
  * "%LORD_FLAGSHIP_NAME" lord flagship name (returns "nothing" if the lord has no flagship)
  
  *some lines will also use custom inputed data. in this case, they will use the '%c#' marker, with # being the order they are added to the line.
### Credits
Starsector team for developing the game\
[Nexerelin](https://github.com/Histidine91/Nexerelin/tree/master) team's codebase for providing excellent references to many obscure parts of the Starsector API \
Interestio for Lord [portraits](https://fractalsoftworks.com/forum/index.php?topic=17066.0)
