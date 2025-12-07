package io.github.vertexia.src.players.portfolio.scripts;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.utils.MilitaryFunc;
import io.github.vertexia.src.players.portfolio.scripts.utils.ValuePoint;
import io.github.vertexia.src.utils.Pair;
import io.github.vertexia.src.utils.Vector2d;

import java.util.LinkedList;
import java.util.Random;

public class MoveToConvergeScr extends BaseScript {

    //This script returns the Move action that places the unit on an own city centre.
    private Random rnd;

    public MoveToConvergeScr(Random rnd)
    {
        this.rnd = rnd;
    }

    @Override
    public Pair<Action, Double> process(GameState gs, Actor ac)
    {
        return new MilitaryFunc().position(gs, ac, actions, rnd, 1, new ValuePoint() {
            @Override
            public double ofInterest(GameState gs, Actor ac, int posX, int posY) {
                Board b = gs.getBoard();
                Vector2d targetPos = new Vector2d(posX, posY);
                LinkedList<Vector2d> neighs = targetPos.neighborhood(1, 0, b.getSize());

                int maxAllies = neighs.size();
                int numAllies = 0;
                for(Vector2d n : neighs)
                {
                    Unit u = b.getUnitAt(n.x, n.y);
                    if(u != null && u.getTribeId() == ac.getTribeId())
                        numAllies++;
                }

                return (double)numAllies/maxAllies;
            }
        });

    }



}
