package me.arynxd.valorous.commands;

import me.arynxd.valorous.entities.application.Application;
import me.arynxd.valorous.entities.command.Command;
import me.arynxd.valorous.entities.command.CommandEvent;
import me.arynxd.valorous.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;

@SuppressWarnings("unused")
public class AcceptCommand extends Command
{
	public AcceptCommand()
	{
		super("Accept", "Accepts the member onto the staff team.", "[none]");
		addAliases("accept");
		addMemberPermissions(Permission.ADMINISTRATOR);
		addSelfPermissions(Permission.MANAGE_CHANNEL);
	}

	@Override
	public void run(CommandEvent event)
	{
		Application application = event.getValorous().getApplicationHandler().getByChannelId(event.getChannel().getIdLong());

		if(application == null)
		{
			EmbedUtil.sendError(event, "There is no application associated with this channel.");
			return;
		}
		else if(application.isActive())
		{
			EmbedUtil.sendError(event, "You cannot close active applications.");
			return;
		}

		event.getValorous().getApplicationHandler().removeApplicationByChannelId(event.getChannel().getIdLong());
		event.getTextChannel().delete().queue(null, error -> {});
	}
}
