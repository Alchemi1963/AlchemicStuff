package me.alchemi.alchemicstuff.objects.enchantments;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.alchemi.al.Library;
import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.al.configurations.Messenger;
import me.alchemi.alchemicstuff.Stuff;
import me.alchemi.alchemicstuff.objects.EnchantmentManager;
import me.alchemi.alchemicstuff.objects.RomanNumber;

public abstract class EnchantmentBase extends Enchantment implements Listener{

	protected final Class<? extends EnchantmentBase> key;
	protected String displayName;
	protected boolean special;
	protected double chance;
	protected double amount;
	
	public EnchantmentBase(Class<? extends EnchantmentBase> key) {
		super(new NamespacedKey(Stuff.getInstance(), key.getSimpleName().toLowerCase()));
		
		this.key = key;
		
		register();
	}
	
	public void load() {
		displayName = EnchantmentManager.getEnchants().getDisplayName(key);
		special = EnchantmentManager.getEnchants().getSpecial(key);
		chance = EnchantmentManager.getEnchants().getChance(key);
		amount = EnchantmentManager.getEnchants().getAmount(key);
	}
	
	private void register() {
		try {
			
			if (!isAcceptingRegistrations()) {
				Field f = Enchantment.class.getDeclaredField("acceptingNew");
				f.setAccessible(true);
				f.set(null, true);
				
				registerEnchantment(this);
				
				stopAcceptingRegistrations();
			} else {
				registerEnchantment(this);
			}

		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {}
		
		EnchantmentManager.register(this);
		Bukkit.getPluginManager().registerEvents(this, Stuff.getInstance());
		
	}

	@Override
	public String getName() {
		return key.getSimpleName().toLowerCase();
	}

	@Override
	public boolean isTreasure() {
		return true;
	}

	@Override
	public boolean isCursed() {
		return false;
	}
	
	public boolean isSpecial() {
		return special;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	protected final boolean allowChance() {
		return new Random().nextInt(100) <= chance;
	}
	
	protected boolean hasEnchantment(ItemStack stack) {
		return stack.containsEnchantment(this);
	}
	
	protected boolean hasEnchantment(LivingEntity entity, EquipmentSlot slot) {
		switch (slot) {
		case CHEST:
			return hasEnchantment(entity.getEquipment().getChestplate());
		case FEET:
			return hasEnchantment(entity.getEquipment().getBoots());
		case HAND:
			return hasEnchantment(entity.getEquipment().getItemInMainHand());
		case HEAD:
			return hasEnchantment(entity.getEquipment().getHelmet());
		case LEGS:
			return hasEnchantment(entity.getEquipment().getLeggings());
		case OFF_HAND:
			return hasEnchantment(entity.getEquipment().getItemInOffHand());
		default:
			return false;
		}
	}
	
	protected int getLevel(ItemStack stack) {
		return stack.getEnchantmentLevel(this);
	}
	
	protected int getLevel(LivingEntity entity, EquipmentSlot slot) {
		switch (slot) {
		case CHEST:
			return getLevel(entity.getEquipment().getChestplate());
		case FEET:
			return getLevel(entity.getEquipment().getBoots());
		case HAND:
			return getLevel(entity.getEquipment().getItemInMainHand());
		case HEAD:
			return getLevel(entity.getEquipment().getHelmet());
		case LEGS:
			return getLevel(entity.getEquipment().getLeggings());
		case OFF_HAND:
			return getLevel(entity.getEquipment().getItemInOffHand());
		default:
			return 0;
		}
	}
	
	protected final ItemStack setLore(ItemStack stack) {
		
		ItemMeta meta = stack.getItemMeta();
		
		List<String> lore;
		if (meta.hasLore()) {
			lore = meta.getLore();	
			
			if (hasEnchantment(stack)) {
				for (String line : lore) {
					if (line.contains(Messenger.formatString(displayName))) {
						int index = lore.indexOf(line);
						line = Messenger.formatString(displayName + " " + RomanNumber.toRoman(stack.getEnchantmentLevel(this) + 1));
						lore.set(index, line);
					}
				}
			} else {
				for (int i = 0; i < lore.size(); i++) {
					if (!lore.get(i).isEmpty() &&
							!lore.get(i).matches("\\w+\\W+(?:M*(?:LD|LM|CM|CD|D?C{0,3})(?:VL|VC|XC|XL|L?X{0,3})(?:IX|IV|V?I{0,3}))")) {
						
						if (special) lore.addAll(i, Arrays.asList("", Messenger.formatString(displayName + " I")));
						else lore.add(i, Messenger.formatString(displayName + " I"));
						break;
					} else if (lore.get(i).equals(lore.get(lore.size() - 1))) {
						
						if (special) lore.addAll(Arrays.asList("", Messenger.formatString(displayName + " I")));
						else lore.add(Messenger.formatString(displayName + " I"));
						
					}
				}
			} 
			
		} else {
			if (special) lore = Arrays.asList("", Messenger.formatString(displayName + " I"));
			else lore = Arrays.asList(Messenger.formatString(displayName + " I"));
		}
		
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	protected ItemStack apply(ItemStack stack) { return apply(stack, 1); }
	
	protected ItemStack apply(ItemStack stack, int level) throws IllegalArgumentException {
		if (hasEnchantment(stack) && stack.getEnchantmentLevel(this) + level > getMaxLevel()) 
			throw new IllegalArgumentException("Stack will overgo maximum level.");
		
		if (hasEnchantment(stack)) stack.addEnchantment(this, stack.getEnchantmentLevel(this) + level);
		else stack.addEnchantment(this, level);
		
		
		return setLore(stack);
	}
	
	public ItemStack give(Player player) {
		ItemStack item = new ItemStack(MaterialWrapper.ENCHANTED_BOOK.getMaterial());
		item = apply(item);
		Library.giveItemStack(item, player);
		return item;
	}
	
	public void onUse(EntityDamageByEntityEvent e) {};
	
	public void onShoot(EntityShootBowEvent e) {};
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventory(InventoryClickEvent e) {
		if (e.getCursor() != null
				&& MaterialWrapper.wrap(e.getCursor()) == MaterialWrapper.ENCHANTED_BOOK
				&& e.getCurrentItem() != null
				&& !e.getCursor().equals(e.getCurrentItem())
				&& canEnchantItem(e.getCurrentItem())) {
			
			for (Enchantment ench : e.getCursor().getEnchantments().keySet()) {
				if (ench.equals(this)) {
					System.out.println(e.getCurrentItem());
					e.setCurrentItem(apply(e.getCurrentItem(), e.getCursor().getEnchantmentLevel(this)));
					e.setCursor(null);
					System.out.println(e.getCurrentItem());
					e.setCancelled(true);
					break;
				}
			}
			
		}
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		return (getItemTarget().includes(item) 
				|| MaterialWrapper.getWrapper(item) == MaterialWrapper.ENCHANTED_BOOK.getMaterial())
					&& noneConflict(item.getEnchantments().keySet())
						&& (!item.containsEnchantment(this) 
							|| item.getEnchantmentLevel(this) < getMaxLevel());
	}
	
	protected final boolean noneConflict(Collection<Enchantment> enchantments) {
		for (Enchantment ench : enchantments) {
			if (conflictsWith(ench)) return false;
		}
		return true;
	}
}
