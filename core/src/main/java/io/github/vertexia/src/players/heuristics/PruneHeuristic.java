package io.github.vertexia.src.players.heuristics;

import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.ActionAssignment;
import io.github.vertexia.src.players.portfolioMCTS.PortfolioTreeNode;

import java.util.ArrayList;
import java.util.Random;

public interface PruneHeuristic {
    default double evaluatePrune(GameState state, ActionAssignment aas){ return 0.0; }
    default boolean[] prune (PortfolioTreeNode parent, ArrayList<ActionAssignment> actions, GameState gameState, int k) {return null;}
    default boolean[] unprune (PortfolioTreeNode parent, ArrayList<ActionAssignment> actions, GameState gameState, boolean[] pruned, Random rnd) {return null;}
}
