package io.github.vertexia.src.core.actions.cityactions.factory;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.cityactions.ResourceGathering;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

import java.util.LinkedList;

public class ResourceGatheringFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {

        City city = (City) actor;
        Board b = gs.getBoard();
        LinkedList<Action> resourceActions = new LinkedList<>();
        int cityId = city.getActorId();

        // loop through bounds of city and add resource actions if they are feasible
        for(Vector2d pos : b.getCityTiles(cityId)) {

            Types.RESOURCE r = b.getResourceAt(pos.x, pos.y);
            if (r == null)
                continue;
            ResourceGathering resourceAction = new ResourceGathering(cityId);
            resourceAction.setResource(r);
            resourceAction.setTargetPos(new Vector2d(pos.x, pos.y));
            if (resourceAction.isFeasible(gs)) {
                resourceActions.add(resourceAction);
            }

        }
        return resourceActions;
    }

}
