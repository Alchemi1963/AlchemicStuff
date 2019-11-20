package me.alchemi.alchemicstuff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.al.configurations.SexyConfiguration;
import me.alchemi.al.objects.base.ConfigBase;

public class Config extends ConfigBase {

	private static Config config;
	
	public Config() throws FileNotFoundException, IOException, InvalidConfigurationException {
		super(Stuff.getInstance());
		
		config = this;
	}

	public static enum ConfigEnum implements IConfigEnum{
		CONFIG(new File(Stuff.getInstance().getDataFolder(), "config.yml"), 1),
		MESSAGES(new File(Stuff.getInstance().getDataFolder(), "messages.yml"), 2);

		final File file;
		final int version;
		SexyConfiguration config;
		
		private ConfigEnum(File file, int version) {
			this.file = file;
			this.version = version;
			this.config = SexyConfiguration.loadConfiguration(file);
		}
		
		@Override
		public SexyConfiguration getConfig() {
			return config;
		}

		@Override
		public File getFile() {
			return file;
		}

		@Override
		public int getVersion() {
			return version;
		}
	}
	
	public static enum Messages implements IMessage{
		
		;
		
		String value;
		String key;

		private Messages(String key) {
			this.key = key;
		}

		public void get() {
			value = getConfig().getString(key, "PLACEHOLDER - STRING NOT FOUND");

		}
  
		public String value() { return value; }

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.MESSAGES.getConfig();
		} 
	
	}
	 
	public static enum Options implements IConfig {
		
		;
		
		private Object value;
		public final String key;
		
		Options(String key){
			this.key = key;
			get();
		}
				
		@Override
		public void get() {
			value = ConfigEnum.CONFIG.getConfig().get(key);
		}
		
		@Override
		public Object value() {
			return value;
		}
		
		@Override
		public boolean asBoolean() {
			return Boolean.parseBoolean(asString());
		}
		
		@Override
		public String asString() {
			return String.valueOf(value);
		}
		
		@Override
		public Sound asSound() {
			
			return Sound.valueOf(asString());
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public List<String> asStringList() {
			try {
				return (List<String>) value;
			} catch (ClassCastException e) { return null; }
		}
		
		@Override
		public int asInt() {
			return Integer.valueOf(asString());
		}
		
		public double asDouble() {
			return Double.valueOf(asString());
		}
		
		@Override
		public ItemStack asItemStack() {
			try {
				return (ItemStack) value;
			} catch (ClassCastException e) { return null; }
		}
		
		@Override
		public Material asMaterial() {
			return MaterialWrapper.getWrapper(asString());
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public List<Integer> asIntList(){
			try {
				return (List<Integer>) value;
			} catch (ClassCastException e) { return null; }
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.CONFIG.getConfig();
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<Float> asFloatList() {
			try {
				return (List<Float>) value;
			} catch (ClassCastException e) { return null; }
		}
	}
	
	
	@Override
	protected IConfigEnum[] getConfigs() {
		return ConfigEnum.values();
	}

	@Override
	protected Set<IConfig> getEnums() {
		return new HashSet<ConfigBase.IConfig>();
	}

	@Override
	protected Set<IMessage> getMessages() {
		return new HashSet<ConfigBase.IMessage>() {
			{
				addAll(Arrays.asList(Messages.values()));
			}
		};
	}
	
	public static Config getConfig() {
		return config;
	}
}
