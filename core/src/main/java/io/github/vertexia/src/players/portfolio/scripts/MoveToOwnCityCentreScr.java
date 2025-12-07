package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.InterestPoint;
import io.github.vertexia.src.players.portfolio.scripts.utils.MilitaryFunc;
import io.github.vertexia.src.utils.Pair;

import java.util.Random;

public class MoveToOwnCityCentreScr extends BaseScript {

    //This script returns the Move action that places the unit on an own city centre.
    private Random rnd;

    public MoveToOwnCityCentreScr(Random rnd)
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
                Types.TERRAIN t = b.getTerrainAt(posX, posY);
                if(t == Types.TERRAIN.CITY)
                {
                    //if city does not belong to me.
                    City c = b.getCityInBorders(posX, posY);
                    return c.getTribeId() == ac.getTribeId();
                }
                return false;
            }
        });

    }



}
