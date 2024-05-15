/**
 * @ Author: Laroustine
 * @ Modified time: 15/05 15:40
 * @ Modified by: Laroustine
 * @ Description: This script has been made by me ↖(^▽^)↗
 */
package data.scripts.plugin;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.MusicPlayerPluginImpl;

import org.apache.log4j.Logger;

public class MusicPlugin extends MusicPlayerPluginImpl {
    public float RATIO_DESTROY = Global.getSettings().getFloat("cbm_destroy_value");
    public float RATIO_LOSE = Global.getSettings().getFloat("cbm_lose_value");
    public float RATIO_WIN = Global.getSettings().getFloat("cbm_win_value");

    protected final Logger LOG = Logger.getLogger(MusicPlugin.class);

    public MusicPlugin() {
        LOG.info("Music level: " + RATIO_DESTROY * 100 + "% Destroy / " + RATIO_LOSE * 100 + "% Lose / " + RATIO_WIN * 100 + "% Win");
    }

    private static float getFleetValue(CampaignFleetAPI fleet) {
        int score = 0;

        for (FleetMemberAPI ship : fleet.getFleetData().getMembersListCopy()) {
            score += ship.getDeploymentPointsCost();
        }
        return (float) score;
    }

    public static float getFleetRatio(CampaignFleetAPI ennemy, CampaignFleetAPI player) {
        float playerCount = getFleetValue(player);
        float ennemyCount = getFleetValue(ennemy);

        if (ennemyCount == playerCount) {
            return 1.0f;
        } else if (playerCount == 0) {
            return 2.0f;
        }
        return ennemyCount / playerCount;
    }

    private String getFleetMusic(CampaignFleetAPI fleet) {
        for (String tag : fleet.getTags()) {
            if (tag.contains("cbm_")) {
                return tag;
            }
        }
        return null;
    }

    private String getShipMusic(CampaignFleetAPI fleet) {
        for (FleetMemberAPI ship : fleet.getFleetData().getMembersListCopy()) {
            for (String tag : ship.getHullSpec().getTags()) {
                if (tag.contains("cbm_")) {
                    return tag;
                }
            }
        }
        return null;
    }

    protected String getCampaignMusic(CombatEngineAPI engine) {
        FactionAPI faction = engine.getContext().getOtherFleet().getFaction();
        String battle = faction.getMusicMap().get("battle");
        String musicId = null;
        String temp = null;
        
        float ratio = getFleetRatio(engine.getContext().getOtherFleet(), engine.getContext().getPlayerFleet());

        LOG.info("The ratio for this battle is : " + (int) (ratio * 100) + "%");
        // Special Cases
        if (engine.getContext().getOtherGoal().equals(FleetGoal.ESCAPE)) {
            musicId = faction.getMusicMap().get("battle_retreat");
        } else if (ratio <= RATIO_DESTROY) {
            musicId = faction.getMusicMap().get("battle_destroy");
        } else if (ratio <= RATIO_LOSE) {
            musicId = faction.getMusicMap().get("battle_losing");
        } else if (ratio >= RATIO_WIN) {
            musicId = faction.getMusicMap().get("battle_advantage");
        }
        // Ships & Fleet
        temp = getFleetMusic(engine.getContext().getOtherFleet());
        temp = temp != null ? temp : getShipMusic(engine.getContext().getOtherFleet());
        musicId = temp != null ? temp : musicId;
        // Handle Find
        if (musicId != null || battle != null) {
            LOG.info("The music for this faction is : " + musicId == null ? battle : musicId);
        } else {
            LOG.info("The Faction [" + faction.getDisplayName() + "] does not have custom music.");
            musicId = super.getMusicSetIdForCombat(engine);
        }
        return musicId == null ? battle : musicId;
    }

    protected String getMissionMusic(CombatEngineAPI engine) {
        String musicId = Global.getSector().getMemory().getString(MUSIC_SET_MEM_KEY);

        if (musicId != null) {
            LOG.info("The music for this mission is : " + musicId);
        } else {
            LOG.info("The Mission [" + engine.getMissionId() + "] does not have custom music.");
            musicId = super.getMusicSetIdForCombat(engine);
        }
        return musicId;
    }

    protected String getSimulationMusic(CombatEngineAPI engine) {
        String name = "cbm_simulation_ost";
        LOG.info("The music for the simulation is started");
        return name;
    }

    @Override
    public String getMusicSetIdForCombat(CombatEngineAPI engine) {
        String musicId = super.getMusicSetIdForCombat(engine);

        if (engine.isInCampaign()) {
            musicId = getCampaignMusic(engine);
        } else if (Global.getSettings().getBoolean("cbm_mission_ost") && engine.isMission()) {
            musicId = getMissionMusic(engine);
        } else if (Global.getSettings().getBoolean("cbm_simulation_ost") && engine.isSimulation()) {
            musicId = getSimulationMusic(engine);
        } else {
            LOG.info("Music is set to default.");
        }
        return musicId;
    }
}
