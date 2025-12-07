package io.github.vertexia.src.core.actions.unitactions.factory;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.unitactions.HealOthers;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

import java.util.LinkedList;

public class HealOthersFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        LinkedList<Action> actions = new LinkedList<>();
        Unit unit = (Unit) actor;

        //Only if the unit can 'attack'
        if(unit.canAttack())
        {
            HealOthers action = new HealOthers(unit.getActorId());
            if(action.isFeasible(gs)){
                actions.add(action);
            }
        }

        return actions;
    }

}
