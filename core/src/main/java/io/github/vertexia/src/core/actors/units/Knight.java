package io.github.vertexia.src.core.actors.units;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.utils.Vector2d;

import static io.github.vertexia.src.core.TribesConfig.*;

public class Knight extends Unit
{
    public Knight(Vector2d pos, int kills, boolean isVeteran, int cityId, int tribeId) {
        super(KNIGHT_ATTACK, KNIGHT_DEFENCE, KNIGHT_MOVEMENT, KNIGHT_MAX_HP, KNIGHT_RANGE, KNIGHT_COST, pos, kills, isVeteran, cityId, tribeId);
    }

    @Override
    public Types.UNIT getType() {
        return Types.UNIT.KNIGHT;
    }

    @Override
    public Knight copy(boolean hideInfo) {
        Knight c = new Knight(getPosition(), getKills(), isVeteran(), getCityId(), getTribeId());
        c.setCurrentHP(getCurrentHP());
        c.setMaxHP(getMaxHP());
        c.setActorId(getActorId());
        c.setStatus(getStatus());
        return hideInfo ? (Knight) c.hide() : c;
    }
}
