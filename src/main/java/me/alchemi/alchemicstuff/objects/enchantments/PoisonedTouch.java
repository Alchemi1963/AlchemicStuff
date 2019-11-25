package me.alchemi.alchemicstuff.objects.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffectType;

import me.alchemi.alchemicstuff.objects.EnchantmentManager;

public class PoisonedTouch extends EnchantmentBase {

	public static final String DEFAULT_DISPLAYNAME = "&2Poisoned Touch";
	public static final boolean DEFAULT_SPECIAL = false;
	public static final double DEFAULT_CHANCE = 100.0D;
	
	public PoisonedTouch() {
		super(PoisonedTouch.class);
	}
	
	public static void setDefaults(EnchantmentManager manager) {
		manager.setDisplayName(PoisonedTouch.class, DEFAULT_DISPLAYNAME);
		manager.setSpecial(PoisonedTouch.class, DEFAULT_SPECIAL);
		manager.setChance(PoisonedTouch.class, DEFAULT_CHANCE);
	}
	
	@EventHandler
	@Override
	public void onUse(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof LivingEntity
				&& e.getDamager() instanceof LivingEntity
				&& (hasEnchantment((LivingEntity) e.getEntity(), EquipmentSlot.CHEST)
						|| hasEnchantment((LivingEntity) e.getEntity(), EquipmentSlot.HEAD)
						|| hasEnchantment((LivingEntity) e.getEntity(), EquipmentSlot.FEET)
						|| hasEnchantment((LivingEntity) e.getEntity(), EquipmentSlot.LEGS))
				&& allowChance()) {
			int lvl = Math.max(Math.max(getLevel((LivingEntity) e.getEntity(), EquipmentSlot.CHEST), getLevel((LivingEntity) e.getEntity(), EquipmentSlot.HEAD))
					, Math.max(getLevel((LivingEntity) e.getEntity(), EquipmentSlot.FEET), getLevel((LivingEntity) e.getEntity(), EquipmentSlot.LEGS)));
			((LivingEntity)e.getDamager()).addPotionEffect(PotionEffectType.POISON.createEffect(60 * lvl, 0), true);
		}
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ARMOR;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return other.equals(Enchantment.THORNS);
	}

}
