/**
 * @ Author: Laroustine
 * @ Modified time: 25/09 11:42
 * @ Modified by: Laroustine
 * @ Description: This script has been made by me ↖(^▽^)↗
 */
package data.scripts.console;

import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;

public class ShowBattleCustomData implements BaseCommand {

    @Override
    public CommandResult runCommand(String args, CommandContext context) {
        CombatEngineAPI bce =  Global.getCombatEngine();
        
        if (context.isInCombat()) {
            Console.showMessage("KEYSET : " + bce.getCustomData().keySet().toString());
            Console.showMessage("Entryset : " + bce.getCustomData().entrySet().toString());
            Console.showMessage("All : " + bce.getCustomData().toString());
            return CommandResult.SUCCESS;
        }
        Console.showMessage(CommonStrings.ERROR_COMBAT_ONLY);
        return CommandResult.WRONG_CONTEXT;
    }

}
