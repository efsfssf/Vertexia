package io.github.vertexia.src.core.actions.tribeactions.factory;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.tribeactions.SendStars;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;

import java.util.LinkedList;

import static io.github.vertexia.src.core.TribesConfig.MIN_STARS_SEND;

public class SendStarsFactory implements ActionFactory {
    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {
        LinkedList<Action> actions = new LinkedList<>();
        Tribe tribe = (Tribe) actor;

        // if tribe has no stars, no actions
        if (tribe.getStars() == 0) {
            return actions;
        }
        for (int ids = 0; ids < gs.getBoard().getTribes().length; ids++) {
            for (int stars = 1; stars < Math.min(tribe.getStars(), MIN_STARS_SEND); stars++) {
                if (ids != tribe.getTribeId()) {
                    if (tribe.canSendStars(stars)) {
                        SendStars ss = new SendStars(tribe.getTribeId());
                        ss.setNumStars(stars);
                        ss.setTargetID(ids);
                        actions.add(ss);
                    }
                }
            }
        }
        return actions;
    }
}
