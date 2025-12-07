package io.github.vertexia.src.core.actions.cityactions.factory;

import io.github.vertexia.src.core.TechnologyTree;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.cityactions.Build;
import io.github.vertexia.src.core.actions.tribeactions.ResearchTech;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

import java.util.LinkedList;

public class BuildFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {

        City city = (City) actor;
        LinkedList<Action> actions = new LinkedList<>();
        Board board = gs.getBoard();
        LinkedList<Vector2d> tiles = board.getCityTiles(city.getActorId());

        for(Vector2d tile : tiles){
            for(Types.BUILDING building: Types.BUILDING.values()){
                //check if tile is empty
                if(board.getBuildingAt(tile.x, tile.y) == null) {
                    Build action = new Build(city.getActorId());
                    action.setBuildingType(building);
                    action.setTargetPos(tile.copy());
                    if (action.isFeasible(gs)) {
                        actions.add(action);
                    }
                }
            }
        }
        return actions;
    }

}
