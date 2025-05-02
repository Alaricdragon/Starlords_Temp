package starlords.util;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.util.Misc;
import exerelin.campaign.DiplomacyManager;
import exerelin.utilities.NexConfig;
import exerelin.utilities.NexUtils;
import exerelin.utilities.NexUtilsFaction;
import exerelin.utilities.NexUtilsMarket;

public class NexerlinUtilitys {
    public static void declarePeace(FactionAPI proposer,FactionAPI propose){
        DiplomacyManager.adjustRelations(proposer,propose,0.51f,null,null,RepLevel.INHOSPITABLE);
    }
    public static void declareWar(FactionAPI proposer,FactionAPI propose){
        DiplomacyManager.adjustRelations(proposer,propose,-1.1f,null,null,RepLevel.HOSTILE);
    }
    public static boolean canBeAttacked(FactionAPI faction){
        if (NexConfig.getFactionConfig(faction.getId()).pirateFaction) return false;
        if (!NexConfig.getFactionConfig(faction.getId()).canInvade) return false;
        if (!NexConfig.getFactionConfig(faction.getId()).playableFaction) return false;
        return true;
    }
    public static boolean canBeAttacked(MarketAPI market){
        return NexUtilsMarket.canBeInvaded(market,false);
    }
    public static boolean canChangeRelations(FactionAPI faction){
        //if (NexConfig.getFactionConfig(faction.getId()).hostileToAll || NexConfig.getFactionConfig(faction.getId()).playableFaction) return false;
        if (!NexConfig.getFactionConfig(faction.getId()).playableFaction) return false;
        if (NexConfig.getFactionConfig(faction.getId()).disableDiplomacy) return false;
        if (Misc.isPirateFaction(faction)) return false;
        return true;
    }
    public static boolean isMinorFaction(FactionAPI faction){
        //ok, so I have lined wether or not something is a minor faction and can be attacked together. this can be overwritten independently, but for now this makes sense I think?
        return !canBeAttacked(faction);
    }
}
