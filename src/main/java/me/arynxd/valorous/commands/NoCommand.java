package me.arynxd.valorous.commands;

import me.arynxd.valorous.entities.command.Command;
import me.arynxd.valorous.entities.command.CommandEvent;

@SuppressWarnings("unused")
public class NoCommand extends Command
{
	public NoCommand()
	{
		super("No", "No.", "[none]");
		addAliases("no");
	}

	@Override
	public void run(CommandEvent event)
	{
		event.getChannel().sendMessage("Computer says no.\n\n https://youtu.be/kMWW3iJeBu0").queue();
	}
}
