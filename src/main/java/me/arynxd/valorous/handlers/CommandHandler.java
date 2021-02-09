package me.arynxd.valorous.handlers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import me.arynxd.valorous.Constants;
import me.arynxd.valorous.Valorous;
import me.arynxd.valorous.commands.*;
import me.arynxd.valorous.entities.command.Command;
import me.arynxd.valorous.entities.command.CommandEvent;
import me.arynxd.valorous.util.EmbedUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandHandler
{
	private final Valorous valorous;
	private final Map<String, Command> commandMap;

	public CommandHandler(Valorous valorous)
	{
		this.valorous = valorous;
		this.commandMap = generateMap();
	}

	private Map<String, Command> generateMap()
	{
		List<Command> commandList = List.of(
				new NoCommand(),
				new ApplyCommand(),
				new AcceptCommand(),
				new DenyCommand(),
				new HelpCommand(),
				new ClearCommand(),
				new TicketCommand(),
				new CloseCommand());

		Map<String, Command> result = new HashMap<>();

		for(Command command : commandList)
		{
			result.put(command.getName(), command);
			for(String alias : command.getAliases())
			{
				result.put(alias, command);
			}
		}
		return new ConcurrentHashMap<>(Collections.unmodifiableMap(result));
	}

	public void handleCommand(GuildMessageReceivedEvent event)
	{
		String content = event.getMessage().getContentRaw().substring(Constants.DEFAULT_PREFIX.length());
		List<String> args = Arrays
				.stream(content.split("\\s+"))
				.filter(arg -> !arg.isBlank())
				.collect(Collectors.toList());

		if(args.isEmpty())
		{
			return;
		}

		String commandQuery = args.get(0).toLowerCase();
		Command cmd = commandMap.get(commandQuery);

		if(cmd == null)
		{
			EmbedUtil.sendError(event.getChannel(), valorous, "Command `" + commandQuery + "` was not found.");
			return;
		}

		args.remove(0);
		cmd.process(new CommandEvent(event, cmd, args, valorous));
	}

	public Map<String, Command> getCommandMap()
	{
		return commandMap;
	}
}
