package io.github.vertexia.src.core.game;

import io.github.vertexia.src.core.Constants;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.core.actions.tribeactions.EndTurn;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.players.Agent;
import io.github.vertexia.src.players.HumanAgent;
import io.github.vertexia.src.utils.*;
import io.github.vertexia.src.utils.mapelites.Feature;
import io.github.vertexia.src.utils.stats.AIStats;
import io.github.vertexia.src.utils.stats.GameplayStats;

import java.util.*;

import static io.github.vertexia.src.core.Constants.*;
import static io.github.vertexia.src.core.Types.ACTION.*;

public class Game {

    // State of the game (objects, ticks, etc).
    private GameState gs;

    // GameState objects for players to make decisions
    private GameState[] gameStateObservations;

    // Seed for the game state.
    private long seed;

    //Random number generator for the game.
    private Random rnd;

    // List of players of the game
    private Agent[] players;

    //Number of players of the game.
    private int numPlayers;

    // Is the game paused from the GUI?
    private boolean paused, animationPaused;

    // AI stats for each player.
    private AIStats[] aiStats;

    // Gameplay stats for each player.
    private GameplayStats[] gpStats;

    /**
     * Constructor of the game
     */
    public Game() {
    }

    /**
     * Initializes the game. This method does the following:
     * Sets the players of the game, the number of players and their IDs
     * Initializes the array to hold the player game states.
     * Assigns the tribes that will play the game.
     * Creates the level reading it from the file 'filename'.
     * Resets the game so it's ready to start.
     * Turn order: by default, turns run following the order in the tribes array.
     *
     * @param players  Players of the game.
     * @param filename Name of the file with the level information.
     * @param seed     Seed for the game (used only for board generation)
     * @param gameMode Game Mode for this game.
     */
    public void init(ArrayList<Agent> players, String filename, long seed, Types.GAME_MODE gameMode) {

        //Initiate the bare bones of the main game classes
        this.seed = seed;
        this.rnd = new Random(seed);
        this.gs = new GameState(rnd, gameMode);

        this.gs.init(filename);
        initGameStructures(players, this.gs.getTribes().length);
        updateAssignedGameStates();
    }

    /**
     * Initializes the game. This method does the following:
     * Sets the players of the game, the number of players and their IDs
     * Initializes the array to hold the player game states.
     * Assigns the tribes that will play the game
     * Generates a new level using the seed levelgen_seed
     * Resets the game so it's ready to start.
     * Turn order: by default, turns run following the order in the tribes array.
     *
     * @param players       Players of the game.
     * @param levelgen_seed Seed for the level generator.
     * @param tribes        Array of tribe types to play with.
     * @param seed          Seed for the game (used only for board generation)
     * @param gameMode      Game Mode for this game.
     */
    public void init(ArrayList<Agent> players, long levelgen_seed, Types.TRIBE[] tribes, long seed, Types.GAME_MODE gameMode) {

        try {
            //Initiate the bare bones of the main game classes
            this.seed = seed;
            this.rnd = new Random(seed);
            this.gs = new GameState(rnd, gameMode);

            this.gs.init(levelgen_seed, tribes);
            initGameStructures(players, tribes);
            updateAssignedGameStates();
        } catch (Exception e) {
            throw new IllegalStateException("Error during initialization: " + e);
        }
    }

    /**
     * Initializes the game from a savegame file
     *
     * @param players  Players who will play this game.
     * @param fileName savegame
     */
    public void init(ArrayList<Agent> players, String fileName) {

        GameLoader gameLoader = new GameLoader(fileName);
        this.seed = gameLoader.getSeed();
        this.rnd = new Random(seed);
        Tribe[] tribes = gameLoader.getTribes();
        this.gs = new GameState(rnd, gameLoader.getGame_mode(), tribes, gameLoader.getBoard(), gameLoader.getTick());
        this.gs.setGameIsOver(gameLoader.getGameIsOver());
        initGameStructures(players, tribes.length);
        updateAssignedGameStates();
    }

    public void update() {
        if (gameOver()) return;

        Tribe[] tribes = gs.getTribes();

        for (int i = 0; i < numPlayers; i++) {
            Tribe tribe = tribes[i];

            if (tribe == null) continue;
            if (tribe.getWinner() != Types.RESULT.INCOMPLETE) continue;

            processTurn(i, tribe);
            if (gameOver()) return;
        }

        gs.incTick();
    }

    private void processTurn(int playerID, Tribe tribe) {

        gs.initTurn(tribe);
        gs.computePlayerActions(tribe);
        updateAssignedGameStates();

        Agent ag = players[playerID];
        boolean continueTurn = true;

        while (continueTurn) {
            Action action = ag.act(gameStateObservations[playerID], null);

            if (action == null || action.getActionType() == END_TURN) {
                break;
            }

            gs.next(action);
            gs.computePlayerActions(tribe);
            updateAssignedGameStates();
        }

        gs.endTurn(tribe);
    }


    /**
     * Initializes game structures depending on number of players and tribes
     *
     * @param players Players to play this game
     * @param nTribes number of tribes the game is set up to start with. Should be the same as players.size().
     */
    private void initGameStructures(ArrayList<Agent> players, int nTribes) {
        if (players.size() != nTribes) {
            System.out.println("ERROR: Number of tribes must _equal_ the number of players. There are " +
                    players.size() + " players for " + nTribes + " tribes in this level.");
            System.exit(-1);
        }

        //Create the players and agents to control them
        numPlayers = players.size();
        this.players = new Agent[numPlayers];
        this.aiStats = new AIStats[numPlayers];
        this.gpStats = new GameplayStats[numPlayers];

        ArrayList<Integer> allIds = new ArrayList<>();
        for (int i = 0; i < numPlayers; ++i)
            allIds.add(i);

        for (int i = 0; i < numPlayers; ++i) {
            this.players[i] = players.get(i);
            this.players[i].setPlayerIDs(i, allIds);
            this.aiStats[i] = new AIStats(i);
            this.gpStats[i] = new GameplayStats(i);
        }

        this.gameStateObservations = new GameState[numPlayers];
    }


    /**
     * Initializes game structures depending on number of players and tribes
     *
     * @param players Players to play this game
     * @param tribes  Array of tribe types to play with.
     */
    private void initGameStructures(ArrayList<Agent> players, Types.TRIBE[] tribes) {
        int nTribes = tribes.length;
        if (players.size() != nTribes) {
            System.out.println("ERROR: Number of tribes must _equal_ the number of players. There are " +
                    players.size() + " players for " + nTribes + " tribes in this level.");
            System.exit(-1);
        }

        //Create the players and agents to control them
        numPlayers = players.size();
        this.players = new Agent[numPlayers];
        this.aiStats = new AIStats[numPlayers];
        this.gpStats = new GameplayStats[numPlayers];

        Tribe[] tribeObjects = gs.getTribes();

        for (int tribeIdx = 0; tribeIdx < tribeObjects.length; ++tribeIdx) {
            Tribe thisTribe = tribeObjects[tribeIdx];
            Types.TRIBE tribeType = thisTribe.getType();

            ArrayList<Integer> allIds = new ArrayList<>();
            int indexInTypes = -1;
            for (int i = 0; i < tribes.length; ++i) {
                allIds.add(i);
                if (tribes[i] == tribeType)
                    indexInTypes = i;
            }

            this.players[tribeIdx] = players.get(indexInTypes);
            this.players[tribeIdx].setPlayerIDs(tribeIdx, allIds);
            this.aiStats[tribeIdx] = new AIStats(tribeIdx);
            this.gpStats[tribeIdx] = new GameplayStats(tribeIdx);
        }
        this.gameStateObservations = new GameState[numPlayers];
    }




    /**
     * Prints the results of the game.
     */
    private void printGameResults() {
        Types.RESULT[] results = getWinnerStatus();
        int[] sc = getScores();
        Tribe[] tribes = gs.getBoard().getTribes();

        TreeSet<TribeResult> ranking = gs.getCurrentRanking();
        System.out.println(gs.getTick() + "; Game Results: ");
        int rank = 1;
        for (TribeResult tr : ranking) {
            int tribeId = tr.getId();
            Agent ag = players[tribeId];
            String[] agentChunks = ag.getClass().toString().split("\\.");
            String agentName = agentChunks[agentChunks.length - 1];

            System.out.print(" #" + rank + ": Tribe " + tribes[tribeId].getType() + " (" + agentName + "): " + results[tribeId] + ", " + sc[tribeId] + " points;");
            System.out.println(" #tech: " + tr.getNumTechsResearched() + ", #cities: " + tr.getNumCities() + ", production: " + tr.getProduction());
            System.out.println("Diplomacy - #wars: " + tribes[tribeId].getnWarsDeclared() + ", #stars sent: " + tribes[tribeId].getnStarsSent());
            rank++;
        }
    }


    /**
     * This method call all agents' end-of-game method for post-processing.
     * Agents receive their final game state and reward
     */
    @SuppressWarnings("UnusedReturnValue")
    private void terminate() {

        Tribe[] tribes = gs.getTribes();
        for (int i = 0; i < numPlayers; i++) {
            Agent ag = players[i];
            ag.result(gs.copy(), tribes[i].getScore());
        }
    }

    private void updateBranchingFactor(AIStats aiStats, int turn, GameState currentGameState, Agent ag) {
        ArrayList<Integer> actionCounts = ag.actionsPerUnit(currentGameState);
        aiStats.addBranchingFactor(turn, actionCounts);
        aiStats.addActionsPerStep(turn, ag.actionsPerGameState(gs));
    }

    /**
     * Updates the gameplay stats after a move
     */
    private void updateGameplayStatsMove(GameplayStats gps, Action played, GameState curGameState)
    {
        gps.logAction(played, curGameState.getTick());
    }

    /**
     * Updates the gameplay stats at the end of a trun
     */
    private void updateGameplayStatsTurn(GameplayStats gps, GameState curGameState)
    {
        gps.logGameState(curGameState);
    }

    /**
     * Returns the winning status of all players.
     * @return the winning status of all players.
     */
    public Types.RESULT[] getWinnerStatus()
    {
        //Build the results array
        Tribe[] tribes = gs.getTribes();
        Types.RESULT[] results = new Types.RESULT[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            Tribe tribe = tribes[i];
            results[i] = tribe.getWinner();
        }
        return results;
    }

    /**
     * Returns the current scores of all players.
     * @return the current scores of all players.
     */
    public int[] getScores()
    {
        //Build the results array
        Tribe[] tribes = gs.getTribes();
        int[] scores = new int[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            scores[i] = tribes[i].getScore();
        }
        return scores;
    }

    /**
     * Updates the state observations for all players with copies of the
     * current game state, adapted for PO.
     */
    private void updateAssignedGameStates() {

        //TODO: Probably we don't need to do this for all players, just the active one.
        for (int i = 0; i < numPlayers; i++) {
            gameStateObservations[i] = getGameState(i);
        }
    }

    /**
     * Returns the game state as seen for the player with the index playerIdx. This game state
     * includes only the observations that are visible if partial observability is enabled.
     * @param playerIdx index of the player for which the game state is generated.
     * @return the game state.
     */
    public GameState getGameState(int playerIdx) {
        return gs.copy(playerIdx);
    }

    public GameState getGameState() {
        return gs;
    }


    /**
     * Returns the game board.
     * @return the game board.
     */
    public Board getBoard()
    {
        return gs.getBoard();
    }

    public Agent[] getPlayers() {
        return players;
    }

    /**
     * Method to identify the end of the game. If the game is over, the winner is decided.
     * The winner of a game is determined by TribesConfig.GAME_MODE and TribesConfig.MAX_TURNS
     * @return true if the game has ended, false otherwise.
     */
    public boolean gameOver() {
        return gs.gameOver();
    }

    public void setAnimationPaused(boolean p) {
        animationPaused = p;
    }
    public void setPaused(boolean p) {
        paused = p;
    }

    public boolean isPaused() {
        return paused;
    }

    public TreeSet<TribeResult> getCurrentRanking() {
        return gs.getCurrentRanking();
    }

    public GameplayStats getGamePlayStats(int id) {
        return gpStats[id];
    }
}
