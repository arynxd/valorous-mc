package me.arynxd.valorous.entities.config;

public enum ConfigOption
{
	TOKEN("token", "token"),
	PRIVILEGEDUSERS("privilegedusers", "0000000000000"),

	APPLICATIONSCHANNEL("applicationschannel", "0000000000000"),
	APPLICATIONSCATEGORY("applicationscategory", "0000000000000"),
	MANAGERROLE("managerrole", "0000000000000"),
	ERRORCHANNEL("errorchannel", "0000000000000"),
	TICKETSCATEGORY("ticketscategory", "0000000000000"),

	LOCALENABLE("localenable", "true"),
	LOCALUSERNAME("localusername", "username"),
	LOCALPASSWORD("localpassword", "password"),
	LOCALDRIVER("localdriver", "org.postgresql.Driver"),
	LOCALURL("localurl", "jdbc:type://host:port/database");

	private final String key;
	private final String defaultValue;

	ConfigOption(String key, String defaultValue)
	{
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue()
	{
		return defaultValue;
	}

	public String getKey()
	{
		return key;
	}
}
