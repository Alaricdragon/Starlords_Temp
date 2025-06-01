package starlords.util.lordUpgrades;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class UpgradeController {
    private static HashMap<String, UpgradeBase> upgrages;
    public static void init(){
        /*so heres the plan on preperation for this nonesense.
        * first, use my knowlage gained in my very first starlord generator on how the hell to read CSV files. we can go about building an 'upgrade.csv' from there.
        * aftorwords, I can start to look into farther improvements.*/
        String[] paths = {
                "starlords.util.lordUpgrades.UpgradeTempAttempt",
        };
        for (String a : paths){
            try {
                attemptToFindAFile(a);
            }catch (Exception e){
                Logger log = Global.getLogger(UpgradeController.class);
                log.info("  ERROR: failed to get path of: "+a);
            }
        }
    }
    private static void attemptToFindAFile(String path){
        Logger log = Global.getLogger(UpgradeController.class);
        log.info("attempting to get a class from a path of: "+path);
        Object thing = Global.getSettings().getInstanceOfScript(path);
        log.info("  scaning gotten thing....");
        log.info("  "+thing.getClass());
        log.info("  "+thing.toString());
        UpgradeBase thing2 = (UpgradeBase) thing;
        log.info("  "+"gotting thing as UpgradeBase...");
        ((UpgradeBase) thing).attemptThing();
        log.info("  "+thing2.getClass());
        log.info("  "+thing2.toString());
    }
}
