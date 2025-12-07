package io.github.vertexia.src.core.actions.unitactions.command;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.unitactions.Recover;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.GameState;

import java.util.ArrayList;

import static io.github.vertexia.src.core.TribesConfig.RECOVER_IN_BORDERS_PLUS_HP;
import static io.github.vertexia.src.core.TribesConfig.RECOVER_PLUS_HP;

public class RecoverCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        Recover action = (Recover)a;
        int unitId = action.getUnitId();

        Unit unit = (Unit) gs.getActor(unitId);
        if(unit == null)
            return false;

        int currentHP = unit.getCurrentHP();
        int addHP = RECOVER_PLUS_HP;

        //Check if action is feasible before execution
        if (action.isFeasible(gs)) {

            int cityID = gs.getBoard().getCityIdAt(unit.getPosition().x, unit.getPosition().y);
            if (cityID != -1){
                ArrayList<Integer> citesID = gs.getTribe(unit.getTribeId()).getCitiesID();
                if (citesID.contains(cityID)){
                    addHP += RECOVER_IN_BORDERS_PLUS_HP;
                }
            }
            unit.setCurrentHP(Math.min(currentHP + addHP, unit.getMaxHP()));
            unit.transitionToStatus(Types.TURN_STATUS.FINISHED);
            return true;
        }
        return false;
    }
}
