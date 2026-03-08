package red.vuis.mercfrontcore.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.assets.impl.GameAsset;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public final class AddonUtils {
	private AddonUtils() {
	}
	
	public static <T> Optional<T> parse(Function<String, ? extends T> parser, String arg) {
		try {
			return Optional.ofNullable(parser.apply(arg));
		} catch (Exception e) {
			return Optional.empty();
		}
	}
	
	public static boolean anyEmpty(Optional<?>... optionals) {
		for (Optional<?> optional : optionals) {
			if (optional.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public static <T1 extends Iterable<T2>, T2 extends Iterable<V>, V> void forEachRecursive(T1 target1, Consumer<? super V> consumer) {
		for (T2 target2 : target1) {
			for (V value : target2) {
				consumer.accept(value);
			}
		}
	}
	
	@SafeVarargs
	public static <T> List<T> concat(Iterable<T> parent, T... other) {
		return Stream.concat(StreamSupport.stream(parent.spliterator(), false), Arrays.stream(other)).toList();
	}
	
	public static Vec2f vec2WithX(Vec2f original, float x) {
		return new Vec2f(x, original.y);
	}
	
	public static Vec2f vec2WithY(Vec2f original, float y) {
		return new Vec2f(original.x, y);
	}
	
	public static Vec3d vec3WithX(Vec3d original, double x) {
		return new Vec3d(x, original.y, original.z);
	}
	
	public static Vec3d vec3WithY(Vec3d original, double y) {
		return new Vec3d(original.x, y, original.z);
	}
	
	public static Vec3d vec3WithZ(Vec3d original, double z) {
		return new Vec3d(original.x, original.y, z);
	}
	
	public static Vec3d copyVec3(Vec3d other) {
		return new Vec3d(other.x, other.y, other.z);
	}
	
	public static String formatVec3(Vec3d vec) {
		return String.format("%.2f, %.2f, %.2f", vec.x, vec.y, vec.z);
	}
	
	public static String listify(Iterator<String> strings) {
		StringBuilder builder = new StringBuilder();
		strings.forEachRemaining(str -> {
			builder.append(str);
			builder.append(", ");
		});
		builder.setLength(builder.length() - 2);
		return builder.toString();
	}
	
	public static String listify(Iterable<String> strings) {
		return listify(strings.iterator());
	}
	
	public static void setPoseFromEntity(FDSPose pose, Entity entity) {
		pose.position = copyVec3(entity.getPos());
		pose.rotation = new Vec2f(entity.getHeadYaw(), entity.getPitch());
	}
	
	public static BFAbstractManager<?, ?, ?> getBfManager() {
		BFAbstractManager<?, ?, ?> manager = BlockFront.getInstance().getManager();
		if (manager == null) {
			throw new IllegalStateException("BlockFront manager is null");
		}
		return manager;
	}
	
	public static void teleportBf(ServerPlayerEntity player, FDSPose pose) {
		BFUtils.teleportPlayer(getBfManager().getPlayerDataHandler(), player, pose);
	}
	
	public static Path getServerDataPath(MinecraftServer server) {
		Path basePath = server.getPath("mercfrontcore");
		if (!Files.isDirectory(basePath)) {
			try {
				Files.createDirectory(basePath);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return basePath;
	}
	
	public static boolean anyGamesActive() {
		return getBfManager().getGames().values().stream()
			.map(GameAsset::getGame)
			.filter(Objects::nonNull)
			.anyMatch(game -> game.getStatus() != GameStatus.IDLE);
	}
	
	public static MutableText getMatchClassText(MatchClass matchClass, int level) {
		return Text.translatable(matchClass.getDisplayTitle()).append(" ").append(Text.translatable("enchantment.level." + (level + 1)));
	}
	
	public static MutableText getDivisionText(DivisionData division) {
		return Text.literal(division.getCountry().getTag().toUpperCase()).append(" ").append(division.getSkin());
	}
	
	public static BFGameType getGameType(AbstractGame<?, ?, ?> game) {
		BFGameType gameType = BFGameType.getByName(game.getType());
		assert gameType != null;
		return gameType;
	}
	
	public static <E extends Enum<E>> E retrieveEnumOrDefault(E[] values, @Nullable String name, E defaultValue) {
		if (name == null) {
			return defaultValue;
		}
		return Arrays.stream(values).filter(value -> value.toString().equalsIgnoreCase(name)).findFirst().orElse(defaultValue);
	}
	
	public static PlayerCloudData createPlayerCloudData(UUID uuid, String username) {
		PlayerCloudData cloudData = new PlayerCloudData(uuid);
		cloudData.setUsername(username);
		return cloudData;
	}
	
	public static PlayerCloudData createPlayerCloudData(Pair<UUID, String> idPair) {
		return createPlayerCloudData(idPair.left(), idPair.right());
	}
	
	public static Pair<UUID, String> createIdPair(GameProfile gameProfile) {
		return Pair.of(gameProfile.getId(), gameProfile.getName());
	}
	
	public static BufferedReader toBufferedReader(InputStream stream) {
		return new BufferedReader(new InputStreamReader(stream));
	}
	
	public static UUID decodeBase64Uuid(String s) {
		ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(s));
		return new UUID(buffer.getLong(), buffer.getLong());
	}
}
