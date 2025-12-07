package io.github.vertexia.src.core.actions.unitactions;

import io.github.vertexia.src.core.TechnologyTree;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.core.actors.units.Unit;

import static io.github.vertexia.src.core.Types.UNIT.*;

public class Upgrade extends UnitAction
{
    public Upgrade(Types.ACTION actionType, int unitId)
    {
        super(actionType);
        super.unitId = unitId;
    }

    @Override
    public boolean isFeasible(final GameState gs) {
        Unit unit = (Unit) gs.getActor(this.unitId);
        Tribe tribe = gs.getTribe(unit.getTribeId());
        TechnologyTree ttree = tribe.getTechTree();

        int stars = gs.getTribe(unit.getTribeId()).getStars();
        return ((unit.getType() == BOAT && ttree.isResearched(Types.TECHNOLOGY.SAILING) && stars >= SHIP.getCost()) ||
                (unit.getType() == SHIP && ttree.isResearched(Types.TECHNOLOGY.NAVIGATION) && stars >= BATTLESHIP.getCost()));
    }

    @Override
    public Action copy() {
        return new Upgrade(this.actionType, this.unitId);
    }

    public String toString() {
        return "UPGRADE by unit " + this.unitId;
    }
}
