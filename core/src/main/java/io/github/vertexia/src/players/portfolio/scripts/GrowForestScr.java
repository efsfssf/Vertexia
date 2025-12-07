package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.cityactions.GrowForest;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.BuildingFunc;
import io.github.vertexia.src.utils.Pair;
import io.github.vertexia.src.utils.Utils;
import io.github.vertexia.src.utils.Vector2d;

import java.util.ArrayList;
import java.util.Random;

import static io.github.vertexia.src.core.Types.BUILDING.LUMBER_HUT;

public class GrowForestScr extends BaseScript {

    //This script returns the Grow Forest action that grows a forest if it's a good location for it.
    private Random rnd;

    public GrowForestScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        ArrayList<Action> candidate_actions = new ArrayList<>();
        double bestScore = 0;

        for(Action act : actions)
        {
            GrowForest action = (GrowForest)act;
            BuildingFunc f = new BuildingFunc();
            Vector2d targetPos = action.getTargetPos();

            double score = f.valueForBaseBuilding(gs, targetPos, new Types.BUILDING[]{LUMBER_HUT}, action.getCityId());
            //Grow if good place for a lumber hut

            if (score > bestScore) {
                candidate_actions.clear();
                bestScore = score;
            }
            if (score == bestScore)
                candidate_actions.add(act);

        }

        int nActions = candidate_actions.size();
        if( nActions > 0)
            return new Pair<> (candidate_actions.get(rnd.nextInt(nActions)), bestScore);
        return null;
    }



}
