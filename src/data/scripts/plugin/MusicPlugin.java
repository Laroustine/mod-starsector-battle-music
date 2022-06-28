/**
 * @ Author: Laroustine
 * @ Modified time: 28/06 12:39
 * @ Modified by: Laroustine
 * @ Description: This script has been made by me ↖(^▽^)↗
 */
package data.scripts.plugin;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.impl.MusicPlayerPluginImpl;

import org.apache.log4j.Logger;

public class MusicPlugin extends MusicPlayerPluginImpl {
    protected final Logger LOG = Logger.getLogger(MusicPlugin.class);

    protected float getFleetRatio(CampaignFleetAPI player, CampaignFleetAPI ennemy) {
        if (player.getFleetPoints() == 0) {
            return 100;
        }
        return ennemy.getFleetPoints() / player.getFleetPoints();
    }

    protected String getCampaignMusic(CombatEngineAPI engine) {
        FactionAPI faction = engine.getContext().getOtherFleet().getFaction();
        String musicId = faction.getMusicMap().get("battle");
        float ratio = getFleetRatio(engine.getContext().getOtherFleet(), engine.getContext().getPlayerFleet());

        // Special Cases
        if (engine.isEnemyInFullRetreat()) {
            musicId = faction.getMusicMap().get("battle_retreat");
        } else if (ratio >= 1.6) {
            musicId = faction.getMusicMap().get("battle_advantage");
        } else if (ratio <= 0.4) {
            musicId = faction.getMusicMap().get("battle_losing");
        }
        // Handle Find
        if (musicId != null) {
            LOG.info("The music for this faction is : " + musicId);
        } else {
            LOG.info("The Faction [" + faction.getDisplayName() + "] does not have custom music.");
            musicId = super.getMusicSetIdForCombat(engine);
        }
        return musicId;
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
        LOG.info("The music for the simulation is started");
        return "music_sim_battle";
    }

    @Override
    public String getMusicSetIdForCombat(CombatEngineAPI engine) {
        String musicId = super.getMusicSetIdForCombat(engine);

        if (engine.isInCampaign()) {
            musicId = getCampaignMusic(engine);
        } else if (Global.getSettings().getBoolean("mission_ost") && engine.isMission()) {
            musicId = getMissionMusic(engine);
        } else if (Global.getSettings().getBoolean("simulation_ost") && engine.isSimulation()) {
            musicId = getSimulationMusic(engine);
        } else {
            LOG.info("Music is set to default.");
        }
        return musicId;
    }
}
