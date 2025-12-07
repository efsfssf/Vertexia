package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.cityactions.GrowForest;
import io.github.vertexia.src.core.actions.unitactions.Move;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.BuildingFunc;
import io.github.vertexia.src.players.portfolio.scripts.utils.InterestPoint;
import io.github.vertexia.src.players.portfolio.scripts.utils.MilitaryFunc;
import io.github.vertexia.src.utils.Pair;
import io.github.vertexia.src.utils.Vector2d;

import java.util.ArrayList;
import java.util.Random;

import static io.github.vertexia.src.core.Types.BUILDING.LUMBER_HUT;

public class MoveToCaptureScr extends BaseScript {

    //This script returns the Move action that places the unit on a ruin, village or enemy city.
    private Random rnd;

    public MoveToCaptureScr(Random rnd)
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
                Types.RESOURCE r = b.getResourceAt(posX, posY);
                Types.TERRAIN t = b.getTerrainAt(posX, posY);
                if(r == Types.RESOURCE.RUINS || t == Types.TERRAIN.VILLAGE)
                    return true;
                else if(t == Types.TERRAIN.CITY)
                {
                    //if city does not belong to me.
                    City c = b.getCityInBorders(posX, posY);
                    return c.getTribeId() != ac.getTribeId();
                }
                return false;
            }
        });

    }



}
