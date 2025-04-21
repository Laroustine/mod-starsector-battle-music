/**
 * @ Author: Laroustine
 * @ Modified time: 2025/04/20 23:30
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
        float RATIO_DESTROY = Global.getSettings().getFloat("cbm_destroy_value");
        float RATIO_LOSE = Global.getSettings().getFloat("cbm_lose_value");
        float RATIO_WIN = Global.getSettings().getFloat("cbm_win_value");
        float RATIO_WIN_XTREM = Global.getSettings().getFloat("cbm_xtrem_win_value");
        String type = MusicPlugin.getBattleType(ce, RATIO_DESTROY, RATIO_LOSE, RATIO_WIN, RATIO_WIN_XTREM);
        Console.showMessage("The fight is currently in [" + (type == null ? "NONE" : type) + "] and has a ratio of " + ratio);
        return CommandResult.SUCCESS;
    }

}
