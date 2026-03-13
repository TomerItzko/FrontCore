package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.game.GameTeam;
import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "com.boehmod.blockfront.game.AbstractGamePlayerManager")
public interface AbstractGamePlayerManagerAccessor {
    @Invoker("getPlayerTeam")
    GameTeam mercfrontcore$getPlayerTeam(UUID playerUuid);
}
