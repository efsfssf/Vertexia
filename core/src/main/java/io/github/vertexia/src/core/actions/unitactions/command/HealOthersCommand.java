package io.github.vertexia.src.core.actions.unitactions.command;

import io.github.vertexia.src.core.TribesConfig;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.unitactions.HealOthers;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.GameState;
import java.util.ArrayList;

public class HealOthersCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        HealOthers action = (HealOthers)a;
        int unitId = action.getUnitId();

        if (action.isFeasible(gs)) {
            Unit unit = (Unit) gs.getActor(unitId);
            ArrayList<Unit> targets = action.getTargets(gs);

            for (Unit target: targets) {
                target.setCurrentHP(Math.min(target.getCurrentHP() + TribesConfig.MINDBENDER_HEAL, target.getMaxHP()));
            }

            unit.transitionToStatus(Types.TURN_STATUS.ATTACKED);
            return true;
        }
        return false;
    }
}
