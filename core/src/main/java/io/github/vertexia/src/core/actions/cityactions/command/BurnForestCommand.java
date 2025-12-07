package io.github.vertexia.src.core.actions.cityactions.command;

import io.github.vertexia.src.core.TribesConfig;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.cityactions.BurnForest;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

public class BurnForestCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        BurnForest action = (BurnForest)a;
        int cityId = action.getCityId();
        City city = (City) gs.getActor(cityId);
        if (action.isFeasible(gs)){
            Vector2d targetPos = action.getTargetPos();
            Board b = gs.getBoard();
            Tribe t = gs.getTribe(city.getTribeId());
            b.setTerrainAt(targetPos.x, targetPos.y, Types.TERRAIN.PLAIN);
            b.setResourceAt(targetPos.x, targetPos.y, Types.RESOURCE.CROPS);
            t.subtractStars(TribesConfig.BURN_FOREST_COST);
            return true;
        }
        return false;
    }
}
