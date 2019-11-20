package me.alchemi.alchemicstuff.objects;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

import me.alchemi.alchemicstuff.Stuff;
import me.alchemi.alchemicstuff.objects.enchantments.EnchantmentBase;
import me.alchemi.alchemicstuff.objects.enchantments.Explosive;
import me.alchemi.alchemicstuff.objects.enchantments.LifeSteal;
import me.alchemi.alchemicstuff.objects.enchantments.Shredder;

public class EnchantmentManager {

	private static Map<String, EnchantmentBase> registry = new HashMap<String, EnchantmentBase>();

	private static EnchantmentManager instance;

	private YamlConfiguration config;
	private File file;
	
	public static final EnchantmentBase SHREDDER = new Shredder();
	public static final EnchantmentBase LIFESTEAL = new LifeSteal();
	public static final EnchantmentBase EXPLOSIVE = new Explosive();
	
	public EnchantmentManager() {
		instance = this;
		file = new File(Stuff.getInstance().getDataFolder(), "enchantments.yml");
		config = YamlConfiguration.loadConfiguration(file);
		load();
		
		for (EnchantmentBase enchant : registry.values()) {
			enchant.load();
		}
	}
	
	public static EnchantmentManager getEnchants() {
		return instance;
	}
	
	public static Map<String, EnchantmentBase> getRegistry() {
		return registry;
	}

	public static EnchantmentBase getByKey(String key) throws IllegalArgumentException {
		if (registry.isEmpty()
				|| !registry.containsKey(key)) throw new IllegalArgumentException(key + " is not registered.");
		
		return registry.get(key);
	}

	public static void register(EnchantmentBase enchantment) {
		registry.put(enchantment.getName(), enchantment);
	}
	
	public void load() {
		setDisplayName("shredder" , "&7Shredding");
		setSpecial("shredder", false);
		
		setDisplayName("lifesteal", "&7Life Steal");
		setSpecial("lifesteal", false);
		setChance("lifesteal", 10.0);
		setAmount("lifesteal", 3);
		
		setDisplayName("explosive", "&4Explosive");
		setSpecial("explosive", false);
		setAmount("explosive", 5);
		
		if (!file.exists()) {
			save();
		}
	}
	
	private void setDisplayName(String enchant, String displayName) { 
		config.addDefault(String.join(".", enchant, "display-name"), displayName);
		config.set(String.join(".", enchant, "display-name"), displayName);
	}
	
	private void setSpecial(String enchant, boolean special) {
		config.addDefault(String.join(".", enchant, "special"), special);
		config.set(String.join(".", enchant, "special"), special);
	}
	
	private void setChance(String enchant, double chance) {
		config.addDefault(String.join(".", enchant, "chance"), chance);
		config.set(String.join(".", enchant, "chance"), chance);
	}
	
	private void setAmount(String enchant, int amount) {
		config.addDefault(String.join(".", enchant, "amount"), amount);
		config.set(String.join(".", enchant, "amount"), amount);
	}
	
	public String getDisplayName(String enchant) {
		return config.getString(String.join(".", enchant, "display-name"));
	}
	
	public boolean getSpecial(String enchant) {
		return config.getBoolean(String.join(".", enchant, "special"));
	}
	
	public double getChance(String enchant) {
		return config.getDouble(String.join(".", enchant, "chance"));
	}
	
	public int getAmount(String enchant) {
		return config.getInt(String.join(".", enchant, "amount"));
	}
	
	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
