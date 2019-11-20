package me.alchemi.alchemicstuff.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.al.objects.base.CommandBase;
import me.alchemi.alchemicstuff.Stuff;
import me.alchemi.alchemicstuff.objects.EnchantmentManager;

public class GiveCommand extends CommandBase {

	public GiveCommand() {
		super(Stuff.getInstance(), "git gud");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player && sender.hasPermission(command.getPermission()))) return true;
		
		if (args.length == 1) {
			try {
				System.out.println(EnchantmentManager.getByKey(args[0]).give((Player) sender));
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
				
			}
		}
		
		return true;
	}

}
