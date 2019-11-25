package me.alchemi.alchemicstuff.objects.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.alchemicstuff.objects.EnchantmentManager;

public class Experience extends EnchantmentBase {

	public static final String DEFAULT_DISPLAYNAME = "&2Experience";
	public static final boolean DEFAULT_SPECIAL = false;
	public static final double DEFAULT_AMOUNT = 1.5D;
	public static final double DEFAULT_CHANCE = 50.0D;
	
	public Experience() {
		super(Experience.class);
	}
	
	public static void setDefaults(EnchantmentManager manager) {
		manager.setDisplayName(Experience.class, DEFAULT_DISPLAYNAME);
		manager.setSpecial(Experience.class, DEFAULT_SPECIAL);
		manager.setAmount(Experience.class, DEFAULT_AMOUNT);
		manager.setChance(Experience.class, DEFAULT_CHANCE);
	}
	
	@EventHandler
	public void onUse(EntityDeathEvent e) {
		if (hasEnchantment(e.getEntity().getKiller(), EquipmentSlot.HAND)
				&& allowChance()) {
			e.setDroppedExp((int) (e.getDroppedExp() * amount * getLevel(e.getEntity().getKiller(), EquipmentSlot.HAND)));
		}
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		
		if (!super.canEnchantItem(item)) {
			if (MaterialWrapper.wrap(item) == MaterialWrapper.BOW
					|| MaterialWrapper.wrap(item) == MaterialWrapper.CROSSBOW) {
				return true;
			} else return false;			
		}
		return super.canEnchantItem(item);
	}
	
	@Override
	public boolean conflictsWith(Enchantment arg0) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

}
