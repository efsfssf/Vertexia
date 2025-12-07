package io.github.vertexia.src.players.portfolio;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.BaseScript;
import io.github.vertexia.src.utils.Pair;

public class ActionAssignment {
    private final BaseScript script;
    private final Actor actor;
    private Action action;
    private double value;

    public ActionAssignment(Actor a, BaseScript s)
    {
        actor = a;
        script = s;
    }

    boolean process(GameState gs)
    {
        Pair<Action, Double> p = script.process(gs, actor);
        if(p != null) {
            action = p.getFirst();
            value = p.getSecond();
        }
        return action != null;
    }

    public BaseScript getScript() {return script;}
    public Actor getActor() {return actor; }
    public Action getAction() {return action;}
    public double getValue() {return value;}

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof ActionAssignment))
        {
            return false;
        }
        ActionAssignment aas = (ActionAssignment)o;

        return actor.getActorId() == aas.getActor().getActorId() && action == aas.getAction();
    }

    public String toString()
    {
        return "Actor " + actor.getActorId() + ", Action " + action.toString()
                + "; " + script.getClass().toString() + "; Value: " + value;
    }
}
