package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.cityactions.LevelUp;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Pair;

import java.util.ArrayList;
import java.util.Random;

import static io.github.vertexia.src.core.Types.CITY_LEVEL_UP.*;

public class LevelUpMilitaryScr extends BaseScript {

    //Selects the action that levels up following a growth strategy: city_wall, border expansion, superunit.
    // If workshop or explorer, picks workshop.

    private Random rnd;

    public LevelUpMilitaryScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        for(Action act : actions) {
            LevelUp action = (LevelUp) act;
            if (action.getBonus() == CITY_WALL || action.getBonus() == BORDER_GROWTH || action.getBonus() == SUPERUNIT || action.getBonus() == WORKSHOP)
                return new Pair<>(act,DEFAULT_VALUE);
        }
        return null;
    }

}
