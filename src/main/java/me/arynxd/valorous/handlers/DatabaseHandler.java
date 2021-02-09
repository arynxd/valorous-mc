package me.arynxd.valorous.handlers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import me.arynxd.valorous.Valorous;
import me.arynxd.valorous.entities.config.ConfigOption;
import me.arynxd.valorous.util.IOUtil;

public class DatabaseHandler
{
	private final Valorous valorous;
	private final HikariDataSource pool;
	private boolean isEnabled = true;

	public DatabaseHandler(Valorous valorous)
	{
		this.valorous = valorous;
		this.pool = initHikari();
		if(isEnabled)
		{
			initTables();
		}
	}

	private HikariDataSource initHikari()
	{
		HikariConfig hikariConfig = new HikariConfig();
		ConfigHandler configuration = valorous.getConfigHandler();

		if(!configuration.getString(ConfigOption.LOCALENABLE).equalsIgnoreCase("true"))
		{
			System.out.println("Database WILL be disabled for this session.");
			isEnabled = false;
			return null;
		}

		hikariConfig.setDriverClassName(configuration.getString(ConfigOption.LOCALDRIVER));
		hikariConfig.setJdbcUrl(configuration.getString(ConfigOption.LOCALURL));

		hikariConfig.setUsername(configuration.getString(ConfigOption.LOCALUSERNAME));
		hikariConfig.setPassword(configuration.getString(ConfigOption.LOCALPASSWORD));

		hikariConfig.setMaximumPoolSize(30);
		hikariConfig.setMinimumIdle(10);
		hikariConfig.setConnectionTimeout(10000);

		try
		{
			return new HikariDataSource(hikariConfig);
		}
		catch(Exception exception)
		{
			System.out.println("Local database offline, connection failure.");
			exception.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	public Connection getConnection()
	{
		if(!isEnabled)
		{
			throw new IllegalStateException("Database is disabled");
		}

		try
		{
			return pool.getConnection();
		}
		catch(Exception exception)
		{
			return getConnection();
		}
	}

	private void initTables()
	{
		initTable("tickets");
	}

	private void initTable(String table) //Helper method for initTables
	{
		try
		{
			InputStream file = IOUtil.getResourceFile("sql/" + table + ".sql");
			if(file == null)
			{
				throw new NullPointerException("File for table '" + table + "' not found");
			}
			getConnection().createStatement().execute(IOUtil.convertToString(file));
		}
		catch(Exception exception)
		{
			System.out.println("Error initializing table: '" + table + "'");
			exception.printStackTrace();
		}
	}
}
