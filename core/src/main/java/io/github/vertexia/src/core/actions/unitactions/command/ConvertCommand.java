package io.github.vertexia.src.core.actions.unitactions.command;

import io.github.vertexia.src.core.Diplomacy;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.unitactions.Convert;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.GameState;

import static io.github.vertexia.src.core.TribesConfig.CONVERT_REPERCUSSION;

public class ConvertCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        Convert action = (Convert) a;

        //Getting current diplomacy
        Diplomacy d = gs.getBoard().getDiplomacy();

        //Check if action is feasible before execution
        if (action.isFeasible(gs)) {
            int unitId = action.getUnitId();
            int targetId = action.getTargetId();
            Unit target = (Unit) gs.getActor(targetId);
            Unit unit = (Unit) gs.getActor(unitId);
            Tribe targetTribe = gs.getTribe(target.getTribeId());

            //remove the unit from its original city.
            int cityId = target.getCityId();
            City c = (City) gs.getActor(cityId);
            gs.getBoard().removeUnitFromCity(target, c, targetTribe);

            //add tribe to converted unit
            target.setTribeId(unit.getTribeId());
            gs.getActiveTribe().addExtraUnit(target);

            // Updating relationship between tribes, deducting 5
            d.updateAllegiance(CONVERT_REPERCUSSION, unit.getTribeId(), target.getTribeId());
            // Checks consequences of the update
            d.checkConsequences(CONVERT_REPERCUSSION, unit.getTribeId(), target.getTribeId());

            //manage status of the units after the action is executed
            unit.transitionToStatus(Types.TURN_STATUS.ATTACKED);
            target.setStatus(Types.TURN_STATUS.FINISHED);
            return true;
        }
        return false;
    }
}
