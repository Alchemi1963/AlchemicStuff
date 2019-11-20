package me.alchemi.alchemicstuff.objects.enchantments;

import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class LifeSteal extends EnchantmentBase{

	public LifeSteal() {
		super("lifesteal");
	}

	@EventHandler
	@Override
	public void onUse(EntityDamageByEntityEvent e) {
	
		if (e.getDamager() instanceof Player
				&& ((Player)e.getDamager()).getInventory().getItemInMainHand() != null
				&& hasEnchanment(((Player)e.getDamager()).getInventory().getItemInMainHand())
				&& new Random().nextInt(100) <= chance) {
			
			double damage = 1.0 - ((double)amount)/(((Player)e.getDamager()).getInventory().getItemInMainHand().getEnchantmentLevel(this) + amount);
			((Player)e.getDamager()).setHealth(((Player)e.getDamager()).getHealth() + (e.getFinalDamage() * damage));
			
		}
		
	}

	@Override
	public void onShoot(EntityShootBowEvent e) {}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}

}
