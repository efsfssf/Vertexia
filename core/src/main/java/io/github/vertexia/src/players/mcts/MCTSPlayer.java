package io.github.vertexia.src.players.mcts;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.tribeactions.EndTurn;
import io.github.vertexia.src.core.game.Game;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.Agent;
import io.github.vertexia.src.utils.ElapsedCpuTimer;

import java.util.ArrayList;
import java.util.Random;

import static io.github.vertexia.src.core.Constants.TURN_TIME_MILLIS;

public class MCTSPlayer extends Agent {

    private Random m_rnd;
    private MCTSParams params;

    public MCTSPlayer(long seed)
    {
        super(seed);
        m_rnd = new Random(seed);
        this.params = new MCTSParams();
    }

    public MCTSPlayer(long seed, MCTSParams params) {
        this(seed);
        this.params = params;
    }

    public Action act(GameState gs, ElapsedCpuTimer ect) {
        //Gather all available actions:
        ArrayList<Action> allActions = gs.getAllAvailableActions();

        if(allActions.size() == 1)
            return allActions.get(0); //EndTurn, it's possible.

        ArrayList<Action> rootActions = params.PRIORITIZE_ROOT ? determineActionGroup(gs, m_rnd) : allActions;
        if(rootActions == null)
            return new EndTurn();

        SingleTreeNode m_root = new SingleTreeNode(params, m_rnd, rootActions.size(), rootActions, this.playerID);
        m_root.setRootGameState(m_root, gs, allPlayerIDs);

        m_root.mctsSearch(ect);

        return rootActions.get(m_root.mostVisitedAction());

    }


    @Override
    public Agent copy() {
        return null;
    }

}
