package io.github.vertexia.src.core.actors.units;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.utils.Vector2d;

import static io.github.vertexia.src.core.TribesConfig.*;

public class Defender extends Unit
{
    public Defender(Vector2d pos, int kills, boolean isVeteran, int cityId, int tribeId) {
        super(DEFENDER_ATTACK, DEFENDER_DEFENCE, DEFENDER_MOVEMENT, DEFENDER_MAX_HP, DEFENDER_RANGE, DEFENDER_COST, pos, kills, isVeteran, cityId, tribeId);
    }

    @Override
    public Types.UNIT getType() {
        return Types.UNIT.DEFENDER;
    }

    @Override
    public Defender copy(boolean hideInfo) {
        Defender c = new Defender(getPosition(), getKills(), isVeteran(), getCityId(), getTribeId());
        c.setCurrentHP(getCurrentHP());
        c.setMaxHP(getMaxHP());
        c.setActorId(getActorId());
        c.setStatus(getStatus());
        return hideInfo ? (Defender) c.hide() : c;
    }
}
