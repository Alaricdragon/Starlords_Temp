package starlords.listeners;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import starlords.controllers.*;
import org.apache.log4j.Logger;
import starlords.person.Lord;
import starlords.person.LordAction;
import starlords.person.LordRequest;
import starlords.util.Constants;
import starlords.util.DefectionUtils;
import starlords.util.LordFleetFactory;
import starlords.util.Utils;

import java.util.List;

public class MonthlyUpkeepListener extends BaseCampaignEventListener {

    public static Logger log = Global.getLogger(MonthlyUpkeepListener.class);

    /**
     * @param permaRegister: if true, automatically sets this listener to always be running
     */
    public MonthlyUpkeepListener(boolean permaRegister) {
        super(permaRegister);
    }

    @Override
    public void reportEconomyMonthEnd() {
        // Give all lords their base monthly wage and pay fleet upkeep.
        List<Lord> lords = LordController.getLordsList();
        for (Lord lord : lords) {
            // make sure mercenary lords dont expire every month
            if (Misc.isMercenary(lord.getLordAPI())) {
                Misc.setMercHiredNow(lord.getLordAPI());
            }
            if (!lord.isMarshal()) {
                lord.setControversy(Math.max(0, lord.getControversy() - 2));
            }

            Pair<Float, Float> result = PoliticsController.getBaseIncomeMultipliers(lord.getFaction());
            // give pirates some more base money since they can't own fiefs
            if (Utils.isMinorFaction(lord.getFaction())) result.one *= 2f;
            lord.addWealth(result.one * Constants.LORD_MONTHLY_INCOME
                    + result.two * lord.getRanking() * Constants.LORD_MONTHLY_INCOME);
            CampaignFleetAPI fleet = lord.getLordAPI().getFleet();
            if (fleet == null || lord.getCurrAction() == LordAction.COMPANION) {
                continue;
            }
            // maintenance cost is 15% of purchase cost, also use FP instead of DP for simplicity
            float cost = LordFleetFactory.COST_MULT * fleet.getFleetPoints() * 0.15f;
            lord.addWealth(-1 * cost);
            //log.info("DEBUG: Lord " + lord.getLordAPI().getNameString() + " incurred expenses of " + cost);
        }
        LifeAndDeathController.getInstance().runMonth();
        FiefController.onMonthPass();
        QuestController.getInstance().resetQuests();
		FiefController.playerAssignFiefs();
        // check for lord betrayal
        calculateLordsBetrayal();
    }

	public void calculateLordsBetrayal() {

		for (Lord lord : LordController.getLordsList()) {
			LordRequest existingRequest = RequestController.getCurrentRequest(lord, LordRequest.FIEF_FOR_DEFECTION);
			if (existingRequest != null) {
				RequestController.endRequest(existingRequest);
			}
			else if (lord.wantsToDefect())
				if (lord.shouldRequestFiefForDefection())
					RequestController.addRequest(new LordRequest(LordRequest.FIEF_FOR_DEFECTION, lord));
				else
					DefectionUtils.performDefection(lord);


			// player faction cant have lords if player is not leading the faction
			if (lord.getFaction().isPlayerFaction() && Misc.getCommissionFaction() != null) {
				DefectionUtils.performDefection(lord, Misc.getCommissionFaction(), true);
			}
		}
	}
}
