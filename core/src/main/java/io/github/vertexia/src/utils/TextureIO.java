package io.github.vertexia.src.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class TextureIO {
    private static TextureIO instance;
    private final HashMap<String, Texture> cache = new HashMap<>();

    private TextureIO() {}

    public static TextureIO GetInstance() {
        if (instance == null)
            instance = new TextureIO();
        return instance;
    }

    public Texture getTexture(String path) {
        if (path == null) return null;

        if (!cache.containsKey(path)) {
            cache.put(path, new Texture(Gdx.files.internal(path)));
        }

        return cache.get(path);
    }
}
