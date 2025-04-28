* "dialogValue": jsonObject. a 'dialogValue' is something that is only called by certain functions, and is basically a inputed value. you can add the following functions to this object:
    * "base" : Intiger. is a static number added to the dialogValues output.
    * "multi" : double. is a multiplayer for the dialogs value final output.
    * please note: every following 'value' can hold the following data: "base" and "multi". the eq used is: (valueOfFunction + base)*multi.
    * "relationWithPlayer": multi. is the relation the lord has to the player.
    * "lordLoyalty": multi. is the relation the lord has with there faction.
    * "lordLoyaltyToPlayerLord": multi. is the relation the lord has with whatever faction the player belongs to.
    * "playerWealth": multi. is the number of credits the player has
    * "lordWealth": multi. is the number of credits the lord has
    * "playerLevel": multi. is the level of the player
    * "lordLevel": multi. is the level of the lord
    * "playerFleetDP": multi. is the total fleet DP of the players fleet
    * "lordFleetDP": multi. is the total fleet DP of the lords fleet.
    * "playerRank": multi. is the rank of the player
    * "lordRank": multi. is the rank of the lord
    * "playerLordRomanceAction": multi. is the number of romantic actions the lord and player have had
    * "lordsInFeast": multi. is the number of lords at the current feast (0 if the interacting lord is not at a feast)
    * "lordProposalSupporters": multi. is the number of lords supporting the interacting lords current proposal.
    * "lordProposalOpposers": multi. is the number of lords opposing the interacting lords current proposal.
    * "playerProposalSupporters": multi. is the number of lords supporting the players current proposal.
    * "playerProposalOpposers": multi. is the number of lords opposing the player current proposal.
    * "curProposalSupporters": multi. is the number of lords supporting the current proposal.
    * "curProposalOpposers": multi. is the number of lords supporting the current proposal.
    * "optionOfCurrProposal": multi. is the lords option of the current proposal in the lords faction counsel
    * "optionOfPlayerProposal": multi. is the lords option of the players current proposal.
    * "playerMarketNumbers": multi. is the number of markets the player's faction has.
    * "lordMarketNumbers": multi. is the number of markets the lords faction has.
    * "playerCommissionedMarketNumbers": multi. is the number of markets the faction the player is working for has. (or the players' faction if none)
    * "playerMarketAverageStability": multi. is the average stability of markets the player's faction has.
    * "lordMarketAverageStability": multi. is the average stability of markets the lords faction has.
    * "playerCommissionedMarketAverageStability": multi. is the average stability of markets the faction the player is working for has. (or the players' faction if none)
    * "validLordNumbers": "base":int, "multi":double, "rules":rule JsonObject. what this does is it looks at all starlords in the game, and the number of lords that meet all "rule" requirements.
    * "validLordNumbers": jsonArray. this jsonArray holds a list of 'validLordNumbers' objects. it then returns the sum of all vailidLordNumbers.
    * "limitedValue": {"base":int,"multi":double, "value":int || dialogvalue, "min": int || dialogvalue ,"max": int || dialogvalue} how this works, is it forces the inputed 'value' to be between the min and max values. this limitiing is done before min and base values are added. by default, min and max are infinity.
    * "limitedValue": jsonArray. this jsonArray holds a list of 'limitValue' objects. it then returns the sum of all limit values.
    * "conditionalValue": "base":int, "dialogValue": dialogvalue JsonObject, "multi":double, "rules":rule JsonObject. this runs the inputed 'rules' json object, and if the requirements are met, it will output its value.
    * "conditionalValue": jsonArray. this jsonArray holds a list of 'conditionalValue' objects. it then returns the sum of all the conditionalValues.
    * "DialogData": jsonObject, were each object is structured as "dialogID":{"base":int, "multi":double}. if the linked dialog data is not a int, it will be ignored
    * "MemoryData": jsonObject, were each object is structured as "MemoryID":{"base":int, "multi":double}. if the linked memoryID is not a int, it will be ignored (keep in mind: this can get any data in starsectors memory. data ID must start with '$' or it might break something)
    * "LordMemoryData": jsonObject, were each object is structured as "lordMemoryID":{"base":int, "multi":double}. if the linked lord data is not a int, it will be ignored
    * "random": int || dialogValue || {"min": int || dialogValue, "max": int || dialogValue}. adds a random value ether between 0 and the inputed value, or between the min and max inputed values.
