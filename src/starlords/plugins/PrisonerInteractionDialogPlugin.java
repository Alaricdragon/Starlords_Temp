package starlords.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.ui.IntelUIAPI;
import com.fs.starfarer.api.util.Misc;
import lombok.Setter;
import starlords.util.GenderUtils;
import starlords.util.StringUtil;
import starlords.util.Utils;

import java.awt.*;
import java.util.Random;

import static starlords.util.Constants.BASE_RANSOM;

public class PrisonerInteractionDialogPlugin extends LordInteractionDialogPluginImpl {
    private enum PrisonerOptionId {
        INIT,
        RELEASE_RESOLVE,
        RANSOM_RESOLVE
    }

    private int ransomAmount;
    @Setter
    private IntelUIAPI ui; // this is the intel ui that should be updated if prisoners are released

    @Override
    protected String setDialogType() {
        return "prisoner";
    }

    @Override
    public void optionSelected(String optionText, Object optionData) {
        /*ok so: what do I need to do here?
        * first of all, there is the randomValue.
        * option set:
        *   [
        *       (untested, done)"setData_ransomAmount"
        *       (done)"option_speak_privately"
        *       "option_release_prisoner"
        *       "option_ransom_prisoner"
        *       "option_leave_prisoner"
        *   ]
        *
        * addons:
        *   (done, untested)memory: changes this to have a timer to expire
        *       example:
        *        Misc.setFlagWithReason(lord.getFleet().getMemoryWithoutUpdate(),
        *        MemFlags.MEMORY_KEY_MAKE_NON_HOSTILE, "starlords", true, time);
        * rules:
        *   (done, untested)add a rule that is 'dialogType'.
        *
        *
        *   "base": 50000
        *   "multi": {
        *       "base":50
        *       "random":100
        *   }
        *   "lordRank": 25000
        * */
        PrisonerOptionId option;
        if (optionData == "OptionId.INIT") {
            option = PrisonerOptionId.INIT;
        } else if (optionData instanceof PrisonerOptionId) {
            option = (PrisonerOptionId) optionData;
        } else {
            super.optionSelected(optionText, optionData);
            return;
        }

        if (prevPlugin.equals(this) && !visual.isShowingPersonInfo(targetLord.getLordAPI())) {
            visual.showPersonInfo(targetLord.getLordAPI(), false, true);
        }

        switch (option) {
            case INIT:
                options.clearOptions();
                int baseRansom = BASE_RANSOM + targetLord.getRanking() * BASE_RANSOM / 2;
                ransomAmount = (int) (baseRansom * (0.5f + new Random(
                        targetLord.getLordAPI().getId().hashCode() * Global.getSector().getClock().getMonth()).nextFloat()));
                textPanel.addPara(StringUtil.getString(CATEGORY,
                        "greeting_" + targetLord.getPersonality().toString().toLowerCase() + "_imprisoned"));
                options.addOption(StringUtil.getString(CATEGORY, "option_speak_privately"), "OptionId.SPEAK_PRIVATELY");
                options.addOption(StringUtil.getString(CATEGORY, "option_release_prisoner"), PrisonerOptionId.RELEASE_RESOLVE);
                options.addOption(StringUtil.getString(CATEGORY, "option_ransom_prisoner", Integer.toString(ransomAmount)),
                        PrisonerOptionId.RANSOM_RESOLVE);
                options.addOption(StringUtil.getString(CATEGORY, "option_leave_prisoner"), "OptionId.LEAVE");
                break;
            case RELEASE_RESOLVE:
                textPanel.addPara(StringUtil.getString(CATEGORY,
                        "release_" + targetLord.getPersonality().toString().toLowerCase(),
                        GenderUtils.manOrWoman(Global.getSector().getPlayerPerson(), false)));
                int change = targetLord.getPersonality().releaseRepGain;
                Utils.adjustPlayerReputation(targetLord.getLordAPI(), change);
                textPanel.addPara(StringUtil.getString(
                        CATEGORY, "relation_increase",
                        targetLord.getLordAPI().getNameString(), Integer.toString(change)), Color.GREEN);
                targetLord.setCaptor(null);
                if (ui != null) {
                    ui.updateIntelList();
                }
                options.clearOptions();
                options.addOption("Leave", "OptionId.LEAVE");
                break;
            case RANSOM_RESOLVE:
                textPanel.addPara(StringUtil.getString(CATEGORY, "release_ransom"));
                textPanel.addPara("Gained " + ransomAmount + " credits.", Color.GREEN);
                Utils.adjustPlayerReputation(targetLord.getLordAPI(), -3);
                textPanel.addParagraph(StringUtil.getString(
                        CATEGORY, "relation_decrease", targetLord.getLordAPI().getNameString(), "3"), Color.RED);
                Global.getSector().getPlayerFleet().getCargo().getCredits().add(ransomAmount);
                targetLord.addWealth(-ransomAmount / 5);
                targetLord.setCaptor(null);
                if (ui != null) {
                    ui.updateIntelList();
                }
                options.clearOptions();
                options.addOption("Leave", "OptionId.LEAVE");
                break;
        }
    }
}
