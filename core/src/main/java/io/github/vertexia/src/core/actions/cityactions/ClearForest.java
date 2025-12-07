package io.github.vertexia.src.core.actions.cityactions;

import io.github.vertexia.src.core.TribesConfig;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

import java.util.LinkedList;

public class ClearForest extends CityAction
{

    public ClearForest(int cityId) {
        super(Types.ACTION.CLEAR_FOREST);
        super.cityId = cityId;
    }


    @Override
    public boolean isFeasible(final GameState gs) {
        Board b = gs.getBoard();

        if(b.getTerrainAt(targetPos.x, targetPos.y) != Types.TERRAIN.FOREST) return false;
        if(b.getCityIdAt(targetPos.x, targetPos.y) != cityId) return false;
        City city = (City) gs.getActor(this.cityId);
        return gs.getTribe(city.getTribeId()).getTechTree().isResearched(Types.TECHNOLOGY.FORESTRY);
    }

    @Override
    public Action copy() {
        ClearForest clear = new ClearForest(this.cityId);
        clear.setTargetPos(this.targetPos.copy());
        return clear;
    }

    public String toString()
    {
        return "CLEAR_FOREST by city " + this.cityId+ " at " + targetPos;
    }
}
