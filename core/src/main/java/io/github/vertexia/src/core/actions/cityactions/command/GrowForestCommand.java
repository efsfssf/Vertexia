package io.github.vertexia.src.core.actions.cityactions.command;

import io.github.vertexia.src.core.TribesConfig;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.cityactions.BurnForest;
import io.github.vertexia.src.core.actions.cityactions.GrowForest;
import io.github.vertexia.src.core.actors.Building;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

public class GrowForestCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        GrowForest action = (GrowForest)a;
        if (action.isFeasible(gs)){
            Board b = gs.getBoard();
            int cityId = action.getCityId();
            City city = (City) gs.getActor(cityId);
            Vector2d targetPos = action.getTargetPos();
            b.setTerrainAt(targetPos.x, targetPos.y, Types.TERRAIN.FOREST);
            b.setResourceAt(targetPos.x, targetPos.y, null);
            gs.getTribe(city.getTribeId()).subtractStars(TribesConfig.GROW_FOREST_COST);
            return true;
        }
        return false;
    }
}
