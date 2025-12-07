package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Pair;

import java.util.Random;

public class RandomScr extends BaseScript {

    private Random rnd;

    public RandomScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac) {
        int nActions = actions.size();
        return new Pair<>(actions.get(rnd.nextInt(nActions)), DEFAULT_VALUE);
    }

}
