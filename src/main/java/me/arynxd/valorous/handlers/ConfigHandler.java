package me.arynxd.valorous.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.arynxd.valorous.entities.config.ConfigOption;

public class ConfigHandler
{
	public static final File CONFIG_FOLDER = new File("config");
	public static final File CONFIG_FILE = new File(CONFIG_FOLDER, "bot.cfg");
	private final List<ConfigurationValue> configValues;

	public ConfigHandler()
	{
		initFolder();
		initFile();
		this.configValues = loadInitialValues();
	}

	private void initFile()
	{
		try
		{
			CONFIG_FILE.createNewFile();
		}
		catch(Exception exception)
		{
			System.out.println("A config error occurred");
			exception.printStackTrace();
			System.exit(1);
		}
	}

	private void initFolder()
	{
		try
		{
			CONFIG_FOLDER.mkdir();
		}
		catch(Exception exception)
		{
			System.out.println("A config error occurred");
			exception.printStackTrace();
			System.exit(1);
		}
	}

	private List<ConfigurationValue> loadInitialValues()
	{
		List<ConfigurationValue> values = new ArrayList<>();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE));
			String line;
			while((line = reader.readLine()) != null)
			{
				if(!line.contains("=") || line.startsWith("#"))
				{
					continue;
				}
				String[] elements = line.split("=");
				values.add(new ConfigurationValue(elements[0], elements[1]));
			}
			return applyDefaults(values);
		}
		catch(Exception exception)
		{
			System.out.println("A config error occurred");
			exception.printStackTrace();
			return Collections.emptyList();
		}
	}

	private List<ConfigurationValue> applyDefaults(List<ConfigurationValue> loadedValues)
	{
		for(ConfigOption configOption : ConfigOption.values())
		{
			if(loadedValues.stream().map(ConfigurationValue::getKey).noneMatch(key -> configOption.getKey().equals(key)))
			{
				loadedValues.add(new ConfigurationValue(configOption.getKey(), configOption.getDefaultValue()));
			}
		}
		save(loadedValues);
		return Collections.unmodifiableList(loadedValues);
	}

	private void save(List<ConfigurationValue> configValues)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for(ConfigurationValue configurationValue : configValues)
		{
			stringBuilder
					.append(configurationValue.getKey())
					.append("=")
					.append(configurationValue.getValue())
					.append("\n");
		}
		try
		{
			FileWriter fileWriter = new FileWriter(CONFIG_FILE);
			fileWriter.write(stringBuilder.toString());
			fileWriter.flush();
		}
		catch(Exception exception)
		{
			System.out.println("A config error occurred");
			exception.printStackTrace();
		}
	}

	public String getString(ConfigOption configOption)
	{
		synchronized(configValues)
		{
			for(ConfigurationValue configurationValue : configValues)
			{
				if(configurationValue.getKey().equals(configOption.getKey()))
				{
					return configurationValue.getValue();
				}
			}
			return configOption.getDefaultValue();
		}
	}

	private static class ConfigurationValue
	{
		private String key;
		private String value;

		public ConfigurationValue(String key, String value)
		{
			this.key = key;
			this.value = value;
		}

		public String getKey()
		{
			return key;
		}

		public void setKey(String key)
		{
			this.key = key;
		}

		public String getValue()
		{
			return value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}
	}
}

