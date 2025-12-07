package io.github.vertexia.src.core.actions.tribeactions.command;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.ActionCommand;
import io.github.vertexia.src.core.actions.tribeactions.ResearchTech;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.game.GameState;

public class ResearchTechCommand implements ActionCommand {

    @Override
    public boolean execute(Action a, GameState gs) {
        ResearchTech action = (ResearchTech)a;
        if(action.isFeasible(gs))
        {
            //Research tech
            Types.TECHNOLOGY tech = action.getTech();
            int tribeId = action.getTribeId();
            Tribe tribe = gs.getTribe(tribeId);
            int techCost = tech.getCost(tribe.getNumCities(), tribe.getTechTree());
            tribe.subtractStars(techCost);
            tribe.getTechTree().doResearch(tech);
            tribe.addScore(tech.getPoints());

            //Flag if research tree is completed.
            if (tribe.getTechTree().isEverythingResearched())
            {
                tribe.allResearched();
            }
            return true;
        }
        return false;
    }
}
