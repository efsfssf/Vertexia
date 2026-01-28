package io.github.vertexia.src.gui;

import java.util.*;

import io.github.vertexia.src.core.Constants;
import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.core.actions.cityactions.CityAction;
import io.github.vertexia.src.core.actions.unitactions.*;
import io.github.vertexia.src.core.actions.unitactions.command.AttackCommand;
import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.actors.City;
import io.github.vertexia.src.core.actors.Tribe;
import io.github.vertexia.src.core.actors.units.Catapult;
import io.github.vertexia.src.core.actors.units.SuperUnit;
import io.github.vertexia.src.core.actors.units.Unit;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.Game;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.core.actions.Action;
import io.github.vertexia.src.players.ActionController;
import io.github.vertexia.src.utils.TextureIO;
import io.github.vertexia.src.utils.Pair;
import io.github.vertexia.src.utils.Vector2d;

import static io.github.vertexia.src.core.Constants.*;
import static io.github.vertexia.src.core.Types.TERRAIN.*;
import static io.github.vertexia.src.core.Types.UNIT.*;
import static io.github.vertexia.src.core.Types.ACTION.*;
import static io.github.vertexia.src.utils.GdxCompat.toColor;
import static io.github.vertexia.src.utils.Vector2d.manhattanDistance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

@SuppressWarnings({"SuspiciousNameCombination", "unchecked"})
public class GameView implements Screen {

    static int gridSize;
    private Game game;
    private final ActionController ac;
    private Board board; //This only counts terrains. Needs to be enhanced with actors, resources, etc.
    private GameState gameState;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final OrthographicCamera camera;
//    private Image backgroundImg;
    private Texture fogImg, shineImg;
    private SelectionState selection;
    private Vector2d panTranslate;  // Used to translate all coordinates for objects drawn on screen
    private boolean effectsLoaded = false;
    private BitmapFont font;

    private Color progressColor = toColor(53, 183, 255);
    private Color negativeColor = toColor(255, 63, 73);
    private Texture starImg, starShadow, capitalImg, capitalShadow, cityWalls; // road;
    private Texture roadVhalf, roadDhalf;

    boolean[][] actionable;

    // Action animations
    private ArrayList<Pair<Pair<Texture, Vector2d>,Pair<Texture, Vector2d>>> sourceTargetAnimationInfo;
    private ArrayList<Double> animationSpeed;
    private ArrayList<Pair<Integer, Integer>> actionAnimationUnitsTribe;
    private UnitAction animatedAction;

    private Texture[] explosionEffect, pierceEffect;
    private Texture[][] slashEffect, healEffect, convertEffect;  // Different per tribe
    private int effectDrawingIdx = -1, effectTribeIdx;
    private ArrayList<Vector2d> effectPositions;
    private EFFECT effectType;  // What effect are we drawing?
    int delay = 5;
    final int nTilesExplosion = 12;
    final int nTilesEffect = 6;
    final int nTilesPierce = 3;

    enum EFFECT{
        EXPLOSION,
        SLASH,
        HEAL,
        CONVERT,
        PIERCE
    }

    /**
     * Dimensions of the window.
     */
    public static Vector2 dimension;
    private static double isometricAngle = -45;

    public GameView(Game game, ActionController ac)
    {
        this.game = game;
        this.ac = ac;
        this.selection = new SelectionState();
        this.panTranslate = new Vector2d(0, 0);
        dimension = new Vector2(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
//        int size = gridSize * CELL_SIZE;
//        int d = (int) Math.sqrt(size * size * 2);

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        // диагональ экрана
        double screenDiagonal = Math.sqrt(w * w + h * h);

        CELL_SIZE = Math.max(16, (int)(0.038 * screenDiagonal));

        Gdx.app.log("GameView", "CELL_SIZE = " + CELL_SIZE);

        this.batch = new SpriteBatch();
        this.shapes = new ShapeRenderer();
        this.font = new BitmapFont();

        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.GUI_GAME_VIEW_SIZE, Constants.GUI_GAME_VIEW_SIZE);

        loadTextures();
        initEffects();

        for (int i = 0; i < nTilesExplosion; i++) {
            for (int j = 0; j < delay; j++) {
                explosionEffect[i*delay+j] = TextureIO.GetInstance().getTexture("img/weapons/effects/explosion/tile" + String.format("%03d", i) + ".png");
            }
        }
        for (int i = 0; i < nTilesPierce; i++) {
            for (int j = 0; j < delay; j++) {
                pierceEffect[i*delay+j] = TextureIO.GetInstance().getTexture("img/weapons/effects/pierce/tile" + String.format("%03d", i) + ".png");
            }
        }


        sourceTargetAnimationInfo = new ArrayList<>();
        animationSpeed = new ArrayList<>();
        actionAnimationUnitsTribe = new ArrayList<>();
        effectPositions = new ArrayList<>();
    }

    private void initEffects() {
        int expLength = nTilesExplosion * delay;
        int pierceLength = nTilesPierce * delay;
        explosionEffect = new Texture[expLength];
        pierceEffect = new Texture[pierceLength];
    }

    private void loadTextures() {
        //        backgroundImg = PLAIN.getTexture(null);
        fogImg = TextureIO.GetInstance().getTexture("img/fog.png");
        shineImg = TextureIO.GetInstance().getTexture("img/shine3.png");
        starImg = TextureIO.GetInstance().getTexture("img/decorations/star.png");
        starShadow = TextureIO.GetInstance().getTexture("img/decorations/starShadow.png");
        capitalImg = TextureIO.GetInstance().getTexture("img/decorations/capital.png");
        capitalShadow = TextureIO.GetInstance().getTexture("img/decorations/capitalShadow.png");
        cityWalls = TextureIO.GetInstance().getTexture("img/terrain/walls.png");
//        road = TextureIO.GetInstance().getTexture("img/terrain/road.png");
        roadDhalf = TextureIO.GetInstance().getTexture("img/terrain/road-d-half.png");
        roadVhalf = TextureIO.GetInstance().getTexture("img/terrain/road-v-half.png");
    }


//    public void paintComponent(Graphics gx)
//    {
//        Graphics2D g = (Graphics2D) gx;
//        paintWithGraphics(g);
//    }

    @Override
    public void render(float delta) {
        if (gameState == null || board == null) {
            return;
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        updateActionableTiles();

        batch.begin();
        paintTerrains(batch);
        paintRoads(batch);
        paintCities(batch);
        paintResourcesBuildings(batch);
        paintUnits(batch);
        paintOtherActions(batch);
        paintEffects(batch);
        batch.end();
    }

//    private void paintWithGraphics(Graphics2D g)
//    {
//        if(gameState == null)
//            return;
//
//        //For a better graphics, enable this: (be aware this could bring performance issues depending on your HW & OS).
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, dimension.width, dimension.height);
//
//        // Update list of actionable tiles to be highlighted (collectible resources)
//        updateActionableTiles();
//        paintTerrains(g);
//        paintRoads(g);
//        paintCities(g);
//        paintResourcesBuildings(g);
//
//        int highlightX = selection.x();
//        int highlightY = selection.y();
//
//        highlightTile(g, highlightX, highlightY);
//
//        drawCityDecorations(g);
//        paintUnits(g);
//        paintActionsHighlightedUnit(g, highlightX, highlightY);
//        paintOtherActions(g);
//        if (GUI_DRAW_EFFECTS) {
//            paintEffects(g);
//        }
//        paintActionAnimations(g);
//
//        g.setColor(Color.BLACK);
//        //player.draw(g); //if we want to give control to the agent to paint something (for debug), start here.
//    }

    private void updateActionableTiles() {
        if (gameState == null) return;

        actionable = new boolean[gridSize][gridSize];
        HashMap<Integer, ArrayList<Action>> actions = gameState.getCityActions();
        for (Map.Entry<Integer, ArrayList<Action>> e: actions.entrySet()) {
            for (Action a: e.getValue()) {
                if (a instanceof CityAction) {
                    Vector2d pos = ((CityAction) a).getTargetPos();
                    if (pos != null) {
                        actionable[pos.x][pos.y] = a.getActionType() == Types.ACTION.RESOURCE_GATHERING;
                    }
                }
            }
        }
    }

    private void paintTerrains(SpriteBatch batch) {
        for(int i = 0; i < gridSize; ++i) {
            for(int j = 0; j < gridSize; ++j) {
                Types.TERRAIN t = board.getTerrainAt(i,j);
                Texture toPaint;
                if (t == null || t == FOG) {
                    toPaint = fogImg;
                } else if (t != CITY) {
                    toPaint = getContextImg(i, j, t);
                } else {
                    toPaint = getContextImg(i, j, PLAIN);
                }
                Vector2d rotated = rotatePoint(j, i);
                batch.draw(toPaint,
                    rotated.x + panTranslate.x,
                    rotated.y + panTranslate.y,
                    CELL_SIZE,
                    CELL_SIZE);
//                paintImageRotated(g, j * CELL_SIZE, i * CELL_SIZE, toPaint, CELL_SIZE, panTranslate);
            }
        }
    }

    private void paintCities(SpriteBatch batch) {
        for(int i = 0; i < gridSize; ++i) {
            for(int j = 0; j < gridSize; ++j) {
                Types.TERRAIN t = board.getTerrainAt(i,j);
                if (t == CITY) {
                    Texture toPaint = t.getTexture(null);

                    Vector2d rotated = rotatePoint(j, i);

                    batch.draw(
                        toPaint,
                        rotated.x + panTranslate.x,
                        rotated.y + panTranslate.y,
                        CELL_SIZE,
                        CELL_SIZE
                    );
                }
            }
        }
    }

    private void paintRoads(SpriteBatch batch) {
        for(int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                Vector2d cur = new Vector2d(i, j);
                LinkedList<Vector2d> neighborhood = cur.neighborhood(1, 0, gridSize);
                boolean anyRoads = false;
                if (board.checkTradeNetwork(i, j)) {
                    for (Vector2d n: neighborhood) {
                        if (board.checkTradeNetwork(n.x, n.y)) {
                            // Draw half road in that direction
                            Vector2d rotated = rotatePoint(j, i);

                            double dx = (n.x - cur.x);
                            double dy = (n.y - cur.y);
                            boolean diagonal = Math.abs(dx) == 1 && Math.abs(dy) == 1;
                            double imageAngleRad = Math.atan2(dx, dy) + Math.toRadians(isometricAngle+90);

                            int x = rotated.x + CELL_SIZE/4;
                            int y = rotated.y - CELL_SIZE/2;

                            if (!diagonal) {
                                drawTextureRotated(batch, roadVhalf, x, y,  CELL_SIZE, (float) imageAngleRad, CELL_SIZE / 2, CELL_SIZE / 2);
                            } else {
                                drawTextureRotated(
                                    batch,
                                    roadDhalf,
                                    x,
                                    y,
                                    CELL_SIZE,
                                    (float)(imageAngleRad - Math.toRadians(45)),
                                    CELL_SIZE / 2f,
                                    CELL_SIZE / 2f
                                );
                            }
                            anyRoads = true;

//                             // Debugging
//                            g.drawString("R", rotated.x + CELL_SIZE/2, rotated.y + CELL_SIZE/2);
                        }
                    }
                    if (!anyRoads && board.getTerrainAt(i, j) != CITY && board.getBuildingAt(i, j) != Types.BUILDING.PORT) {
                        Vector2d rotated = rotatePoint(j, i);

                        batch.draw(
                            roadVhalf,
                            rotated.x + panTranslate.x,
                            rotated.y + panTranslate.y,
                            CELL_SIZE,
                            CELL_SIZE
                        );
                    }
                }
            }
        }
    }

    private void paintResourcesBuildings(SpriteBatch batch) {
        for(int i = 0; i < gridSize; ++i) {
            for(int j = 0; j < gridSize; ++j) {
                Types.TERRAIN t = board.getTerrainAt(i,j);
                Types.RESOURCE r = board.getResourceAt(i,j);
                if (actionable[i][j])
                {
                    Vector2d rotated = rotatePoint(j, i);
                    batch.draw(shineImg, rotated.x + panTranslate.x, rotated.y + panTranslate.y, CELL_SIZE, CELL_SIZE);
                }
                int imgSize = (int) (CELL_SIZE * 0.75);
                drawTextureCentered(
                    batch,
                    (r == null) ? null : r.getTexture(t),
                    j, i,
                    imgSize
                );

                Types.BUILDING b = board.getBuildingAt(i,j);
                if (b != null && b != Types.BUILDING.CUSTOMS_HOUSE && !(b.isTemple())) imgSize = CELL_SIZE;

                drawTextureCentered(
                    batch,
                    (b == null) ? null : b.getTexture(),
                    j, i,
                    imgSize
                );
            }
        }
    }

//    private void highlightTile(Graphics2D g, int highlightX, int highlightY) {
//        if (highlightX != -1) {
//            Stroke oldStroke = g.getStroke();
//            g.setColor(Color.BLUE);
//            g.setStroke(new BasicStroke(3));
//
//            Vector2d p = rotatePoint(highlightX, highlightY);
//            drawRotatedRect(g, p.x, p.y, CELL_SIZE - 1, CELL_SIZE - 1, panTranslate);
//            g.setStroke(oldStroke);
//            g.setColor(Color.BLACK);
//        }
//    }

    private void paintUnits(SpriteBatch batch) {
        for(int i = 0; i < gridSize; ++i) {
            for(int j = 0; j < gridSize; ++j) {
                Unit u = board.getUnitAt(i,j);
                if (u != null) {
                    int imgSize = Math.max(1, (int)(CELL_SIZE * 0.75));
                    if (u instanceof SuperUnit || u instanceof Catapult) imgSize = Math.max(1, CELL_SIZE);

                    Vector2d rotated = rotatePoint(j, i);
                    int x = rotated.x + CELL_SIZE / 4;
                    int y = (int)(rotated.y - imgSize * 0.75f);

                    ArrayList<Action> possibleActions = gameState.getUnitActions(u);
                    boolean exhausted = (possibleActions == null || possibleActions.size() == 0);

                    Tribe t = gameState.getTribe(u.getTribeId());
                    int imageTribeId = t.getType().getKey();

                    paintUnit(batch, x, y, u.getType(), imgSize, imageTribeId, exhausted);

                    if (u.getType().isWaterUnit()) {
                        // Paint base land unit for water units
                        int size = imgSize/2;
                        paintUnit(batch, x + CELL_SIZE/4, y + CELL_SIZE/4, board.getBaseLandUnit(u), size, imageTribeId, exhausted);
                    }

//                    Font f = g.getFont();
//                    g.setFont(new Font(f.getFontName(), Font.PLAIN, CELL_SIZE/5));
//                    g.setColor(Color.black);
//                    g.drawString(u.getCurrentHP() + "/" + u.getMaxHP(), x + g.getFont().getSize()/2 + panTranslate.x, y + panTranslate.y);
//                    g.setFont(f);
                    String hp = u.getCurrentHP() + "/" + u.getMaxHP();
                    float fontScale = Math.max(0.5f, CELL_SIZE / 20f);
                    font.getData().setScale(fontScale);
                    drawText(batch, hp,
                        x + CELL_SIZE / 4f,
                        y + CELL_SIZE / 4f
                    );
                }
            }
        }
    }

    private void paintUnit(SpriteBatch batch, int x, int y, Types.UNIT type, int imgSize, int tribeId, boolean exhausted) {
        String imgFile = type.getTextureFile();

        if (type == null) return;

        Texture texture;

        if (exhausted) {
            String exhaustedStr = imgFile + tribeId + "Exhausted.png";
            texture  = TextureIO.GetInstance().getTexture(exhaustedStr);
        } else {
            texture = type.getTexture(tribeId);
        }

        if (texture == null) return;

        batch.draw(
            texture,
            x + panTranslate.x,
            y + panTranslate.y,
            imgSize,
            imgSize
        );

    }

    private void paintActionsHighlightedUnit(SpriteBatch batch, int highlightX, int highlightY) {
        if (selection.highlightInGridBounds(gridSize)) {
            Unit u = board.getUnitAt(highlightY, highlightX);
            if (u != null && !selection.clickedTwice()) {
                ArrayList<Action> possibleActions = gameState.getUnitActions(u);
                if (possibleActions != null && possibleActions.size() > 0) {
                    for (Action a : possibleActions) {
                        if (!(a.getActionType() == Types.ACTION.EXAMINE || a.getActionType() == Types.ACTION.CAPTURE)) {
                            Texture actionImg = Types.ACTION.getTexture(a);

                            if (actionImg != null) {
                                Vector2d pos = ActionPositionResolver.get(gameState, a);

                                if (pos != null) {
                                    if (a.getActionType() == Types.ACTION.MOVE) {
                                        drawTextureCentered(batch, actionImg, pos.y, pos.x,  CELL_SIZE);
                                    } else {
                                        Vector2d rotated = rotatePoint(pos.y, pos.x);
                                        batch.draw(actionImg, rotated.x + panTranslate.x, rotated.y + panTranslate.y - CELL_SIZE / 2f, CELL_SIZE, CELL_SIZE);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void paintOtherActions(SpriteBatch batch) {
        HashMap<Integer, ArrayList<Action>> actions = gameState.getUnitActions();
        for (Map.Entry<Integer, ArrayList<Action>> e: actions.entrySet()) {
            for (Action a : e.getValue()) {
                if (a.getActionType() == EXAMINE || a.getActionType() == CAPTURE) {
                    Texture actionImg = Types.ACTION.getTexture(a);
                    if (actionImg != null) {
                        Vector2d pos = ActionPositionResolver.get(gameState, a);

                        if (pos != null) {
                            Vector2d rotated = rotatePoint(pos.y, pos.x);
                            int imgSize = (int)(CELL_SIZE*0.5);

                            batch.draw(
                                actionImg,
                                rotated.x + panTranslate.x + CELL_SIZE,
                                rotated.y + panTranslate.y - imgSize / 2f,
                                imgSize,
                                imgSize
                            );
                        }
                    }
                }
            }
        }
    }

    // *****************************************************************************************************************

    private void drawTextureCentered(SpriteBatch batch, Texture texture, float gridX, float gridY, float size) {
        if (texture == null) return;

        Vector2d rotated = rotatePoint(gridX, gridY);

        float x = rotated.x + panTranslate.x + CELL_SIZE / 2f - size / 2f;
        float y = rotated.y + panTranslate.y + CELL_SIZE / 2f - size / 2f;

        batch.draw(texture, x, y, size, size);
    }

    private void drawTextureRotated(SpriteBatch batch, Texture texture, float x, float y, float size, float angleRad, float originX, float originY) {
        if (texture == null) return;

        float angleDeg = (float) Math.toDegrees(angleRad);

        batch.draw(
            texture,
            x + panTranslate.x,
            y + panTranslate.y,
            originX, originY,          // origin (точка вращения)
            size, size,                // width / height
            1f, 1f,                    // scale
            angleDeg,                  // rotation (градусы!)
            0, 0,                      // srcX, srcY
            texture.getWidth(),
            texture.getHeight(),       // srcWidth, srcHeight
            false, false               // flipX, flipY
        );

    }

//    private static void paintImageRotated(Graphics2D gphx, int x, int y, Image img, int imgSize, Vector2d panTranslate) {
//        if (img != null) {
//            int w = img.getWidth(null);
//            int h = img.getHeight(null);
//            float scaleX = (float)imgSize/w;
//            float scaleY = (float)imgSize/h;
//
//            Graphics2D g2 = (Graphics2D)gphx.create();
//            g2.translate(panTranslate.x, panTranslate.y + dimension.width/2.0);
//            g2.rotate(Math.toRadians(isometricAngle));
//            g2.drawImage(img, (int)(x + CELL_SIZE/2.0 - imgSize/2.0),
//                    (int)(y + CELL_SIZE/2.0 - imgSize/2.0),
//                    (int) (w*scaleX), (int) (h*scaleY), null);
//            g2.dispose();
//        }
//    }

//    private static void paintImageRotated(Graphics2D gphx, int x, int y, Texture img, int imgSize, Vector2d panTranslate,
//                                          double angle, int xAnchor, int yAnchor) {
//        if (img != null) {
//            int w = img.getWidth(null);
//            int h = img.getHeight(null);
//            float scaleX = (float)imgSize/w;
//            float scaleY = (float)imgSize/h;
//
//            Graphics2D g2 = (Graphics2D)gphx.create();
//            g2.translate(panTranslate.x, panTranslate.y);
//            g2.rotate(angle, xAnchor, yAnchor);
//            g2.drawImage(img, x, y, (int) (w*scaleX), (int) (h*scaleY), null);
//            g2.dispose();
//        }
//    }

//    private static void paintImage(Graphics2D gphx, int x, int y, Texture img, int imgSize, Vector2d panTranslate)
//    {
//        if (img != null) {
//            int w = img.getWidth(null);
//            int h = img.getHeight(null);
//            float scaleX = (float)imgSize/w;
//            float scaleY = (float)imgSize/h;
//            gphx.drawImage(img, x + panTranslate.x, y + panTranslate.y,
//                    (int) (w*scaleX), (int) (h*scaleY), null);
//        }
//    }
//
//    private static void drawRotatedRect(Graphics2D g, int x, int y, int width, int height, Vector2d panTranslate) {
//        Graphics2D g2 = (Graphics2D) g.create();
//        x += panTranslate.x;
//        y += panTranslate.y;
//        g2.rotate(Math.toRadians(isometricAngle), x, y);
//        g2.drawRect(x, y, width, height);
//        g2.dispose();
//    }

    /**
     * Expects coordinates in grid, translates to screen coordinates.
     */
    public Vector2d rotatePoint(double x, double y) {
        double d = Math.sqrt(2*CELL_SIZE*CELL_SIZE);
        double x2 = x * CELL_SIZE + y * d/2 - x * d/5;
        double y2 = y * CELL_SIZE - y * d/5 - x * d/2;
        y2 += camera.viewportWidth /2.0;
        return new Vector2d((int)x2, (int)y2);
    }

    /**
     * Expects screen coordinates, returns coordinates in grid.
     */
    public Vector2d rotatePointReverse(double x, double y) {
        double d = Math.sqrt(2*CELL_SIZE*CELL_SIZE);
        y -= camera.viewportWidth /2.0;
        double x2 = (x*(CELL_SIZE-d/5.0) - y*d/2.0)/(CELL_SIZE*CELL_SIZE - 2*CELL_SIZE*d/5.0 + (d/5)*(d/5) + d*d/4.0);
        double y2 = (y + x2 * d/2.0)/(CELL_SIZE - d/5.0);
        return new Vector2d((int)x2, (int)y2);
    }
//
//    private void drawCityDecorations(Graphics2D g) {
//        for(int i = 0; i < gridSize; ++i) {
//            for (int j = 0; j < gridSize; ++j) {
//                Types.TERRAIN terrainAt = board.getTerrainAt(i, j);
//                if (terrainAt == CITY) {
//                    int d = (int)Math.sqrt(CELL_SIZE*CELL_SIZE*2);
//                    int fontSize = CELL_SIZE/3;
//                    Font textFont = new Font(getFont().getName(), Font.PLAIN, fontSize);
//                    g.setFont(textFont);
//
//                    int cityID = board.getCityIdAt(i,j);
//                    City c = (City) board.getActor(cityID);
//
//                    if (c != null) {
//                        int cityCapacity = c.getLevel() + 1;
//                        int progress = c.getPopulation();
//                        int units = c.getUnitsID().size();
//                        Tribe tr = gameState.getTribe(c.getTribeId());
//                        Color col = Types.TRIBE.values()[tr.getType().getKey()].getColorDark();
//                        Color colTransparent = new Color(col.getRed(), col.getGreen(), col.getBlue(), 170);
//
//                        // Draw city walls
//                        if (c.hasWalls()) {
//                            Vector2d rotatedP = rotatePoint(j + 0.4, i - 0.2);
//                            paintImage(g, rotatedP.x, rotatedP.y, cityWalls, CELL_SIZE, panTranslate);
//                        }
//
//                        // Draw city border
//                        LinkedList<Vector2d> tiles = board.getCityTiles(cityID);
//                        int nCityTiles = tiles.size();
//                        int nNeighbours = 4;
//                        boolean[][] tileNeighbours = new boolean[nCityTiles][nNeighbours];  // order: left, right, up, down
//                        Pair<Vector2d, Vector2d>[] lines = new Pair[]{new Pair<>(new Vector2d(0, 0), new Vector2d(0, 1)),
//                                new Pair<>(new Vector2d(1, 0), new Vector2d(1, 1)),
//                                new Pair<>(new Vector2d(0, 0), new Vector2d(1, 0)),
//                                new Pair<>(new Vector2d(0, 1), new Vector2d(1, 1))};
//                        for (int k = 0; k < nCityTiles-1; k++) {
//                            Vector2d t1 = tiles.get(k);
//                            for (int p = k+1; p < nCityTiles; p++) {
//                                Vector2d t2 = tiles.get(p);
//                                if (t1.equals(t2)) continue;
//                                if (t1.x - t2.x == 1 && t1.y == t2.y) {  // t1 to the right of t2
//                                    tileNeighbours[k][0] = true;
//                                    tileNeighbours[p][1] = true;
//                                } else if (t1.x - t2.x == -1 && t1.y == t2.y) { // t1 to the left of t2
//                                    tileNeighbours[k][1] = true;
//                                    tileNeighbours[p][0] = true;
//                                } else if (t1.x == t2.x && t1.y - t2.y == 1) {  // t1 underneath t2
//                                    tileNeighbours[k][2] = true;
//                                    tileNeighbours[p][3] = true;
//                                } else if (t1.x == t2.x && t1.y - t2.y == -1) {  // t1 above t2
//                                    tileNeighbours[k][3] = true;
//                                    tileNeighbours[p][2] = true;
//                                }
//                            }
//                        }
//                        g.setColor(col);
//                        Stroke oldStroke = g.getStroke();
//                        g.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
//                        // Draw lines for tiles that don't have a neighbour on a side
//                        for (int t = 0; t < nCityTiles; t++) {
//                            Vector2d tile = tiles.get(t);
//                            for (int n = 0; n < nNeighbours; n++) {
//                                if (!tileNeighbours[t][n]) {
//                                    // draw line on this side
//                                    Vector2d from = rotatePoint(lines[n].getFirst().y + tile.y, lines[n].getFirst().x + tile.x);
//                                    Vector2d to = rotatePoint(lines[n].getSecond().y + tile.y, lines[n].getSecond().x + tile.x);
//                                    g.drawLine(from.x + panTranslate.x, from.y + panTranslate.y,
//                                            to.x + panTranslate.x, to.y + panTranslate.y);
//                                }
//                            }
//                        }
//                        g.setStroke(oldStroke);
//
//                        // Draw capital img + city name/ID + number of stars
//                        String cityName = "" + cityID;
//                        String production = "" + c.getProduction();
//
//                        int sections = 2;
//                        if (c.isCapital()) {
//                            sections = 3;
//                        }
//                        int h = d / 4;
//                        double nameWidth = GUI_CITY_TAG_WIDTH + sections * h;
//                        Vector2d namePos = rotatePoint(j, i);
//                        Rectangle nameRect = new Rectangle((int) (namePos.x + d / 2.0 - nameWidth * 2 / 3.0),
//                                (int) (namePos.y + d / 2.0 - h), (int) nameWidth, h);
//                        g.setColor(colTransparent);
//                        g.fillRect(nameRect.x + panTranslate.x, nameRect.y + panTranslate.y, nameRect.width, nameRect.height);
//                        g.setColor(Color.WHITE);
//
//                        g.drawString(cityName, (int) (nameRect.x + (sections-2) * h + fontSize / 4.0 + panTranslate.x),
//                                (int) (nameRect.y + h * 1.1 - fontSize / 4.0 + panTranslate.y));
//
//                        // Draw number of stars
//                        paintImage(g, (int) (nameRect.x + nameRect.width * (0.35 + (sections-2)*0.2) + SHADOW_OFFSET),
//                                nameRect.y + SHADOW_OFFSET, starShadow, h, panTranslate);
//                        paintImage(g, (int) (nameRect.x + nameRect.width * (0.35 + (sections-2)*0.2)), nameRect.y, starImg, h, panTranslate);
//                        drawStringShadow(g, production, (int) (nameRect.x + nameRect.width - fontSize * 0.75),
//                                (int) (nameRect.y + h * 1.1 - fontSize / 4.0));
//                        g.setColor(Color.WHITE);
//                        g.drawString(production, (int) (nameRect.x + nameRect.width - fontSize * 0.75 + panTranslate.x),
//                                (int) (nameRect.y + h * 1.1 - fontSize / 4.0 + panTranslate.y));
//
//                        // Draw capital sign
//                        if (c.isCapital()) {
//                            paintImage(g, nameRect.x + SHADOW_OFFSET, nameRect.y + SHADOW_OFFSET, capitalShadow, h, panTranslate);
//                            paintImage(g, nameRect.x, nameRect.y, capitalImg, h, panTranslate);
//                        }
//
//                        // Draw level
//                        h /= 2;
//                        int sectionWidth = h;
//                        int w = cityCapacity * sectionWidth;
//                        Rectangle bgRect = new Rectangle(nameRect.x + nameRect.width / 2 - w / 2, nameRect.y + nameRect.height, w, h);
//                        drawRoundRectShadowHighlight(g, bgRect);
//                        g.setColor(Color.WHITE);
//                        g.fillRoundRect(bgRect.x + panTranslate.x, bgRect.y + panTranslate.y,
//                                bgRect.width, bgRect.height, ROUND_RECT_ARC, ROUND_RECT_ARC);
//
//                        // Draw population/progress
//                        if (progress >= 0) {
//                            g.setColor(progressColor);
//                        } else {
//                            g.setColor(negativeColor);
//                        }
//                        int pw = Math.abs(progress) * sectionWidth;
//                        Rectangle pgRect = new Rectangle(bgRect.x, bgRect.y, pw, bgRect.height);
//                        g.fillRoundRect(pgRect.x + panTranslate.x, pgRect.y + +panTranslate.y,
//                                pgRect.width, pgRect.height, ROUND_RECT_ARC, ROUND_RECT_ARC);
//
//                        // Draw unit counts
//                        g.setColor(Color.black);
//                        int radius = h / 2;
//                        int unitHeight = bgRect.y + h / 2 - radius / 2;
//                        for (int u = 0; u < units; u++) {
//                            g.fillOval(bgRect.x + sectionWidth * u + sectionWidth / 2 - radius / 2 + panTranslate.x,
//                                    unitHeight + panTranslate.y, radius, radius);
//                        }
//
//                        // Draw section separations
//                        for (int l = 0; l < cityCapacity - 1; l++) {
//                            int lx = bgRect.x + sectionWidth * (l + 1);
//                            g.drawLine(lx + panTranslate.x, bgRect.y + panTranslate.y,
//                                    lx + panTranslate.x, bgRect.y + bgRect.height + panTranslate.y);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void drawRoundRectShadowHighlight(Graphics2D g, Rectangle rect) {
//        g.setColor(new Color(0, 0, 0, 122));
//        g.fillRoundRect(rect.x + SHADOW_OFFSET + panTranslate.x,
//                rect.y + SHADOW_OFFSET + panTranslate.y, rect.width, rect.height,
//                ROUND_RECT_ARC, ROUND_RECT_ARC);
//        g.setColor(new Color(255, 255, 255, 122));
//        g.fillRoundRect(rect.x - SHADOW_OFFSET + panTranslate.x,
//                rect.y - SHADOW_OFFSET + panTranslate.y, rect.width, rect.height,
//                ROUND_RECT_ARC, ROUND_RECT_ARC);
//    }
//
//    private void drawStringShadow (Graphics2D g, String s, int x, int y) {
//        g.setColor(new Color(0, 0, 0, 122));
//        g.drawString(s, x+SHADOW_OFFSET + panTranslate.x, y+SHADOW_OFFSET + panTranslate.y);
//    }


    private void loadEffectsIfNeeded() {
        if (effectsLoaded) return;

        int nPlayers = game.getPlayers().length;
        slashEffect = new Texture[nPlayers][];
        healEffect = new Texture[nPlayers][];
        convertEffect = new Texture[nPlayers][];
        int effLength = nTilesEffect * delay;
        for (int j = 0; j < nPlayers; j++) {
            slashEffect[j%nPlayers] = new Texture[effLength];
            healEffect[j%nPlayers] = new Texture[effLength];
            convertEffect[j%nPlayers] = new Texture[effLength];
            for (int i = 0; i < nTilesEffect; i++) {
                for (int k = 0; k < delay; k++) {
                    slashEffect[j % nPlayers][i*delay + k] = TextureIO.GetInstance().getTexture("img/weapons/effects/slash/" + j + "/tile" + String.format("%03d", i) + ".png");
                    healEffect[j % nPlayers][i*delay + k] = TextureIO.GetInstance().getTexture("img/weapons/effects/heal/" + j + "/tile" + String.format("%03d", i) + ".png");
                    convertEffect[j % nPlayers][i*delay + k] = TextureIO.GetInstance().getTexture("img/weapons/effects/convert/" + j + "/tile" + String.format("%03d", i) + ".png");
                }
            }
        }
    }


    /**
     * Paints the board
     * @param gs current game state
     */
    public void paint(GameState gs)
    {
        //The tribe Id of which the turn gs at this point
        //int gameTurn = 0;// gs.getTick() % gs.getTribes().length;
        gameState = gs; //.copy(gameTurn);
        board = gameState.getBoard();
        gridSize = board.getSize();
    }

    void updatePan(Vector2d panTranslate) {
        this.panTranslate = new Vector2d(this.panTranslate.x + panTranslate.x,
                this.panTranslate.y + panTranslate.y);
    }

    void setPanToTribe(GameState gs) {
        // Focus on capital of tribe
        Tribe t = gs.getTribe(gs.getActiveTribeID());
        int capitalID = t.getCapitalID();
        Actor a = gs.getActor(capitalID);
        Vector2d pos = a.getPosition();

        // Get position in screen coordinates, and set pan to the negative difference to center
        Vector2d screenPoint = rotatePoint(pos.y, pos.x);
        panTranslate = new Vector2d(
            (int) (-screenPoint.x - CELL_SIZE / 2f + camera.viewportWidth / 2f),
            (int) (-screenPoint.y - CELL_SIZE / 2f + camera.viewportHeight / 2f)
        );
    }

    public Vector2d getPanTranslate() {
        return panTranslate;
    }

    /**
     * Gets the dimensions of the window.
     * @return the dimensions of the window.
     */
//    public Dimension getPreferredSize() {
//        return dimension;
//    }
    public Vector2d getViewSize() {
        return new Vector2d(
            (int) camera.viewportWidth,
            (int) camera.viewportHeight
        );
    }



    private Texture getContextImg(int i, int j, Types.TERRAIN t) {
        Texture toPaint;
        boolean cornerUL = (i == 0 && j == 0);
        boolean cornerDR = (i == gridSize - 1 && j == gridSize -1);
        Types.TERRAIN diagUR = null;
        if (i > 0 && j < gridSize - 1) diagUR = board.getTerrainAt(i - 1, j + 1);

        if (t == DEEP_WATER || t == SHALLOW_WATER) {
            // If this is the last tile on the row
            boolean down = (i == gridSize - 1);
            // If the tile above is not water
            boolean top = (i > 0 && board.getTerrainAt(i - 1, j) != SHALLOW_WATER && board.getTerrainAt(i - 1, j) != DEEP_WATER);
            // If first tile on column
            boolean left = (j == 0);
            // If the tile to the right is not water
            boolean right = (j < gridSize - 1 && board.getTerrainAt(i, j + 1) != SHALLOW_WATER && board.getTerrainAt(i, j + 1) != DEEP_WATER);
            if (down) {
                if (left) {
                    toPaint = t.getTexture("down-left");
                } else if (right) {
                    if (top) {
                        toPaint = t.getTexture("top-down-right");
                    } else {
                        if (t == SHALLOW_WATER && (diagUR == SHALLOW_WATER || diagUR == DEEP_WATER)) {
                            toPaint = t.getTexture("down-right-ur");
                        } else {
                            toPaint = t.getTexture("down-right");
                        }
                    }
                } else if (top) {
                    if (t == SHALLOW_WATER && (diagUR == SHALLOW_WATER || diagUR == DEEP_WATER)) {
                        toPaint = t.getTexture("top-down-ur");
                    } else {
                        toPaint = t.getTexture("top-down");
                    }
                } else {
                    toPaint = t.getTexture("down");
                }
            } else if (top) {
                if (t == SHALLOW_WATER && (j == gridSize-1 || diagUR == SHALLOW_WATER || diagUR == DEEP_WATER)) {
                    if (left) {
                        toPaint = t.getTexture("top-left-ur");
                    } else {
                        toPaint = t.getTexture("top-ur");
                    }
                } else {
                    if (right) {
                        if (left) {
                            toPaint = t.getTexture("top-left-right");
                        } else {
                            toPaint = t.getTexture("top-right");
                        }
                    } else if (left) {
                        toPaint = t.getTexture("top-left");
                    } else {
                        toPaint = t.getTexture("top");
                    }
                }
            } else if (right) {
                if (i == 0 || j < gridSize - 1 && t == SHALLOW_WATER && (diagUR == SHALLOW_WATER || diagUR == DEEP_WATER)) {
                    toPaint = t.getTexture("right-ur");
                } else {
                    toPaint = t.getTexture("right");
                }
            } else if (left) {
                toPaint = t.getTexture("left");
            } else {
                toPaint = t.getTexture(null);
            }
        } else {
            // If this is the last tile on the row, or the tile underneath is water, get the '-down' img
            boolean down = (i == gridSize - 1 || board.getTerrainAt(i+1, j) == SHALLOW_WATER);
            // If the same is true for the column instead, this is a '-left' img
            boolean left = (j == 0 || board.getTerrainAt(i, j-1) == SHALLOW_WATER);

            if (down) {
                if (j == gridSize - 1) {
                    if (cornerDR) {
                        toPaint = t.getTexture("corner-dr");
                    } else {
                        toPaint = t.getTexture("down-dr");
                    }
                } else {
                    if (left) {
                        if (j == 0) {
                            if (i < gridSize - 1 && (board.getTerrainAt(i, j + 1) == SHALLOW_WATER ||
                                            board.getTerrainAt(i, j + 1) == DEEP_WATER)) {
                                toPaint = t.getTexture("down-left-el-dr");
                            } else {
                                toPaint = t.getTexture("down-left-el");
                            }
                        } else if (i == gridSize - 1) {
                            if (board.getTerrainAt(i - 1, j) == SHALLOW_WATER
                                    || board.getTerrainAt(i - 1, j) == DEEP_WATER) {
                                toPaint = t.getTexture("down-left-ed-ul");
                            } else {
                                toPaint = t.getTexture("down-left-ed");
                            }
                        } else {
                            toPaint = t.getTexture("down-left");
                        }
                    } else {
                        if (j < gridSize - 1 && i < gridSize - 1 &&
                                (board.getTerrainAt(i, j + 1) == SHALLOW_WATER ||
                                board.getTerrainAt(i, j + 1) == DEEP_WATER)) {
                            toPaint = t.getTexture("down-dr");
                        } else {
                            toPaint = t.getTexture("down");
                        }
                    }
                }
            } else if (left) {
                if (i == 0 || board.getTerrainAt(i - 1, j) == SHALLOW_WATER ||
                        board.getTerrainAt(i - 1, j) == DEEP_WATER) {
                    if (cornerUL) {
                        toPaint = t.getTexture("corner-ul");
                    } else {
                        if (j == 0) {
                            toPaint = t.getTexture("left");
                        } else {
                            toPaint = t.getTexture("left-ul");
                        }
                    }
                } else {
                    toPaint = t.getTexture("left");
                }
            } else {
                Types.TERRAIN tl = board.getTerrainAt(i, j - 1);
                Types.TERRAIN td = board.getTerrainAt(i + 1, j);
                Types.TERRAIN tld = board.getTerrainAt(i + 1, j - 1);
                if (i < gridSize-1 && j > 0 && (tld == SHALLOW_WATER || tld == DEEP_WATER) &&
                        (tl != SHALLOW_WATER && tl != DEEP_WATER) &&
                        (td != SHALLOW_WATER && td != DEEP_WATER)) {
                    toPaint = t.getTexture("dl");
                } else {
                    toPaint = t.getTexture(null);
                }
            }
        }
        return toPaint;
    }

    // TODO: can draw more effects of actions, e.g. healing, disband
    void paintEffects(SpriteBatch batch) {
        if (effectDrawingIdx != -1) {
            Texture effectImage = null;
            if (effectType == EFFECT.EXPLOSION) {
                if (effectDrawingIdx >= explosionEffect.length) {
                    effectDrawingIdx = -1;  // Finished
                    effectPositions.clear();
                    return;
                }
                effectImage = explosionEffect[effectDrawingIdx];
            } else if (effectType == EFFECT.PIERCE) {
                if (effectDrawingIdx >= pierceEffect.length) {
                    effectDrawingIdx = -1;  // Finished
                    effectPositions.clear();
                    return;
                }
                effectImage = pierceEffect[effectDrawingIdx];
            } else if (effectType == EFFECT.SLASH) {
                if (effectDrawingIdx >= slashEffect[effectTribeIdx].length) {
                    effectDrawingIdx = -1;  // Finished
                    effectPositions.clear();
                    return;
                }
                effectImage = slashEffect[effectTribeIdx][effectDrawingIdx];
            } else if (effectType == EFFECT.HEAL) {
                if (effectDrawingIdx >= healEffect[effectTribeIdx].length) {
                    effectDrawingIdx = -1;  // Finished
                    effectPositions.clear();
                    return;
                }
                effectImage = healEffect[effectTribeIdx][effectDrawingIdx];
            } else if (effectType == EFFECT.CONVERT) {
                if (effectDrawingIdx >= convertEffect[effectTribeIdx].length) {
                    effectDrawingIdx = -1;  // Finished
                    effectPositions.clear();
                    return;
                }
                effectImage = convertEffect[effectTribeIdx][effectDrawingIdx];
            }

            // Draw effect
            if (effectImage != null) {
                for (Vector2d effectPosition : effectPositions) {
                    Vector2d rotated = rotatePoint(1.0 * effectPosition.x / CELL_SIZE, 1.0 * effectPosition.y / CELL_SIZE);
                    float x = rotated.x + CELL_SIZE / 5f;
                    float y = rotated.y - CELL_SIZE / 2f;
                    batch.draw(
                        effectImage,
                        x + panTranslate.x,
                        y + panTranslate.y,
                        CELL_SIZE,
                        CELL_SIZE
                    );
                    if (effectType == EFFECT.SLASH || effectType == EFFECT.CONVERT) {
                        drawTextureRotated(
                            batch,
                            effectImage,
                            x,
                            y,
                            CELL_SIZE,
                            (float) Math.PI / 2,
                            CELL_SIZE / 2f,
                            CELL_SIZE / 2f
                        );
                    }
//                    paintImage(g, rotated.x + CELL_SIZE / 5, rotated.y - CELL_SIZE / 2, effectImage, CELL_SIZE, panTranslate);
//                    if (effectType == EFFECT.SLASH || effectType == EFFECT.CONVERT) {
//                        int x = rotated.x + CELL_SIZE / 5;
//                        int y = rotated.y - CELL_SIZE / 2;
//                        paintImageRotated(g, x, y, effectImage, CELL_SIZE, panTranslate, Math.PI / 2, x + CELL_SIZE/2, y + CELL_SIZE/2);
//                    }
                }
                effectDrawingIdx++;
            } else {
                effectDrawingIdx = -1;
                effectPositions.clear();
            }
        }
    }

    void paintAction(UnitAction a) {
        if (a.getActionType() == ATTACK || a.getActionType() == CONVERT || a.getActionType() == HEAL_OTHERS) {  // These are the actions currently animated
            animationSpeed.clear();
            actionAnimationUnitsTribe.clear();
            sourceTargetAnimationInfo.clear();
            animatedAction = null;

            Unit source = (Unit) gameState.getBoard().getActor(a.getUnitId());
            Texture weapon1 = source.getType().getWeaponImage(source.getTribeId());

            if (weapon1 != null) {
                // Pause the game, paint this weapon image travelling from attacker to target
                game.setAnimationPaused(true);
                Pair<Texture, Vector2d> sourceAnimationInfo = new Pair<>(weapon1, new Vector2d(source.getPosition().y * CELL_SIZE, source.getPosition().x * CELL_SIZE));
                ArrayList<Unit> targets = new ArrayList<>();
                Texture weapon2 = null;

                if (a.getActionType() == ATTACK) {
                    Unit t = (Unit) gameState.getBoard().getActor(((Attack) a).getTargetId());
                    targets.add(t);
                    weapon2 = t.getType().getWeaponImage(t.getTribeId());  // units can retaliate in Attack actions

                    this.effectType = EFFECT.SLASH;
                    if (source.getType() == CATAPULT || source.getType() == SHIP || source.getType() == BATTLESHIP) {
                        this.effectType = EFFECT.EXPLOSION;
                    } else if (source.getType() == ARCHER || source.getType() == BOAT) {
                        this.effectType = EFFECT.PIERCE;
                    }
                } else if (a.getActionType() == CONVERT) {
                    this.effectType = EFFECT.CONVERT;
                    Unit target = (Unit) gameState.getBoard().getActor(((Convert) a).getTargetId());
                    targets.add(target);
                } else {
                    // Heal Others
                    this.effectType = EFFECT.HEAL;
                    ArrayList<Unit> ts = ((HealOthers) a).getTargets(gameState);
                    targets.addAll(ts);
                }

                for (Unit target: targets) {
                    Pair<Texture, Vector2d> targetAnimationInfo = new Pair<>(weapon2, new Vector2d(target.getPosition().y * CELL_SIZE, target.getPosition().x * CELL_SIZE));
                    animationSpeed.add(Math.max(1.0,
                            Math.min(25.0/(FRAME_DELAY+25), manhattanDistance(source.getPosition(), target.getPosition()) * (2.5/(FRAME_DELAY+25)))));
                    actionAnimationUnitsTribe.add(new Pair<>(source.getTribeId(), target.getTribeId()));
                    sourceTargetAnimationInfo.add(new Pair<>(sourceAnimationInfo, targetAnimationInfo));
                }
                if (targets.size() > 0) {
                    this.animatedAction = a;
                }
            }
        }
    }

//    private void paintActionAnimations(Graphics2D g) {
//        if (sourceTargetAnimationInfo.size() > 0) {
//            ArrayList<Integer> finished = new ArrayList<>();
//            for (int i = 0; i < sourceTargetAnimationInfo.size(); i++) {
//                Pair<Texture,Vector2d> source = sourceTargetAnimationInfo.get(i).getFirst();
//                Pair<Texture,Vector2d> target = sourceTargetAnimationInfo.get(i).getSecond();
//
//                // Sprite not yet reached its destination, paint current and calculate next
//                Vector2d currentPosition = source.getSecond().copy();
//
//                // Next position, move closer to target
//                int xDir = (int) (CELL_SIZE * animationSpeed.get(i) * Math.signum(target.getSecond().x - currentPosition.x));
//                int yDir = (int) (CELL_SIZE * animationSpeed.get(i) * Math.signum(target.getSecond().y - currentPosition.y));
//                source.getSecond().add(xDir, yDir);
//                Vector2d nextPosition = source.getSecond().copy();
//
//                // Rotate image in direction of travel
//                double dx = nextPosition.x - currentPosition.x;
//                double dy = nextPosition.y - currentPosition.y;
//                double imageAngleRad = Math.atan2(dx, dy);// + Math.toRadians(180);
//
//                Vector2d rotated = rotatePoint(1.0 * currentPosition.x / CELL_SIZE, 1.0 * currentPosition.y / CELL_SIZE);
//                int x = rotated.x + CELL_SIZE / 2;
//                int y = rotated.y - CELL_SIZE / 4;
//                paintImageRotated(g, x, y, source.getFirst(), CELL_SIZE / 2, panTranslate, imageAngleRad, x + CELL_SIZE/4, y + CELL_SIZE/4);
//
//                if (currentPosition.equalsPlusError(target.getSecond(), CELL_SIZE * 0.5)) {
//                    // Reached destination, no more drawing. Reset animation variables and unpause game, unless retaliation happening
//
//                    // Draw end of animation effect
//                    effectPositions.add(target.getSecond());
//                    effectTribeIdx = actionAnimationUnitsTribe.get(i).getFirst();
//                    effectDrawingIdx = 0;
//
//                    boolean finishAnimation = true;
//                    if (animatedAction.getActionType() == ATTACK && target.getFirst() != null) {
//                        boolean retaliates = new AttackCommand().isRetaliation((Attack) animatedAction, gameState);
//                        if(retaliates) {
//                            // Retaliating! Reset variables to target's attack
//                            Vector2d startPosition = target.getSecond().copy();
//                            Vector2d targetPosition = board.getActor(animatedAction.getUnitId()).getPosition().copy();
//                            Vector2d endPosition = new Vector2d(targetPosition.y * CELL_SIZE, targetPosition.x * CELL_SIZE);
//                            source = new Pair<>(target.getFirst(), startPosition);
//                            target = new Pair<>(null, endPosition);
//                            actionAnimationUnitsTribe.get(i).swap();
//                            sourceTargetAnimationInfo.set(i, new Pair<>(source, target));
//                            finishAnimation = false;
//                        }
//                    }
//
//                    if(finishAnimation)
//                    {
//                        finished.add(i);
//                    }
//                }
//            }
//            for (int i: finished) {
//                sourceTargetAnimationInfo.remove(i);
//                animationSpeed.remove(i);
//                actionAnimationUnitsTribe.remove(i);
//            }
//        } else {
//            if (effectDrawingIdx == -1 || !GUI_DRAW_EFFECTS) {
//                game.setAnimationPaused(false);
//            }
//        }
//    }

    private void drawText(
        SpriteBatch batch,
        String text,
        float x,
        float y
    ) {
        font.draw(batch, text, x + panTranslate.x, y + panTranslate.y);
    }

    Action getAnimatedAction() {
        if (sourceTargetAnimationInfo.size() == 0 && animatedAction != null) {
            Action a = animatedAction.copy();
            animatedAction = null;
            return a;
        }
        return null;
    }

    @Override
    public void show() {
        loadEffectsIfNeeded();

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        camera.setToOrtho(false, w, h);
        camera.position.set(w / 2f, h / 2f, 0);
        camera.update();

        // диагональ экрана
        double screenDiagonal = Math.sqrt(w * w + h * h);

        CELL_SIZE = Math.max(16, (int)(0.038 * screenDiagonal));

        Gdx.app.log("GameView", "CELL_SIZE = " + CELL_SIZE);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose();
        shapes.dispose();
        font.dispose();
    }
}
