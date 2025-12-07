package io.github.vertexia.src.core.actions.cityactions.command;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.cityactions.Destroy;
import io.github.vertexia.src.core.actors.Building;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

public class DestroyCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        Destroy action = (Destroy)a;
        if (action.isFeasible(gs)){
            int cityId = action.getCityId();
            Vector2d targetPos = action.getTargetPos();
            City city = (City) gs.getActor(cityId);
            Building buildingToRemove = city.getBuilding(targetPos.x, targetPos.y);

            Board b = gs.getBoard();
            b.setBuildingAt(targetPos.x, targetPos.y, null);

            if(buildingToRemove.type == Types.BUILDING.PORT)
            {
                //If a port is removed, then the tile stops belonging to the trade network
                b.destroyPort(targetPos.x, targetPos.y);
            }

            city.removeBuilding(gs, buildingToRemove);
            return true;
        }
        return false;
    }
}
