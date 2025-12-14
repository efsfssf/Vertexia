package io.github.vertexia.src.utils;

import com.badlogic.gdx.graphics.Color;

public class GdxCompat {
    public static Color toColor(int r, int g, int b) {
        return new Color(r / 255f, g / 255f, b / 255f, 1f);
    }

    public static Color toColor(int r, int g, int b, int a) {
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }
}

