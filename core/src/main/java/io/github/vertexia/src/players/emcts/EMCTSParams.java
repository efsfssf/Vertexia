package io.github.vertexia.src.players.emcts;

import io.github.vertexia.src.players.heuristics.AlgParams;

public class EMCTSParams extends AlgParams {
    public float bias = 1f;
    public int depth = 10;
    public int NODE_SIZE = 20;
}
