package io.github.vertexia.src.core.actions.tribeactions.factory;

import io.github.vertexia.src.core.Diplomacy;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.tribeactions.DeclareWar;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;

import java.util.LinkedList;

import static io.github.vertexia.src.core.TribesConfig.ALLEGIANCE_MAX;

public class DeclareWarFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        LinkedList<Action> actions = new LinkedList<>();
        Tribe tribe = (Tribe) actor;

        // getting the diplomacy of the current GameState
        Diplomacy d = gs.getBoard().getDiplomacy();
        // getting the allegiances
        int[][] allegiances = d.getAllegianceStatus();
        if (!tribe.getHasDeclaredWar()) {
            for (int i = 0; i < allegiances.length; i++) {
                if (allegiances[tribe.getTribeId()][i] > -(float)(ALLEGIANCE_MAX/2.0) && tribe.getTribeId() != i) {
                    DeclareWar declareWar = new DeclareWar(tribe.getTribeId());
                    declareWar.setTargetID(i);
                    actions.add(declareWar);
                }
            }
        }
        return actions;
    }
}
