package io.github.vertexia.src;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import org.json.JSONObject;

import io.github.vertexia.src.gui.GameView;
import io.github.vertexia.src.players.ActionController;

public class GameStarter extends Game {

    private ActionController ac;

    @Override
    public void create() {
        String jsonText = Gdx.files.internal("play.json").readString();

        JSONObject config;
        try {
            config = new JSONObject(jsonText);
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse play.json", e);
        }

        // 2. Инициализируем core-логику
        Play play = new Play(config);
        ac = new ActionController();

        // ⚠️ ВАЖНО: Play.start() ДОЛЖЕН ВЕРНУТЬ Game (core)
        io.github.vertexia.src.core.game.Game coreGame = play.startAndGetGame();

        // 3. Устанавливаем LibGDX Screen
        setScreen(new GameView(coreGame, ac));
    }

    @Override public void render() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }
}

