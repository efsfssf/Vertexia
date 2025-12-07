package io.github.vertexia.src.core.actions.cityactions;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.actors.Building;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.core.actors.City;

public class Destroy extends CityAction
{
    public Destroy(int cityId)
    {
        super(Types.ACTION.DESTROY);
        super.cityId = cityId;
    }

    @Override
    public boolean isFeasible(final GameState gs)
    {
        City city = (City) gs.getActor(this.cityId);
        if(gs.getBoard().getBuildingAt(targetPos.x, targetPos.y) == null) return false;
        Building buildingToRemove = city.getBuilding(targetPos.x, targetPos.y);
        if(buildingToRemove == null || buildingToRemove.type == null) return false;
        if(gs.getBoard().getCityIdAt(targetPos.x, targetPos.y) != this.cityId) return false;
        return gs.getTribe(city.getTribeId()).getTechTree().isResearched(Types.TECHNOLOGY.CONSTRUCTION);
    }

    @Override
    public Action copy() {
        Destroy destroy = new Destroy(this.cityId);
        destroy.setTargetPos(this.targetPos.copy());
        return destroy;
    }

    public String toString()
    {
        return "DESTROY by city " + this.cityId+ " at " + targetPos;
    }
}
