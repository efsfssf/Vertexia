package io.github.vertexia.src.core.actions.tribeactions.command;

import io.github.vertexia.src.core.TribesConfig;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.tribeactions.BuildRoad;
import io.github.vertexia.src.core.actions.tribeactions.EndTurn;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

public class EndTurnCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        EndTurn action = (EndTurn)a;
        if(action.isFeasible(gs))
        {
            gs.setEndTurn(true);
            return true;
        }
        return false;
    }
}
