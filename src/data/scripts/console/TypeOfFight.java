/**
 * @ Author: Laroustine
 * @ Modified time: 25/09 01:22
 * @ Modified by: Laroustine
 * @ Description: This script has been made by me ↖(^▽^)↗
 */
package data.scripts.console;

import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;

import data.scripts.plugin.MusicPlugin;

public class TypeOfFight implements BaseCommand {

    @Override
    public CommandResult runCommand(String args, CommandContext context) {
        if (!context.isInCombat()) {
            Console.showMessage(CommonStrings.ERROR_COMBAT_ONLY);
            return CommandResult.WRONG_CONTEXT;
        }
        CombatEngineAPI ce = Global.getCombatEngine();
        float ratio = MusicPlugin.getFleetRatio(ce.getContext().getOtherFleet(), ce.getContext().getPlayerFleet());
        String type = "NONE";

        if (ratio >= 1.6) {
            type = "battle_advantage";
        } else if (ratio <= 0.4) {
            type = "battle_losing";
        } else {
            type = "battle";
        }
        Console.showMessage("The fight is currently in [" + type + "] and has a ratio of " + ratio);
        return CommandResult.SUCCESS;
    }

}
