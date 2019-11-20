package me.alchemi.alchemicstuff;

import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.base.PluginBase;
import me.alchemi.alchemicstuff.command.GiveCommand;
import me.alchemi.alchemicstuff.objects.EnchantmentManager;
import me.alchemi.alchemicstuff.tabcomplete.GiveTabComplete;

public class Stuff extends PluginBase {

	private static Stuff instance;
	
	public static Stuff getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		setMessenger(new Messenger(this));
		
		try {
			new Config();
			messenger.print("&6Configs enabled!");
		} catch (IOException | InvalidConfigurationException e) {
			System.err.println("[PLUGIN]: Could not enable config files.\nDisabling plugin...");
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		}
		
		new EnchantmentManager();
		
		registerEvents();
		enableCommands();
		
		messenger.print("&6Let the sexiness begin...");
	}
	
	@Override
	public void onDisable() {
		
		messenger.print("&4Jack Harkness mode off...");
	}
	
	public void enableCommands() {
		getCommand("giveenchant").setExecutor(new GiveCommand());
		
		getCommand("giveenchant").setTabCompleter(new GiveTabComplete());
	}
	
	public void registerEvents() {
//		for (Listener listen : Arrays.asList(new Shredder(),
//				new LifeSteal())) {
//			Bukkit.getPluginManager().registerEvents(listen, this);
//		}
	}
	
	public Messenger getMessenger() {
		return messenger;
	}
	
}
