package starlords.controllers;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import starlords.PMC.PMC;
import starlords.PMC.PMC_Faction;
import starlords.util.factionUtils.FactionTemplateController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class PMCController {
    private static final String memKey = "$STARLORD_PMC_CONTROLLER";
    @Getter
    private static HashMap<String,PMC> PMCs = new HashMap<>();
    public static void initPMCs(){
        load();
        //todo: make it so this looks at all factions that has starlords, and adds them.

        //todo: make it so this reads the json file, and creates PMCs for certen lords..

        //todo: make it so this can create PMC's for random 'powerful' lords
        for (String a : (String[]) LordController.getFactionsWithLords().toArray()){
            String c = PMC_Faction.transformID(a);
            if (PMCs.containsKey(c)) continue;
            PMC_Faction b = new PMC_Faction(a);
            PMCs.put(c,b);
            FactionTemplateController.getTemplate(a).setPrimaryPMC(b);
            //todo: make it so there is some type of json or CSV file related to pmcs for factions, so that can be a thing.
        }
    }
    public static void load(){
        if (!Global.getSector().getMemory().contains(memKey)) initPMCs();
        PMCs = (HashMap<String, PMC>) Global.getSector().getMemory().get(memKey);
        for (String a: PMCs.keySet()){
            PMC b = PMCs.get(a);
            b.loadConnectedMemory();
            b.getDataHolder();
        }
    }
    public static void save(){
        for (String a: PMCs.keySet()){
            PMC b = PMCs.get(a);
            b.saveCompressedMemory();
            b.saveDataHolder();
        }
        Global.getSector().getMemory().set(memKey,PMCs);
    }
}
