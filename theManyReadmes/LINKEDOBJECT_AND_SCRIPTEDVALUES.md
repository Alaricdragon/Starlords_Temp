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
      * d0: the number of values this list holds
      * d10: the 'value' of this item in the list.
      * d11: the 'weight' of this value in the list
      * keep in mind, d10 and d11 can repeat themselves as many times as required.
      * examples: '~~D_LR:4:1:10:2:5' (10/15 1, 5/15 5). '~~D_LR:10:1:10:2:5:3:15:4:20:5:25' (10/85 1, 5/85 2, 15/85 3, 20/85 4, 25/85 5)
  * for String:
    * "S_LR": a list of values were one is selected based on weight. should look like: '~~S_LR:d0:S10:d11'
      * d0: the number of values this list holds
      * S10: the 'value' of this item in the list.
      * d11: the 'weight' of this value in the list
      * keep in mind, d10 and d11 can repeat themselves as many times as required.
      * examples: '~~S_LR:4:cat:10:bird:5' (10/15 cat, 5/15 bird). '~~S_LR:10:cat:10:bird:5:dog:15:bear:20:frog:25' (10/85 cat, 5/85 bird, 15/85 dog, 20/85 bear, 25/85 frog)
    * S_FactionPortrait: a path to a random portrait from the inputted faction. should look like: 'S_FactPort:s0:d1'
      * s0: the faction ID you want to get a portrait from.
      * d1: the 'gender'. set to 0 for any, set to 1 for male, set to 2 for female. rounds down.
  * for object:


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