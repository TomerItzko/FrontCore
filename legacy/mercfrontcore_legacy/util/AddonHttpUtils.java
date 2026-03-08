package red.vuis.mercfrontcore.util;

public final class AddonHttpUtils {
	private AddonHttpUtils() {
	}
	
	public static boolean isStatusOk(int statusCode) {
		return statusCode >= 200 && statusCode < 300;
	}
}
