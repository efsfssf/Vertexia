package io.github.vertexia.src.core.actions.unitactions.factory;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.unitactions.Move;
import io.github.vertexia.src.core.actions.unitactions.StepMove;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.graph.PathNode;
import io.github.vertexia.src.utils.graph.Pathfinder;

import java.util.LinkedList;

public class MoveFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        Unit unit = (Unit) actor;
        LinkedList<Action> moves = new LinkedList<>();
        Pathfinder tp = new Pathfinder(unit.getPosition(), new StepMove(gs, unit));

        //If a units turn is FINISHED don't do unnecessary calculations.
        if(unit.canMove()) {
            for(PathNode tile : tp.findPaths()) {
                if(gs.getBoard().getUnitAt(tile.getX(), tile.getY()) == null) {
                    Move action = new Move(unit.getActorId());
                    action.setDestination(tile.getPosition());
                    moves.add(action);
                }
            }
        }
        return moves;
    }

}
