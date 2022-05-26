/*
** Script by Laroustine
*/
package data.scripts.plugin;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.impl.MusicPlayerPluginImpl;

import org.apache.log4j.Logger;

public class MusicPlugin extends MusicPlayerPluginImpl {
    protected final Logger LOG = Logger.getLogger(MusicPlugin.class);

    @Override
    public String getMusicSetIdForCombat(CombatEngineAPI engine) {
        if (engine.isInCampaign()) {
            FactionAPI faction = engine.getContext().getOtherFleet().getFaction();
            String musicId = faction.getMusicMap().get("battle");

            if (musicId != null) {
                LOG.info("The music for this faction is : " + musicId);
                return musicId;
            }
            LOG.info("The Faction [" + faction.getDisplayName() + "] does not have custom music.");
        } else if (engine.isMission()) {
            LOG.info("The Mission [" + engine.getMissionId() + "] does not have custom music.");
            LOG.info("Missions don't have anyway of having custom music yet.");
        } else if (engine.isSimulation()) {
            LOG.info("Music is set to default.");
        }
        return super.getMusicSetIdForCombat(engine);
    }
}
