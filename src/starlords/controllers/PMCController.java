package starlords.controllers;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import starlords.PMC.PMC;
import java.util.ArrayList;
import java.util.List;


public class PMCController {
    private static final String memKey = "$STARLORD_PMC_CONTROLLER";
    @Getter
    private static List<PMC> PMCs = new ArrayList<>();
    public static void initPMCs(){
        //todo: make it so this looks at all factions that has starlords, and adds them.


        //todo: make it so this reads the json file, and creates PMCs for certen lords..

        //todo: make it so this can create PMC's for random 'powerfull' lords
    }
    public static void load(){
        if (!Global.getSector().getMemory().contains(memKey)) initPMCs();
        PMCs = (List<PMC>) Global.getSector().getMemory().get(memKey);
        for (PMC a: PMCs){
            a.loadConnectedMemory();
            a.getDataHolder();
        }
    }
    public static void save(){
        for (PMC a: PMCs){
            a.saveCompressedMemory();
            a.saveDataHolder();
        }
        Global.getSector().getMemory().set(memKey,PMCs);
    }
}
