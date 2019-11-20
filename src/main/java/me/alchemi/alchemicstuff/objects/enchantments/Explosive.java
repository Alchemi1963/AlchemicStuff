package me.alchemi.alchemicstuff.objects.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class Explosive extends EnchantmentBase {

	public Explosive() {
		super("explosive");
	}

	@Override
	@EventHandler
	public void onUse(EntityDamageByEntityEvent e) {
		
		
		if (e.getEntity() instanceof LivingEntity
				&& ((LivingEntity)e.getEntity()).getEquipment().getChestplate() != null
				&& hasEnchanment(((LivingEntity)e.getEntity()).getEquipment().getChestplate())) {
			
//			e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 
//					amount * ((Player)e.getEntity()).getInventory().getChestplate().getEnchantmentLevel(this));
			TNTPrimed tnt = e.getEntity().getLocation().getWorld().spawn(e.getEntity().getLocation(), TNTPrimed.class);
			tnt.setFuseTicks(0);
			System.out.println(tnt.getYield());
		}
	}
	
	@EventHandler
	public void onExplode(EntityDamageEvent e) {
		System.out.println(e.getCause());
	}

	@Override
	public void onShoot(EntityShootBowEvent e) {

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
