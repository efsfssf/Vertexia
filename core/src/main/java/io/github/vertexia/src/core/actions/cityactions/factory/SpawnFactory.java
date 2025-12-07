package io.github.vertexia.src.core.actions.cityactions.factory;

import io.github.vertexia.src.core.TechnologyTree;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.cityactions.Spawn;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

import java.util.LinkedList;

public class SpawnFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        City city = (City) actor;
        LinkedList<Action> actions = new LinkedList<>();

        for(Types.UNIT unit: Types.UNIT.values()){
            Spawn newAction = new Spawn(city.getActorId());
            newAction.setUnitType(unit);
            newAction.setTargetPos(city.getPosition().copy());
            if (newAction.isFeasible(gs)) {
                actions.add(newAction);
            }
        }

        return actions;
    }

}
