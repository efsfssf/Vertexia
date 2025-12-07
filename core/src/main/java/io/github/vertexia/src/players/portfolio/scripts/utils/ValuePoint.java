package io.github.vertexia.src.players.portfolio.scripts.utils;

import io.github.vertexia.src.core.actors.Actor;
import io.github.vertexia.src.core.game.GameState;

public interface ValuePoint
{
    double ofInterest(GameState gs, Actor ac, int posX, int posY);
}
