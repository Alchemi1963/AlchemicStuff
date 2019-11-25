package me.alchemi.alchemicstuff.objects.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.al.objects.handling.ItemFactory;
import me.alchemi.alchemicstuff.objects.EnchantmentManager;

public class Chopping extends EnchantmentBase {

	public static final String DEFAULT_DISPLAYNAME = "&cChopping";
	public static final boolean DEFAULT_SPECIAL = true;
	public static final double DEFAULT_CHANCE = 10.0D;
	
	public Chopping() {
		super(Chopping.class);
	}
	
	public static void setDefaults(EnchantmentManager manager) {
		manager.setDisplayName(Chopping.class, DEFAULT_DISPLAYNAME);
		manager.setSpecial(Chopping.class, DEFAULT_SPECIAL);
		manager.setChance(Chopping.class, DEFAULT_CHANCE);
	}
	
	@EventHandler
	public void onTrigger(EntityDeathEvent e) {
		if (e.getEntity().getKiller() != null &&
				hasEnchantment(e.getEntity().getKiller(), EquipmentSlot.HAND)
				&& allowChance(getLevel(e.getEntity().getKiller(), EquipmentSlot.HAND))) {
			switch(e.getEntityType()) {
			
			case WITHER_SKELETON:
				e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), 
						new ItemStack(MaterialWrapper.WITHER_SKELETON_SKULL.getMaterial()));
				break;
			case SKELETON:
				e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), 
						new ItemStack(MaterialWrapper.SKELETON_SKULL.getMaterial()));
				break;
			case ZOMBIE:
				e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), 
						new ItemStack(MaterialWrapper.ZOMBIE_HEAD.getMaterial()));
				break;
			case CREEPER:
				e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), 
						new ItemStack(MaterialWrapper.CREEPER_HEAD.getMaterial()));
				break;
			case ENDER_DRAGON:
				e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), 
						new ItemStack(MaterialWrapper.DRAGON_HEAD.getMaterial()));
				break;
			case IRON_GOLEM:
				e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), 
						new ItemStack(MaterialWrapper.IRON_BLOCK.getMaterial()));
				break;
			case PLAYER:
				
				ItemFactory head = new ItemFactory(MaterialWrapper.PLAYER_HEAD);
				ItemMeta meta = head.getItemMeta();
				((SkullMeta)meta).setOwningPlayer((Player) e.getEntity());
				head.setItemMeta(meta);
				e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), head);
				break;
			default:
				break;
		
			}
		}
	}
	
	@Override
	public boolean conflictsWith(Enchantment arg0) {
		return Enchantment.LOOT_BONUS_MOBS.equals(arg0);
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public int getMaxLevel() {
		return 10;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

}
