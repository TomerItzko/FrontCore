package red.vuis.mercfrontcore.util;

import net.minecraft.util.math.ColorHelper;

public final class ColorUtils {
	private ColorUtils() {
	}
	
	public static float redFloat(int color) {
		return ColorHelper.Argb.getRed(color) / 255f;
	}
	
	public static float greenFloat(int color) {
		return ColorHelper.Argb.getGreen(color) / 255f;
	}
	
	public static float blueFloat(int color) {
		return ColorHelper.Argb.getBlue(color) / 255f;
	}
	
	public static float alphaFloat(int color) {
		return ColorHelper.Argb.getAlpha(color) / 255f;
	}
}
