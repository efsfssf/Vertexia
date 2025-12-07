package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.MilitaryFunc;
import io.github.vertexia.src.utils.Pair;

import java.util.ArrayList;
import java.util.Random;

import static io.github.vertexia.src.core.Types.TECHNOLOGY.*;

public class ResearchRoadsScr extends BaseScript {


    //Selects the action that researchers a tech in the Roads branch.

    private Random rnd;

    public ResearchRoadsScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        if(actions.size() == 1)
            return new Pair<>(actions.get(0), DEFAULT_VALUE);

        ArrayList<Types.TECHNOLOGY> techs = new ArrayList<>();
        techs.add(RIDING);
        techs.add(ROADS);
        techs.add(FREE_SPIRIT);
        techs.add(CHIVALRY);
        techs.add(TRADE);

        return new MilitaryFunc().getPreferredResearchTech(gs, actions, techs, rnd);
    }

}
