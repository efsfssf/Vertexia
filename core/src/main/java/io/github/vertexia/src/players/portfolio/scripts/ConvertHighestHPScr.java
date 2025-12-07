package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.MilitaryFunc;
import io.github.vertexia.src.utils.Pair;

public class ConvertHighestHPScr extends BaseScript {

    //This script returns the convert action that targets the enemy unit with highest current HP.


    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac) {
        return new MilitaryFunc().getActionByActorAttr(gs, actions, ac, Feature.HP, true);
    }

}
