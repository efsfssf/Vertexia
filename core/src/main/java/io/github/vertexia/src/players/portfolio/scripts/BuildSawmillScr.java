package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.BuildingFunc;
import io.github.vertexia.src.utils.Pair;

import java.util.Random;

import static io.github.vertexia.src.core.Types.BUILDING.SAWMILL;

public class BuildSawmillScr extends BaseScript {

    //Selects the action that builds a Sawmill in the best possible place.

    private Random rnd;

    public BuildSawmillScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        return new BuildingFunc().buildSupportBuilding(SAWMILL, gs, actions, rnd);
    }

}
