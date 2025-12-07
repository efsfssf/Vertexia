package io.github.vertexia.src.core.actions.cityactions.command;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.cityactions.Build;
import io.github.vertexia.src.core.actors.Building;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.Temple;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

public class BuildCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        Build action = (Build)a;
        int cityId = action.getCityId();
        City city = (City) gs.getActor(cityId);
        Tribe tribe = gs.getTribe(city.getTribeId());
        Board board = gs.getBoard();

        if(action.isFeasible(gs)) {
            Vector2d targetPos = action.getTargetPos();
            Types.BUILDING buildingType = action.getBuildingType();

            tribe.subtractStars(buildingType.getCost());
            board.setBuildingAt(targetPos.x, targetPos.y, buildingType);
            board.setResourceAt(targetPos.x, targetPos.y, null);

            if(buildingType.isTemple())
                city.addBuilding(gs, new Temple(targetPos.x, targetPos.y, buildingType, cityId));
            else
                city.addBuilding(gs, new Building(targetPos.x, targetPos.y, buildingType, cityId));

            if(buildingType == Types.BUILDING.PORT)
                board.buildPort(targetPos.x, targetPos.y);
            if(buildingType.isMonument())
                tribe.monumentIsBuilt(buildingType);
            if(buildingType == Types.BUILDING.LUMBER_HUT)
                board.setTerrainAt(targetPos.x, targetPos.y, Types.TERRAIN.PLAIN);

            return true;
        }
        return false;
    }
}
