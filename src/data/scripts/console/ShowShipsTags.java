/**
 * @ Author: Laroustine
 * @ Modified time: 25/09 01:29
 * @ Modified by: Laroustine
 * @ Description: This script has been made by me ↖(^▽^)↗
 */
package data.scripts.console;

import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

public class ShowShipsTags implements BaseCommand {
    private void getShipTags(CampaignFleetAPI fleet) {
        for (FleetMemberAPI ship : fleet.getFleetData().getMembersListCopy()) {
            Console.showMessage("- " + ship.getShipName() + " (" + ship.getHullSpec().getHullName() + "): " + ship.getHullSpec().getTags());
        }
    }
    
    @Override
    public CommandResult runCommand(String args, CommandContext context) {
        CombatEngineAPI bce =  Global.getCombatEngine();
        
        if (context.isInCombat() && bce.isInCampaign()) {
            Console.showMessage("Your Fleet :");
            getShipTags(bce.getContext().getPlayerFleet());
            Console.showMessage("Enemmy Fleet :");
            getShipTags(bce.getContext().getOtherFleet());
            return CommandResult.SUCCESS;
        }
        Console.showMessage(CommonStrings.ERROR_COMBAT_ONLY + " Works only in real battles.");
        return CommandResult.WRONG_CONTEXT;
    }

}
