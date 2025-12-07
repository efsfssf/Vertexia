package io.github.vertexia.src.core.actions.unitactions.factory;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.unitactions.Upgrade;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.core.Types;

import java.util.LinkedList;

public class UpgradeFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        Unit unit = (Unit) actor;
        LinkedList<Action> upgradeActions = new LinkedList<>();

        Types.ACTION actionType = null;
        if(unit.getType() == Types.UNIT.BOAT) actionType = Types.ACTION.UPGRADE_BOAT;
        if(unit.getType() == Types.UNIT.SHIP) actionType = Types.ACTION.UPGRADE_SHIP;

        Upgrade action = new Upgrade(actionType, unit.getActorId());

        if(action.isFeasible(gs)){
            upgradeActions.add(action);
        }
        return upgradeActions;
    }

}
