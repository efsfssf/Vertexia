package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.cityactions.LevelUp;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Pair;

import java.util.ArrayList;
import java.util.Random;

import static io.github.vertexia.src.core.Types.CITY_LEVEL_UP.*;

public class LevelUpGrowthScr extends BaseScript {

    //Selects the action that levels up following a growth strategy: workshop, resources or pop_growth.
    // If park or super unit, picks at random.

    private Random rnd;

    public LevelUpGrowthScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        ArrayList<Action> candidate_actions = new ArrayList<>();

        for(Action act : actions)
        {
            LevelUp action = (LevelUp) act;
            if(action.getBonus() == WORKSHOP || action.getBonus() == RESOURCES || action.getBonus() == POP_GROWTH)
                return new Pair<>(act, DEFAULT_VALUE);

            if(action.getBonus() == PARK || action.getBonus() == SUPERUNIT)
                candidate_actions.add(action);
        }

        int nActions = candidate_actions.size();
        if( nActions > 0)
            return new Pair<>(candidate_actions.get(rnd.nextInt(nActions)), DEFAULT_VALUE);
        return null;
    }

}
