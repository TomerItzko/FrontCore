package dev.tomerdev.mercfrontcore.client.event;

import java.util.List;
import java.util.function.Supplier;
import com.boehmod.blockfront.client.BFClient;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.net.ConnectionMode;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundSourceEvent;
import net.neoforged.neoforge.client.event.sound.PlayStreamingSourceEvent;
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.client.command.MercFrontCoreClientCommand;
import dev.tomerdev.mercfrontcore.client.input.InputAcceptor;
import dev.tomerdev.mercfrontcore.client.input.InputTracker;
import dev.tomerdev.mercfrontcore.client.input.MouseButton;
import dev.tomerdev.mercfrontcore.client.render.AssetEditRenderer;
import dev.tomerdev.mercfrontcore.client.render.RenderObject;
import dev.tomerdev.mercfrontcore.client.render.SpawnViewRenderer;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfig;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;
import dev.tomerdev.mercfrontcore.data.AddonCommonData;

@EventBusSubscriber(modid = AddonConstants.MOD_ID, value = Dist.CLIENT)
public final class MercFrontCoreClientEvents {
    private static final List<Supplier<? extends RenderObject>> RENDER_OBJECTS = List.of(
        RenderObject.of(AssetEditRenderer::new),
        RenderObject.of(SpawnViewRenderer::new)
    );
    private static boolean forceVanillaTitle;

    private MercFrontCoreClientEvents() {
    }

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        MercFrontCoreClientCommand.register(event.getDispatcher());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onScreenOpening(ScreenEvent.Opening event) {
        var next = event.getNewScreen();
        if (next == null || next.getClass() == TitleScreen.class) {
            if (next != null) {
                forceVanillaTitle = false;
            }
            return;
        }

        String className = next.getClass().getName();
        boolean forceByClass = shouldForceOfflineTitle(className);
        boolean isTitle = next instanceof TitleScreen;
        boolean isLobbyTitle = className.contains("LobbyTitleScreen");
        boolean isBfIntro = className.endsWith("BFIntroScreen");
        boolean isTitleFamily = className.contains(".client.screen.title.");
        boolean forceBfTitleFamily = isLobbyTitle || isTitleFamily;
        boolean forceByMode = isOfflineConnectionMode() && (isTitle || isLobbyTitle || isBfIntro || isTitleFamily);
        if (forceByClass) {
            forceVanillaTitle = true;
        }
        if (forceByClass
            || forceByMode
            || forceBfTitleFamily
            || (forceVanillaTitle && (isTitle || isLobbyTitle || isTitleFamily))) {
            event.setNewScreen(new TitleScreen());
        }
    }

    @SubscribeEvent
    public static void onInputMouseButtonPost(InputEvent.MouseButton.Post event) {
        for (Supplier<? extends RenderObject> supplier : RENDER_OBJECTS) {
            RenderObject obj = supplier.get();
            if (obj instanceof InputAcceptor acceptor) {
                MouseButton.apply(event.getButton(), event.getAction() > 0 ? acceptor::mousePressed : acceptor::mouseReleased);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderFramePre(RenderFrameEvent.Pre event) {
        var client = net.minecraft.client.MinecraftClient.getInstance();
        InputTracker.getInstance().update(client.mouse);
        if (client.world == null) {
            AddonCommonData.getInstance().getProfileOverrides().clear();
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            return;
        }
        for (Supplier<? extends RenderObject> supplier : RENDER_OBJECTS) {
            supplier.get().render();
        }
    }

    @SubscribeEvent
    public static void onClientTickPost(ClientTickEvent.Post event) {
        var client = net.minecraft.client.MinecraftClient.getInstance();
        var screen = client.currentScreen;
        if (screen == null || screen.getClass() == TitleScreen.class) {
            return;
        }
        String className = screen.getClass().getName();
        boolean titleFamily = className.contains(".client.screen.title.");
        boolean isBfTitleFamily = className.contains("LobbyTitleScreen") || titleFamily;
        if (shouldForceOfflineTitle(className)) {
            forceVanillaTitle = true;
        }
        if (shouldForceOfflineTitle(className)
            || isBfTitleFamily
            || (isOfflineConnectionMode() && titleFamily)
            || (forceVanillaTitle && titleFamily)) {
            client.setScreen(new TitleScreen());
        }
    }

    @SubscribeEvent
    public static void onPlaySoundSource(PlaySoundSourceEvent event) {
        applyScaledVolume(event.getSound(), event.getName(), event.getChannel());
    }

    @SubscribeEvent
    public static void onPlayStreamingSource(PlayStreamingSourceEvent event) {
        applyScaledVolume(event.getSound(), event.getName(), event.getChannel());
    }

    private static void applyScaledVolume(SoundInstance sound, String pathFromEvent, Object channel) {
        if (sound == null || channel == null) {
            return;
        }
        Identifier location = resolveSoundLocation(sound);
        String namespace = location != null ? location.getNamespace() : "";
        String path = pathFromEvent != null && !pathFromEvent.isBlank()
            ? pathFromEvent
            : (location != null ? location.getPath() : "");
        if (path.isBlank()) {
            return;
        }

        float scale = resolveSoundScale(namespace, path);
        if (Math.abs(scale - 1.0f) < 0.001f) {
            return;
        }
        try {
            channel.getClass().getMethod("setVolume", float.class).invoke(channel, clamp(sound.getVolume() * scale));
        } catch (Throwable ignored) {
        }
    }

    private static float resolveSoundScale(String namespace, String path) {
        String lower = path.toLowerCase(java.util.Locale.ROOT);
        boolean blockfrontNamespace = "blockfront".equals(namespace);
        MercFrontCoreConfig.AudioSettings audio = MercFrontCoreConfigManager.get().audio;
        if (isGrenadePath(lower)) {
            return clamp(audio.grenadeSfxVolume);
        }
        if (isGunPath(lower)) {
            return clamp(audio.gunSfxVolume);
        }
        if (blockfrontNamespace && isLikelyWeaponPath(lower)) {
            return clamp(audio.gunSfxVolume);
        }
        return 1.0f;
    }

    private static Identifier resolveSoundLocation(SoundInstance sound) {
        try {
            Object value = sound.getClass().getMethod("getLocation").invoke(sound);
            if (value instanceof Identifier id) {
                return id;
            }
        } catch (Throwable ignored) {
        }
        try {
            Object value = sound.getClass().getMethod("getId").invoke(sound);
            if (value instanceof Identifier id) {
                return id;
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static boolean isGunPath(String path) {
        return path.contains("gun")
            || path.contains("weapon")
            || path.contains("rifle")
            || path.contains("pistol")
            || path.contains("shotgun")
            || path.contains("smg")
            || path.contains("lmg")
            || path.contains("fire")
            || path.contains("shot")
            || path.contains("bullet")
            || path.contains("reload")
            || path.contains("mag")
            || path.contains("mg");
    }

    private static boolean isGrenadePath(String path) {
        return path.contains("grenade")
            || path.contains("frag")
            || path.contains("explosion")
            || path.contains("blast");
    }

    private static boolean isLikelyWeaponPath(String path) {
        return path.contains("weapon")
            || path.contains("wep")
            || path.contains("wpn")
            || path.contains("shoot")
            || path.contains("fire")
            || path.contains("reload");
    }

    private static float clamp(float value) {
        return Math.max(0.0f, Math.min(2.0f, value));
    }

    private static boolean shouldForceOfflineTitle(String className) {
        return className.contains("Offline")
            || className.endsWith("BFIntroScreen")
            || className.endsWith("OfflineScreen")
            || className.contains("title.offline")
            // BlockFront 0.7.1.2b obfuscated offline-path screen observed in runtime logs.
            || className.equals("com.boehmod.blockfront.cZ");
    }

    private static boolean isOfflineConnectionMode() {
        try {
            BFClientManager manager = BFClient.getManager();
            if (manager != null && manager.getConnectionManager() != null
                && manager.getConnectionManager().getConnectionMode() == ConnectionMode.OFFLINE) {
                return true;
            }
        } catch (Throwable ignored) {
        }
        return false;
    }
}
