package me.alchemi.alchemicstuff.objects.enchantments;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.alchemicstuff.objects.EnchantmentManager;

@SuppressWarnings("deprecation")
public class Shredder extends EnchantmentBase {

	public static final String DEFAULT_DISPLAYNAME = "&7Shredding";
	public static final boolean DEFAULT_SPECIAL = false;
	
	public Shredder() {
		super(Shredder.class);
	}
	
	public static void setDefaults(EnchantmentManager manager) {
		manager.setDisplayName(Shredder.class, DEFAULT_DISPLAYNAME);
		manager.setSpecial(Shredder.class, DEFAULT_SPECIAL);
	}

	@Override
	public void onUse(EntityDamageByEntityEvent e) {}

	@EventHandler
	@Override
	public void onShoot(EntityShootBowEvent e) {}
	
	@EventHandler
	public void onRight(PlayerInteractEvent e) {
		
		for (Plugin plug : Bukkit.getPluginManager().getPlugins()) {
			for (RegisteredListener l : HandlerList.getRegisteredListeners(plug)) {
				for (Method m : l.getListener().getClass().getDeclaredMethods()) {
					if (Arrays.asList(m.getParameterTypes()).contains(EntityTargetLivingEntityEvent.class)) {
						System.out.println(plug);
						System.out.println(l.getListener().getClass());
						System.out.println(Arrays.toString(m.getParameterTypes()));
					}
				}
			}
		}
		
		if (e.getItem() == null) return;
		
		if (hasEnchantment(e.getItem())
				&& (e.getAction() == Action.RIGHT_CLICK_AIR
					|| e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			
			PlayerInventory inv = e.getPlayer().getInventory();
			boolean creative = e.getPlayer().getGameMode() == GameMode.CREATIVE;
			boolean infinite = e.getItem().containsEnchantment(ARROW_INFINITE) && e.getItem().getEnchantmentLevel(this) >= 2;
			
			int index = -1;
			ItemStack arrowStack;
						
			if (inv.contains(MaterialWrapper.TIPPED_ARROW.getMaterial())) {  
				index = inv.first(MaterialWrapper.TIPPED_ARROW.getMaterial());
				arrowStack = inv.getItem(index);
			} else if (inv.contains(MaterialWrapper.SPECTRAL_ARROW.getMaterial())) {
				index = inv.first(MaterialWrapper.SPECTRAL_ARROW.getMaterial());
				arrowStack = inv.getItem(index);
			} else if (inv.contains(MaterialWrapper.ARROW.getMaterial())) {
				index = inv.first(MaterialWrapper.ARROW.getMaterial());
				arrowStack = inv.getItem(index);
			} else if (creative) {
				arrowStack = new ItemStack(MaterialWrapper.ARROW.getMaterial());
			} else {
				return;
			}
			
			
			switch(MaterialWrapper.wrap(MaterialWrapper.getWrapper(arrowStack))) {
			case TIPPED_ARROW:
				TippedArrow ta = e.getPlayer().launchProjectile(TippedArrow.class, e.getPlayer().getEyeLocation().getDirection().normalize().multiply(3));
				if (creative 
						|| infinite) ta.setPickupStatus(PickupStatus.CREATIVE_ONLY);
				ta.setShooter(e.getPlayer());
				ta.setBasePotionData(((PotionMeta)arrowStack.getItemMeta()).getBasePotionData());
				Bukkit.getPluginManager().callEvent(new EntityShootBowEvent(e.getPlayer(), e.getItem(), ta, 1.0F));
				break;
			case SPECTRAL_ARROW:
				SpectralArrow sa = e.getPlayer().launchProjectile(SpectralArrow.class, e.getPlayer().getEyeLocation().getDirection().normalize().multiply(3));
				if (creative 
						|| infinite) sa.setPickupStatus(PickupStatus.CREATIVE_ONLY);
				sa.setShooter(e.getPlayer());
				break;
			case ARROW:
				Arrow a = e.getPlayer().launchProjectile(Arrow.class, e.getPlayer().getEyeLocation().getDirection().normalize().multiply(3));
				if (creative 
						|| infinite) a.setPickupStatus(PickupStatus.CREATIVE_ONLY);
				a.setShooter(e.getPlayer());
				Bukkit.getPluginManager().callEvent(new EntityShootBowEvent(e.getPlayer(), e.getItem(), a, 1.0F));
				break;
			default:
				break;
			}
			
			if (!(creative 
					&& infinite)) {
				arrowStack.setAmount(arrowStack.getAmount() - 1);
				e.getPlayer().getInventory().setItem(index, arrowStack);
				
				ItemStack bow = e.getItem().clone();
				
				Damageable meta = ((Damageable)bow.getItemMeta());
				meta.setDamage(meta.getDamage() + 1);
				bow.setItemMeta((ItemMeta) meta);
				e.getPlayer().getInventory().setItemInMainHand(bow);
				e.getPlayer().updateInventory();
			} else if (!creative) {
				ItemStack bow = e.getItem().clone();
				
				Damageable meta = ((Damageable)bow.getItemMeta());
				meta.setDamage(meta.getDamage() + 1);
				bow.setItemMeta((ItemMeta) meta);
				e.getPlayer().getInventory().setItemInMainHand(bow);
				e.getPlayer().updateInventory();
			}
			e.setCancelled(true);
			
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
		return EnchantmentTarget.BOW;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}

	
	
}
