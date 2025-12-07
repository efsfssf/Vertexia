package io.github.vertexia.src.core.actions.unitactions.factory;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.unitactions.MakeVeteran;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.GameState;

import java.util.LinkedList;

public class MakeVeteranFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        Unit unit = (Unit) actor;
        LinkedList<Action> actions = new LinkedList<>();

        MakeVeteran action = new MakeVeteran(unit.getActorId());
        if(action.isFeasible(gs))
        {
            actions.add(action);
        }

        return actions;
    }

}
