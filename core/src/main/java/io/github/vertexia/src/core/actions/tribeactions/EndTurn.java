package io.github.vertexia.src.core.actions.tribeactions;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.core.Types;

public class EndTurn extends TribeAction {

    public EndTurn(){ super(Types.ACTION.END_TURN); }
    public EndTurn(int tribeId)
    {
        super(Types.ACTION.END_TURN);
        this.tribeId = tribeId;
    }

    @Override
    public boolean isFeasible(final GameState gs) {
        Tribe tribe = gs.getTribe(tribeId);
        return gs.canEndTurn(tribe.getTribeId());
    }


    @Override
    public Action copy() {
        return new EndTurn(this.tribeId);
    }

    public String toString() {
        return "END_TURN by tribe " + this.tribeId;
    }
}
