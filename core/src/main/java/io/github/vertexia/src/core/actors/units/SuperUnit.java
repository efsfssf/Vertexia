package io.github.vertexia.src.core.actors.units;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.utils.Vector2d;

import static io.github.vertexia.src.core.TribesConfig.*;

public class SuperUnit extends Unit
{
    public SuperUnit(Vector2d pos, int kills, boolean isVeteran, int cityId, int tribeId) {
        super(SUPERUNIT_ATTACK, SUPERUNIT_DEFENCE, SUPERUNIT_MOVEMENT, SUPERUNIT_MAX_HP, SUPERUNIT_RANGE, SUPERUNIT_COST, pos, kills, isVeteran, cityId, tribeId);
    }

    @Override
    public Types.UNIT getType() {
        return Types.UNIT.SUPERUNIT;
    }

    @Override
    public SuperUnit copy(boolean hideInfo) {
        SuperUnit c = new SuperUnit(getPosition(), getKills(), isVeteran(), getCityId(), getTribeId());
        c.setCurrentHP(getCurrentHP());
        c.setMaxHP(getMaxHP());
        c.setActorId(getActorId());
        c.setStatus(getStatus());
        return hideInfo ? (SuperUnit) c.hide() : c;
    }
}
