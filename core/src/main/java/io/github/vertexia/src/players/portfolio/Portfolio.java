package io.github.vertexia.src.players.portfolio;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.BaseScript;

import java.util.ArrayList;
import java.util.TreeMap;

public abstract class Portfolio {

    public abstract void initPortfolio();
    public abstract ArrayList<ActionAssignment> produceActionAssignments(GameState state);
    public abstract TreeMap<Types.ACTION, BaseScript[]> getPortfolio();
    public abstract BaseScript[] scripts(Types.ACTION actionType);
}
