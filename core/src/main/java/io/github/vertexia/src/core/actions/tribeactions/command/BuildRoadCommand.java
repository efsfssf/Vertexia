package io.github.vertexia.src.core.actions.tribeactions.command;

import io.github.vertexia.src.core.TribesConfig;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.tribeactions.BuildRoad;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

import java.util.Vector;

public class BuildRoadCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        BuildRoad action = (BuildRoad)a;
        if(action.isFeasible(gs))
        {
            int tribeId = action.getTribeId();
            Vector2d position = action.getPosition();
            Tribe tribe = gs.getTribe(tribeId);
            tribe.subtractStars(TribesConfig.ROAD_COST);
            gs.getBoard().addRoad(position.x, position.y);
            return true;
        }
        return false;
    }
}
