package io.github.vertexia.src.core.actions.unitactions.command;

import io.github.vertexia.src.core.TribesConfig;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.unitactions.MakeVeteran;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.GameState;

public class MakeVeteranCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        MakeVeteran action = (MakeVeteran)a;
        int unitId = action.getUnitId();
        Unit unit = (Unit) gs.getActor(unitId);
        if(action.isFeasible(gs))
        {
            unit.setVeteran(true);
            unit.setMaxHP(unit.getMaxHP() + TribesConfig.VETERAN_PLUS_HP);
            unit.setCurrentHP(unit.getMaxHP());
            return true;
        }
        return false;
    }
}
