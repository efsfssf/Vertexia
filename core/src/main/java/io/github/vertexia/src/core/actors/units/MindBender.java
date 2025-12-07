package io.github.vertexia.src.core.actors.units;

import io.github.vertexia.src.core.Types;
import io.github.vertexia.src.utils.Vector2d;

import static io.github.vertexia.src.core.TribesConfig.*;

public class MindBender extends Unit
{
    public MindBender(Vector2d pos, int kills, boolean isVeteran, int cityId, int tribeId) {
        super(MINDBENDER_ATTACK, MINDBENDER_DEFENCE, MINDBENDER_MOVEMENT, MINDBENDER_MAX_HP, MINDBENDER_RANGE, MINDBENDER_COST, pos, kills, isVeteran, cityId, tribeId);
    }

    @Override
    public Types.UNIT getType() {
        return Types.UNIT.MIND_BENDER;
    }

    @Override
    public MindBender copy(boolean hideInfo) {
        MindBender c = new MindBender(getPosition(), getKills(), isVeteran(), getCityId(), getTribeId());
        c.setCurrentHP(getCurrentHP());
        c.setMaxHP(getMaxHP());
        c.setActorId(getActorId());
        c.setStatus(getStatus());
        return hideInfo ? (MindBender) c.hide() : c;
    }
}
