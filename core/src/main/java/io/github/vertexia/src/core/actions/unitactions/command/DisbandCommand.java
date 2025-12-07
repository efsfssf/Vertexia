package io.github.vertexia.src.core.actions.unitactions.command;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.unitactions.Convert;
import io.github.vertexia.src.core.actions.unitactions.Disband;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;

public class DisbandCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        Disband action = (Disband)a;
        int unitId = action.getUnitId();

        Unit unit = (Unit) gs.getActor(unitId);
        Board b = gs.getBoard();
        Tribe t = gs.getTribe(unit.getTribeId());
        City c = (City) b.getActor(unit.getCityId());

        if(action.isFeasible(gs))
        {
            int starsGained = (int) (unit.COST / 2.0); //half, rounded down
            t.addStars(starsGained);
            b.removeUnitFromBoard(unit);
            b.removeUnitFromCity(unit, c, t);
            t.subtractScore(unit.getType().getPoints());
            return true;
        }

        return false;
    }
}
