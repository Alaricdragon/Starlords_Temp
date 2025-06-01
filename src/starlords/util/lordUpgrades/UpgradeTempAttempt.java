package starlords.util.lordUpgrades;

import com.fs.starfarer.api.Global;

import org.apache.log4j.Logger;

public class UpgradeTempAttempt extends UpgradeBase{
    @Override
    public void attemptThing() {
        Logger log = Global.getLogger(UpgradeTempAttempt.class);
        log.info("  WE GOT IT BOYS, PART 2 JHAHAHAHAHAHAH");
    }
}
