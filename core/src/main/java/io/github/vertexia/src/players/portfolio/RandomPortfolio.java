package io.github.vertexia.src.players.portfolio;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.players.portfolio.scripts.RandomScr;
import io.github.vertexia.src.players.portfolio.scripts.BaseScript;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class RandomPortfolio extends Portfolio
{

    BaseScript[] portfolio;

    public RandomPortfolio()
    {
        initPortfolio();
    }


    @Override
    public void initPortfolio() {
        portfolio = new BaseScript[]{new RandomScr(new Random())};
    }

    public ArrayList<ActionAssignment> produceActionAssignments(GameState state)
    {
        ArrayList<ActionAssignment> list = new ArrayList<>();

        for(BaseScript s : portfolio)
        {
            ArrayList<Unit> units = state.getUnits(state.getActiveTribeID());
            for(Unit u : units)
            {
                if(state.getUnitActions(u) != null && state.getUnitActions(u).size() > 0)
                {
                    ActionAssignment aas = new ActionAssignment(u, s);
                    list.add(aas);
                }
            }

            ArrayList<City> cities = state.getCities(state.getActiveTribeID());
            for(City c : cities)
            {
                if(state.getCityActions(c) != null && state.getCityActions(c).size() > 0) {
                    ActionAssignment aas = new ActionAssignment(c, s);
                    list.add(aas);
                }
            }

            if(state.getTribeActions() != null && state.getTribeActions().size() > 0)
                list.add (new ActionAssignment(state.getActiveTribe(), s));
        }

        return list;
    }

    @Override
    public TreeMap<Types.ACTION, BaseScript[]> getPortfolio() {
        return null;
    }

    @Override
    public BaseScript[] scripts(Types.ACTION actionType) {
        return portfolio;
    }
}
