package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.MilitaryFunc;
import io.github.vertexia.src.utils.Pair;

public class AttackWeakestScr extends BaseScript {

    //This script returns the attack action that targets the weakest enemy unit. We
    //  understand the weakest unit as the one with the lowest DEFENCE value.

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac) {
        return new MilitaryFunc().getActionByActorAttr(gs, actions, ac, Feature.DEFENCE, false);
    }

}
