package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.BuildingFunc;
import io.github.vertexia.src.utils.Pair;

import java.util.ArrayList;
import java.util.Random;

import static io.github.vertexia.src.core.Types.BUILDING.*;

public class BuildTempleScr extends BaseScript {

    //Selects the action that builds a temple in the best possible place.

    private Random rnd;

    public BuildTempleScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        ArrayList<Action> candidate_actions = new ArrayList<>();
        double bestScore = 0;

        //Looks for the first monument that can be built in an idle space. Returns null if no idle spaces.
        Types.BUILDING[] targets = new Types.BUILDING[]{TEMPLE, FOREST_TEMPLE, WATER_TEMPLE, MOUNTAIN_TEMPLE};

        for(Types.BUILDING target : targets){
            Pair<Action, Double> p = new BuildingFunc().buildInIdle(target, gs, actions, rnd);
            if(p != null) {
                Action buildTempleAction = p.getFirst();
                double score = p.getSecond();
                if (score > bestScore) {
                    candidate_actions.clear();
                    bestScore = score;
                }
                if (score == bestScore)
                    candidate_actions.add(buildTempleAction);
            }
        }

        int nActions = candidate_actions.size();
        if( nActions > 0)
            return new Pair<>(candidate_actions.get(rnd.nextInt(nActions)), bestScore);
        return null;
    }

}
