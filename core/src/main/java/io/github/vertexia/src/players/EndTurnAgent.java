package io.github.vertexia.src.players;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.tribeactions.EndTurn;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.ElapsedCpuTimer;

public class EndTurnAgent extends Agent {

    public EndTurnAgent(long seed)
    {
        super(seed);
    }

    @Override
    public Action act(GameState gs, ElapsedCpuTimer ect) {
        return new EndTurn(gs.getActiveTribeID());
    }

    @Override
    public Agent copy() {
        return null;
    }
}
