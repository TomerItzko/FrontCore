package red.vuis.mercfrontcore.client.render;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import red.vuis.mercfrontcore.client.input.InputTracker;

public abstract class RenderObject {
	protected final MinecraftClient client;
	protected final VertexConsumerProvider.Immediate vertexConsumers;
	protected DrawContext context;
	protected MatrixStack matrices;
	protected final Camera camera;
	protected final Frustum frustum;
	protected final InputTracker input;
	
	protected RenderObject(MinecraftClient client, InputTracker input) {
		this.client = client;
		vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();
		camera = client.gameRenderer.getCamera();
		frustum = client.worldRenderer.getFrustum();
		this.input = input;
	}
	
	public static <T extends RenderObject> Supplier<T> of(BiFunction<MinecraftClient, InputTracker, T> constructor) {
		return new Supplier<>() {
			private T instance;
			
			@Override
			public T get() {
				if (instance == null) {
					instance = constructor.apply(MinecraftClient.getInstance(), InputTracker.getInstance());
				}
				return instance;
			}
		};
	}
	
	@MustBeInvokedByOverriders
	public void render() {
		context = new DrawContext(client, vertexConsumers);
		matrices = context.getMatrices();
	}
	
	protected void translate(Vec3d vec) {
		AddonRendering.translate(matrices, vec);
	}
	
	protected void cameraAsOrigin() {
		AddonRendering.cameraAsOrigin(matrices, camera);
	}
	
	protected void billboardString(String text, Vec3d position, float scale) {
		billboardString(text, position, scale, true);
	}
	
	protected void billboardString(String text, Vec3d position, float scale, boolean depthTest) {
		AddonRendering.billboardString(matrices, camera, client.textRenderer, vertexConsumers, text, position, scale, depthTest);
	}
	
	protected void billboardTexture(Identifier texture, Vec3d position, float width, float height, float alpha) {
		billboardTexture(texture, position, width, height, alpha, true);
	}
	
	protected void billboardTexture(Identifier texture, Vec3d position, float width, float height, float alpha, boolean depthTest) {
		AddonRendering.billboardTexture(matrices, camera, texture, position, width, height, alpha, depthTest);
	}
	
	protected void line(Vec3d start, Vec3d end, int color) {
		AddonRendering.line(matrices, start, end, color, false);
	}
	
	protected void boxOutline(Box box, int color) {
		AddonRendering.boxOutline(matrices, box, color, false);
	}
}
