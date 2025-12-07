package io.github.vertexia.src.core.actors.units;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.utils.Vector2d;

import static io.github.vertexia.src.core.TribesConfig.*;

public class Swordman extends Unit
{
    public Swordman(Vector2d pos, int kills, boolean isVeteran, int cityId, int tribeId) {
        super(SWORDMAN_ATTACK, SWORDMAN_DEFENCE, SWORDMAN_MOVEMENT, SWORDMAN_MAX_HP, SWORDMAN_RANGE, SWORDMAN_COST, pos, kills, isVeteran, cityId, tribeId);
    }

    @Override
    public Types.UNIT getType() {
        return Types.UNIT.SWORDMAN;
    }

    @Override
    public Swordman copy(boolean hideInfo) {
        Swordman c = new Swordman(getPosition(), getKills(), isVeteran(), getCityId(), getTribeId());
        c.setCurrentHP(getCurrentHP());
        c.setMaxHP(getMaxHP());
        c.setActorId(getActorId());
        c.setStatus(getStatus());
        return hideInfo ? (Swordman) c.hide() : c;
    }
}
