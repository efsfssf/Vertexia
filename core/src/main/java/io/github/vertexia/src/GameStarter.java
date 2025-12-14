package io.github.vertexia.src;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import org.json.JSONObject;

public class GameStarter implements ApplicationListener {

    @Override
    public void create() {
        String jsonText = Gdx.files.internal("play.json").readString();

        JSONObject config;
        try {
            config = new JSONObject(jsonText);
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse play.json", e);
        }

        Play play = new Play(config);
        play.start();
    }

    @Override public void render() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}
}

