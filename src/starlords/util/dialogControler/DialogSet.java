package starlords.util.dialogControler;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.controllers.EventController;
import starlords.controllers.LordController;
import starlords.controllers.PoliticsController;
import starlords.faction.LawProposal;
import starlords.lunaSettings.StoredSettings;
import starlords.person.Lord;
import starlords.util.Constants;
import starlords.util.GenderUtils;
import starlords.util.dialogControler.dialogRull.*;
import starlords.util.dialogControler.dialog_addon.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DialogSet {
    //for getting dialog types with a search
    private static HashMap<String,DialogSet> dialogSets = new HashMap<>();
    //for getting organized dialog data
    private static ArrayList<ArrayList<DialogSet>> organizedDialogSets = new ArrayList<>();
    //for getting the default option set of any given line.
    private static HashMap<String,String> defaltOptionSets = new HashMap<>();
    @SneakyThrows
    public static void setup(){
        setupA();
        setupB();
    }
    @SneakyThrows
    private static void setupA(){
        JSONObject jsonObject = Global.getSettings().getMergedJSONForMod("data/lords/dialog.json", Constants.MOD_ID);
        dialogSets = new HashMap<>();
        organizedDialogSets = new ArrayList<>();
        for (Iterator it2 = jsonObject.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            new DialogSet(key2,jsonObject.getJSONObject(key2));
        }
    }
    @SneakyThrows
    private static void setupB(){
        JSONObject jsonObject = Global.getSettings().getMergedJSONForMod("data/lords/default_dialog_options.json",Constants.MOD_ID);
        defaltOptionSets = new HashMap<>();
        for (Iterator it2 = jsonObject.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            defaltOptionSets.put(key2,jsonObject.getString(key2));
        }
    }
    public static void addParaWithInserts(String key, Lord lord, TextPanelAPI textPanel,OptionPanelAPI options, InteractionDialogAPI dialog){
        //DialogSet.addParaWithInserts("ERROR",targetLord,textPanel,options);
        //DialogSet.addOptionWithInserts("ERROR",null,null,targetLord,textPanel,options);
        //addParaWithInserts(key, lord, textPanel, options,new HashMap<String,String>());
        addParaWithInserts(key, lord, textPanel, options,dialog,false);
    }
    public static void addParaWithInserts(String key, Lord lord, TextPanelAPI textPanel,OptionPanelAPI options, InteractionDialogAPI dialog,boolean forceHide){
        //DialogSet.addParaWithInserts("ERROR",targetLord,textPanel,options);
        //DialogSet.addOptionWithInserts("ERROR",null,null,targetLord,textPanel,options);
        addParaWithInserts(key, lord, textPanel, options,dialog,forceHide,new HashMap<String,String>());
    }
    public static void addParaWithInserts(String key, Lord lord, TextPanelAPI textPanel,OptionPanelAPI options, InteractionDialogAPI dialog,boolean forceHide,HashMap<String,String> markersReplaced) {
        DialogSet set = getSet(lord, key);
        if (set == null) {
            Logger log = Global.getLogger(DialogSet.class);
            log.info("ERROR: FAILED TO GET DIALOG SET OF KEY: "+key);
            return;
        }
        set.applyLine(key, lord, textPanel, options,dialog,forceHide,markersReplaced);
    }
    @Deprecated
    public static void addParaWithInserts(String key, Lord lord, TextPanelAPI textPanel, OptionPanelAPI options,HashMap<String,String> markersReplaced){
        DialogSet set = getSet(lord, key);
        if (set == null) return;
        //set.applyLine(key, lord, textPanel, options,false,markersReplaced);
    }
    @Deprecated
    public static void addParaWithInserts(String key, Lord lord, TextPanelAPI textPanel,OptionPanelAPI options){
        DialogSet set = getSet(lord, key);
        if (set == null) return;
        //set.applyLine(key, lord, textPanel, options,false,markersReplaced);
    }
    public static void addOptionWithInserts(String key,String tooltipKey, Object optionData, InteractionDialogAPI dialog, Lord lord, TextPanelAPI textPanel, OptionPanelAPI options){
        //addOptionWithInserts(key,tooltipKey, optionData,lord, textPanel,options,new HashMap<>());
        addOptionWithInserts(key,lord, textPanel,options,dialog,new HashMap<String,String>());
    }
    public static void addOptionWithInserts(String key, Lord lord, TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, HashMap<String,String> markersReplaced){
        DialogSet set = getSet(lord, key);
        if (set == null) return;
        set.applyOption(key,  lord,  textPanel,  options,dialog, markersReplaced);
    }
    @Deprecated
    public static void addOptionWithInserts(String key, String tooltipKey, Object optionData, Lord lord, TextPanelAPI textPanel, OptionPanelAPI options){
        DialogSet set = getSet(lord, key);
        if (set == null) return;
        //set.applyOptionOLD(key,tooltipKey, lord, textPanel, optionData, options,markersReplaced);
    }
    @Deprecated
    public static void addOptionWithInserts(String key, Lord lord, TextPanelAPI textPanel, OptionPanelAPI options, HashMap<String,String> markersReplaced){
        DialogSet set = getSet(lord, key);
        if (set == null) return;
        //set.applyOption(key,  lord,  textPanel,  options,dialog, markersReplaced);
    }
    @Deprecated
    public static void addOptionWithInserts(String key, Lord lord, TextPanelAPI textPanel, OptionPanelAPI options){
        DialogSet set = getSet(lord, key);
        if (set == null) return;
        //set.applyOption(key,  lord,  textPanel,  options,dialog, markersReplaced);
    }

    private static DialogSet getSet(Lord lord,String id){
        for (LordDialogController a : lord.getTemplate().dialogOverride){
            if (a.canUseDialog(lord) && dialogSets.containsKey(a.dialogLink) && dialogSets.get(a.dialogLink).hasLine(id)){
                return dialogSets.get(a.dialogLink);
            }
        }
        for (int a = organizedDialogSets.size() - 1; a >= 0; a--){
            for (DialogSet b : organizedDialogSets.get(a)){
                if (b.canUseDialog(lord) && b.hasLine(id)){
                    return b;
                }
            }
        }
        return null;
    }
    public static String getLine(Lord lord,String id){
        DialogSet set = getSet(lord, id);
        if (set != null) return set.getLine(id);
        return "ERROR: unable to get line of dialog. id of: "+id;
    }
    public static String getLineWithInserts(Lord lord,String id){
        String line = getLine(lord,id);
        return insertDefaltData(line,lord);
    }
    public static String insertData(String line, String mark, String replaced){
        /*StringBuilder out = new StringBuilder();
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
        }*/
        return line.replaceAll(mark,replaced);
    }
    public static String insertAdditionalData(String line,HashMap<String,String> markersReplaced){
        for (String a : markersReplaced.keySet()){
            line = insertData(line,a,markersReplaced.get(a));
        }
        return line;
    }
    public static String insertDefaltData(String line, Lord lord){
        line = getPlayerStringMods(line,lord);
        line = getPlayerPartnerStringMods(line,lord);

        line = getLordStringMods(line,lord);
        line = getLordPartnerStringMods(line,lord);

        line = getHostLordStringMods(line,lord);
        line = getHostLordPartnerStringMods(line,lord);

        line = getWeddingTargetLordStringMods(line,lord);
        line = getWeddingTargetPartnerStringMods(line,lord);
        return line;
    }

    private static String getPlayerStringMods(String line, Lord lord){
        String data = Global.getSector().getPlayerFaction().getDisplayName();
        line = insertData(line,"%PLAYER_FACTION_NAME",data);

        data = Global.getSector().getPlayerPerson().getNameString();
        line = insertData(line,"%PLAYER_NAME",data);

        data = Global.getSector().getPlayerPerson().getName().getFirst();
        line = insertData(line,"%PLAYER_NAME_FIRST",data);

        data = Global.getSector().getPlayerPerson().getName().getLast();
        line = insertData(line,"%PLAYER_NAME_LAST",data);

        data = Global.getSector().getPlayerPerson().getManOrWoman();
        line = insertData(line,"%PLAYER_GENDER_MAN_OR_WOMEN",data);

        data = Global.getSector().getPlayerPerson().getHeOrShe();
        line = insertData(line,"%PLAYER_GENDER_HE_OR_SHE",data);

        data = Global.getSector().getPlayerPerson().getHimOrHer();
        line = insertData(line,"%PLAYER_GENDER_HIM_OR_HER",data);

        data = Global.getSector().getPlayerPerson().getHisOrHer();
        line = insertData(line,"%PLAYER_GENDER_HIS_OR_HER",data);

        data = LordController.getPlayerLord().getFormalWear();
        line = insertData(line,"%PLAYER_GENDER_SUIT_OR_DRESS",data);

        data = Global.getSector().getPlayerPerson().getGender().name();
        line = insertData(line,"%PLAYER_GENDER_NAME",data);

        data = Global.getSector().getPlayerPerson().getGender().name();
        line = insertData(line,"%PLAYER_GENDER_NAME",data);

        FleetMemberAPI flownShip = getFlownShip(Global.getSector().getPlayerPerson(),Global.getSector().getPlayerFleet());
        data = "nothing";//todo: move this, like everything else, into the strings file. for modality of different languages.
        if (flownShip != null) data = flownShip.getHullSpec().getHullName();
        line = insertData(line,"%PLAYER_FLAGSHIP_HULLNAME",data);

        data = "nothing";//todo: move this, like everything else, into the strings file. for modality of different languages.
        if (flownShip != null) data = flownShip.getShipName();
        line = insertData(line,"%PLAYER_FLAGSHIP_NAME",data);

        data = GenderUtils.husbandOrWife(Global.getSector().getPlayerPerson(), false);
        line = insertData(line,"%PLAYER_GENDER_HUSBAND_OR_WIFE",data);

        data = "nothing";
        LawProposal lordProposal = PoliticsController.getProposal(lord);
        if (lordProposal != null) data = lordProposal.getTitle();
        line = insertData(line,"%PLAYER_PROPOSAL_NAME",data);
        return line;
    }
    private static String getPlayerPartnerStringMods(String line, Lord lord){
        if (LordController.getPlayerLord().getSpouse() != null) return get_StringMods(line,LordController.getLordById(LordController.getPlayerLord().getSpouse()),"PLAYER_SPOUSE_");
        return getPlayerNoPartnerStringMods(line,lord);
    }
    private static String getPlayerNoPartnerStringMods(String line, Lord lord){
        String data = Global.getSector().getPlayerFaction().getDisplayName();
        line = insertData(line,"%PLAYER_SPOUSE_FACTION_NAME",data);

        data = Global.getSector().getPlayerPerson().getNameString();
        line = insertData(line,"%PLAYER_SPOUSE_NAME",data);

        data = Global.getSector().getPlayerPerson().getName().getFirst();
        line = insertData(line,"%PLAYER_SPOUSE_NAME_FIRST",data);

        data = Global.getSector().getPlayerPerson().getName().getLast();
        line = insertData(line,"%PLAYER_SPOUSE_NAME_LAST",data);

        data = Global.getSector().getPlayerPerson().getManOrWoman();
        line = insertData(line,"%PLAYER_SPOUSE_GENDER_MAN_OR_WOMEN",data);

        data = Global.getSector().getPlayerPerson().getHeOrShe();
        line = insertData(line,"%PLAYER_SPOUSE_GENDER_HE_OR_SHE",data);

        data = Global.getSector().getPlayerPerson().getHimOrHer();
        line = insertData(line,"%PLAYER_SPOUSE_GENDER_HIM_OR_HER",data);

        data = Global.getSector().getPlayerPerson().getHisOrHer();
        line = insertData(line,"%PLAYER_SPOUSE_GENDER_HIS_OR_HER",data);

        data = LordController.getPlayerLord().getFormalWear();
        line = insertData(line,"%PLAYER_SPOUSE_GENDER_SUIT_OR_DRESS",data);

        data = Global.getSector().getPlayerPerson().getGender().name();
        line = insertData(line,"%PLAYER_SPOUSE_GENDER_NAME",data);

        FleetMemberAPI flownShip = getFlownShip(Global.getSector().getPlayerPerson(),Global.getSector().getPlayerFleet());
        data = "nothing";//todo: move this, like everything else, into the strings file. for modality of different languages.
        if (flownShip != null) data = flownShip.getHullSpec().getHullName();
        line = insertData(line,"%PLAYER_SPOUSE_FLAGSHIP_HULLNAME",data);

        data = "nothing";//todo: move this, like everything else, into the strings file. for modality of different languages.
        if (flownShip != null) data = flownShip.getShipName();
        line = insertData(line,"%PLAYER_SPOUSE_FLAGSHIP_NAME",data);

        data = GenderUtils.husbandOrWife(Global.getSector().getPlayerPerson(), false);
        line = insertData(line,"%PLAYER_SPOUSE_GENDER_HUSBAND_OR_WIFE",data);

        data = "nothing";
        LawProposal lordProposal = PoliticsController.getProposal(lord);
        if (lordProposal != null) data = lordProposal.getTitle();
        line = insertData(line,"%PLAYER_SPOUSE_PROPOSAL_NAME",data);
        return line;
    }
    private static String getLordStringMods(String line, Lord lord){
        return get_StringMods(line,lord,"LORD_");
    }
    private static String getLordPartnerStringMods(String line, Lord lord){
        if (lord.getSpouse() != null) lord = LordController.getLordById(lord.getSpouse());
        return get_StringMods(line,lord,"LORD_SPOUSE_");
    }
    private static String getHostLordStringMods(String line, Lord lord){
        boolean check = EventController.getCurrentFeast(lord.getLordAPI().getFaction()) != null && EventController.getCurrentFeast(lord.getLordAPI().getFaction()).getOriginator() != null;
        if (check){
            Lord temp = EventController.getCurrentFeast(lord.getLordAPI().getFaction()).getOriginator();
            if (temp != null) lord = temp;
        }
        return get_StringMods(line,lord,"LORD_HOST_");
    }
    private static String getHostLordPartnerStringMods(String line, Lord lord){
        boolean check = EventController.getCurrentFeast(lord.getLordAPI().getFaction()) != null && EventController.getCurrentFeast(lord.getLordAPI().getFaction()).getOriginator() != null;
        if (check){
            Lord temp = EventController.getCurrentFeast(lord.getLordAPI().getFaction()).getOriginator();
            if (temp != null) {
                lord = temp;
                if (lord.getSpouse() != null) lord = LordController.getLordById(lord.getSpouse());
            }
            return get_StringMods(line,lord,"LORD_HOST_SPOUSE_");
        }
        return get_StringMods(line,lord,"LORD_HOST_SPOUSE_");
    }
    private static String getWeddingTargetLordStringMods(String line, Lord lord){
        boolean check = EventController.getCurrentFeast(lord.getLordAPI().getFaction()) != null;
        if (check){
            Lord temp = EventController.getCurrentFeast(lord.getLordAPI().getFaction()).getWeddingCeremonyTarget();
            if (temp != null) lord = temp;
        }
        return get_StringMods(line,lord,"WEDDING_TARGET");
    }
    private static String getWeddingTargetPartnerStringMods(String line, Lord lord){
        boolean check = EventController.getCurrentFeast(lord.getLordAPI().getFaction()) != null;
        if (check){
            Lord temp = EventController.getCurrentFeast(lord.getLordAPI().getFaction()).getWeddingCeremonyTarget();
            if (temp != null){
                lord = temp;
                if (lord.getSpouse() != null){
                    lord = LordController.getLordById(lord.getSpouse());
                }
            }
        }
        return get_StringMods(line,lord,"WEDDING_TARGET_SPOUSE");
    }


    private static String get_StringMods(String line, Lord lord, String key){
        String data = lord.getFaction().getDisplayName();
        line = insertData(line,"%"+key+"FACTION_NAME",data);

        data = Global.getSector().getFaction(lord.getTemplate().factionId).getDisplayName();//Global.getSector().getPlayerFaction().getDisplayName();
        line = insertData(line,"%"+key+"STARTING_FACTION_NAME",data);

        data = lord.getLordAPI().getNameString();
        line = insertData(line,"%"+key+"NAME",data);

        data = Global.getSector().getPlayerPerson().getName().getFirst();
        line = insertData(line,"%"+key+"NAME_FIRST",data);

        data = Global.getSector().getPlayerPerson().getName().getLast();
        line = insertData(line,"%"+key+"NAME_LAST",data);

        data = lord.getLordAPI().getManOrWoman();
        line = insertData(line,"%"+key+"GENDER_MAN_OR_WOMEN",data);

        data = lord.getLordAPI().getHeOrShe();
        line = insertData(line,"%"+key+"GENDER_HE_OR_SHE",data);

        data = lord.getLordAPI().getHimOrHer();
        line = insertData(line,"%"+key+"GENDER_HIM_OR_HER",data);

        data = lord.getLordAPI().getHisOrHer();
        line = insertData(line,"%"+key+"GENDER_HIS_OR_HER",data);

        data = lord.getFormalWear();
        line = insertData(line,"%"+key+"GENDER_SUIT_OR_DRESS",data);

        data = lord.getLordAPI().getGender().name();
        line = insertData(line,"%"+key+"GENDER_NAME",data);

        FleetMemberAPI flownShip = getFlownShip(lord.getLordAPI(),lord.getFleet());
        data = "nothing";//todo: move this, like everything else, into the strings file. for modality of different languages.
        if (flownShip != null) data = flownShip.getHullSpec().getHullName();
        line = insertData(line,"%"+key+"FLAGSHIP_HULLNAME",data);

        data = "nothing";//todo: move this, like everything else, into the strings file. for modality of different languages.
        if (flownShip != null) data = flownShip.getShipName();
        line = insertData(line,"%"+key+"FLAGSHIP_NAME",data);

        data = GenderUtils.husbandOrWife(lord.getLordAPI(), false);
        line = insertData(line,"%"+key+"GENDER_HUSBAND_OR_WIFE",data);

        data = "nothing";
        LawProposal lordProposal = PoliticsController.getProposal(lord);
        if (lordProposal != null) data = lordProposal.getTitle();
        line = insertData(line,"%"+key+"PROPOSAL_NAME",data);
        return line;
    }

    private static FleetMemberAPI getFlownShip(PersonAPI lord, CampaignFleetAPI fleet){
        FleetMemberAPI output = fleet.getFlagship();
        if (fleet.getFlagship().getCaptain().getId().equals(lord.getId())) return output;
        for (FleetMemberAPI a : fleet.getFleetData().getMembersListCopy()){
            if (a.getCaptain() != null && a.getCaptain().getId().equals(lord.getId())){
                return a;
            }
        }
        return null;
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
        for (int a = mark.length(); a >= 0; a--){
            if (b < 0) return false;
            if (line.charAt(b) != mark.charAt(a)) return false;
            b--;
        }
        return true;
    }

    public static ArrayList<Object> getOrganizedReplacedMarkers(String line, String[] markers, String[] replacedData){
        //this was built with the objective of working with the games base highlight system, allowing flexible marker positions with highlights.
        //it was NEVER TESTED. at all. but I put a lot of work into it, so I will keep it just in case.
        String replacedMarker= "%s";
        ArrayList<Object> out = new ArrayList<>();

        // get positions of each marker in line. gets an array to 'out' for each different marker.
        ArrayList<ArrayList<Integer>> stringPos = getPositionOfMarkers(line,markers);

        // organizes the markers into there positions in the inputted line.
        int strings=0;
        for (ArrayList<Integer> a : stringPos){
            strings+=a.size();
        }
        String[] markerOutput = new String[strings];
        int[] id = new int[strings];
        int a = 0;
        while(a < strings){
            int b = 9999999;
            int id2 = 0;
            for (int c = 0; c < stringPos.size(); c++){
                ArrayList<Integer> d = stringPos.get(c);
                if (d.get(id[c]) < b){
                    b = id[c];
                    id2 = c;
                }
            }
            markerOutput[a] = replacedData[id2];
            a++;
        }

        //modify the primary string, so its markers are replaced with modified markers.
        for (int b = 0; b < markers.length; b++){
            line = insertData(line,markers[b],replacedMarker);
        }
        out.add(line);
        out.add(markerOutput);
        return out;
    }
    private static ArrayList<ArrayList<Integer>> getPositionOfMarkers(String line, String[] markers){
        ArrayList<ArrayList<Integer>> out = new ArrayList<>();

        for (String mark : markers) {
            ArrayList<Integer> out2 = new ArrayList<>();
            if (markAtStart(line, mark)) {
                out2.add(0);
            }
            String[] lines = line.split(mark);
            int pos = 0;
            for (int a = 0; a < lines.length; a++) {
                pos += lines.length;
                out2.add(pos);
            }
            if (markAtEnd(line, mark)) {
                out2.add(line.length()-1);
            }
            out.add(out2);
        }


        return out;
    }


    @Getter
    private HashMap<String,String> dialog = new HashMap<>();
    private HashMap<String,String> dialogOptionData = new HashMap<>();
    private HashMap<String,String> hint = new HashMap<>();
    private HashMap<String,String> shotcut = new HashMap<>();
    private ArrayList<DialogRule_Base> rules = new ArrayList();
    private HashMap<String,ArrayList<DialogRule_Base>> hide = new HashMap<>();
    private HashMap<String,ArrayList<DialogRule_Base>> enable = new HashMap<>();
    private HashMap<String,ArrayList<String>> additionalOptions = new HashMap<>();
    private HashMap<String,ArrayList<DialogAddon_Base>> addons = new HashMap<>();
    private HashMap<String, Color> colorOverride = new HashMap<>();
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
                boolean isObject = true;
                try{
                    lines.getJSONObject(key);
                }catch (Exception e){
                    isObject = false;
                }
                if (isObject){
                    getLineAsObjectFromJSon(key,lines.getJSONObject(key));
                    continue;
                }
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
    public void applyLine(String key, Lord lord, TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, boolean forceHide, HashMap<String,String> markersReplaced){
        Logger log = Global.getLogger(StoredSettings.class);
        //apply a paragraph of text for the current line
        if (shouldHide(key,textPanel,options,lord)) return;
        String line = this.getLine(key);
        if (line != null && !line.equals("") && !shouldHide(key, textPanel, options, lord) && !forceHide) {
            line = insertDefaltData(line, lord);
            line = insertAdditionalData(line, markersReplaced);
            if (colorOverride.containsKey(key)) {
                textPanel.addPara(line, colorOverride.get(key));
            } else {
                textPanel.addPara(line);
            }
        }
        //add on all addons.
        if (addons.containsKey(key)){
            for (DialogAddon_Base a : addons.get(key)){
                a.apply(textPanel,options,dialog,lord);
            }
        }
        if (additionalOptions.containsKey(key)){
            for (String a : additionalOptions.get(key)){
                addOptionWithInserts(a, lord, textPanel, options,dialog,markersReplaced);
            }
        }
        //add on all options
        log.info("attempting to add options from line" + key);
        boolean builtOptions = false;
        if (additionalOptions.containsKey(key)){
            log.info("ADDITIONAL OPTIONS: adding options from key of: " + key);
            if (additionalOptions.get(key).size() != 0){
                options.clearOptions();
                builtOptions = true;
            }
            for (String a : additionalOptions.get(key)){
                log.info("  ADDITIONAL OPTIONS: a single option of key: "+a);
                addOptionWithInserts(a,lord,textPanel,options,dialog,markersReplaced);
            }
        }else if (defaltOptionSets.containsKey(key)){
            log.info("getting default options from, to key: "+key+", "+defaltOptionSets.get(key));
            addParaWithInserts(defaltOptionSets.get(key),lord,textPanel,options,dialog,false,markersReplaced);
            builtOptions = true;
        }
        if (!builtOptions){
            log.info("got no options from key of: "+key);
            //???????? unknown process.
            //what I would attempt to do here is run the optionData of the line. effectively, just changing lines directly after. this can be done with chained lines though?
        }
    }
    public void applyOption(String key, Lord lord, TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog,HashMap<String,String> markersReplaced){
        DialogOption optionData = new DialogOption(dialogOptionData.get(key),addons.get(key));
        applyOption(key,lord,textPanel,optionData,options,dialog,markersReplaced);
    }
    public void applyOption(String key, Lord lord, TextPanelAPI textPanel,Object optionData,OptionPanelAPI options, InteractionDialogAPI dialog,HashMap<String,String> markersReplaced){
        Logger log = Global.getLogger(StoredSettings.class);
        String line = this.getLine(key);
        if (!shouldHide(key, textPanel, options, lord) && line != null) {
            line = insertDefaltData(line, lord);
            line = insertAdditionalData(line, markersReplaced);
            if (hint.containsKey(key)) {
                String line2 = hint.get(key);
                line2 = insertDefaltData(line2, lord);
                line2 = insertAdditionalData(line2, markersReplaced);
                if (colorOverride.containsKey(key)) {
                    options.addOption(line, optionData, colorOverride.get(key), line2);
                } else {
                    options.addOption(line, optionData, line2);
                }
                //is this even required????
                options.setTooltip(optionData, line2);
            } else {
                if (colorOverride.containsKey(key)) {
                    options.addOption(line, optionData, colorOverride.get(key), "");
                } else {
                    options.addOption(line, optionData);
                }
            }
            if (shotcut.containsKey(key)) {
                switch (shotcut.get(key)) {
                    case "ESCAPE":
                        options.setShortcut(optionData, 1, false, false, false, false);
                        break;
                    case "CONTROL":
                        //I am unsure how to do this, tbh.
                        break;
                    case "ALT":
                        break;
                    case "SHIFT":
                        break;
                }
            }
            if (enable.containsKey(key) && !shouldEnable(key,textPanel,options,lord)) options.setEnabled(optionData,false);
        }
        //log.info("attempting to add options from options" + key);
        boolean builtOptions = false;
        if (additionalOptions.containsKey(key)) {
            //log.info("ADDITIONAL OPTIONS: adding options from key of: " + key);
            if (additionalOptions.get(key).size() != 0) {
                options.clearOptions();
                builtOptions = true;
            }
            for (String a : additionalOptions.get(key)) {
                //log.info("  ADDITIONAL OPTIONS: a single option of key: " + a);
                addOptionWithInserts(a, lord, textPanel, options, dialog, markersReplaced);
            }
        } else if (defaltOptionSets.containsKey(key)) {
            //log.info("getting default options from, to key: " + key + ", " + defaltOptionSets.get(key));
            addParaWithInserts(defaltOptionSets.get(key), lord, textPanel, options, dialog, false, markersReplaced);
            builtOptions = true;
        }
        if (!builtOptions) {
            //log.info("got no options from key of: " + key);
            //???????? unknown process.
        //what I would attempt to do here is run the optionData of the line. effectively, just changing lines directly after. this can be done with chained lines though?
        }

    }
    public void applyOptionOLD(String key,String hintKey, Lord lord, TextPanelAPI textPanel, Object optionData, OptionPanelAPI options,HashMap<String,String> markersReplaced){
        String line = this.getLine(key);
        line = insertDefaltData(line,lord);
        line = insertAdditionalData(line,markersReplaced);

        if (hintKey != null){
            String line2 = this.getLine(hintKey);
            line2 = insertDefaltData(line2,lord);
            line2 = insertAdditionalData(line2,markersReplaced);
            if (colorOverride.containsKey(key)) {
                options.addOption(line, optionData,colorOverride.get(key), line2);
            }else{
                options.addOption(line, optionData, line2);
            }
            //is this even required????
            options.setTooltip(optionData,line2);
        }else{
            if (colorOverride.containsKey(key)) {
                options.addOption(line, optionData,colorOverride.get(key),"");
            }else{
                options.addOption(line, optionData);
            }
        }

    }
    private boolean shouldHide(String key,TextPanelAPI textPanel,OptionPanelAPI options,Lord lord){
        if (!hide.containsKey(key) || hide.get(key).size() == 0) return false;
        for (DialogRule_Base a : hide.get(key)){
            if (!a.condition(lord)) return true;
        }
        return false;
    }
    private boolean shouldEnable(String key,TextPanelAPI textPanel,OptionPanelAPI options,Lord lord){
        for (DialogRule_Base a : enable.get(key)){
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

    @SneakyThrows
    public void getLineAsObjectFromJSon(String key, JSONObject line) {
        if (line.has("line")) {
            addLine(key, line.getString("line"));
        }else{
            addLine(key,null);
        }
        if (line.has("addons")) {
            JSONObject addons = line.getJSONObject("addons");
            ArrayList<DialogAddon_Base> newAddons = new ArrayList<DialogAddon_Base>();
            for (Iterator it = addons.keys(); it.hasNext(); ) {
                String key2 = (String) it.next();
                DialogAddon_Base addon = null;
                switch (key2) {
                    case "repIncrease":
                        addon = addAddon_repIncrease(addons, key2);
                        break;
                    case "repDecrease":
                        addon = addAddon_repDecrease(addons, key2);
                        break;
                    case "creditsIncrease":
                        addon = addAddon_creditsIncrease(addons, key2);
                        break;
                    case "creditsDecrease":
                        addon = addAddon_creditsDecrease(addons, key2);
                        break;
                    case "romanceActionIncrease":
                        addon = addAddon_romanceActionIncrease(addons, key2);
                        break;
                    case "romanceActionDecrease":
                        addon = addAddon_romanceActionDecrease(addons, key2);
                        break;
                    case "additionalText":
                        addon = addAddon_additionalText(addons,key2);
                        break;
                    case "startWedding":
                        addon = addAddon_startWedding(addons,key2);
                        break;
                    case "dedicateTournamentVictoryToLord":
                        addon = addAddon_dedicateTournamentVictoryToLord(addons,key2);
                        break;
                    case "startTournament":
                        addon = addAddon_startTournament(addons,key2);
                        break;
                    case "setHeldDate":
                        addon = addAddon_setHeldDate(addons,key2);
                        break;
                    case "setProfessedAdmiration":
                        addon = addAddon_setProfessedAdmirationThisFeast(addons,key2);
                        break;
                    case "setCourted":
                        addon = addAddon_setCourted(addons,key2);
                        break;
                    case "removeCommoditysFromPlayerFleet":
                        for(DialogAddon_Base a : addAddon_removeCommoditysFromPlayerFleet(addons,key2)){
                            if (a != null) newAddons.add(a);
                        }
                        break;
                    case "addCommoditysToPlayerFleet":
                        for(DialogAddon_Base a : addAddon_addCommoditysToPlayerFleet(addons,key2)){
                            if (a != null) newAddons.add(a);
                        }
                        break;
                    case "wedPlayerToLord":
                        addon = addAddon_wedPlayerToLord(addons,key2);
                        break;
                    case "wedPlayerToWeddingTarget":
                        addon = addAddon_wedPlayerToWeddingTarget(addons,key2);
                        break;
                    case "setInPlayerFleet":
                        addon = addAddon_setInPlayerFleet(addons,key2);
                        break;
                    case "setPledgedFor_CurrentProposal":
                        addon = addAddon_setPledgedFor_CurrentProposal(addons,key2);
                        break;
                    case "setPledgedAgainst_CurrentProposal":
                        addon = addAddon_setPledgedAgainst_CurrentProposal(addons,key2);
                        break;
                    case "setPledgedFor_PlayerProposal":
                        addon = addAddon_setPledgedFor_PlayerProposal(addons,key2);
                        break;
                    case "setPledgedAgainst_PlayerProposal":
                        addon = addAddon_setPledgedAgainst_PlayerProposal(addons,key2);
                        break;
                    case "setSwayed":
                        addon = addAddon_setSwayed(addons,key2);
                        break;
                    case "giveCreditsToLord":
                        addon = addAddon_giveCreditsToLord(addons,key2);
                        break;
                    case "takeCreditsFromLord":
                        addon = addAddon_takeCreditsFromLord(addons,key2);
                        break;
                    case "setPlayerSupportForLordProposal":
                        addon = addAddon_setPlayerSupportForLordProposal(addons,key2);
                        break;
                    case "setPlayerSupportForCurProposal":
                        addon = addAddon_setPlayerSupportForCurProposal(addons,key2);
                        break;
                }
                if (addon != null) newAddons.add(addon);
            }
            this.addons.put(key, newAddons);
        }
        if (line.has("color")){
            boolean isArray = true;
            try {
                line.getJSONObject("color");
            }catch (Exception e){
                isArray = false;
            }
            if (isArray){
                JSONObject color = line.getJSONObject("color");
                int r=0,g=0,b=0,a=255;
                if (color.has("r")) r = color.getInt("r");
                if (color.has("g")) g = color.getInt("g");
                if (color.has("b")) b = color.getInt("b");
                if (color.has("a")) a = color.getInt("a");
                colorOverride.put(key,new Color(r,g,b,a));
            }else{
                getColorDefault(key,line.getString("color"));
            }
        }
        if (line.has("show")){
            JSONObject rulesTemp = line.getJSONObject("show");
            hide.put(key,getDialogFromJSon(rulesTemp));
        }
        if (line.has("enable")){
            JSONObject rulesTemp = line.getJSONObject("enable");
            enable.put(key,getDialogFromJSon(rulesTemp));
        }
        if (line.has("options")){
            //JSONObject options = line.getJSONObject("options");
            JSONArray options = line.getJSONArray("options");
            ArrayList<String> optionsTemp = new ArrayList<>();
            for (int a = 0; a < options.length(); a++){
                optionsTemp.add(options.getString(a));
            }
            additionalOptions.put(key,optionsTemp);
        }
        if (line.has("optionData")){
            dialogOptionData.put(key,line.getString("optionData"));
        }
        if (line.has("hint")){
            hint.put(key,line.getString("hint"));
        }
        if (line.has("shortcut")){
            shotcut.put(key,line.getString("shortcut"));
        }
    }
    private void getColorDefault(String key, String color){
        switch (color){
            case "RED":{
                colorOverride.put(key,Color.RED);
                break;
            }
            case "GREEN":{
                colorOverride.put(key,Color.GREEN);
                break;
            } case "YELLOW":{
                colorOverride.put(key,Color.YELLOW);
            }
        }
    }

    @SneakyThrows
    private static DialogAddon_Base addAddon_repIncrease(JSONObject json, String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogAddon_repIncrease(json2.getInt("min"),json2.getInt("max"));
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_repDecrease(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogAddon_repDecrease(json2.getInt("min"),json2.getInt("max"));
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_creditsIncrease(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogAddon_creditIncrease(json2.getInt("min"),json2.getInt("max"));
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_creditsDecrease(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogAddon_creditDecrease(json2.getInt("min"),json2.getInt("max"));
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_romanceActionIncrease(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogAddon_romanticActionIncrease(json2.getInt("min"),json2.getInt("max"));
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_romanceActionDecrease(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogAddon_romanticActionDecrease(json2.getInt("min"),json2.getInt("max"));
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_additionalText(JSONObject json,String key){
        String json2 = json.getString(key);
        return new DialogAddon_additionalText(json2);
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_startWedding(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        if (!json2) return null;
        return new DialogAddon_startWedding();
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_dedicateTournamentVictoryToLord(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        if (!json2) return null;
        return new DialogAddon_dedicateTournamentVictoryToLord();
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_startTournament(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        if (!json2) return null;
        return new DialogAddon_startTournament();
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_setHeldDate(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_setHeldDate(json2);
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_setProfessedAdmirationThisFeast(JSONObject json, String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_setProfessedAdmirationThisFeast(json2);
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_setCourted(JSONObject json, String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_setCourted(json2);
    }
    @SneakyThrows
    private static ArrayList<DialogAddon_Base> addAddon_removeCommoditysFromPlayerFleet(JSONObject json,String key){
        ArrayList<DialogAddon_Base> out = new ArrayList<>();
        JSONObject json2 = json.getJSONObject(key);
        for (Iterator it2 = json2.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            JSONObject json3 = json2.getJSONObject(key2);
            out.add(new DialogAddon_removeCommoditysFromPlayerFleet(key2,json3.getInt("min"),json3.getInt("max")));
        }
        return out;
    }
    @SneakyThrows
    private static ArrayList<DialogAddon_Base> addAddon_addCommoditysToPlayerFleet(JSONObject json,String key){
        ArrayList<DialogAddon_Base> out = new ArrayList<>();
        JSONObject json2 = json.getJSONObject(key);
        for (Iterator it2 = json2.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            JSONObject json3 = json2.getJSONObject(key2);
            out.add(new DialogAddon_addCommoditysToPlayerFleet(key2,json3.getInt("min"),json3.getInt("max")));
        }
        return out;
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_wedPlayerToLord(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_wedPlayerToLord(json2);
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_wedPlayerToWeddingTarget(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_wedPlayerToWeddingTarget(json2);
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_setInPlayerFleet(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        if (json2) return new DialogAddon_setInPlayerFleetTrue();
        return new DialogAddon_setInPlayerFleetFalse();
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_setPledgedFor_CurrentProposal(JSONObject json, String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_setPledgedFor_CurrentProposal(json2);
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_setPledgedAgainst_CurrentProposal(JSONObject json, String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_setPledgedAgainst_CurrentProposal(json2);
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_setPledgedFor_PlayerProposal(JSONObject json, String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_setPledgedFor_PlayerProposal(json2);
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_setPledgedAgainst_PlayerProposal(JSONObject json, String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_setPledgedAgainst_PlayerProposal(json2);
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_setSwayed(JSONObject json, String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_setSwayed(json2);
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_giveCreditsToLord(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogAddon_giveCreditsToLord(json2.getInt("min"),json2.getInt("max"));
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_takeCreditsFromLord(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogAddon_takeCreditsFromLord(json2.getInt("min"),json2.getInt("max"));
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_setPlayerSupportForLordProposal(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_setPlayerSupportForLordProposal(json2);
    }
    @SneakyThrows
    private static DialogAddon_Base addAddon_setPlayerSupportForCurProposal(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogAddon_setPlayerSupportForCurProposal(json2);
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
                case "isMarried":
                    rules.add(addRule_isMarried(rulesTemp,key));
                    break;
                case "isPlayerMarried":
                    rules.add(addRule_isPlayerMarried(rulesTemp,key));
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
                case "isHostingFeast":
                    rules.add(addRule_isHostingFeast(rulesTemp,key));
                    break;
                case "playerSubject":
                    rules.add(addRule_playerSubject(rulesTemp,key));
                    break;
                case "playerFactionMarital":
                    rules.add(addRule_playerFactionMarital(rulesTemp,key));
                    break;
                case "lordFactionMarital":
                    rules.add(addRule_lordFactionMarital(rulesTemp,key));
                    break;
                case "lordPersonality":
                    rules.add(addRule_personality(rulesTemp,key));
                    break;
                case "lordBattlerPersonality":
                    rules.add(addRule_battlerPersonaility(rulesTemp,key));
                    break;
                case "playerWealth":
                    rules.add(addRule_playerWealth(rulesTemp,key));
                    break;
                case "lordWealth":
                    rules.add(addRule_lordWealth(rulesTemp,key));
                    break;
                case "playerLevel":
                    rules.add(addRule_playerLevel(rulesTemp,key));
                    break;
                case "lordLevel":
                    rules.add(addRule_lordLevel(rulesTemp,key));
                    break;
                case "playerRank":
                    rules.add(addRule_playerRank(rulesTemp,key));
                    break;
                case "lordRank":
                    rules.add(addRule_lordRank(rulesTemp,key));
                    break;
                case "lordsCourted":
                    rules.add(addRule_lordsCourted(rulesTemp,key));
                    break;
                case "isLordCourtedByPlayer":
                    rules.add(addRule_isLordCourtedByPlayer(rulesTemp,key));
                    break;
                case "playerLordRomanceAction":
                    rules.add(addRule_playerLordRomanceAction(rulesTemp,key));
                    break;
                case "availableTournament":
                    rules.add(addRule_availableTournament(rulesTemp,key));
                    break;
                case "playerTournamentVictory":
                    rules.add(addRule_playerTournamentVictory(rulesTemp,key));
                    break;
                case "lordTournamentVictory":
                    rules.add(addRule_lordTournamentVictory(rulesTemp,key));
                    break;
                case "playerTournamentVictoryDedicated":
                    rules.add(addRule_playerTournamentVictoryDedicated(rulesTemp,key));
                    break;
                case "feastIsHostingWedding":
                    rules.add(addRule_feastIsHostingWedding(rulesTemp,key));
                    break;
                case "firstMeeting":
                    rules.add(addRule_firstMeeting(rulesTemp,key));
                    break;
                case "tournamentDedicatedToLord":
                    rules.add(addRule_tournamentDedicatedToLord(rulesTemp,key));
                    break;
                case "lordsInFeast":
                    rules.add(addRule_lordsInFeast(rulesTemp,key));
                    break;
                case "hasProfessedAdmirationThisFeast":
                    rules.add(addRule_hasProfessedAdmirationThisFeast(rulesTemp,key));
                    break;
                case "hasHeldDateThisFeast":
                    rules.add(addRule_hasHeldDateThisFeast(rulesTemp,key));
                    break;
                case "lordPledgedSupport_forActiveProposal":
                    rules.add(addRule_lordPledgedSupport_forActiveProposal(rulesTemp,key));
                    break;
                case "lordPledgedSupport_againstActiveProposal":
                    rules.add(addRule_lordPledgedSupport_againstActiveProposal(rulesTemp,key));
                    break;
                case "lordPledgedSupport_forPlayerProposal":
                    rules.add(addRule_lordPledgedSupport_forPlayerProposal(rulesTemp,key));
                    break;
                case "lordPledgedSupport_againstPlayerProposal":
                    rules.add(addRule_lordPledgedSupport_againstPlayerProposal(rulesTemp,key));
                    break;
                case "playerProposalExists":
                    rules.add(addRule_playerProposalExists(rulesTemp,key));
                    break;
                case "lordProposalExists":
                    rules.add(addRule_lordProposalExists(rulesTemp,key));
                    break;
                case "lordFactionHasActiveConsole":
                    rules.add(addRule_lordFactionHasActiveConsole(rulesTemp,key));
                    break;
                case "playerFactionHasActiveConsole":
                    rules.add(addRule_playerFactionHasActiveConsole(rulesTemp,key));
                    break;
                case "lordActingInPlayerFleet":
                    rules.add(addRule_lordActingInPlayerFleet(rulesTemp,key));
                    break;
                case "lordAndPlayerSameFaction":
                    rules.add(addRule_lordAndPlayerSameFaction(rulesTemp,key));
                    break;
                case "playerHasCommodity":
                    rules.addAll(addRule_playerHasCommodity(rulesTemp,key));
                    break;
                case "lordsFavItem":
                    rules.addAll(addRule_lordsFavItem(rulesTemp,key));
                    break;
                case "isWeddingTarget":
                    rules.add(addRule_isWeddingTarget(rulesTemp,key));
                    break;
                case "optionOfCurrProposal":
                    rules.add(addRule_optionOfCurrProposal(rulesTemp,key));
                    break;
                case "optionOfPlayerProposal":
                    rules.add(addRule_optionOfPlayerProposal(rulesTemp,key));
                    break;
                case "isSwayed":
                    rules.add(addRule_isSwayed(rulesTemp,key));
                    break;
                case "lordProposalSupporters":
                    rules.add(addRule_lordProposalSupporters(rulesTemp,key));
                    break;
                case "lordProposalOpposers":
                    rules.add(addRule_lordProposalOpposers(rulesTemp,key));
                    break;
                case "lordProposalPlayerSupports":
                    rules.add(addRule_lordProposalPlayerSupports(rulesTemp,key));
                    break;
                case "playerProposalSupporters":
                    rules.add(addRule_playerProposalSupporters(rulesTemp,key));
                    break;
                case "playerProposalOpposers":
                    rules.add(addRule_playerProposalOpposers(rulesTemp,key));
                    break;
                case "playerProposalLordSupports":
                    rules.add(addRule_playerProposalLordSupports(rulesTemp,key));
                    break;
                case "curProposalSupporters":
                    rules.add(addRule_curProposalSupporters(rulesTemp,key));
                    break;
                case "curProposalOpposers":
                    rules.add(addRule_curProposalOpposers(rulesTemp,key));
                    break;
                case "curProposalPlayerSupports":
                    rules.add(addRule_curProposalPlayerSupports(rulesTemp,key));
                    break;
                case "curProposalLordSupports":
                    rules.add(addRule_curProposalLordSupports(rulesTemp,key));
                    break;
                case "random":
                    rules.add(addRule_random(rulesTemp,key));
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
    private static DialogRule_Base addRule_isMarried(JSONObject json, String key){
        return new DialogRule_isMarried(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_isPlayerMarried(JSONObject json, String key){
        return new DialogRule_isPlayerMarried(json.getBoolean(key));
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
    @SneakyThrows
    private static DialogRule_Base addRule_isHostingFeast(JSONObject json, String key){
        return new DialogRule_isHostingFeast(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerSubject(JSONObject json, String key){
        return new DialogRule_playerSubject(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerFactionMarital(JSONObject json, String key){
        return new DialogRule_playerFactionMarital(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordFactionMarital(JSONObject json, String key){
        return new DialogRule_lordFactionMarital(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_personality(JSONObject json,String key){
        ArrayList<String> whiteList = new ArrayList<>();
        JSONObject ruleAdded = json.getJSONObject(key);
        for (Iterator it2 = ruleAdded.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            if (ruleAdded.getBoolean(key2)){
                whiteList.add(key2);
                continue;
            }
        }
        return new DialogRule_personaility(whiteList);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_battlerPersonaility(JSONObject json,String key){
        ArrayList<String> whiteList = new ArrayList<>();
        JSONObject ruleAdded = json.getJSONObject(key);
        for (Iterator it2 = ruleAdded.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            if (ruleAdded.getBoolean(key2)){
                whiteList.add(key2);
                continue;
            }
        }
        return new DialogRule_battlePersonaility(whiteList);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordLevel(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_lordLevel(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordRank(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_lordRank(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordsCourted(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_lordsCourted(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordWealth(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_lordWealth(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerLevel(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_playerLevel(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerRank(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_playerRank(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerWealth(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_playerWealth(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_isLordCourtedByPlayer(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_isLordCourtedByPlayer(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerLordRomanceAction(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_playerLordRomanceAction(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_availableTournament(JSONObject json, String key){
        return new DialogRule_availableTournament(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerTournamentVictory(JSONObject json, String key){
        return new DialogRule_playerTournamentVictory(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordTournamentVictory(JSONObject json, String key){
        return new DialogRule_lordTournamentVictroy(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerTournamentVictoryDedicated(JSONObject json, String key){
        return new DialogRule_playerTournamentVictroyDedicated(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_feastIsHostingWedding(JSONObject json, String key){
        return new DialogRule_feastIsHostingWedding(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_firstMeeting(JSONObject json, String key){
        return new DialogRule_firstMeeting(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_tournamentDedicatedToLord(JSONObject json, String key){
        return new DialogRule_tournamentDedicatedToLord(json.getBoolean(key));
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordsInFeast(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_lordsInFeast(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_hasProfessedAdmirationThisFeast(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_hasProfessedAdmirationThisFeast(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_hasHeldDateThisFeast(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_hasHeldDateThisFeast(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordPledgedSupport_forActiveProposal(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_lordPledgedSupport_forActiveProposal(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordPledgedSupport_againstActiveProposal(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_lordPledgedSupport_againstActiveProposal(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordPledgedSupport_forPlayerProposal(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_lordPledgedSupport_forPlayerProposal(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordPledgedSupport_againstPlayerProposal(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_lordPledgedSupport_againstPlayerProposal(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerProposalExists(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_playerProposalExists(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordProposalExists(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_lordProposalExists(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordFactionHasActiveConsole(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_lordFactionHasActiveConsole(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerFactionHasActiveConsole(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_playerFactionHasActiveConsole(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordActingInPlayerFleet(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_lordActingInPlayerFleet(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordAndPlayerSameFaction(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_lordAndPlayerSameFaction(json2);
    }
    @SneakyThrows
    private static ArrayList<DialogRule_Base> addRule_playerHasCommodity(JSONObject json,String key){
        ArrayList<DialogRule_Base> output = new ArrayList<>();
        JSONObject json2 = json.getJSONObject(key);
        for (Iterator it = json2.keys(); it.hasNext();) {
            String key2 = (String) it.next();
            output.add(new DialogRule_playerHasCommodity(key2,json2.getJSONObject(key2)));
        }
        return output;
    }
    @SneakyThrows
    private static ArrayList<DialogRule_Base> addRule_lordsFavItem(JSONObject json,String key){
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
        if (whiteList.size() != 0) rules.add(new DialogRule_lordsFavItem_whitelist(whiteList));
        if (blackList.size() != 0) rules.add(new DialogRule_lordsFavItem_blacklist(blackList));
        return rules;
    }
    @SneakyThrows
    private static DialogRule_Base addRule_isWeddingTarget(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_isWeddingTarget(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_optionOfCurrProposal(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_optionOfCurrProposal(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_optionOfPlayerProposal(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_optionOfPlayerProposal(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_isSwayed(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_isSwayed(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordProposalSupporters(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_lordProposalSupporters(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordProposalOpposers(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_lordProposalOpposers(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_lordProposalPlayerSupports(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_lordProposalPlayerSupports(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerProposalSupporters(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_playerProposalSupporters(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerProposalOpposers(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_playerProposalOpposers(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_playerProposalLordSupports(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_playerProposalLordSupports(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_curProposalSupporters(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_curProposalSupporters(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_curProposalOpposers(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_curProposalOpposers(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_curProposalPlayerSupports(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_curProposalPlayerSupports(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_curProposalLordSupports(JSONObject json,String key){
        boolean json2 = json.getBoolean(key);
        return new DialogRule_curProposalLordSupports(json2);
    }
    @SneakyThrows
    private static DialogRule_Base addRule_random(JSONObject json,String key){
        JSONObject json2 = json.getJSONObject(key);
        return new DialogRule_random(json2);
    }
}
