package io.github.vertexia.src.core.actions.cityactions;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;
import io.github.vertexia.src.core.Types;

public class CityAction extends Action {

    public CityAction(Types.ACTION aType)
    {
        this.actionType = aType;
    }

    protected int cityId;
    protected Vector2d targetPos;

    /** Setters and getters */

    public int getCityId() { return cityId; }
    public Vector2d getTargetPos() { return targetPos; }

    public void setCityId(int cityId) {this.cityId = cityId; }
    public void setTargetPos(Vector2d pos) { targetPos = pos; }

    @Override
    public boolean isFeasible(GameState gs) {
        return false;
    }

    @Override
    public Action copy() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof CityAction))
            return false;
        CityAction other = (CityAction) o;

        return cityId == other.cityId && actionType == other.actionType && targetPos == other.getTargetPos();
    }
}
