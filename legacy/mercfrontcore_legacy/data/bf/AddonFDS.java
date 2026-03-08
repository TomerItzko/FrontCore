package red.vuis.mercfrontcore.data.bf;

import java.util.function.Consumer;

import com.boehmod.bflib.fds.tag.FDSBoolean;
import com.boehmod.bflib.fds.tag.FDSInteger;
import com.boehmod.bflib.fds.tag.FDSString;
import com.boehmod.bflib.fds.tag.FDSTagCompound;

public final class AddonFDS {
	private AddonFDS() {
	}
	
	public static void ifBoolean(FDSTagCompound root, String tag, Consumer<Boolean> consumer) {
		if (root.hasTag(tag) && root.get(tag) instanceof FDSBoolean fds) {
			consumer.accept(fds.getValue());
		}
	}
	
	public static void ifInteger(FDSTagCompound root, String tag, Consumer<Integer> consumer) {
		if (root.hasTag(tag) && root.get(tag) instanceof FDSInteger fds) {
			consumer.accept(fds.getValue());
		}
	}
	
	public static void ifString(FDSTagCompound root, String tag, Consumer<String> consumer) {
		if (root.hasTag(tag) && root.get(tag) instanceof FDSString fds) {
			consumer.accept(fds.getValue());
		}
	}
}
