package io.github.vertexia.src.core.actions.tribeactions.factory;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.tribeactions.BuildRoad;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

import java.util.ArrayList;
import java.util.LinkedList;

public class BuildRoadFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        LinkedList<Action> actions = new LinkedList<>();
        Tribe tribe = (Tribe) actor;

        //if not, nothing to build.
        if (!tribe.canBuildRoads())
            return actions;

        //We are able to build roads, let's find where can this be built
        ArrayList<Vector2d> positions = gs.getBoard().getBuildRoadPositions(tribe.getTribeId());
        for (Vector2d v : positions) {
            BuildRoad br = new BuildRoad(tribe.getTribeId());
            br.setPosition(v);
            actions.add(br);
        }

        //All the actions.
        return actions;
    }

}
