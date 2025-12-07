package io.github.vertexia.src.core.actions.tribeactions.command;

import io.github.vertexia.src.core.Diplomacy;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.tribeactions.SendStars;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;

public class SendStarsCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs){
        SendStars action = (SendStars) a;
        Tribe tribe = gs.getTribe(action.getTribeId());
        Tribe target = gs.getTribe(action.getTargetID());
        if (action.isFeasible(gs)){
            // adding and subtracting stars
            tribe.subtractStars(action.getNumStars());
            target.addStars(action.getNumStars());

            tribe.setStarsSent(tribe.getStarsSent() + action.getNumStars());
            tribe.setnStarsSent(tribe.getnStarsSent() + action.getNumStars());

            // adding score for the tribe sending stars
            tribe.addScore(action.getNumStars());

            // updating the diplomacy
            Diplomacy d = gs.getBoard().getDiplomacy();
            int tribeID = action.getTribeId();
            int targetID = action.getTargetID();
            d.updateAllegiance(action.getNumStars(), tribeID, targetID);
            d.checkConsequences(action.getNumStars(), tribeID, targetID);
            return true;
        }
        return false;
    }
}
