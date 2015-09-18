package nl.queuemanager.core.platform;

import java.io.File;

public abstract class PlatformHelperWindows extends PlatformHelper {
	
	public File getUserDataFolder() {
		return new File(System.getenv("APPDATA"));
	}
	
}
