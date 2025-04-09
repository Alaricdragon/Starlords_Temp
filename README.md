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
  * "lordAndPlayerSameFaction" if set to false, the lord and player must not be part of the same faction to meet requirements. if set to ture, the lord and player must be part of the same faction to meet requirements.
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
  * "isWeddingTarget": always reutrns false if not at a feast, or feast is not hosting a wedding. if set to false, the lord no must be being married at this feast. if set to true, the lord be getting married this feast.
  * "firstMeeting": if set to true, this must be your first time meeting this starlord. if set to false, you must have met this starlord before.
  * "lordsInFeast": always returns false if: not at a feast. is the number of lords that must be at the current feast to meet requirements. set between a "min" and "max" value. range should be at least 0
  * "hasProfessedAdmirationThisFeast": always returns false if: not at a feast. if set to false, you must have not professed admiration this feast to meet requirements. if set to true, you must have already professed admiration this feast to meet requirements.
  * "hasHeldDateThisFeast": always returns false if: not at a feast. if set to false, you must have not heald a date this feast to meet requirements. if set to true, you must have already head a date this feast to meet requirements.
  * "lordPledgedSupport_forActiveProposal": always returns false if the lords faction is not voting on anything. if set to false, the lord must not promised opposition to the active proposal. if set to true, the lord must have promised support for the active proposal.
  * "lordPledgedSupport_againstActiveProposal": always returns false if the lords faction is not voting on anything. if set to false, the lord must not have promised support for the active proposal. if set to true, the lord must have promised opposition of the active proposal.
  * "lordPledgedSupport_forPlayerProposal":  always returns false if the player has no proposal. if set to false, the lord must not have promised support for the players proposal. if set to true, the lord must have promised support for the players proposal.
  * "lordPledgedSupport_againstPlayerProposal": always returns false if the has no proposal. if set to false, the lord must not promised opposition to the players proposal. if set to true, the lord must have promised opposition of the players proposal.
  * "playerProposalExists": if set to false, the player must not have a proposal to meet requirements. if set to true, the player must have a proposal to meet requirements.
  * "lordProposalExists": if set to false, the lord must not have a proposal to meet requirements. if set to true, the lord must have a proposal to meet requirements.
  * "lordFactionHasActiveConsole": if set to false, the lords faction must not be actively voting on a proposal. if set to true, the lords faction must be voting on a proposal
  * "playerFactionHasActiveConsole": if set to false, the players faction must not be actively voting on a proposal. if set to true, the players faction must be voting on a proposal 
  * "lordActingInPlayerFleet": if set to false, the lord must not be in the player fleet to meet requirements. if set to true, the lord must be in the player fleet to meet requirements.
  * "playerHasCommodity": is a jsonObject contining a set of commodityID's, each with a min and max value. player must have between the min and max value of each commoidty in there cargo bay to meet requirements.
  * "lordsFavItem": is the lord fav item required to meet requirements. set to true for whitelist, and false for blacklist. To meet requirements, a lord must have a fav item of one of the 'true' fav items (if any are created in this rule), and must not have a fav item of the 'false' fav items. 
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
      * "removeCommoditysFromPlayerFleet": 
        * "commodityID..."
          * "min": Integer
          * "max": Integer
      * "addCommoditysToPlayerFleet":
        * "commodityID..."
          * "min": Integer
          * "max": Integer
      * "additionalText": "lineID" adds an additional line of dialog, with the inputed name
      * "wedPlayerToLord": boolean. if set to true, marry's the lord and player. if set to false, and the player and lord are married, un-marry's the lord and player.
      * "wedPlayerToWeddingTarget": boolean. if set to true, marry's the wedding target and player. if set to false, and the player and wedding target are married, un-marry's the wedding target and player.
      * "startWedding": boolean. if set to true, sets the current feast the player is at to a wedding ceremony (or howeer that works). will do nothing if not at a feast.
      * "dedicateTournamentVictoryToLord": boolean. if set to true, dedicates your tournament victory to the target lord. only runs if you are at a feast, and won the tournament.
      * "startTournament": boolean. if set to true, starts a Tournament. 
      * "setHeldDate": boolean. sets if you have held a data this feast.  
      * "setProfessedAdmiration": boolean. sets if you have held professed admiration this feast.
      * "setCourted" : boolean. sets whether you are courting this lord. setting this to true lets you do romance =)
      * "setInPlayerFleet": boolean. sets wether or not this lord is in the player fleet, or in their own fleet.
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
    * "show" is an jsonObject that contains the same functions as 'rules', but instead of determining if a line can be ran, if any conditions in the object are false, and this line is selected, it will not be shown. for "option_"'s, if this line is selected and the 'hide' is true, it will not be ran.
    * "enable": (only effects 'option_' lines) is a jsonObject that contains the same functions as 'rules', but instead of determining if a line can be ran. if any conditions in this object are false, and this option will be greyed out and unable to be used.
    * "optionData" is the link to a line that happens when you click this option. if called as a line, and no options are selected for this line, it will automaticly load the linked line. 
    * "hint" is the hover over hint that happens when you hover only an option. only works if this line is called as an option
    * "shortcut" : "shortcutLey" if this is set to one of the acsepted value, will add a hotkey to an option. only works for "option_" lines. possable options are:
      * "ESCAPE"
    * "lines" is the dialog lines for every line a starlord speaks. this comes in 2 forms. the first, wish we will call basic, and the second, that we will call advanced:
  * for both basic and advanced lines, you can also input a number of custom markers into your dialog that will be replaced with data automaticly. the markers are as follows
  * there are a few diffrent targets for markers. the targets are:
    * PLAYER
    * PLAYER_SPOUSE (note: will backup to PLAYER if player has no spouse)
    * LORD
    * LORD_SPOUSE (note: will backup to LORD if LORD has no spouse)
    * LORD_HOST (note: will backup to LORD if not at feast, or organizer does not exsist)
    * LORD_HOST_SPOUSE (note: will backup to LORD_HOST if organizer does not have a spouse)
    * WEDDING_TARGET (note: will backup to LORD if not at a wedding)
    * WEDDING_TARGET_SPOUSE (note: will backup to WEDDING_TARGET if wedding target has no spouse)
  markers of the following. (replace TARGET with diffrent target types at will)
      * "%TARGET_FACTION_NAME" 
      * "%TARGET_STARTING_FACTION_NAME"
      * "%TARGET_NAME"
      * "%TARGET_NAME_FIRST"
      * "%TARGET_NAME_LAST"
      * "%TARGET_GENDER_MAN_OR_WOMEN"
      * "%TARGET_GENDER_HE_OR_SHE"
      * "%TARGET_GENDER_HIM_OR_HER"
      * "%TARGET_GENDER_HIS_OR_HER"
      * "%TARGET_GENDER_HUSBAND_OR_WIFE"
      * "%TARGET_GENDER_SUIT_OR_DRESS"
      * "%TARGET_GENDER_NAME"
      * "%TARGET_FLAGSHIP_HULLNAME" target flagship ship hull name (return "nothing" if the target has no flagship)
      * "%TARGET_FLAGSHIP_NAME" target flagship name (returns "nothing" if the target has no flagship)

  *some lines will also use custom inputted data. in this case, they will use the '%c#' marker, with # being the order they are added to the line.
  *available lines to override are follows:
    * "greeting"
      * "option_avoid_battle" :           OptionId.SUGGEST_CEASEFIRE
      * "option_ask_tournament" :         OptionId.ASK_TOURNAMENT
      * "option_dedicate_tournament" :    "dedicate_tournament"
      * "option_host_wedding" :           "marriage_ceremony"
      * "option_ask_current_task" :       "current_task_desc"
      * "option_ask_question" :           "ask_question"
      * "option_suggest_action" :         OptionId.SUGGEST_ACTION
      * "option_speak_privately" :        OptionId.SPEAK_PRIVATELY
      * "option_cutComLink" :             "exitDialog"
      
    * "dedicate_tournament"
      * copys 'greeting' options
  
    * "start_tournament":
      * "option_continue_to_tournament" : -opens tournament-
      * "option_avoid_tournament" :       "greeting"
    
    * "current_task_desc":
      * '%c0' is the lords current target name.
      * null options (keeps options that are up when loaded.)

    * "ask_question"
      * "option_ask_location" :         "OptionId.ASK_LOCATION"
      * "option_ask_quest" :            "OptionId.ASK_QUEST"
      * "option_profess_admiration" :   "admiration_response"
      * "option_ask_date" :             "spend_time_together"
      * "option_ask_marriage" :         "marriage_response"
      * "option_ask_leave_party" :      "leave_party_explanation"
      * "option_ask_join_party" :       "join_party_explanation"
      * "option_sway_council_support" : "OptionId.SWAY_PROPOSAL_COUNCIL"
      * "option_sway_council_oppose" :  "OptionId.SWAY_PROPOSAL_COUNCIL"
      * "option_sway_player" :          "OptionId.SWAY_PROPOSAL_PLAYER"
      * "option_nevermind_askQuestion" :"greeting"

    * "admiration_response"
      * copys 'ask_question' options

    * "spend_time_together"
      * "option_give_gift" :            "give_gift"
      * "option_dont_give_gift" :       "ask_question"
    
    * "give_gift"
      * "option_gift_alpha_core" :      "dislikeGift_alpha_core" || "likeGift_alpha_core"
      * "option_gift_hand_weapons" :    "dislikeGift_hand_weapons" || "likeGift_hand_weapons"
      * "option_gift_food" :            "dislikeGift_food" || "likeGift_food"
      * "option_gift_luxury_goods" :    "dislikeGift_luxury_goods" || "likeGift_luxury_goods"
      * "option_gift_lobster" :         "dislikeGift_lobster" || "likeGift_lobster"
      * "option_gift_drugs" :           "dislikeGift_drugs" || "likeGift_drugs"
      * "option_gift_domestic_goods" :  "dislikeGift_domestic_goods" || "likeGift_domestic_goods"
      * "options_give_gift_exit" :      "ask_question"

    * "dislikeGift_alpha_core"
      * copys 'ask_question' options
    * "dislikeGift_alpha_core"
      * copys 'ask_question' options
    * "dislikeGift_hand_weapons"
      * copys 'ask_question' options
    * "dislikeGift_food"
      * copys 'ask_question' options
    * "dislikeGift_luxury_goods"
      * copys 'ask_question' options
    * "dislikeGift_lobster"
      * copys 'ask_question' options
    * "dislikeGift_drugs"
      * copys 'ask_question' options
    * "dislikeGift_domestic_goods"
      * copys 'ask_question' options

    * "likeGift_alpha_core"
      * copys 'ask_question' options
    * "likeGift_alpha_core"
      * copys 'ask_question' options
    * "likeGift_hand_weapons"
      * copys 'ask_question' options
    * "likeGift_food"
      * copys 'ask_question' options
    * "likeGift_luxury_goods"
      * copys 'ask_question' options
    * "likeGift_lobster"
      * copys 'ask_question' options
    * "likeGift_drugs"
      * copys 'ask_question' options
    * "likeGift_domestic_goods"
      * copys 'ask_question' options

    * "marriage_ceremony"
        * copys 'greeting' options

    * "marriage_response"
        * copys 'ask_question' options

    * "join_party_explanation"
        * "option_confirm_join_party": ask_question
        * "option_nevermind_join_party": ask_question
    
    * "leave_party_explanation"
        * "option_confirm_leave_party": ask_question
        * "option_nevermind_leave_party": ask_question


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
