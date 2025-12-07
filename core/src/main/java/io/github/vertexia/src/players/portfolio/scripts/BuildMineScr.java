package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.BuildingFunc;
import io.github.vertexia.src.utils.Pair;

import java.util.Random;

import static io.github.vertexia.src.core.Types.BUILDING.*;

public class BuildMineScr extends BaseScript {

    //Selects the action that builds a Forge in the best possible place.

    private Random rnd;

    public BuildMineScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        return new BuildingFunc().buildBaseBuilding(MINE, FORGE, gs, actions, rnd);
    }

}
