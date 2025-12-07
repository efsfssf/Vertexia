package io.github.vertexia.src.core.actions.unitactions;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.utils.Vector2d;
import io.github.vertexia.src.utils.graph.PathNode;
import io.github.vertexia.src.utils.graph.Pathfinder;

import java.util.ArrayList;

public class Move extends UnitAction
{
    private Vector2d destination;

    public Move(int unitId)
    {
        super(Types.ACTION.MOVE);
        super.unitId = unitId;
    }

    public void setDestination(Vector2d destination) {this.destination = destination; }
    public Vector2d getDestination() { return destination; }

    @Override
    public boolean isFeasible(final GameState gs)
    {
        Unit unit = (Unit) gs.getActor(this.unitId);
        if(unit == null)
            return false;
        Pathfinder tp = new Pathfinder(unit.getPosition(), new StepMove(gs, unit));

        //If the unit can move and the destination is vacant, try to reach it.
        if(unit.canMove() && gs.getBoard().getUnitAt(destination.x, destination.y) == null) {
            ArrayList<PathNode> path = tp.findPathTo(destination);
//            if(path == null)
//            {
//                System.out.println("ERROR calculating a path (if actions were created by MoveFactory)");
//            }
            return path != null;
        }
        return false;
    }


    @Override
    public Action copy() {
        Move move = new Move(this.unitId);
        move.setDestination(this.destination);
        return move;
    }

    public String toString()
    {
        return "MOVE by unit " + this.unitId + " to " + destination;
    }

    public boolean equals(Object o) {
        if(!(o instanceof Move))
            return false;
        Move other = (Move) o;

        return super.equals(other) && destination == other.destination;
    }

}
