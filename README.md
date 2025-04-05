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
  * please note: if you intend for a dialog to only be used by certain lords, you should set the priority to 0. then it will never be used by anyone other than the lords set to use that dialog in particular
* "rules" is each requirement that must be met for a set of dialog to be used by a given starlord. every condition must be met for this to happen. conditions are as follows:
  * "relationWithPlayer" is the relationship range that this lord must have with a player to meet requirements. set between a "min" and "max" value. range must be between -100 and 100.
  * "startingFaction" is the starting faction required to meet requirements. starting faction is the faction a lord was part of when they first spawned. set to true for whitelist, and false for blacklist. To meet requirements, a lord must have a starting faction of one of the 'true' factions (if any are created in this rule), and must not have a stating faction of the 'false' factions.
  * "currentFaction" is the current faction required to meet requirements. set to true for whitelist, and false for blacklist. To meet requirements, a lord must have a current faction of one of the 'true' factions (if any are created in this rule), and must not have a current faction of the 'false' factions.
  * "isMarriedToPlayer" if set to true, the lord must be married to the player to meet requirements. if set to false, the lord must not be married to the player to meet requirements
  * "isMarried" if set to true, the lord must be married to meet requirements, if set to false, the lord must not be married to meet requirements.
  * "isPlayerMarried" if set to true, the player must be married to meet requirements, if set to false, the player must not be married to meet requirements.
  * "willEngage" if set to true, the lord must be willing to attack the player to meet requirements. if set to false, they must be unwilling to attack to meet requirements
  * "hostileFaction" if set to true, the lord must be part of a faction hostile player to meet requirements. if set to false, the lord must not be part of a faction hostile player to meet requirements 
  * "isAtFeast" if set to true, the lord must be at a feast to meet requirements. if set to false, the lord must not be at a feast to meet requirements
  * "isHostingFeast" if set to true, the lord must be at a feast and hosting it to meet requirements. if set to false, the lord must not be at a feast or not hosting it to meet requirements
  * "playerSubject" if set to true, the lord must be part of the player faction (and as such, the player must be there ruler) to meet requirements. if set to false, the lord must not be part of the player faction to meet requirements.
  * "playerFactionMarital" if set to true, the player must be both part of the lords faction, and the marital of the lord to meet requirements. if set to false, must ether not be in the same faction, or the player must not be the martal to meet requirements
  * "lordFactionMarital" if set to true, the player must be both part of the lords faction, and the lord must be the marital to meet requirements. if set to false, must ether not be in the same faction, or the lord must not be the martal to meet requirements
  * "lordPersonality": is the lord personality type this rule requires to meet requirements. set 'true' to all lord personality types you want to be allowed to meet requirements.
    * "Upstanding"
    * "Martial"
    * "Calculating"
    * "Quarrelsome"
  * "lordBattlerPersonality": is the lord personality type this rule requires to meet requirements. set 'true' to all lord personality types you want to be allowed to meet requirements.
    * "Reckless"
    * "Aggressive"
    * "Steady"
    * "Cautious"
    * "Timid"
  * "playerWealth": is the number of credits the player must have to meet requirements. set between a "min" and "max" value. 
  * "lordWealth": is the number of credits the lord must have to meet requirements. set between a "min" and "max" value.
  * "playerLevel": is the level the player must be to meet requirements. set between a "min" and "max" value.
  * "lordLevel": is the level the lord must be to meet requirements. set between a "min" and "max" value.
  * "playerRank": is the number of rank the player must be to meet requirements. set between a "min" and "max" value. range should be between 0 and 2
  * "lordRank": is the number of credits the player must have to meet requirements. set between a "min" and "max" value. range should be between 0 and 2
  * "lordsCourted": is the number of lords the player must have has professed admiration to meet requirements. set between a "min" and "max" value.
  * "isLordCourtedByPlayer": if set to true, the lord must have been professed to by the player to meet requirements. if set to false, the lord must not have been professed to by the player to meet requirements.
  * "playerLordRomanceAction":  is the number of romantic actions the player must have had with the lord meet requirements. set between a "min" and "max" value. (romantic actions can be default, be gained by giving gifts, or dedecating tornament victorys)
  * "availableTournament" : always returns false if: not at a feast. if set to true, the lord must be at a feast that has yet to have a tournament. if set to false, the tournament must have already happened
  * "playerTournamentVictory": always returns false if: not at a feast. if set to true, the player must have won the tournament at the current feast. if set to false, the player must have not won a tournament current feast. 
  * "lordTournamentVictory": always returns false if: not at a feast. if set to true, the lord must have won the tournament at the current feast. if set to false, the lord must have not won a tournament at the current feast.
  * "playerTournamentVictoryDedicated": always returns false, if: not at a feast, or the player has not won the tournament there. if set to true, the player must has already dedicated there victory at the tournament. if set to false, the player must have not already dedicated there victory at the tournament
  * "tournamentDedicatedToLord": always returns false if: not at a feast. if set to true, the lord must be the one the tournament is dedicated to. if set to false, the lord must not be the one the tournament is dedicated to
  * "feastIsHostingWedding": if set to true, the lord must both be at a feast, and there must be a wedding being hosted there. if set to faslse, the lord must be at a feast, and a wedding must not be hosted there.
  * "firstMeeting": if set to true, this must be your first time meeting this starlord. if set to false, you must have met this starlord before.
  * "lordsInFeast": always returns false if: not at a feast. is the number of lords that must be at the current feast to meet requirements. set between a "min" and "max" value. range should be at least 0
  * basic is simply a "lineID": "new string";
  * advanced is more complicated. its a json object, that must include a "line" (to act as the normal lineID), but also additional json peramiters. "addons" are . the "addons" are as follows:
    * "addons" additional conditions and effects that you can have run at the moment this line is ran. most 'addons' also add a line of dialog to show what effects they had. any "option_" will only run addons after the option is selected. and "tooltip_" line cannot use "addons"
      * "repIncrease":
          * "min": Integer
          * "max": Integer
      * "repDecrease": 
          * "min": Integer
          * "max": Integer
      * "creditsIncrease": 
          * "min": Integer
          * "max": Integer
      * "creditsDecrease": 
          * "min": Integer
          * "max": Integer
      * "romanceActionIncrease": note: does not add text indicators (also of note: one value of romanceAction represents a major romantic action)
        * "min": Integer
        * "max": Integer
      * "romanceActionDecrease": note: does not add text indicators (also of note: one value of romanceAction represents a major romantic action)
        * "min": Integer
        * "max": Integer
      * "additionalText": "lineID" adds an additional line of dialog, with the inputed name
      * "startWedding": boolean if set to true, sets the current feast the player is at to a wedding ceremony (or howeer that works). will do nothing if not at a feast.
      * "dedicateTournamentVictoryToLord": boolean. if set to true, dedicates your tournament victory to the target lord. only runs if you are at a feast, and won the tournament.
      * "startTournament": boolean. if set to true, starts a Tournament. 
    * "color" color override for this dialog line. not required. has 3 'preset' colors, but also the option for a custom color. cannot be used in any "tooltip_" line.
      * "RED"
      * "GREEN"
      * "YELLOW"
      * optional array is as follows: (with a default value of 0 for r,b,g, and a default value of 255 for a.)
      * "r"
      * "g"
      * "b"
      * "a"
    * "options" is an jsonArray containing the lineID's of any option or option set that this line should run. please note, that most lines by default have a entry in the [default_dialog_options.json](https://github.com/Deluth-git/Starlords/blob/master/data/lords/default_dialog_options.json) file
    * "show" is an jsonObject that contains the same functions as 'rules', but instead of determining if a line can be ran, if all "show" any show functions ar efalse, and this line is selected, it will not be shown. for "option_"'s, if this line is selected and the 'hide' is true, it will not be ran.
    * "optionData" is the link to a line that happens when you click this option. if called as a line, and no options are selected for this line, it will automaticly load the linked line. 
    * "hint" is the hover over hint that happens when you hover only an option. only works if this line is called as an option
    * "shortcut" : "shortcutLey" if this is set to one of the acsepted value, will add a hotkey to an option. only works for "option_" lines. possable options are:
      * "ESCAPE"
    * "lines" is the dialog lines for every line a starlord speaks. this comes in 2 forms. the first, wish we will call basic, and the second, that we will call advanced:
  * for both basic and advanced lines, you can also input a number of custom markers into your dialog that will be replaced with data automaticly. the markers are as follows
  * "%PLAYER_FACTION_NAME"
  * "%PLAYER_NAME" 
  * "%PLAYER_GENDER_MAN_OR_WOMEN"
  * "%PLAYER_GENDER_HE_OR_SHE"
  * "%PLAYER_GENDER_HIM_OR_HER"
  * "%PLAYER_GENDER_HIS_OR_HER"
  * "%PLAYER_GENDER_HUSBAND_OR_WIFE"
  * "%PLAYER_GENDER_NAME" player gender
  * "%PLAYER_FLAGSHIP_HULLNAME" player captioned ship hull name (return "nothing" if the player has no captioned ship)
  * "%PLAYER_FLAGSHIP_NAME" player captioned ship name (returns "nothing" if the player has no captioned ship)
  
  * Note: all %PLAYER_SPOUSE markers will instead return the players data if they are not married. so be careful using this to avoid confusion or accidental mockery
  * "%PLAYER_SPOUSE_FACTION_NAME"
  * "%PLAYER_SPOUSE_NAME"
  * "%PLAYER_SPOUSE_GENDER_MAN_OR_WOMEN"
  * "%PLAYER_SPOUSE_GENDER_HE_OR_SHE"
  * "%PLAYER_SPOUSE_GENDER_HIM_OR_HER"
  * "%PLAYER_SPOUSE_GENDER_HIS_OR_HER"
  * "%PLAYER_SPOUSE_GENDER_HUSBAND_OR_WIFE"
  * "%PLAYER_SPOUSE_GENDER_NAME" player gender
  * "%PLAYER_SPOUSE_FLAGSHIP_HULLNAME" partners currently captioned ships hullname (return "nothing" if the partner has no captioned ship).
  * "%PLAYER_SPOUSE_FLAGSHIP_NAME" partner captioned ship name (returns "nothing" if the partner has no captioned ship).

  * "%LORD_FACTION_NAME" 
  * "%LORD_STARTING_FACTION_NAME"
  * "%LORD_NAME"
  * "%LORD_GENDER_MAN_OR_WOMEN"
  * "%LORD_GENDER_HE_OR_SHE"
  * "%LORD_GENDER_HIM_OR_HER"
  * "%LORD_GENDER_HIS_OR_HER"
  * "%LORD_GENDER_HUSBAND_OR_WIFE"
  * "%LORD_GENDER_NAME"
  * "%LORD_FLAGSHIP_HULLNAME" lord flagship ship hull name (return "nothing" if the lord has no flagship)
  * "%LORD_FLAGSHIP_NAME" lord flagship name (returns "nothing" if the lord has no flagship)

  * Note: all %LORD_SPOUSE markers will instead return the lords data if they are not married. so be careful using this to avoid confusion or accidental mockery
  * "%LORD_SPOUSE_FACTION_NAME"
  * "%LORD_SPOUSE_STARTING_FACTION_NAME"
  * "%LORD_SPOUSE_NAME"
  * "%LORD_SPOUSE_GENDER_MAN_OR_WOMEN"
  * "%LORD_SPOUSE_GENDER_HE_OR_SHE"
  * "%LORD_SPOUSE_GENDER_HIM_OR_HER"
  * "%LORD_SPOUSE_GENDER_HIS_OR_HER"
  * "%LORD_SPOUSE_GENDER_HUSBAND_OR_WIFE"
  * "%LORD_SPOUSE_GENDER_NAME"
  * "%LORD_SPOUSE_FLAGSHIP_HULLNAME" partner captioned ship hull name (return "nothing" if the partner has no captioned ship).
  * "%LORD_SPOUSE_FLAGSHIP_NAME" partner captioned ship name (returns "nothing" if the partner has no captioned ship).

  * note: all %LORD_HOST markers will instead return the lord you are talking to if you are not at a feast. please use carefully, to avoid confusion or accidental gloating
  * "%LORD_HOST_FACTION_NAME"
  * "%LORD_HOST_STARTING_FACTION_NAME"
  * "%LORD_HOST_NAME"
  * "%LORD_HOST_GENDER_MAN_OR_WOMEN"
  * "%LORD_HOST_GENDER_HE_OR_SHE"
  * "%LORD_HOST_GENDER_HIM_OR_HER"
  * "%LORD_HOST_GENDER_HIS_OR_HER"
  * "%LORD_HOST_GENDER_HUSBAND_OR_WIFE"
  * "%LORD_HOST_GENDER_NAME"
  * "%LORD_HOST_FLAGSHIP_HULLNAME" lord flagship ship hull name (return "nothing" if the lord has no flagship)
  * "%LORD_HOST_FLAGSHIP_NAME" lord flagship name (returns "nothing" if the lord has no flagship)

  * Note: all %LORD_HOST_SPOUSE markers will instead return the lords data if they are not married. so be careful using this to avoid confusion or accidental mockery
  * "%LORD_HOST_SPOUSE_FACTION_NAME"
  * "%LORD_HOST_SPOUSE_STARTING_FACTION_NAME"
  * "%LORD_HOST_SPOUSE_NAME"
  * "%LORD_HOST_SPOUSE_GENDER_MAN_OR_WOMEN"
  * "%LORD_HOST_SPOUSE_GENDER_HE_OR_SHE"
  * "%LORD_HOST_SPOUSE_GENDER_HIM_OR_HER"
  * "%LORD_HOST_SPOUSE_GENDER_HIS_OR_HER"
  * "%LORD_HOST_SPOUSE_GENDER_HUSBAND_OR_WIFE"
  * "%LORD_HOST_SPOUSE_GENDER_NAME"
  * "%LORD_HOST_SPOUSE_FLAGSHIP_HULLNAME" partner captioned ship hull name (return "nothing" if the partner has no captioned ship).
  * "%LORD_HOST_SPOUSE_FLAGSHIP_NAME" partner captioned ship name (returns "nothing" if the partner has no captioned ship).

  *some lines will also use custom inputted data. in this case, they will use the '%c#' marker, with # being the order they are added to the line.
  *available lines to override are follows:
    * "greeting"
      * "option_avoid_battle" :           OptionId.SUGGEST_CEASEFIRE
      * "option_ask_tournament" :         OptionId.ASK_TOURNAMENT
      * "option_dedicate_tournament" :    "dedicate_tournament"
      * "option_host_wedding" :           OptionId.START_WEDDING
      * "option_ask_current_task" :       OptionId.ASK_CURRENT_TASK
      * "option_ask_question" :           OptionId.ASK_QUESTION
      * "option_suggest_action" :         OptionId.SUGGEST_ACTION
      * "option_speak_privately" :        OptionId.SPEAK_PRIVATELY
      * "option_cutComLink" :             "exitDialog"
      
    * "dedicate_tournament"
      * copys 'greeting' options
    






    * ---old data. here for refrenece as I slowly cut though the code and finish things.
    * "greeting_host_feast"
      * "option_ask_tournament" :         OptionId.ASK_TOURNAMENT
      * "option_dedicate_tournament" :    OptionId.DEDICATE_TOURNAMENT
      * "option_host_wedding" :           OptionId.START_WEDDING
      * "option_ask_current_task" :       OptionId.ASK_CURRENT_TASK
      * "option_ask_question" :           OptionId.ASK_QUESTION
      * "option_suggest_action" :         OptionId.SUGGEST_ACTION
      * "option_speak_privately" :        OptionId.SPEAK_PRIVATELY
      * "option_cutComLink" :             exitDialog
    * "greeting_feast"
      * "option_ask_tournament" :         OptionId.ASK_TOURNAMENT
      * "option_dedicate_tournament" :    OptionId.DEDICATE_TOURNAMENT
      * "option_host_wedding" :           OptionId.START_WEDDING
      * "option_ask_current_task" :       OptionId.ASK_CURRENT_TASK
      * "option_ask_question" :           OptionId.ASK_QUESTION
      * "option_suggest_action" :         OptionId.SUGGEST_ACTION
      * "option_speak_privately" :        OptionId.SPEAK_PRIVATELY
      * "option_cutComLink" :             exitDialog
    * "greetings_first"
      * "option_ask_current_task" :       OptionId.ASK_CURRENT_TASK
      * "option_ask_question" :           OptionId.ASK_QUESTION
      * "option_suggest_action" :         OptionId.SUGGEST_ACTION
      * "option_speak_privately" :        OptionId.SPEAK_PRIVATELY
      * "option_cutComLink" :             exitDialog
    * "greetings_other"
      * "option_ask_current_task" :       OptionId.ASK_CURRENT_TASK
      * "option_ask_question" :           OptionId.ASK_QUESTION
      * "option_suggest_action" :         OptionId.SUGGEST_ACTION
      * "option_speak_privately" :        OptionId.SPEAK_PRIVATELY
      * "option_cutComLink" :             exitDialog
    * "greetings_hostile"
      * "option_avoid_battle" :           OptionId.SUGGEST_CEASEFIRE
      * "option_speak_privately" :        OptionId.SPEAK_PRIVATELY
      * "option_cutComLink" :             exitDialog










    * "greeting_host_feast"
    * "greeting_feast"
    * "greetings_first"
    * "greetings_hostile"
    * "greetings_other"
    * "dedicate_tournament"
    * "dedicate_tournament_again"
    * "confirm_start_tournament"
    * "cant_start_tournament"
    * "current_task_desc_campaign2"
    * "current_task_desc_campaign"
    * "current_task_desc_companion"
    * "current_task_desc_follow"
    * "current_task_desc_defend"
    * "current_task_desc_raid"
    * "current_task_desc_venture"
    * "current_task_desc_upgrade_fleet"
    * "current_task_desc_collect_taxes"
    * "current_task_desc_patrol"
    * "current_task_desc_feast"
    * "current_task_desc_none"
    * "admiration_response"
    * "spend_time_together#" # is a value between 1 and 36. date is chosen randomly
    * "spend_time_together_after"
    * "spend_time_together_hint"
    * "marriage_response"
    * "accept_marriage_second"
    * "refuse_marriage_hint"
    * "marriage_ceremony" %c0 is your new partners name, and %c1 is the name of the person hosting the ceremony
    * "join_party_explanation"
    * "leave_party_explanation"
    *
    *
    *
    *
    *
    *
    *
    *
    *
    * "relation_increase" %c0 represents amount of reputation gained
    * "relation_decrease" %c0 represents amount of reputation gained
    * "addedIntel" 
    *
    * "option_avoid_battle"
    * "tooltip_avoid_battle"
    *
    * "option_suggest_action"
    * "option_speak_privately"
    * "option_cutComLink"
    * "option_ask_current_task"
    * "option_ask_question"
    * "option_ask_location"
    * "option_ask_tournament"
    * "option_dedicate_tournament"
    * "option_host_wedding" %c0 represents your new spouse name
    * "option_continue_to_tournament"
    * "option_avoid_tournament"
    * "option_profess_admiration"
    * "option_ask_date"
    * "option_ask_marriage"
    * "option_ask_leave_party"
    * "option_ask_join_party"
    * "option_sway_council_support"
    * "option_sway_council_oppose"
    * "option_sway_player"
    * "option_nevermind_askQuestion"
    * "option_give_gift"
    * "option_dont_give_gift"
    * "option_confirm_join_party"
    * "option_nevermind_join_party"
    * "option_confirm_leave_party"
    * "option_nevermind_leave_party"
    *
    *
    *
    *
    *
### Credits
Starsector team for developing the game\
[Nexerelin](https://github.com/Histidine91/Nexerelin/tree/master) team's codebase for providing excellent references to many obscure parts of the Starsector API \
Interestio for Lord [portraits](https://fractalsoftworks.com/forum/index.php?topic=17066.0)
