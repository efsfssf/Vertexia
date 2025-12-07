package io.github.vertexia.src.core.actions.unitactions.factory;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.unitactions.Capture;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;

import java.util.LinkedList;

public class CaptureFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        Unit unit = (Unit) actor;
        LinkedList<Action> captures = new LinkedList<>();

        if(!unit.isFresh())
            return captures;

        Types.TERRAIN t = gs.getBoard().getTerrainAt(unit.getPosition().x, unit.getPosition().y);
        Capture capture;

        if(t == Types.TERRAIN.VILLAGE)
        {
            capture = new Capture(unit.getActorId());
            capture.setTargetCity(-1);
        }else if(t == Types.TERRAIN.CITY) {
            // get city from board, check if action is feasible and add to list
            Board b = gs.getBoard();
            City c = b.getCityInBorders(unit.getPosition().x, unit.getPosition().y);
            if (c != null) {
                capture = new Capture(unit.getActorId());
                capture.setTargetCity(c.getActorId());
            }else return captures;
        }else return captures;

        capture.setCaptureType(t);
        if (capture.isFeasible(gs)) {
            captures.add(capture);
        }

        return captures;
    }

}
