package me.arynxd.valorous.commands;

import java.util.OptionalInt;
import me.arynxd.valorous.entities.command.Command;
import me.arynxd.valorous.entities.command.CommandEvent;
import me.arynxd.valorous.util.EmbedUtil;
import me.arynxd.valorous.util.Parser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;

@SuppressWarnings("unused")
public class ClearCommand extends Command
{
	public ClearCommand()
	{
		super("Clear", "Clears messages from the current channel.", "[amount {50}]");
		addAliases("clear", "purge");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addSelfPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	public void run(CommandEvent event)
	{
		if(event.getArgs().isEmpty())
		{
			EmbedUtil.sendError(event, "Enter an amount to clear");
			return;
		}

		MessageChannel channel = event.getChannel();
		OptionalInt amount = new Parser(event.getArgs().get(0), event).parseAsUnsignedInt();

		if(amount.isPresent())
		{
			if(amount.getAsInt() > 50)
			{
				EmbedUtil.sendError(event, "Enter an amount less than or equal to 50.");
				return;
			}

			channel.getIterableHistory()
					.takeAsync(amount.getAsInt() + 1)
					.thenAccept(messages ->
					{
						channel.purgeMessages(messages);
						EmbedUtil.sendSuccess(event.getChannel(), "Deleted " + (messages.size() - 1) + " messages.");
					});
		}
	}
}
