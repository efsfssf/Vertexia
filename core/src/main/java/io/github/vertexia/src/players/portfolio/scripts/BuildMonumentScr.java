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

public class BuildMonumentScr extends BaseScript {

    //Selects the action that builds a monument in the best possible place.

    private Random rnd;

    public BuildMonumentScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        ArrayList<Action> candidate_actions = new ArrayList<>();
        double bestScore = Double.NEGATIVE_INFINITY;

        //Looks for the first monument that can be built in an idle space. Returns null if no idle spaces.
        Types.BUILDING[] targets = new Types.BUILDING[]{ALTAR_OF_PEACE, EMPERORS_TOMB, EYE_OF_GOD, GATE_OF_POWER,
                PARK_OF_FORTUNE, TOWER_OF_WISDOM, GRAND_BAZAR,};

        for(Types.BUILDING target : targets){
            Pair<Action, Double> p = new BuildingFunc().buildInIdle(target, gs, actions, rnd);
            if(p != null) {
                Action buildMonument = p.getFirst();
                double score = p.getSecond();
                if (score > bestScore) {
                    candidate_actions.clear();
                    bestScore = score;
                }
                if (score == bestScore)
                    candidate_actions.add(buildMonument);
            }
        }

        int nActions = candidate_actions.size();
        if( nActions > 0)
            return new Pair<>(candidate_actions.get(rnd.nextInt(nActions)), bestScore);
        return null;
    }

}
