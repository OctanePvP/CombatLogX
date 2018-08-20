package com.SirBlobman.expansion.compatworldguard.config;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.SirBlobman.combatlogx.CombatLogX;
import com.SirBlobman.combatlogx.config.Config;
import com.SirBlobman.combatlogx.utility.Util;
import com.SirBlobman.expansion.compatworldguard.CompatWorldGuard;

public class ConfigWG extends Config {
	private static final File FOLDER = CompatWorldGuard.FOLDER;
	private static final File FILE = new File(FOLDER, "worldguard.yml");
	private static YamlConfiguration config = new YamlConfiguration();
	
	public static void save() {save(config, FILE);}
	public static YamlConfiguration load() {
		if(!FILE.exists()) copyFromJar("worldguard.yml", FOLDER);
		config = load(FILE);
		defaults();
		checkValidForceField();
		return config;
	}
	
	public static String NO_ENTRY_MODE;
	public static double NO_ENTRY_KNOCKBACK_STRENGTH;
	public static int MESSAGE_COOLDOWN;
	public static boolean FORCEFIELD_ENABLED;
	public static int FORCEFIELD_SIZE;
	public static Material FORCEFIELD_MATERIAL;
	public static String FORCEFIELD_MATERIAL_NAME;
	
	private static void defaults() {
		NO_ENTRY_MODE = get(config, "world guard.no entry.mode", "KNOCKBACK");
		NO_ENTRY_KNOCKBACK_STRENGTH = get(config, "world guard.no entry.knockback power", 1.5D);
		MESSAGE_COOLDOWN = get(config, "world guard.no entry.message cooldown", 5);
		FORCEFIELD_ENABLED = get(config, "world guard.forcefield.enabled", true);
		FORCEFIELD_SIZE = get(config, "world guard.forcefield.size", 4);
		FORCEFIELD_MATERIAL_NAME = get(config, "world guard.forcefield.material", Material.RED_STAINED_GLASS.name());
	}
	
	public static enum NoEntryMode {CANCEL, TELEPORT, KNOCKBACK, KILL};
	public static NoEntryMode getNoEntryMode() {
		if(NO_ENTRY_MODE == null || NO_ENTRY_MODE.isEmpty()) load();
		String mode = NO_ENTRY_MODE.toUpperCase();
		try {
			NoEntryMode nem = NoEntryMode.valueOf(mode);
			return nem;
		} catch(Throwable ex) {
			String error = "Invalid Mode '" + NO_ENTRY_MODE + "' in 'worldguard.yml'. Valid modes are CANCEL, TELEPORT, KNOCKBACK, or KILL";
			Util.print(error);
			return NoEntryMode.CANCEL;
		}
	}
	
	private static void checkValidForceField() {
		Plugin plugin = JavaPlugin.getPlugin(CombatLogX.class);
		if (FORCEFIELD_ENABLED && !plugin.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
			plugin.getLogger().log(Level.WARNING, "ForceField is enabled but ProtocolLib isn't, disabling forcefield...");
			FORCEFIELD_ENABLED = false;
			return;
		}
		Material forceFieldMaterial = Material.getMaterial(FORCEFIELD_MATERIAL_NAME);
		if(!(forceFieldMaterial != null && forceFieldMaterial.isBlock())) FORCEFIELD_ENABLED = false;
		FORCEFIELD_MATERIAL = forceFieldMaterial;
	}
}