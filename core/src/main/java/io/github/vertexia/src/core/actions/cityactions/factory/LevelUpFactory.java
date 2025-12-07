package io.github.vertexia.src.core.actions.cityactions.factory;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.cityactions.LevelUp;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.game.GameState;

import java.util.LinkedList;

public class LevelUpFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        LinkedList<Action> actions = new LinkedList<>();
        City city = (City) actor;

        LinkedList<Types.CITY_LEVEL_UP> bonuses = Types.CITY_LEVEL_UP.getActions(city.getLevel());
        for (Types.CITY_LEVEL_UP bonus : bonuses) {
            LevelUp lUp = new LevelUp(city.getActorId());
            lUp.setBonus(bonus);
            lUp.setTargetPos(city.getPosition().copy());
            if(lUp.isFeasible(gs)) {
                actions.add(lUp);
            }
        }

        return actions;
    }

}
