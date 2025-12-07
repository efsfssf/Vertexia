package io.github.vertexia.src.core.actions.cityactions.command;

import io.github.vertexia.src.core.TribesConfig;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.cityactions.ClearForest;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

public class ClearForestCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        ClearForest action = (ClearForest)a;
        if (action.isFeasible(gs)){
            int cityId = action.getCityId();
            City city = (City) gs.getActor(cityId);
            Vector2d targetPos = action.getTargetPos();
            gs.getBoard().setTerrainAt(targetPos.x, targetPos.y, Types.TERRAIN.PLAIN);
            gs.getTribe(city.getTribeId()).addStars(TribesConfig.CLEAR_FOREST_STAR);
            return true;
        }
        return false;
    }
}
