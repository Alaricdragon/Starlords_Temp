"addons" are additional conditions and effects that you can have run at the moment this line is ran. some 'addons' also add a line of dialog to show what effects they had. any "option_" will only run addons after the option is selected. and "tooltip_" line cannot use "addons"
* modifiers:
  * "conditionalAddon": {"rules": [DIALOG_RULES.md](https://github.com/Alaricdragon/Starlords_Temp/tree/master/theManyReadmes/DIALOG_RULES.md) ,"addons": [DIALOG_ADDONS.md](https://github.com/Alaricdragon/Starlords_Temp/tree/master/theManyReadmes/DIALOG_ADDONS.md) } this addon only runs the addons from its 'addon' jsonObject only if its 'rules' jsonObject meets requirements.
* targets:
    * "targetLord" does nothing if: target lord is unset. runs the contained 'addon' json object, for the target lord instead of the interacting lord
* value changes: value changes can contain the following data:
  * Integer: the value that will be added / removed from a given stat. if min and max are both set, the value will be a random number between the 2.
  * dialogValue: a ([DIALOG_VALUES.md](https://github.com/Alaricdragon/Starlords_Temp/tree/master/theManyReadmes/DIALOG_VALUES.md)) json object. the return value of the dialog value is what the item must have to meet requirements.
  * {"min","max"}: were "min" and "max" can be a Integer, or a ([DIALOG_VALUES.md](https://github.com/Alaricdragon/Starlords_Temp/tree/master/theManyReadmes/DIALOG_VALUES.md)).
  * "repChange" changes the relation between the lord and player
  * "creditsChange" changes the amount of credits the player has.
  * "exchangeCreditsWithLord" gives / takes credits from a lord. a negative value gives, a positive value takes.
  * "romanceChange" changes the stored romance value between the lord and player.
  * "changeCommoditysInPlayersFleet" holds a jsonObject of commoditys, were the value is how mush this commoidity changes. the value follows the same rules as any other value addon.
* "additionalText": "lineID" adds an additional line of dialog, with the inputed name. also take a jsonArray as input, running each inputed line in sequence.
* "wedPlayerToLord": boolean. if set to true, marry's the lord and player. if set to false, and the player and lord are married, un-marry's the lord and player.
* "wedPlayerToWeddingTarget": boolean. if set to true, marry's the wedding target and player. if set to false, and the player and wedding target are married, un-marry's the wedding target and player.
* "startWedding": boolean. if set to true, sets the current feast the player is at to a wedding ceremony (or howeer that works). will do nothing if not at a feast.
* "dedicateTournamentVictoryToLord": boolean. if set to true, dedicates your tournament victory to the target lord. only runs if you are at a feast, and won the tournament.
* "startTournament": boolean. if set to true, starts a Tournament.
* "defectLordToFaction": String || {"factionID" String, "newRank":Intiger || dialogValue, "includeFiefs": Boolean}. causes the lord to defect to the target faction. can optionaly set the lords rank, or if they take there fiefs with them. default is rank 0, and will take fiefs with them. (although some factions cannot have fiefs taken in defection.)
  * if you want the lord to defect to the players current faction, set the base String, of factionID to "playerCurrFaction".
* "playSound": String || {"soundID":String, "pitch":Integer || dialogValue, "volume": Integer || dialogValue}. runs Global.getSoundPlayer().playUISound on the inputed data.
* "attemptToAddRandomQuest": boolean. if set to true, it will run ether the '' line, or the '' line. depending on if a quest can actually get gotten or not. please note: this is to be removed, and replaced with a more complicated quest system in the future.
* "preventAttacksOnPlayer" Integer. sets the number of days that the interacting lord will be unable to attack the player.
* "preventAttacksOnTargetLord" Integer. sets the number of days that the interacting lord will be unable to attack the target lord. (if one exsists)
* "releaseLord": boolean. if set to true, releases the starlord from captivity.
* "captureLord": boolean. if set to true, puts the starlord into the player's prison.
* "setHeldDate": boolean. sets if you have held a data this feast.
* "setProfessedAdmiration": boolean. sets if you have held professed admiration this feast.
* "setCourted" : boolean. sets whether you are courting this lord. setting this to true lets you do romance =)
* "setInPlayerFleet": boolean. sets whether this lord is in the player fleet, or in their own fleet.
* "setPledgedFor_CurrentProposal": boolean. sets whether the lord you are talking to has pledged support for the current proposal or not.
* "setPledgedAgainst_CurrentProposal": boolean. sets whether the lord you are talking to has pledged opposition for the current proposal or not.
* "setPledgedFor_PlayerProposal": boolean. sets whether the lord you are talking to has pledged support for the player proposal or not.
* "setPledgedAgainst_PlayerProposal": boolean. sets whether the lord you are talking to has pledged support for the player proposal or not.
* "setPlayerSupportForLordProposal": boolean. sets weather the player is currently supporting the lords proposal, or is opposed to the lords proposal.
* "setPlayerSupportForCurProposal": boolean. sets weather the player is currently supporting the current proposal or is opposed to the current proposal.
* "setSwayed": boolean. sets whether the lord you are talking to has been swayed or not. if so, they cannot be swayed until the next proposal.
* "setPersonalityKnown": boolean. sets weather the player knows this lords personality.
* "setLordRank": Integer || dialogValue. sets the lords rank.
* "setPlayerRank": Integer || dialogValue. sets the players rank.
* "AICommand" : String. this commands that lord to change there AI action. you can command a lord to do the following:
  *  "END_CURRENT_ACTIONS" this lets the lord chose a new action to take. all on there own.
  *  "FOLLOW_PLAYER_FLEET" this make the lord follow the player fleet.
  *  "FOLLOW_TARGET_LORD" this makes the lord follow the target lord (if one exists)
  *  "RAID_TARGET_MARKET": this make the lord raid the target market (if one exists)
  *  "UPGRADE" this make the lord go and upgrade there fleet
  *  "PATROL_TARGET_MARKET" this makes the lord go and patrol the target market (if one exists)
  *  --NOT YET ADDED--"ORGANIZE_CAMPAIGN" this makes the lord organize a camping. (provided they have the power to do so, and one is not already active.)
  *  --NOT YET ADDED--"JOIN_CAMPAIGN" this makes the lord join the active campaign. (if one exists)
* "setDialogData": jsonObject. sets data that is stored in the dialog, and is deleted when the interaction ends. if a jsonObject with a min/max is one of its lines, it acts as a increase to the value.
  * each line is "dataID" : String || boolean || integer/([DIALOG_VALUES.md](https://github.com/Alaricdragon/Starlords_Temp/tree/master/theManyReadmes/DIALOG_VALUES.md)) || JsonObject {min: integer/dialogValue,max: integer/dialogValue}
* "setMemoryData": jsonObject. sets memory key data. memory is held in the save file, and will remain forever. please keep in mind, chose your memorys dataID wisely. your dataID must start with '$', and should not be the same as ANY OTHER MEMEORY IN THE GAME. be carefull. if a jsonObject with a min/max is one of its lines, it acts as a increase to the value.
  * each line is "dataID" : String || boolean || integer/([DIALOG_VALUES.md](https://github.com/Alaricdragon/Starlords_Temp/tree/master/theManyReadmes/DIALOG_VALUES.md)) || JsonObject {min: integer/dialogValue,max: integer/dialogValue}
  * each line can also be: "dataID": {"time":Integer/([DIALOG_VALUES.md](https://github.com/Alaricdragon/Starlords_Temp/tree/master/theManyReadmes/DIALOG_VALUES.md)), "data": (the same data as a normal dataID)}. in this instance, the data will only last for the inputted 'time' of days, before being removed.
* "setLordMemoryData": jsonObject. sets memory key data, linked to the interacted starlord. memory is held in the save file, and will remain forever, or until the starlord dies. each starlords memory is stored in "$STARLORDS_LORD_ADDITIONAL_MEMORY_%lordID". (were lordID is the ID tied to the lord). so try not to grab that directly please. if a jsonObject with a min/max is one of its lines, it acts as a increase to the value.
  * each line is "dataID" : String || boolean || integer/([DIALOG_VALUES.md](https://github.com/Alaricdragon/Starlords_Temp/tree/master/theManyReadmes/DIALOG_VALUES.md)) || JsonObject {min: integer/dialogValue,max: integer/dialogValue}
  * each line can also be: "dataID": {"time":Integer/([DIALOG_VALUES.md](https://github.com/Alaricdragon/Starlords_Temp/tree/master/theManyReadmes/DIALOG_VALUES.md)), "data": (the same data as a normal dataID)}. in this instance, the data will only last for the inputted 'time' of days, before being removed.
* "setLordTags": jsonObject. were each item is 'tagName' : boolean. set to true to add a tag, false to remove it. tags will remain on a starlord until removed.
