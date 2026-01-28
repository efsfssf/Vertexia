package io.github.vertexia.src.gui;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.unitactions.*;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.utils.Vector2d;

import static io.github.vertexia.src.core.Types.ACTION.*;

public final class ActionPositionResolver {

    private ActionPositionResolver() {}

    public static Vector2d get(GameState gs, Action a) {
        if (a.getActionType() == MOVE) {
            return new Vector2d(
                ((Move) a).getDestination().x,
                ((Move) a).getDestination().y
            );
        }

        if (a.getActionType() == ATTACK) {
            Unit target = (Unit) gs.getActor(((Attack) a).getTargetId());
            return target.getPosition();
        }

        if (a.getActionType() == RECOVER) {
            Unit u = (Unit) gs.getActor(((UnitAction) a).getUnitId());
            return u.getPosition();
        }

        if (a.getActionType() == CAPTURE || a.getActionType() == EXAMINE) {
            Unit u = (Unit) gs.getActor(((UnitAction) a).getUnitId());
            return new Vector2d(u.getPosition().x - 1, u.getPosition().y);
        }

        if (a.getActionType() == CONVERT) {
            Unit target = (Unit) gs.getActor(((Convert) a).getTargetId());
            return target.getPosition();
        }

        return null;
    }
}
