/**
 * @ Author: Laroustine
 * @ Modified time: 24/09 17:45
 * @ Modified by: Laroustine
 * @ Description: This script has been made by me ↖(^▽^)↗
 */
package data.scripts.console;

import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;

public class TypeOfFight implements BaseCommand {

    @Override
    public CommandResult runCommand(String args, CommandContext context) {
        if (!context.isInCombat()) {
            Console.showMessage(CommonStrings.ERROR_COMBAT_ONLY);
            return CommandResult.WRONG_CONTEXT;
        }
        float ratio = 0.0f;
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
