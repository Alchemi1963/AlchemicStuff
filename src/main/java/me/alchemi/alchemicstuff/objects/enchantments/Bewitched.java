package me.alchemi.alchemicstuff.objects.enchantments;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.scheduler.BukkitRunnable;

import me.alchemi.alchemicstuff.Stuff;
import me.alchemi.alchemicstuff.objects.EnchantmentManager;

public class Bewitched extends EnchantmentBase {

	public static final String DEFAULT_DISPLAYNAME = "&5Bewitched";
	public static final boolean DEFAULT_SPECIAL = true;
	public static final double DEFAULT_CHANCE = 60D;
	public static final double DEFAULT_AMOUNT = 20D;
	
	public Bewitched() {
		super(Bewitched.class);
	}
	
	public static void setDefaults(EnchantmentManager manager) {
		manager.setDisplayName(Bewitched.class, DEFAULT_DISPLAYNAME);
		manager.setSpecial(Bewitched.class, DEFAULT_SPECIAL);
		manager.setChance(Bewitched.class, DEFAULT_CHANCE);
		manager.setAmount(Bewitched.class, DEFAULT_AMOUNT);
	}
	
	@EventHandler
	@Override
	public void onShoot(EntityShootBowEvent e) {
		if (hasEnchantment(e.getBow())
				&& allowChance()) {
			Entity projectile = e.getProjectile();
			projectile.setCustomName("bewitched");
			e.setProjectile(projectile);
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if (e.getEntity().getCustomName() != null 
				&& e.getEntity().getCustomName().equals("bewitched")) {
			Location loc = e.getEntity().getLocation();
			if (loc != null) {
				for (int i = 0; i < amount; i++) {
					Entity bat = loc.getWorld().spawnEntity(loc, EntityType.BAT);
					bat.setMetadata("bewitched", new MetadataValueAdapter(Stuff.getInstance()) {
						
						@Override
						public Object value() {
							Location l = e.getEntity().getLocation();
							return l.getBlockX() + "-" + l.getBlockY() + "-" + loc.getBlockZ();
						}
						
						@Override
						public void invalidate() {}
					});
				}
				new BukkitRunnable() {
					
					@Override
					public void run() {
						
						for (Bat batbat : e.getEntity().getWorld().getEntitiesByClass(Bat.class)) {
							if (batbat.hasMetadata("bewitched")) {
								Location l = e.getEntity().getLocation();
								if (batbat.getMetadata("bewitched").get(0).asString()
										.equals(l.getBlockX() + "-" + l.getBlockY() + "-" + loc.getBlockZ())) batbat.setHealth(0D);
							}
						}
					}
				}.runTaskLater(Stuff.getInstance(), 600);
			}
		}
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
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
