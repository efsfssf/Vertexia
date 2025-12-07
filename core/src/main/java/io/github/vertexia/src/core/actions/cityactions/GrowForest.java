package io.github.vertexia.src.core.actions.cityactions;

import io.github.vertexia.src.core.TribesConfig;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.utils.Vector2d;

import java.util.LinkedList;

public class GrowForest extends CityAction
{

    public GrowForest(int cityId)
    {
        super(Types.ACTION.GROW_FOREST);
        super.cityId = cityId;
    }


    @Override
    public boolean isFeasible(final GameState gs) {
        City city = (City) gs.getActor(this.cityId);
        Board b = gs.getBoard();
        if(b.getTerrainAt(targetPos.x, targetPos.y) != Types.TERRAIN.PLAIN) return false;
        if(b.getCityIdAt(targetPos.x, targetPos.y) != city.getActorId()) return false;

        Tribe t = gs.getTribe(city.getTribeId());
        if(t.getStars() < TribesConfig.GROW_FOREST_COST) return false;
        return t.getTechTree().isResearched(Types.TECHNOLOGY.SPIRITUALISM);
    }

    @Override
    public Action copy() {
        GrowForest grow = new GrowForest(this.cityId);
        grow.setTargetPos(this.targetPos.copy());
        return grow;
    }

    public String toString()
    {
        return "GROW_FOREST by city " + this.cityId+ " at " + targetPos;
    }
}
