package io.github.vertexia.src.players;

import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.tribeactions.TribeAction;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.ElapsedCpuTimer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class ActionController extends Agent {

    private Queue<Action> actionsQueue;

    public ActionController() {
        super(0);
        actionsQueue = new ArrayDeque<>();
    }

    public ActionController(ArrayList<Action> actionsQueue) {
        super(0);
        this.actionsQueue = new ArrayDeque<>(actionsQueue);
    }

    public void addAction(Action candidate, GameState gs)
    {
        actionsQueue.add(candidate);
    }

    public void addActions(ArrayList<Action> candidates, GameState gs) {
        actionsQueue.addAll(candidates);
    }

    public Action getAction() {
        return actionsQueue.poll();
    }

    @Override
    public Action act(GameState gs, ElapsedCpuTimer ect) {
        return getAction();
    }

    public ActionController copy() {
        ActionController copy = new ActionController();
        copy.actionsQueue = new ArrayDeque<>(actionsQueue);
        return copy;
    }

    public void reset() {
        actionsQueue.clear();
    }
}
