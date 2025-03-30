package starlords.util.dialogControler;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.campaign.FactionManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.dialogControler.dialogRull.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DialogSet {
    //for getting dialog types with a search
    private static HashMap<String,DialogSet> dialogSets = new HashMap<>();
    //for getting organized dialog data
    private static ArrayList<ArrayList<DialogSet>> organizedDialogSets = new ArrayList<>();
    @SneakyThrows
    public static void setup(JSONObject jsonObject){
        dialogSets = new HashMap<>();
        organizedDialogSets = new ArrayList<>();
        for (Iterator it2 = jsonObject.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            new DialogSet(key2,jsonObject.getJSONObject(key2));
        }
    }
    public static String getLine(Lord lord,String id){
        for (LordDialogController a : lord.getTemplate().dialogOverride){
            if (a.canUseDialog(lord) && dialogSets.containsKey(a.dialogLink) && dialogSets.get(a.dialogLink).hasLine(id)){
                return dialogSets.get(a.dialogLink).getLine(id);
            }
        }
        for (int a = organizedDialogSets.size() - 1; a >= 0; a--){
            for (DialogSet b : organizedDialogSets.get(a)){
                if (b.canUseDialog(lord) && b.hasLine(id)){
                    return b.getLine(id);
                }
            }
        }
        return "ERROR: unable to get line of dialog. id of: "+id;
    }
    public static String getLineWithInserts(Lord lord,String id){
        String line = getLine(lord,id);
        return insertDefaltData(line,lord);
    }
    public static String insertData(String line, String mark, String replaced){
        StringBuilder out = new StringBuilder();
        if (markAtStart(line,mark)){
            out.append(replaced);
        }
        String[] lines = line.split(mark);
        for (String a : lines){
            out.append(a);
            out.append(replaced);
        }
        if (markAtEnd(line,mark)){
            out.append(replaced);
        }
        return out.toString();
    }

    public static String insertDefaltData(String line, Lord lord){
        //just repeat the 2 lines here with everything I want to get by default.
        /*
        -p0: player faction name
        -p1: player partner name
        -p2: player name
        -p3_0: player getManOrWoman
        -p3_1: player getHeOrShe
        -p3_2: player getHimOrHer
        -p3_3: player getHisOrHer
        -p3_4: player getGender().name
        -p4: player flagship????
        -p4: player fleet name???? do I need this? 'your fleet'???
        -p6:
        -p7:
        -p8:

        -l0: lord faction name
        -l1: lord partner name
        -l2: lord name
        -l3: lord gender
        -l4: lord fleet name
        -l5: lord flagship
         */
        line = getPlayerStringMods(line,lord);
        line = getLordStringMods(line,lord);

        return line;
    }
    private static String getPlayerStringMods(String line, Lord lord){
        String data = Global.getSector().getPlayerFaction().getDisplayName();
        line = insertData(line,"p0",data);

        data = "";//Global.getSector().getPlayerFaction().getDisplayName();
        line = insertData(line,"p1",data);

        data = Global.getSector().getPlayerPerson().getNameString();
        line = insertData(line,"p2",data);

        data = Global.getSector().getPlayerPerson().getManOrWoman();
        line = insertData(line,"p3_0",data);

        data = Global.getSector().getPlayerPerson().getHeOrShe();
        line = insertData(line,"p3_1",data);

        data = Global.getSector().getPlayerPerson().getHimOrHer();
        line = insertData(line,"p3_2",data);

        data = Global.getSector().getPlayerPerson().getHisOrHer();
        line = insertData(line,"p3_3",data);

        data = Global.getSector().getPlayerPerson().getGender().name();
        line = insertData(line,"p3_4",data);

        data = "nothing";//todo: move this, like everything else, into the strings file. for modality of different languages.
        if (Global.getSector().getPlayerFleet().getFlagship() != null) data = Global.getSector().getPlayerFleet().getFlagship().getHullSpec().getHullName();
        line = insertData(line,"p4",data);

        data = "nothing";//todo: move this, like everything else, into the strings file. for modality of different languages.
        if (Global.getSector().getPlayerFleet().getFlagship() != null) data = Global.getSector().getPlayerFleet().getFlagship().getShipName();
        line = insertData(line,"p5",data);

        return line;
    }
    private static String getLordStringMods(String line, Lord lord){
        String data = lord.getFaction().getDisplayName();
        line = insertData(line,"l0",data);

        data = Global.getSector().getFaction(lord.getTemplate().factionId).getDisplayName();//Global.getSector().getPlayerFaction().getDisplayName();
        line = insertData(line,"l1",data);

        data = lord.getLordAPI().getNameString();
        line = insertData(line,"l2",data);

        data = lord.getLordAPI().getManOrWoman();
        line = insertData(line,"l3_0",data);

        data = lord.getLordAPI().getHeOrShe();
        line = insertData(line,"l3_1",data);

        data = lord.getLordAPI().getHimOrHer();
        line = insertData(line,"l3_2",data);

        data = lord.getLordAPI().getHisOrHer();
        line = insertData(line,"l3_3",data);

        data = lord.getLordAPI().getGender().name();
        line = insertData(line,"l3_4",data);

        data = "nothing";//todo: move this, like everything else, into the strings file. for modality of different languages.
        if (lord.getFleet().getFlagship() != null) data = lord.getFleet().getFlagship().getHullSpec().getHullName();
        line = insertData(line,"l4",data);

        data = "nothing";//todo: move this, like everything else, into the strings file. for modality of different languages.
        if (lord.getFleet().getFlagship() != null) lord.getFleet().getFlagship().getShipName();
        line = insertData(line,"p5",data);
        return line;
    }

    private static boolean markAtStart(String line, String mark){
        for (int a = 0; a < mark.length(); a++){
            if (a > line.length()) return false;
            if (line.charAt(a) != mark.charAt(a)) return false;
        }
        return true;
    }
    private static boolean markAtEnd(String line, String mark){
        int b = line.length() - 1;
        for (int a = 0; a < mark.length(); a++){
            if (b < 0) return false;
            if (line.charAt(b) != mark.charAt(a)) return false;
            b--;
        }
        return true;
    }


    @Getter
    private HashMap<String,String> dialog = new HashMap<>();
    private ArrayList<DialogRule_Base> rules = new ArrayList();
    public DialogSet(String name,int priority){
        while (organizedDialogSets.size() < priority){
            organizedDialogSets.add(new ArrayList<>());
        }
        organizedDialogSets.get(priority).add(this);
        dialogSets.put(name,this);
    }
    @SneakyThrows
    public DialogSet(String name,JSONObject jsonObject){
        int priority = 2;

        //add conditions
        if (jsonObject.has("rules")){
            JSONObject rulesTemp = jsonObject.getJSONObject("rules");
            rules = getDialogFromJSon(rulesTemp);
        }
        //add lines
        if (jsonObject.has("lines")){
            JSONObject lines = jsonObject.getJSONObject("lines");
            for (Iterator it = lines.keys(); it.hasNext();) {
                String key = (String) it.next();
                addLine(key,lines.getString(key));
            }
        }

        if (jsonObject.has("priority")) priority = jsonObject.getInt("priority");
        while (organizedDialogSets.size() <= priority){
            organizedDialogSets.add(new ArrayList<>());
        }
        organizedDialogSets.get(priority).add(this);
        dialogSets.put(name,this);
    }
    public boolean canUseDialog(Lord lord){
        for (DialogRule_Base a : rules){
            if (!a.condition(lord)) return false;
        }
        return true;
    }
    public void addLine(String key,String line){
        dialog.put(key,line);
    }
    public String getLine(String key){
        return dialog.get(key);
    }
    public boolean hasLine(String key){
        return dialog.containsKey(key);
    }






    public static ArrayList<DialogRule_Base> getDialogFromJSon(JSONObject rulesTemp){
        ArrayList<DialogRule_Base> rules = new ArrayList<>();
        for (Iterator it = rulesTemp.keys(); it.hasNext();) {
            String key = (String) it.next();
            switch (key){
                //have the different rule sets here.
                case "relationWithPlayer":
                    rules.add(addRule_relationWithPlayer(rulesTemp,key));
                    break;
                case "startingFaction":
                    rules.addAll(addRule_startingFaction(rulesTemp,key));
                    break;
                case "currentFaction":
                    rules.addAll(addRule_currentFaction(rulesTemp,key));
                    break;
                case "isMarriedToPlayer":
                    rules.add(addRule_isMarriedToPlayer(rulesTemp,key));
                    break;
                case "willEngage":
                    rules.add(addRule_willEngage(rulesTemp,key));
                    break;
                case "hostileFaction":
                    rules.add(addRule_hostileFaction(rulesTemp,key));
                    break;
                case "isAtFeast":
                    rules.add(addRule_isAtFeast(rulesTemp,key));
                    break;
            }
        }
        return rules;
    }
    @SneakyThrows
    private static DialogRule_Base addRule_relationWithPlayer(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_relationWithPlayer(json2);
    }
    @SneakyThrows
    private static ArrayList<DialogRule_Base> addRule_startingFaction(JSONObject json,String key){
        ArrayList<DialogRule_Base> rules = new ArrayList<>();
        ArrayList<String> whiteList = new ArrayList<>();
        ArrayList<String> blackList = new ArrayList<>();
        JSONObject ruleAdded = json.getJSONObject(key);
        for (Iterator it2 = ruleAdded.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            if (ruleAdded.getBoolean(key2)){
                whiteList.add(key2);
                continue;
            }
            blackList.add(key2);
        }
        if (whiteList.size() != 0) rules.add(new DialogRule_startingFaction_whitelist(whiteList));
        if (blackList.size() != 0) rules.add(new DialogRule_startingFaction_blacklist(blackList));
        return rules;
    }
    @SneakyThrows
    private static ArrayList<DialogRule_Base> addRule_currentFaction(JSONObject json,String key){
        ArrayList<DialogRule_Base> rules = new ArrayList<>();
        ArrayList<String> whiteList = new ArrayList<>();
        ArrayList<String> blackList = new ArrayList<>();
        JSONObject ruleAdded = json.getJSONObject(key);
        for (Iterator it2 = ruleAdded.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            if (ruleAdded.getBoolean(key2)){
                whiteList.add(key2);
                continue;
            }
            blackList.add(key2);
        }
        if (whiteList.size() != 0) rules.add(new DialogRule_currentFaction_whitelist(whiteList));
        if (blackList.size() != 0) rules.add(new DialogRule_currentFaction_blacklist(blackList));
        return rules;
    }
    @SneakyThrows
    private static DialogRule_Base addRule_isMarriedToPlayer(JSONObject json, String key){
        return new DialogRule_isMarriedToPlayer(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_willEngage(JSONObject json, String key){
        return new DialogRule_willEngage(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_hostileFaction(JSONObject json, String key){
        return new DialogRule_hostileFaction(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_isAtFeast(JSONObject json, String key){
        return new DialogRule_isAtFeast(json.getBoolean(key));
    }
}
