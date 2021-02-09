package me.arynxd.valorous.commands;

import java.util.EnumSet;
import me.arynxd.valorous.Valorous;
import me.arynxd.valorous.entities.command.Command;
import me.arynxd.valorous.entities.command.CommandEvent;
import me.arynxd.valorous.entities.config.ConfigOption;
import me.arynxd.valorous.entities.database.Ticket;
import me.arynxd.valorous.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;

@SuppressWarnings("unused")
public class TicketCommand extends Command
{
	public TicketCommand()
	{
		super("Ticket", "Creates a new ticket.", "[none]");
		addAliases("ticket");
		addSelfPermissions(Permission.MANAGE_CHANNEL);
	}

	@Override
	public void run(CommandEvent event)
	{
		long userId = event.getAuthor().getIdLong();
		long managerId = Long.parseLong(event.getValorous().getConfigHandler().getString(ConfigOption.MANAGERROLE));
		Valorous valorous = event.getValorous();
		Category category = event.getGuild().getCategoryById(event.getValorous().getConfigHandler().getString(ConfigOption.TICKETSCATEGORY));

		if(category == null)
		{
			EmbedUtil.sendError(event, "There is no category setup for ticket channels, cannot continue.");
			return;
		}

		if(Ticket.hasTicket(userId, valorous))
		{
			EmbedUtil.sendError(event, "You already have an open ticket.");
			return;
		}

		category.createTextChannel("ticket-" + event.getAuthor().getName())
				.addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
				.addMemberPermissionOverride(event.getAuthor().getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_READ, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY), null)
				.addRolePermissionOverride(managerId, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_READ, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY), null)
				.queue(channel ->
				{
					Ticket.createTicket(userId, channel.getIdLong(), valorous);
					channel.sendMessage(event.getAuthor().getAsMention()).queue();
				});
	}
}
