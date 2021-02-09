package me.arynxd.valorous.commands;

import java.util.concurrent.TimeUnit;
import me.arynxd.valorous.entities.command.Command;
import me.arynxd.valorous.entities.command.CommandEvent;
import me.arynxd.valorous.entities.database.Ticket;
import me.arynxd.valorous.util.EmbedUtil;

@SuppressWarnings("unused")
public class CloseCommand extends Command
{
	public CloseCommand()
	{
		super("Close", "Closes a ticket.", "[none]");
		addAliases("close");
	}

	@Override
	public void run(CommandEvent event)
	{
		Ticket ticket = Ticket.getByChannelId(event.getChannel().getIdLong(), event.getValorous());

		if(ticket == null)
		{
			EmbedUtil.sendError(event, "Could not find a ticket associated to this channel.");
			return;
		}

		ticket.close(event.getValorous());
		event.getTextChannel().delete().queueAfter(10, TimeUnit.SECONDS, null, error -> {});
		EmbedUtil.sendSuccess(event.getChannel(), "Ticket closed!");
	}
}
