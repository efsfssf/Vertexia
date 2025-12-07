package io.github.vertexia.src.core.actors.units;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.utils.Vector2d;

import static io.github.vertexia.src.core.TribesConfig.*;

public class Rider extends Unit
{
    public Rider(Vector2d pos, int kills, boolean isVeteran, int cityId, int tribeId) {
        super(RIDER_ATTACK, RIDER_DEFENCE, RIDER_MOVEMENT, RIDER_MAX_HP, RIDER_RANGE, RIDER_COST, pos, kills, isVeteran, cityId, tribeId);
    }

    @Override
    public Types.UNIT getType() {
        return Types.UNIT.RIDER;
    }

    @Override
    public Rider copy(boolean hideInfo) {
        Rider c = new Rider(getPosition(), getKills(), isVeteran(), getCityId(), getTribeId());
        c.setCurrentHP(getCurrentHP());
        c.setMaxHP(getMaxHP());
        c.setActorId(getActorId());
        c.setStatus(getStatus());
        return hideInfo ? (Rider) c.hide() : c;
    }
}
