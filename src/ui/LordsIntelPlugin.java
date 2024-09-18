package ui;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.RuleBasedInteractionDialogPluginImpl;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;
import controllers.EventController;
import controllers.LordController;
import controllers.RelationController;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Keyboard;
import person.Lord;
import person.LordAction;
import person.LordEvent;
import plugins.LordInteractionDialogPluginImpl;
import plugins.LordsModPlugin;
import util.StringUtil;
import util.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static util.Constants.CATEGORY_UI;
import static util.Constants.DEBUG_MODE;

public class LordsIntelPlugin extends BaseIntelPlugin {

    public static final Object OPEN_COMMS_BUTTON = new Object();
    @Getter
    @Setter
    private Lord lord;

    private LordsIntelPlugin(Lord lord) {
        this.lord = lord;
    }

    @Override
    public boolean isHidden() {
        return !lord.isKnownToPlayer() && !DEBUG_MODE;
    }

    @Override
    public boolean hasSmallDescription() {
        return false;
    }

    @Override
    public boolean hasLargeDescription() {
        return true;
    }

    @Override
    public void createLargeDescription(CustomPanelAPI panel, float width, float height) {
        float pad = 3;
        float opad = 10;

        FactionAPI faction = lord.getFaction();
        boolean isSubject = faction.equals(Global.getSector().getPlayerFaction());
        CampaignFleetAPI fleet = lord.getLordAPI().getFleet();
        Color uiColor = Global.getSettings().getBasePlayerColor();
        Color factionColor = faction.getBaseUIColor();
        TooltipMakerAPI outer = panel.createUIElement(width, height, true);
        outer.addSectionHeading(getSmallDescriptionTitle(), factionColor,
                faction.getDarkUIColor(), com.fs.starfarer.api.ui.Alignment.MID, opad);


        // sprite and basic info (relations, rank, fiefs, wealth, last seen, orders)
        TooltipMakerAPI summary = outer.beginImageWithText(lord.getLordAPI().getPortraitSprite(), 128);
        String fiefStr = "";
        for (SectorEntityToken fief : lord.getFiefs()) {
            fiefStr += fief.getName() + ", ";
        }
        if (!fiefStr.isEmpty()) {
            fiefStr = fiefStr.substring(0, fiefStr.length() - 2);
        }
        String wealthStr;
        if (lord.getPlayerRel() < Utils.getThreshold(RepLevel.WELCOMING) && !isSubject && !DEBUG_MODE) {
            wealthStr = "[REDACTED]";
        } else {
            wealthStr = Integer.toString((int) lord.getWealth());
        }

        String personalityStr;
        if (lord.isPersonalityKnown()) {
            personalityStr = lord.getPersonality().toString();
            personalityStr = personalityStr.substring(0, 1) + personalityStr.substring(1).toLowerCase();
        } else {
            personalityStr = "Unknown";
        }
        String orderStr;
        if (lord.getPlayerRel() < Utils.getThreshold(RepLevel.FRIENDLY) && !isSubject && !DEBUG_MODE) {
            orderStr = "[REDACTED]";
        } else if (lord.getCurrAction() == LordAction.IMPRISONED) {
            orderStr = "Imprisoned by " + LordController.getLordOrPlayerById(lord.getCaptor()).getLordAPI().getNameString();
        } else if (lord.getCurrAction() == null || !fleet.isAlive()) {
            orderStr = "None";
        } else if (lord.getCurrAction() != LordAction.CAMPAIGN) {
            orderStr = StringUtil.getString(CATEGORY_UI, "fleet_" + lord.getCurrAction().base.toString().toLowerCase() + "_desc", lord.getTarget().getName());
        } else {
            if (lord.isMarshal()) {
                LordEvent campaign = EventController.getCurrentCampaign(lord.getLordAPI().getFaction());
                if (campaign.getTarget() == null) {
                    orderStr = StringUtil.getString(CATEGORY_UI, "fleet_campaign_lead_desc", lord.getTarget().getName());
                } else {
                    orderStr = StringUtil.getString(CATEGORY_UI, "fleet_campaign_lead_desc", campaign.getTarget().getName());
                }
            } else {
                orderStr = StringUtil.getString(CATEGORY_UI, "fleet_campaign_follow_desc");
            }
        }
        String lastSeenStr;
        if (lord.getPlayerRel() < Utils.getThreshold(RepLevel.COOPERATIVE) && !isSubject && !DEBUG_MODE) {
            lastSeenStr = "[REDACTED]";
        }  else if (!fleet.isAlive()) {
            lastSeenStr = "N/A";
        } else {
            lastSeenStr = Utils.getNearbyDescription(fleet);
        }

        summary.addPara("Rank: " + lord.getTitle(), pad, uiColor, factionColor, "Rank: ", lord.getTitle());
        if (fiefStr.isEmpty()) {
            summary.addPara("Fiefs: None", uiColor, pad);
        } else {
            summary.addPara("Fiefs: " + fiefStr, pad, uiColor, factionColor, "Fiefs: ", fiefStr);
        }
        summary.addPara("Personality: " + personalityStr, uiColor, pad);
        summary.addPara("Wealth: " + wealthStr, uiColor, pad);
        summary.addPara("Last Seen: " + lastSeenStr, uiColor, pad);
        summary.addPara("Orders: " + orderStr, uiColor, pad);
        summary.addPara("Kills: " + lord.getKills(), uiColor, pad);
        outer.addImageWithText(pad);
        outer.addRelationshipBar(lord.getLordAPI(), pad);
        // lore
        outer.addSectionHeading("Character Bio", faction.getBrightUIColor(), faction.getDarkUIColor(), Alignment.LMID, opad);
        outer.addPara(lord.getTemplate().lore, opad);

        outer.addSectionHeading("Fleet Composition", faction.getBrightUIColor(), faction.getDarkUIColor(), Alignment.LMID, opad);
        // shiplist
        if (lord.getPlayerRel() >= Utils.getThreshold(RepLevel.FRIENDLY) || isSubject || DEBUG_MODE) {
            int rows = 3;
            if (lord.getLordAPI().getFleet().getNumShips() <= 30) rows = 2;
            outer.addShipList(15, rows, 48, factionColor,
                    lord.getLordAPI().getFleet().getMembersWithFightersCopy(), opad);
        } else {
            outer.addPara("[REDACTED]", pad);
        }

        // comms
        // TODO this doesnt really work
        float buttonPad = Math.min(Math.max(opad, height - outer.getHeightSoFar() - 160), 100 + pad);
        ButtonAPI button = outer.addButton("Open Comms", OPEN_COMMS_BUTTON, 150, 20, buttonPad);
        button.setShortcut(Keyboard.KEY_C, true);
        if (lord.getPlayerRel() < Utils.getThreshold(RepLevel.COOPERATIVE) && !isSubject && !DEBUG_MODE) {
            button.setEnabled(false);
            outer.addTooltipToPrevious(new ToolTip(175, "Requires higher relations"), TooltipMakerAPI.TooltipLocation.BELOW);
        }
        panel.addUIElement(outer).inTL(0, 0);
    }

    @Override
    public void buttonPressConfirmed(Object buttonId, IntelUIAPI ui) {
        if (buttonId == OPEN_COMMS_BUTTON && lord.getPlayerRel() >= Utils.getThreshold(RepLevel.COOPERATIVE)) {
            LordInteractionDialogPluginImpl conversationDelegate = new LordInteractionDialogPluginImpl();
            ui.showDialog(lord.getLordAPI().getFleet(), conversationDelegate);
        }
    }

    @Override
    public String getName() {
        return lord.getTitle() + " " + lord.getTemplate().name;
    }

    @Override
    public String getSortString() {
        FactionAPI playerFaction = Misc.getCommissionFaction();
        if (playerFaction == null) playerFaction = Global.getSector().getPlayerFaction();
        if (lord.getLordAPI().getFaction().equals(playerFaction)) {
            // move player faction to the top of the list
            return "0" + lord.getLordAPI().getFaction().getDisplayName() + lord.getTemplate().name;
        }
        return lord.getLordAPI().getFaction().getDisplayName() + lord.getTemplate().name;
    }

    @Override
    public FactionAPI getFactionForUIColors() {
        return lord.getLordAPI().getFaction();
    }

    @Override
    public String getIcon() {
        return lord.getLordAPI().getPortraitSprite();
    }

    @Override
    public Set<String> getIntelTags(SectorMapAPI map) {
        Set<String> tags = super.getIntelTags(map);
        tags.add(StringUtil.getString(CATEGORY_UI, "lords_category"));
        return tags;
    }

    @Override
    public Color getTitleColor(ListInfoMode mode) {
        return lord.getLordAPI().getFaction().getBaseUIColor();
    }

    @Override
    public Color getBackgroundGlowColor() {
        return lord.getLordAPI().getFaction().getDarkUIColor();
    }

    public static void createProfile(Lord lord) {
        LordsIntelPlugin profile = new LordsIntelPlugin(lord);
        Global.getSector().getIntelManager().addIntel(profile, true);
        profile.setNew(false);
    }

}
