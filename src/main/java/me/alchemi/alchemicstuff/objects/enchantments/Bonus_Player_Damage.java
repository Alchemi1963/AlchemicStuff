package me.alchemi.alchemicstuff.objects.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Illager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Pillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.alchemi.alchemicstuff.objects.EnchantmentManager;

public class Bonus_Player_Damage extends EnchantmentBase{

	public static final String DEFAULT_DISPLAYNAME = "&8Hanged Man's Venom";
	public static final boolean DEFAULT_SPECIAL = false;
	public static final double DEFAULT_AMOUNT = 1.5D;
		
	public Bonus_Player_Damage() {
		super(Bonus_Player_Damage.class);
	}
	
	public static void setDefaults(EnchantmentManager manager) {
		manager.setDisplayName(Bonus_Player_Damage.class, DEFAULT_DISPLAYNAME);
		manager.setSpecial(Bonus_Player_Damage.class, DEFAULT_SPECIAL);
		manager.setAmount(Bonus_Player_Damage.class, DEFAULT_AMOUNT);
	}

	@EventHandler
	@Override
	public void onUse(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof LivingEntity
				&& (e.getEntity() instanceof HumanEntity
						|| e.getEntity() instanceof NPC
						|| e.getEntity() instanceof Illager
						|| e.getEntity() instanceof Pillager)
				&& hasEnchantment((LivingEntity) e.getDamager(), EquipmentSlot.HAND)) {
			e.setDamage(e.getDamage() + (amount * getLevel((LivingEntity) e.getDamager(), EquipmentSlot.HAND)));
		}
	}
	
	@Override
	public boolean conflictsWith(Enchantment arg0) {
		return !(arg0.equals(Enchantment.DAMAGE_UNDEAD) || arg0.equals(Enchantment.DAMAGE_ARTHROPODS));
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
