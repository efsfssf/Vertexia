package io.github.vertexia.src.core.actions.unitactions.factory;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.unitactions.Attack;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

import java.util.LinkedList;

public class AttackFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        Unit unit = (Unit) actor;
        LinkedList<Action> attacks = new LinkedList<>();

        //Only if unit can attack.
        if(unit.canAttack()) {
            Board b = gs.getBoard();
            Vector2d position = unit.getPosition();

            // Loop through unit range, check if tile observable and action feasible, if so add action
            LinkedList<Vector2d> potentialTiles = position.neighborhood(unit.RANGE, 0, b.getSize()); //use neighbourhood for board limits
            for (Vector2d tile : potentialTiles) {
                Unit other = b.getUnitAt(tile.x, tile.y);
                if (other != null && other.getTribeId() != unit.getTribeId()) {
                    // Check if there is actually a unit there (and it's not me)
                    Attack a = new Attack(unit.getActorId());
                    a.setTargetId(other.getActorId());
                    if (a.isFeasible(gs)) {
                        attacks.add(a);
                    }
                }
            }
        }

        return attacks;
    }

}
