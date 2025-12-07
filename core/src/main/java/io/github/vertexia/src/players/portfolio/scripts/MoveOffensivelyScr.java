package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.InterestPoint;
import io.github.vertexia.src.players.portfolio.scripts.utils.MilitaryFunc;
import io.github.vertexia.src.utils.Pair;

import java.util.Random;

public class MoveOffensivelyScr extends BaseScript {

    //This script returns the Move action that places the unit on an own city tile.
    private Random rnd;

    public MoveOffensivelyScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        return new MilitaryFunc().moveTowards(gs, ac, actions, rnd, new InterestPoint() {
            @Override
            public boolean ofInterest(GameState gs, Actor ac, int posX, int posY) {
                Board b = gs.getBoard();
                int cityIdAt = b.getCityIdAt(posX, posY);
                if(cityIdAt != -1)
                {
                    Tribe tribe = gs.getTribe(ac.getTribeId());
                    return !tribe.getCitiesID().contains(cityIdAt);
                }
                return false;
            }
        });

    }



}
