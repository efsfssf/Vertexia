package io.github.vertexia.src.core.actors.units;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.utils.Vector2d;

import static io.github.vertexia.src.core.TribesConfig.*;

public class Archer extends Unit
{
    public Archer(Vector2d pos, int kills, boolean isVeteran, int cityId, int tribeId) {
        super(ARCHER_ATTACK, ARCHER_DEFENCE, ARCHER_MOVEMENT, ARCHER_MAX_HP, ARCHER_RANGE, ARCHER_COST, pos, kills, isVeteran, cityId, tribeId);
    }

    @Override
    public Types.UNIT getType() {
        return Types.UNIT.ARCHER;
    }

    @Override
    public Archer copy(boolean hideInfo) {
        Archer c = new Archer(getPosition(), getKills(), isVeteran(), getCityId(), getTribeId());
        c.setCurrentHP(getCurrentHP());
        c.setMaxHP(getMaxHP());
        c.setActorId(getActorId());
        c.setStatus(getStatus());
        return hideInfo ? (Archer) c.hide() : c;
    }
}
