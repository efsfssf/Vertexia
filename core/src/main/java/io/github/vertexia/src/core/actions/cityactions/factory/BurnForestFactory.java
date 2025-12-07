package io.github.vertexia.src.core.actions.cityactions.factory;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.cityactions.BurnForest;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

import java.util.LinkedList;

public class BurnForestFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        LinkedList<Action> actions = new LinkedList<>();
        City city = (City) actor;
        LinkedList<Vector2d> tiles = gs.getBoard().getCityTiles(city.getActorId());
        for(Vector2d tile: tiles){
            BurnForest action = new BurnForest(city.getActorId());
            action.setTargetPos(new Vector2d(tile.x, tile.y));
            if(action.isFeasible(gs))
            {
                actions.add(action);
            }
        }
        return actions;
    }

}
