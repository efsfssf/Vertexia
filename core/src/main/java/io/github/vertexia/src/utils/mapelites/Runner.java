package io.github.vertexia.src.utils.mapelites;

import io.github.vertexia.src.utils.stats.GameplayStats;

import java.util.ArrayList;

public interface Runner {
    ArrayList<GameplayStats> run(double[] genome);
}
