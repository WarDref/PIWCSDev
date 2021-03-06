package test;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

import ru.windcorp.mineragenesis.MineraGenesis;
import ru.windcorp.mineragenesis.addon.MGAddonLoader;
import ru.windcorp.mineragenesis.addon.MGAddonManager;
import ru.windcorp.mineragenesis.interfaces.MGHelper;
import ru.windcorp.mineragenesis.rb.RockBiomesAddon;
import ru.windcorp.mineragenesis.request.ChunkData;

public class FakeLoader {

	public static void load(String path) {
		MineraGenesis.isDebugging = true;
		MineraGenesis.setImplementation(null, null, null,
				new MGHelper() {
					
					@Override
					public Path getWorldDataDirectory() {
						return null;
					}
					
					@Override
					public Path getGlobalConfigurationDirectory() {
						return FileSystems.getDefault().getPath(path);
					}
				},
				System.out::println,
				FakeLoader::crash,
				FakeLoader::registerName);
		
		MineraGenesis.loadConfig();
		
		MGAddonManager.registerAddon(RockBiomesAddon::loadHook, new MGAddonLoader.Metadata("rb", "RockBiomes", "1.0-test"));
		MGAddonManager.initializeAddons();
	}
	
	private static final ArrayList<String> NAMES = new ArrayList<>(); {
		NAMES.add("\uFFFF");
		NAMES.add("\uFFFF");
	}
	
	public static int registerName(String name) {
		int id = NAMES.indexOf(name);
		if (id == -1) {
			id = NAMES.size();
			NAMES.add(name);
		}
		return id;
	}
	
	public static String getName(short mgid) {
		return getName(ChunkData.getId(mgid));
	}
	
	public static String getName(int id) {
		return NAMES.get(id);
	}
	
	public static String getMeta(short mgid) {
		return getMeta(ChunkData.getMeta(mgid));
	}
	
	public static String getMeta(int meta) {
		if (meta == -1) {
			return ":*";
		}
		
		if (meta == 0) {
			return "";
		}
		
		return ":" + meta;
	}
	
	private static void crash(Throwable exception, String message) {
		System.err.println(message);
		exception.printStackTrace();
		System.exit(1);
	}
	
}
