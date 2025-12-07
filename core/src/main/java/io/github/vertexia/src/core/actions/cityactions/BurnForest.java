package io.github.vertexia.src.core.actions.cityactions;

import io.github.vertexia.src.core.TribesConfig;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.core.actors.City;

public class BurnForest extends CityAction
{
    public BurnForest(int cityId) {
        super(Types.ACTION.BURN_FOREST);
        super.cityId = cityId;
    }

    @Override
    public boolean isFeasible(final GameState gs) {
        City city = (City) gs.getActor(this.cityId);

        Board b = gs.getBoard();
        if(b.getTerrainAt(targetPos.x, targetPos.y) != Types.TERRAIN.FOREST) return false;
        if(b.getCityIdAt(targetPos.x, targetPos.y) != this.cityId) return false;

        Tribe t = gs.getTribe(city.getTribeId());
        if(t.getStars() < TribesConfig.BURN_FOREST_COST) return false;
        return t.getTechTree().isResearched(Types.TECHNOLOGY.CHIVALRY);
    }

    @Override
    public Action copy() {
        BurnForest burn = new BurnForest(this.cityId);
        burn.setTargetPos(this.targetPos.copy());
        return burn;
    }

    public String toString()
    {
        return "BURN_FOREST by city " + this.cityId+ " at " + targetPos;
    }
}
