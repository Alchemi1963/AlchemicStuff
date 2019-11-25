package me.alchemi.alchemicstuff.objects.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.alchemi.alchemicstuff.objects.EnchantmentManager;

public class Explosive extends EnchantmentBase {

	public static final String DEFAULT_DISPLAYNAME = "&4Explosive";
	public static final boolean DEFAULT_SPECIAL = false;
	public static final double DEFAULT_AMOUNT = 1.2D;
	public static final double DEFAULT_CHANCE = 20.0D;
	
	public Explosive() {
		super(Explosive.class);
	}
	
	public static void setDefaults(EnchantmentManager manager) {
		manager.setDisplayName(Explosive.class, DEFAULT_DISPLAYNAME);
		manager.setSpecial(Explosive.class, DEFAULT_SPECIAL);
		manager.setAmount(Explosive.class, DEFAULT_AMOUNT);
		manager.setChance(Explosive.class, DEFAULT_CHANCE);
	}

	@Override
	@EventHandler
	public void onUse(EntityDamageByEntityEvent e) {
		
		if (e.getEntity() instanceof LivingEntity
				&& !e.getEntity().getUniqueId().toString().equals(e.getDamager().getCustomName())
				&& ((LivingEntity)e.getEntity()).getEquipment().getChestplate() != null
				&& hasEnchantment(((LivingEntity)e.getEntity()).getEquipment().getChestplate())) {
			
			TNTPrimed tnt = e.getEntity().getLocation().getWorld().spawn(
					((LivingEntity)e.getEntity()).getLocation().add(0, 0.5, 0), TNTPrimed.class);
			tnt.setFuseTicks(0);
			tnt.setYield((float) 
					(amount * 
							(1.0 + ((LivingEntity)e.getEntity()).getEquipment().getChestplate().getEnchantmentLevel(this)/10)));
			tnt.setIsIncendiary(false);
			tnt.setCustomName(e.getEntity().getUniqueId().toString());
			
		} else if (e.getEntity() instanceof LivingEntity
				&& e.getEntity().getUniqueId().toString().equals(e.getDamager().getCustomName())
				&& e.getCause() == DamageCause.ENTITY_EXPLOSION
				&& ((LivingEntity)e.getEntity()).getEquipment().getChestplate() != null
				&& hasEnchantment(((LivingEntity)e.getEntity()).getEquipment().getChestplate())) e.setCancelled(true);
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public int getStartLevel() {
		return 0;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ARMOR_TORSO;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return other.equals(Enchantment.ARROW_FIRE);
	}

}
