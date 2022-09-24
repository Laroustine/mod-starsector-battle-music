/**
 * @ Author: Laroustine
 * @ Modified time: 24/09 19:31
 * @ Modified by: Laroustine
 * @ Description: This script has been made by me ↖(^▽^)↗
 */
package data.scripts.console;

import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;

public class ShowTags implements BaseCommand {

    @Override
    public CommandResult runCommand(String args, CommandContext context) {
        CombatEngineAPI bce =  Global.getCombatEngine();
        
        if (context.isInCombat() && bce.isInCampaign()) {
            String pfleet = bce.getContext().getPlayerFleet().getTags().toString();
            String efleet = bce.getContext().getOtherFleet().getTags().toString();
            
            Console.showMessage("Your Fleet : " + pfleet);
            Console.showMessage("Enemmy Fleet : " + efleet);
            return CommandResult.SUCCESS;
        }
        Console.showMessage(CommonStrings.ERROR_COMBAT_ONLY + " Works only in real battles.");
        return CommandResult.WRONG_CONTEXT;
    }

}
