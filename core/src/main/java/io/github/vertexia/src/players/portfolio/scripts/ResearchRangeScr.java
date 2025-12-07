package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.tribeactions.ResearchTech;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.MilitaryFunc;
import io.github.vertexia.src.utils.Pair;

import java.util.ArrayList;
import java.util.Random;

import static io.github.vertexia.src.core.Types.TECHNOLOGY.*;

public class ResearchRangeScr extends BaseScript {

    //Selects the action that researchers a tech in the Range branch.

    private Random rnd;

    public ResearchRangeScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        if(actions.size() == 1)
            return new Pair<>(actions.get(0), DEFAULT_VALUE);

        ArrayList<Types.TECHNOLOGY> techs = new ArrayList<>();
        techs.add(HUNTING);
        techs.add(ARCHERY);
        techs.add(FORESTRY);
        techs.add(SPIRITUALISM);
        techs.add(MATHEMATICS);

        return new MilitaryFunc().getPreferredResearchTech(gs, actions, techs, rnd);
    }

}
