package me.arynxd.valorous.entities.command;

import java.util.List;
import me.arynxd.valorous.Valorous;
import me.arynxd.valorous.entities.config.ConfigOption;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandEvent
{
	private final GuildMessageReceivedEvent event;
	private final Valorous valorous;
	private final Command command;
	private final List<String> args;

	public CommandEvent(GuildMessageReceivedEvent event, Command command, List<String> args, Valorous valorous)
	{
		this.event = event;
		this.command = command;
		this.args = args;
		this.valorous = valorous;
	}

	public MessageChannel getChannel()
	{
		return event.getChannel();
	}

	public TextChannel getTextChannel()
	{
		return event.getChannel();
	}


	public Valorous getValorous()
	{
		return valorous;
	}

	public User getAuthor()
	{
		return event.getAuthor();
	}

	public Command getCommand()
	{
		return command;
	}

	public List<String> getArgs()
	{
		return args;
	}

	public Member getMember()
	{
		return event.getMember();
	}

	public JDA getJDA()
	{
		return event.getJDA();
	}

	public Guild getGuild()
	{
		return event.getGuild();
	}

	public Message getMessage()
	{
		return event.getMessage();
	}

	public boolean memberPermissionCheck(Permission... permissions)
	{
		return (event.getMember() != null && event.getMember().hasPermission(event.getChannel(), permissions));
	}

	public boolean selfPermissionCheck(Permission... permissions)
	{
		return event.getGuild().getSelfMember().hasPermission(permissions);
	}

	public boolean memberPermissionCheck(List<Permission> permissions)
	{
		return (event.getMember() != null && event.getMember().hasPermission(event.getChannel(), permissions));
	}

	public boolean selfPermissionCheck(List<Permission> permissions)
	{
		return event.getGuild().getSelfMember().hasPermission(permissions);
	}

	public boolean isDeveloper()
	{
		return List.of(valorous.getConfigHandler().getString(ConfigOption.PRIVILEGEDUSERS).split(",")).contains(getAuthor().getId());
	}
}
