package io.github.vertexia.src;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.game.Game;
import org.json.JSONArray;
import io.github.vertexia.src.players.*;
import io.github.vertexia.src.players.emcts.EMCTSAgent;
import io.github.vertexia.src.players.emcts.EMCTSParams;
import io.github.vertexia.src.players.heuristics.PrunePortfolioHeuristic;
import io.github.vertexia.src.players.mc.MCParams;
import io.github.vertexia.src.players.mc.MonteCarloAgent;
import io.github.vertexia.src.players.mcts.MCTSParams;
import io.github.vertexia.src.players.mcts.MCTSPlayer;
import io.github.vertexia.src.players.oep.OEPAgent;
import io.github.vertexia.src.players.oep.OEPParams;
import io.github.vertexia.src.players.osla.OSLAParams;
import io.github.vertexia.src.players.osla.OneStepLookAheadAgent;
import io.github.vertexia.src.players.portfolio.Portfolio;
import io.github.vertexia.src.players.portfolio.SimplePortfolio;
import io.github.vertexia.src.players.portfolioMCTS.PortfolioMCTSParams;
import io.github.vertexia.src.players.portfolioMCTS.PortfolioMCTSPlayer;
import io.github.vertexia.src.players.rhea.RHEAAgent;
import io.github.vertexia.src.players.rhea.RHEAParams;

import static io.github.vertexia.src.core.Constants.*;
import static io.github.vertexia.src.core.Types.TRIBE.*;
import static io.github.vertexia.src.core.Types.TRIBE.OUMAJI;

class Run {


    /**
     * Runs a game, no visuals nor human player
     */
    static void runGame(Game game) {
        while (!game.gameOver()) {
            game.update();
        }
    }

    public enum PlayerType
    {
        DONOTHING,
        HUMAN,
        RANDOM,
        OSLA,
        MC,
        SIMPLE,
        MCTS,
        RHEA,
        OEP,
        EMCTS,
        PORTFOLIO_MCTS
    }

    public static double K_INIT_MULT = 0.5;
    public static double T_MULT = 2.0;
    public static double A_MULT = 1.5;
    public static double B = 1.3;
    public static double[] pMCTSweights;

    public static int MAX_LENGTH;
    public static boolean PRUNING;
    public static boolean PROGBIAS;
    public static boolean FORCE_TURN_END;
    public static boolean MCTS_ROLLOUTS;
    public static int POP_SIZE;


    static Run.PlayerType parsePlayerTypeStr(String arg) throws Exception
    {
        switch(arg)
        {
            case "Human": return Run.PlayerType.HUMAN;
            case "Do Nothing": return Run.PlayerType.DONOTHING;
            case "Random": return Run.PlayerType.RANDOM;
            case "Rule Based": return Run.PlayerType.SIMPLE;
            case "OSLA": return Run.PlayerType.OSLA;
            case "MC": return Run.PlayerType.MC;
            case "MCTS": return Run.PlayerType.MCTS;
            case "RHEA": return Run.PlayerType.RHEA;
            case "OEP": return Run.PlayerType.OEP;
            case "pMCTS": return Run.PlayerType.PORTFOLIO_MCTS;
            case "EMCTS": return Run.PlayerType.EMCTS;
        }
        throw new Exception("Error: unrecognized Player Type: " + arg);
    }

    static Types.TRIBE parseTribeStr(String arg) throws Exception
    {
        switch(arg)
        {
            case "Xin Xi": return XIN_XI;
            case "Imperius": return IMPERIUS;
            case "Bardur": return BARDUR;
            case "Oumaji": return OUMAJI;
            case "Kickoo": return KICKOO;
            case "Hoodrick": return HOODRICK;
            case "Luxidoor": return LUXIDOOR;
            case "Vengir": return VENGIR;
            case "Zebasi": return ZEBASI;
            case "Ai-Mo": return AI_MO;
            case "Quetzali": return QUETZALI;
            case "Yadakk": return YADAKK;
        }
        throw new Exception("Error: unrecognized Tribe: " + arg);
    }

    public static double[] getWeights(JSONArray w) {
        if (w == null) return null;
        double[] weights = new double[w.length()];
        for (int i = 0; i < weights.length; ++i)
        {
            weights[i] = w.getDouble(i);
        }
        return weights;
    }

    public static Agent getAgent(Run.PlayerType playerType, long agentSeed)
    {
        switch (playerType)
        {
            case DONOTHING: return new DoNothingAgent(agentSeed);
            case RANDOM: return new RandomAgent(agentSeed);
            case SIMPLE: return new SimpleAgent(agentSeed);
            case OSLA:
                OSLAParams oslaParams = new OSLAParams();
                oslaParams.stop_type = oslaParams.STOP_FMCALLS; //Upper bound
                oslaParams.heuristic_method = oslaParams.DIFF_HEURISTIC;
                return new OneStepLookAheadAgent(agentSeed, oslaParams);
            case MC:
                MCParams mcparams = new MCParams();
                mcparams.stop_type = mcparams.STOP_FMCALLS;
                mcparams.heuristic_method = mcparams.DIFF_HEURISTIC;
                mcparams.PRIORITIZE_ROOT = true;
                mcparams.ROLLOUT_LENGTH = MAX_LENGTH;
                mcparams.FORCE_TURN_END = FORCE_TURN_END ? 5 : mcparams.ROLLOUT_LENGTH + 1;
                return new MonteCarloAgent(agentSeed, mcparams);
            case MCTS:
                MCTSParams mctsParams = new MCTSParams();
                mctsParams.stop_type = mctsParams.STOP_FMCALLS;
                mctsParams.heuristic_method = mctsParams.DIFF_HEURISTIC;
                mctsParams.PRIORITIZE_ROOT = true;
                mctsParams.ROLLOUT_LENGTH = MAX_LENGTH;
                mctsParams.FORCE_TURN_END = FORCE_TURN_END ? 5 : mctsParams.ROLLOUT_LENGTH + 1;
                mctsParams.ROLOUTS_ENABLED = MCTS_ROLLOUTS;
                return new MCTSPlayer(agentSeed, mctsParams);
            case PORTFOLIO_MCTS:
                PortfolioMCTSParams portfolioMCTSParams = new PortfolioMCTSParams();
                portfolioMCTSParams.stop_type = portfolioMCTSParams.STOP_FMCALLS;
                portfolioMCTSParams.heuristic_method = portfolioMCTSParams.DIFF_HEURISTIC;
                portfolioMCTSParams.PRIORITIZE_ROOT = false;
                portfolioMCTSParams.ROLLOUT_LENGTH = MAX_LENGTH;
                portfolioMCTSParams.PRUNING = PRUNING;
                portfolioMCTSParams.PROGBIAS = PROGBIAS;
                portfolioMCTSParams.K_init_mult = K_INIT_MULT;
                portfolioMCTSParams.A_mult = A_MULT;
                portfolioMCTSParams.B = B;
                portfolioMCTSParams.T_mult = T_MULT;
                Portfolio p = new SimplePortfolio(agentSeed);
                portfolioMCTSParams.setPortfolio(p);
                portfolioMCTSParams.pruneHeuristic = new PrunePortfolioHeuristic(p);
                if(Run.pMCTSweights != null)
                    portfolioMCTSParams.pruneHeuristic.setWeights(Run.pMCTSweights);
                return new PortfolioMCTSPlayer(agentSeed, portfolioMCTSParams);
            case OEP:
                OEPParams oepParams = new OEPParams();
                oepParams.stop_type = oepParams.STOP_FMCALLS;
                oepParams.heuristic_method = oepParams.DIFF_HEURISTIC;
                return new OEPAgent(agentSeed, oepParams);
            case EMCTS:
                EMCTSParams emctsParams = new EMCTSParams();
                emctsParams.stop_type = emctsParams.STOP_FMCALLS;
                emctsParams.heuristic_method = emctsParams.DIFF_HEURISTIC;
                return new EMCTSAgent(agentSeed,emctsParams);
            case RHEA:
                RHEAParams rheaParams = new RHEAParams();
                rheaParams.stop_type = rheaParams.STOP_FMCALLS;
                rheaParams.heuristic_method = rheaParams.DIFF_HEURISTIC;
                rheaParams.INDIVIDUAL_LENGTH = MAX_LENGTH;
                rheaParams.FORCE_TURN_END = rheaParams.INDIVIDUAL_LENGTH + 1;
                rheaParams.POP_SIZE = POP_SIZE;
                return new RHEAAgent(agentSeed, rheaParams);
        }
        return null;
    }
}
