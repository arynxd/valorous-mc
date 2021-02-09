package me.arynxd.valorous.entities.command;

import java.util.ArrayList;
import java.util.List;
import me.arynxd.valorous.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;

public abstract class Command
{
	private final String name;
	private final String description;
	private final String syntax;
	private final List<Permission> memberPermissions;
	private final List<Permission> selfPermissions;
	private final ArrayList<String> aliases;
	private final ArrayList<CommandFlag> flags;

	protected Command(String name, String description, String syntax)
	{
		this.name = name;
		this.description = description;
		this.syntax = syntax;
		this.memberPermissions = new ArrayList<>();
		this.selfPermissions = new ArrayList<>();
		this.aliases = new ArrayList<>();
		this.flags = new ArrayList<>();
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public String getSyntax()
	{
		return syntax;
	}

	public List<Permission> getMemberPermissions()
	{
		return memberPermissions;
	}

	public List<Permission> getSelfPermissions()
	{
		return selfPermissions;
	}

	public void addAliases(String... aliases)
	{
		this.aliases.addAll(List.of(aliases));
	}

	public void addMemberPermissions(Permission... permissions)
	{
		this.memberPermissions.addAll(List.of(permissions));
	}

	public void addSelfPermissions(Permission... permissions)
	{
		this.selfPermissions.addAll(List.of(permissions));
	}

	public List<String> getAliases()
	{
		return aliases;
	}

	public List<CommandFlag> getFlags()
	{
		return flags;
	}

	public void addFlags(CommandFlag... flags)
	{
		this.flags.addAll(List.of(flags));
	}

	public boolean hasFlag(CommandFlag flag)
	{
		return this.flags.contains(flag);
	}

	public abstract void run(CommandEvent event);

	public void process(CommandEvent event)
	{
		if(event.isDeveloper())
		{
			run(event);
		}
		else if(!event.isDeveloper() && event.getCommand().hasFlag(CommandFlag.DEVELOPER_ONLY))
		{
			EmbedUtil.sendError(event, "That command is for developers only.");
		}
		else if(!event.memberPermissionCheck(event.getCommand().getMemberPermissions()))
		{
			EmbedUtil.sendError(event, "You are missing some required permissions to execute that command.");
		}
		else if(!event.selfPermissionCheck(event.getCommand().getSelfPermissions()))
		{
			EmbedUtil.sendError(event, "I am missing some permissions required to execute that command.");
		}
		else
		{
			run(event);
		}
	}
}
