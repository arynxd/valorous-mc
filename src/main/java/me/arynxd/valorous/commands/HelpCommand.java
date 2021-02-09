package me.arynxd.valorous.commands;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import me.arynxd.valorous.entities.command.Command;
import me.arynxd.valorous.entities.command.CommandEvent;
import me.arynxd.valorous.util.EmbedUtil;
import me.arynxd.valorous.util.Parser;
import net.dv8tion.jda.api.EmbedBuilder;

@SuppressWarnings("unused")
public class HelpCommand extends Command
{
	public HelpCommand()
	{
		super("Help", "Shows the help menu.", "<page>");
		addAliases("help", "?", "cmd");
	}

	@Override
	public void run(CommandEvent event)
	{
		if(event.getArgs().isEmpty())
		{
			event.getChannel().sendMessage(getHelpPages(event).get(0).build()).queue();
			return;
		}
		OptionalInt page = new Parser(event.getArgs().get(0), event).parseAsUnsignedInt();

		if(page.isPresent())
		{
			if(page.getAsInt() + 1 > getHelpPages(event).size() + 1)
			{
				EmbedUtil.sendError(event, "Page `" + page.getAsInt() + "` does not exist.");
				return;
			}

			event.getChannel().sendMessage(getHelpPages(event).get(page.getAsInt() - 1).build()).queue();
		}
	}

	public List<EmbedBuilder> getHelpPages(CommandEvent event)
	{
		List<EmbedBuilder> pages = new ArrayList<>();
		List<Command> commands = new ArrayList<>();
		for(Command cmd : event.getValorous().getCommandHandler().getCommandMap().values())
		{
			if(!commands.contains(cmd))
			{
				commands.add(cmd);
			}
		}

		EmbedBuilder embedBuilder = new EmbedBuilder();
		int fieldCount = 0;
		int page = 1;
		for(int i = 0; i < commands.size(); i++)
		{
			Command cmd = commands.get(i);
			if(fieldCount < 5)
			{
				fieldCount++;
				embedBuilder.setTitle("Help page: " + page);
				embedBuilder.addField(cmd.getName(), cmd.getDescription() + "\n**" + cmd.getAliases().get(0) + "**`" + cmd.getSyntax() + "`", false);
				embedBuilder.setColor(Color.GRAY);
				embedBuilder.setFooter("<> Optional;  [] Required; {} Maximum Quantity | ");
			}
			else
			{
				pages.add(embedBuilder);
				embedBuilder = new EmbedBuilder();
				fieldCount = 0;
				page++;
				i--;
			}
		}
		return pages;
	}
}
