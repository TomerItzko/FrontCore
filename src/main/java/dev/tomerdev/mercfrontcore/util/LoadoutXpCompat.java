package dev.tomerdev.mercfrontcore.util;

import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameTeam;
import dev.tomerdev.mercfrontcore.setup.LoadoutIndex;
import com.boehmod.blockfront.util.StringUtils;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class LoadoutXpCompat {
    private LoadoutXpCompat() {
    }

    public static int getEffectiveXp(PlayerCloudData profile, MatchClass matchClass) {
        if (profile == null) {
            return 0;
        }
        if (matchClass == null) {
            return 0;
        }
        return Math.max(0, profile.getExpForClass(matchClass.ordinal()));
    }

    public static int resolveMinimumXp(
        AbstractGame<?, ?, ?> game,
        GameTeam team,
        MatchClass matchClass,
        int classLevel,
        Loadout fallbackLoadout
    ) {
        int minimumXp = fallbackLoadout == null ? 0 : fallbackLoadout.getMinimumXp();
        if (game == null || team == null || matchClass == null || classLevel < 0) {
            return minimumXp;
        }
        try {
            DivisionData divisionData = team.getDivisionData(game);
            if (divisionData == null) {
                return minimumXp;
            }
            var levels = LoadoutIndex.currentFlat().get(
                new LoadoutIndex.Identifier(divisionData.getCountry(), divisionData.getSkin(), matchClass)
            );
            if (levels == null || classLevel >= levels.size()) {
                return minimumXp;
            }
            return Math.max(minimumXp, levels.get(classLevel).getMinimumXp());
        } catch (Throwable ignored) {
            return minimumXp;
        }
    }

    public static MutableText createMinimumXpMessage(int minimumXp, int effectiveXp) {
        MutableText requiredXp = Text.literal(StringUtils.formatLong(minimumXp)).formatted(Formatting.GRAY);
        MutableText currentXp = Text.literal(StringUtils.formatLong(effectiveXp)).formatted(Formatting.GRAY);
        return Text.translatable("bf.message.gamemode.class.error.exp", requiredXp, currentXp)
            .formatted(Formatting.RED);
    }
}
