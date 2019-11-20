package me.alchemi.alchemicstuff.tabcomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.al.objects.base.TabCompleteBase;
import me.alchemi.alchemicstuff.objects.EnchantmentManager;
import me.alchemi.alchemicstuff.objects.enchantments.EnchantmentBase;

public class GiveTabComplete extends TabCompleteBase {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		List<Object> list = new ArrayList<Object>();

		if (!(sender instanceof Player && sender.hasPermission(command.getPermission())))
			return Arrays.asList("");

		for (EnchantmentBase ench : EnchantmentManager.getRegistry().values()) {
			list.add(ench.getName());
		}
		
		return returnSortSuggest(list, args);
	}
	
}
