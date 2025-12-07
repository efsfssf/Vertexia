package io.github.vertexia.src.core.actions.unitactions.factory;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.unitactions.Convert;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

import java.util.LinkedList;

public class ConvertFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        Unit unit = (Unit) actor;
        LinkedList<Action> converts = new LinkedList<>();

        //Only if unit can attack.
        if(unit.canAttack()) {
            Board b = gs.getBoard();
            Vector2d position = unit.getPosition();

            LinkedList<Vector2d> potentialTiles = position.neighborhood(unit.RANGE, 0, b.getSize()); //use neighbourhood for board limits
            for (Vector2d tile : potentialTiles) {
                Unit target = b.getUnitAt(tile.x, tile.y);
                if(target != null && target.getTribeId() != unit.getTribeId())
                {
                    // Check if there is actually a unit there (and it's not me)
                    Convert c = new Convert(unit.getActorId());
                    c.setTargetId(target.getActorId());
                    if(c.isFeasible(gs)){
                        converts.add(c);
                    }
                }
            }
        }

        return converts;
    }

}
