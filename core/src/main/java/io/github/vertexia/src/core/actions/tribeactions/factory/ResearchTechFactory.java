package io.github.vertexia.src.core.actions.tribeactions.factory;

import io.github.vertexia.src.core.TechnologyTree;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionFactory;
import io.github.vertexia.src.core.actions.tribeactions.ResearchTech;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;

import java.util.LinkedList;

public class ResearchTechFactory implements ActionFactory {

    @Override
    public LinkedList<Action> computeActionVariants(final Actor actor, final GameState gs) {

        Tribe tribe = (Tribe) actor;
        LinkedList<Action> actions = new LinkedList<>();
        TechnologyTree techTree = tribe.getTechTree();
        int stars = tribe.getStars();
        int numCities = tribe.getNumCities();

        //Technically, we can do faster than this (by pruning branches of the
        // tech tree that are not reachable), although this makes the code more general.
        for(Types.TECHNOLOGY tech : Types.TECHNOLOGY.values())
        {
            if(stars >= tech.getCost(numCities, techTree) && techTree.isResearchable(tech))
            {
                ResearchTech newAction = new ResearchTech(tribe.getTribeId());
                newAction.setTech(tech);
                actions.add(newAction);
            }
        }
        return actions;
    }

}
