package io.github.vertexia.src.players;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.ElapsedCpuTimer;

public class HumanAgent extends Agent {

    ActionController ac;

    public HumanAgent(ActionController ac)
    {
        super(0);
        this.ac = ac;
    }

    @Override
    public Action act(GameState gs, ElapsedCpuTimer ect) {
        return ac.getAction();
    }

    @Override
    public Agent copy() {
        return new HumanAgent(ac);
    }
}
