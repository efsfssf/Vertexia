package io.github.vertexia.src.players.portfolio.scripts.utils;

import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.Board;
import io.github.vertexia.src.core.game.GameState;
import io.github.vertexia.src.utils.Vector2d;

public interface InterestPoint
{
    boolean ofInterest(GameState gs, Actor ac, int posX, int posY);
}
