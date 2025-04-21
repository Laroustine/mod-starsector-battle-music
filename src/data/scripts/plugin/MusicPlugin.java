/**
 * @ Author: Laroustine
 * @ Modified time: 2025/04/21 14:22
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
    public float RATIO_WIN_XTREM = Global.getSettings().getFloat("cbm_xtrem_win_value");

    public static final String MUSIC_BATTLE_XTR = "battle_xtreme";
    public static final String MUSIC_BATTLE_ADV = "battle_advantage";
    public static final String MUSIC_BATTLE = "battle";
    public static final String MUSIC_BATTLE_LOSE = "battle_losing";
    public static final String MUSIC_BATTLE_DES = "battle_destroy";
    public static final String MUSIC_BATTLE_RET = "battle_retreat";
    public static final String MUSIC_PLAYER_RET = "player_retreat";

    protected final Logger LOG = Logger.getLogger(MusicPlugin.class);

    public MusicPlugin() {
        LOG.info("Music level: "
                + RATIO_DESTROY * 100
                + "% Destroy / "
                + RATIO_LOSE * 100
                + "% Lose / "
                + RATIO_WIN * 100
                + "% Win");
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

        if (playerCount == 0) {
            return 0;
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

    public static String getBattleType(CombatEngineAPI engine, float destroy, float lose, float win, float xtrem) {
        FactionAPI faction = engine.getContext().getOtherFleet().getFaction();
        float ratio = getFleetRatio(engine.getContext().getOtherFleet(), engine.getContext().getPlayerFleet());

        if (Global.getSettings().getBoolean("cbm_player_retreat")
                && engine.getContext().getPlayerGoal().equals(FleetGoal.ESCAPE)) {
            return faction.getMusicMap().get(MUSIC_PLAYER_RET);
        } else if (engine.getContext().getOtherGoal().equals(FleetGoal.ESCAPE)) {
            return faction.getMusicMap().get(MUSIC_BATTLE_RET);
        } else if (ratio <= destroy) {
            return faction.getMusicMap().get(MUSIC_BATTLE_DES);
        } else if (ratio <= lose) {
            return faction.getMusicMap().get(MUSIC_BATTLE_LOSE);
        } else if (ratio >= xtrem) {
            return faction.getMusicMap().get(MUSIC_BATTLE_XTR);
        } else if (ratio >= win) {
            return faction.getMusicMap().get(MUSIC_BATTLE_ADV);
        }
        return null;
    }

    protected String getCampaignMusic(CombatEngineAPI engine) {
        FactionAPI faction = engine.getContext().getOtherFleet().getFaction();
        String battle = faction.getMusicMap().get(MUSIC_BATTLE);
        String musicId = getBattleType(engine, RATIO_DESTROY, RATIO_LOSE, RATIO_WIN, RATIO_WIN_XTREM);
        String temp = null;

        LOG.info("The ratio for this battle is : "
                + (int) (getFleetRatio(engine.getContext().getOtherFleet(), engine.getContext().getPlayerFleet()) * 100)
                + "%");
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
        String missionId = engine.getMissionId();
        String musicId = Global.getSector().getMemory().getString(missionId);

        if (musicId != null) {
            LOG.info("The music for this mission is : " + musicId);
        } else {
            LOG.info("The Mission [" + missionId + "] does not have custom music.");
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
        String musicDefault = super.getMusicSetIdForCombat(engine);
        String musicId = null;

        if (engine.isInCampaign()) {
            musicId = getCampaignMusic(engine);
        } else if (engine.isMission() && Global.getSettings().getBoolean("cbm_mission_ost")) {
            musicId = getMissionMusic(engine);
        } else if (engine.isSimulation() && Global.getSettings().getBoolean("cbm_simulation_ost")) {
            musicId = getSimulationMusic(engine);
        } else {
            LOG.info("Music is set to default.");
        }
        return musicId == null ? musicDefault : musicId;
    }
}
