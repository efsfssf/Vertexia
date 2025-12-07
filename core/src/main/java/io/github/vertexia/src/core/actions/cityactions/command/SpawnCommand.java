package io.github.vertexia.src.core.actions.cityactions.command;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.cityactions.ResourceGathering;
import io.github.vertexia.src.core.actions.cityactions.Spawn;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

public class SpawnCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        Spawn action = (Spawn)a;
        if (action.isFeasible(gs)){
            int cityId = action.getCityId();
            Types.UNIT unit_type = action.getUnitType();
            City city = (City) gs.getActor(cityId);
            Vector2d cityPos = city.getPosition();
            Unit newUnit = Types.UNIT.createUnit(new Vector2d(cityPos.x, cityPos.y), 0, false, city.getActorId(), city.getTribeId(), unit_type);
            gs.getBoard().addUnit(city, newUnit);
            Tribe tribe = gs.getTribe(city.getTribeId());
            tribe.subtractStars(unit_type.getCost());
            tribe.addScore(unit_type.getPoints());
            return true;
        }
        return false;
    }
}
