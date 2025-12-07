package io.github.vertexia.src.core.actions.unitactions.factory;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.unitactions.Disband;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.GameState;

import java.util.LinkedList;

public class DisbandFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        LinkedList<Action> disbands = new LinkedList<>();
        Unit unit = (Unit) actor;

        Disband disbandAction = new Disband(unit.getActorId());
        if(disbandAction.isFeasible(gs))
            disbands.add(disbandAction);

        return disbands;
    }

}
