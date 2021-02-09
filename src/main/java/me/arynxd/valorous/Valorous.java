package me.arynxd.valorous;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import javax.security.auth.login.LoginException;
import me.arynxd.valorous.entities.config.ConfigOption;
import me.arynxd.valorous.events.MessageReceive;
import me.arynxd.valorous.handlers.ApplicationHandler;
import me.arynxd.valorous.handlers.CommandHandler;
import me.arynxd.valorous.handlers.ConfigHandler;
import me.arynxd.valorous.handlers.DatabaseHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Valorous
{
	private final DatabaseHandler databaseHandler;
	private final CommandHandler commandHandler;
	private final ConfigHandler configHandler;
	private final EventWaiter eventWaiter;
	private final ApplicationHandler applicationHandler;
	private JDA jda;

	public Valorous()
	{
		this.configHandler = new ConfigHandler(this);
		this.commandHandler = new CommandHandler(this);
		this.databaseHandler = new DatabaseHandler(this);
		this.eventWaiter = new EventWaiter();
		this.applicationHandler = new ApplicationHandler(this);
	}

	public ApplicationHandler getApplicationHandler()
	{
		return applicationHandler;
	}

	public ConfigHandler getConfigHandler()
	{
		return configHandler;
	}

	public CommandHandler getCommandHandler()
	{
		return commandHandler;
	}

	public JDA getJDA()
	{
		return jda;
	}

	public DatabaseHandler getDatabaseHandler()
	{
		return databaseHandler;
	}

	public EventWaiter getEventWaiter()
	{
		return eventWaiter;
	}

	public void build() throws LoginException, InterruptedException
	{
		this.jda = JDABuilder.createDefault(configHandler.getString(ConfigOption.TOKEN))
				.setMemberCachePolicy(MemberCachePolicy.NONE)
				.disableCache(
						CacheFlag.ACTIVITY,
						CacheFlag.EMOTE,
						CacheFlag.CLIENT_STATUS,
						CacheFlag.ROLE_TAGS,
						CacheFlag.MEMBER_OVERRIDES)

				.addEventListeners(
						eventWaiter,
						new MessageReceive(this))

				.build().awaitReady();
	}
}
