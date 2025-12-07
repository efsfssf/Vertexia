package io.github.vertexia.src.core.actions.tribeactions.command;

import io.github.vertexia.src.core.Diplomacy;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.tribeactions.DeclareWar;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;

import static io.github.vertexia.src.core.TribesConfig.ALLEGIANCE_MAX;

public class DeclareWarCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs){
        DeclareWar action = (DeclareWar)a;
        if (action.isFeasible(gs)){
            Diplomacy d = gs.getBoard().getDiplomacy();
            int tribeID = action.getTribeId();
            int targetID = action.getTargetID();
            Tribe tribe = gs.getTribe(tribeID);
            tribe.setHasDeclaredWar(true);
            tribe.setnWarsDeclared(tribe.getnWarsDeclared() + 1);
            d.updateAllegiance((int) (-(float)(ALLEGIANCE_MAX/2.0) - d.getAllegianceStatus()[tribeID][targetID]), tribeID, targetID);
            d.checkConsequences((int) (-(float)(ALLEGIANCE_MAX/2.0)), tribeID, targetID);
            return true;
        }
        return false;
    }
}
