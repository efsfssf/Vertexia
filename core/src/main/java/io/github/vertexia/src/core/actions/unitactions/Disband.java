package io.github.vertexia.src.core.actions.unitactions;

import io.github.vertexia.src.core.TechnologyTree;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.core.actors.units.Unit;

public class Disband extends UnitAction
{
    public Disband(int unitId)
    {
        super(Types.ACTION.DISBAND);
        super.unitId = unitId;
    }

    @Override
    public boolean isFeasible(final GameState gs)
    {
        Unit unit = (Unit) gs.getActor(this.unitId);
        TechnologyTree tt = gs.getTribe(unit.getTribeId()).getTechTree();
        return unit.isFresh() && tt.isResearched(Types.TECHNOLOGY.FREE_SPIRIT);
    }

    @Override
    public Action copy() {
        return new Disband(this.unitId);
    }

    @Override
    public String toString() {
        return "DISBAND of unit " + this.unitId;
    }
}
