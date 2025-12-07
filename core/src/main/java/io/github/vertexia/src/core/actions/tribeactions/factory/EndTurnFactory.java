package io.github.vertexia.src.core.actions.tribeactions.factory;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.tribeactions.EndTurn;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;

import java.util.LinkedList;

public class EndTurnFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        Tribe tribe = (Tribe) actor;
        LinkedList<Action> endTurns = new LinkedList<>();
        if(gs.canEndTurn(tribe.getTribeId()))
            endTurns.add(new EndTurn(tribe.getActorId()));
        return endTurns;
    }

}
