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
import io.github.vertexia.src.utils.Vector2d;

import java.util.Random;

public class MoveToEmbarkScr extends BaseScript {

    //This script returns the Move action that places the unit on an own city centre.
    private Random rnd;

    public MoveToEmbarkScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        Vector2d pos = ac.getPosition();
        Types.TERRAIN t = gs.getBoard().getTerrainAt(pos.x, pos.y);
        if(t.isWater())
            return null;

        return new MilitaryFunc().moveTowards(gs, ac, actions, rnd, new InterestPoint() {
            @Override
            public boolean ofInterest(GameState gs, Actor ac, int posX, int posY) {
                Board b = gs.getBoard();
                Types.BUILDING building = b.getBuildingAt(posX, posY);
                if(building == Types.BUILDING.PORT)
                {
                    //if it's in a city that belongs to this tribe.
                    City c = b.getCityInBorders(posX, posY);
                    if(c != null)
                        return c.getTribeId() == ac.getTribeId();
                }
                return false;
            }
        });

    }



}
