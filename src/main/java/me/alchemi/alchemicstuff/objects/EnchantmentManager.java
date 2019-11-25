package me.alchemi.alchemicstuff.objects;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

import me.alchemi.alchemicstuff.Stuff;
import me.alchemi.alchemicstuff.objects.enchantments.Bewitched;
import me.alchemi.alchemicstuff.objects.enchantments.BonusPlayerDamage;
import me.alchemi.alchemicstuff.objects.enchantments.Chopping;
import me.alchemi.alchemicstuff.objects.enchantments.EnchantmentBase;
import me.alchemi.alchemicstuff.objects.enchantments.Experience;
import me.alchemi.alchemicstuff.objects.enchantments.Explosive;
import me.alchemi.alchemicstuff.objects.enchantments.LifeSteal;
import me.alchemi.alchemicstuff.objects.enchantments.PoisonedTouch;
import me.alchemi.alchemicstuff.objects.enchantments.Shredder;

public class EnchantmentManager {

	private static Map<String, EnchantmentBase> registry = new HashMap<String, EnchantmentBase>();

	private static EnchantmentManager instance;

	private YamlConfiguration config;
	private File file;
	
	public static final EnchantmentBase SHREDDER = new Shredder();
	public static final EnchantmentBase LIFESTEAL = new LifeSteal();
	public static final EnchantmentBase EXPLOSIVE = new Explosive();
	public static final EnchantmentBase EXPERIENCE = new Experience();
	public static final EnchantmentBase CHOPPING = new Chopping();
	public static final EnchantmentBase BONUS_PLAYER_DAMAGE = new BonusPlayerDamage();
	public static final EnchantmentBase BEWITCHED = new Bewitched();
	public static final EnchantmentBase POISONED_TOUCH = new PoisonedTouch();
	
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
		Shredder.setDefaults(this);
		
		LifeSteal.setDefaults(this);
		
		Explosive.setDefaults(this);
		
		Experience.setDefaults(this);
		
		Chopping.setDefaults(this);
		
		BonusPlayerDamage.setDefaults(this);
		
		Bewitched.setDefaults(this);
		
		PoisonedTouch.setDefaults(this);
		save();
		if (!file.exists()) {
			save();
		}
	}
	
	public void setDisplayName(Class<? extends EnchantmentBase> enchant, String displayName) { 
		config.addDefault(String.join(".", enchant.getSimpleName().toLowerCase(), "display-name"), displayName);
		config.set(String.join(".", enchant.getSimpleName().toLowerCase(), "display-name"), displayName);
	}
	
	public void setSpecial(Class<? extends EnchantmentBase> enchant, boolean special) {
		config.addDefault(String.join(".", enchant.getSimpleName().toLowerCase(), "special"), special);
		config.set(String.join(".", enchant.getSimpleName().toLowerCase(), "special"), special);
	}
	
	public void setChance(Class<? extends EnchantmentBase> enchant, double chance) {
		config.addDefault(String.join(".", enchant.getSimpleName().toLowerCase(), "chance"), chance);
		config.set(String.join(".", enchant.getSimpleName().toLowerCase(), "chance"), chance);
	}
	
	public void setAmount(Class<? extends EnchantmentBase> enchant, double amount) {
		config.addDefault(String.join(".", enchant.getSimpleName().toLowerCase(), "amount"), amount);
		config.set(String.join(".", enchant.getSimpleName().toLowerCase(), "amount"), amount);
	}
	
	public void setOther(Class<? extends EnchantmentBase> enchant, String key, Object object) {
		config.addDefault(String.join(".", enchant.getSimpleName().toLowerCase(), key), object);
		config.set(String.join(".", enchant.getSimpleName().toLowerCase(), key), object);
	}
	
	public String getDisplayName(Class<? extends EnchantmentBase> enchant) {
		return config.getString(String.join(".", enchant.getSimpleName().toLowerCase(), "display-name"));
	}
	
	public boolean getSpecial(Class<? extends EnchantmentBase> enchant) {
		return config.getBoolean(String.join(".", enchant.getSimpleName().toLowerCase(), "special"));
	}
	
	public double getChance(Class<? extends EnchantmentBase> enchant) {
		return config.getDouble(String.join(".", enchant.getSimpleName().toLowerCase(), "chance"));
	}
	
	public double getAmount(Class<? extends EnchantmentBase> enchant) {
		return config.getDouble(String.join(".", enchant.getSimpleName().toLowerCase(), "amount"));
	}
	
	public Object getObject(Class<? extends EnchantmentBase> enchant, String key) {
		return config.get(String.join(".", enchant.getSimpleName().toLowerCase(), key));
	}
	
	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
