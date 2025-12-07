package io.github.vertexia.src.core.actors.units;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.utils.Vector2d;

import static io.github.vertexia.src.core.TribesConfig.*;

public class Warrior extends Unit
{
    public Warrior(Vector2d pos, int kills, boolean isVeteran, int cityId, int tribeId) {
        super(WARRIOR_ATTACK, WARRIOR_DEFENCE, WARRIOR_MOVEMENT, WARRIOR_MAX_HP, WARRIOR_RANGE, WARRIOR_COST, pos, kills, isVeteran, cityId, tribeId);
    }

    @Override
    public Types.UNIT getType() {
        return Types.UNIT.WARRIOR;
    }

    @Override
    public Warrior copy(boolean hideInfo) {
        Warrior c = new Warrior(getPosition(), getKills(), isVeteran(), getCityId(), getTribeId());
        c.setCurrentHP(getCurrentHP());
        c.setMaxHP(getMaxHP());
        c.setActorId(getActorId());
        c.setStatus(getStatus());
        return hideInfo ? (Warrior) c.hide() : c;
    }
}
