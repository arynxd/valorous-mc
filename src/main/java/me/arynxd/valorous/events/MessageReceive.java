package me.arynxd.valorous.events;

import me.arynxd.valorous.Constants;
import me.arynxd.valorous.Valorous;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceive extends ListenerAdapter
{
	private final Valorous valorous;

	public MessageReceive(Valorous valorous)
	{
		this.valorous = valorous;
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		String content = event.getMessage().getContentRaw();

		if(!content.startsWith(Constants.DEFAULT_PREFIX))
		{
			return;
		}

		valorous.getCommandHandler().handleCommand(event);
	}
}
