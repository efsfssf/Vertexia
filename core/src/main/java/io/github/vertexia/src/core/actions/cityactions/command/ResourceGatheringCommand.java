package io.github.vertexia.src.core.actions.cityactions.command;

import io.github.vertexia.src.core.TribesConfig;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.Types.CITY_LEVEL_UP;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.cityactions.LevelUp;
import io.github.vertexia.src.core.actions.cityactions.ResourceGathering;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

public class ResourceGatheringCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        ResourceGathering action = (ResourceGathering)a;
        if(action.isFeasible(gs)){
            int cityId = action.getCityId();
            City city = (City) gs.getActor(cityId);
            Vector2d position = action.getTargetPos();
            gs.getBoard().setResourceAt(position.x, position.y, null);
            Tribe tribe = gs.getTribe(city.getTribeId());
            Types.RESOURCE resource = action.getResource();
            tribe.subtractStars(resource.getCost());
            switch (resource){
                case FISH:
                case ANIMAL:
                case FRUIT:
                    city.addPopulation(tribe, resource.getBonus());
                    return true;
                case WHALES: //Whaling is the only resource which provides extra stars
                    Board b = gs.getBoard();
                    Tribe t  = b.getTribe(city.getTribeId());
                    t.addStars(resource.getBonus());
                    return true;
            }
        }
        return false;
    }
}
