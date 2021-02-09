package me.arynxd.valorous.commands;

import java.util.EnumSet;
import me.arynxd.valorous.entities.application.Application;
import me.arynxd.valorous.entities.command.Command;
import me.arynxd.valorous.entities.command.CommandEvent;
import me.arynxd.valorous.entities.config.ConfigOption;
import me.arynxd.valorous.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;

@SuppressWarnings("unused")
public class ApplyCommand extends Command
{
	public ApplyCommand()
	{
		super("Application.", "Apply for higher positions.", "[none]");
		addAliases("apply");
	}

	@Override
	public void run(CommandEvent event)
	{
		Category category = event.getGuild().getCategoryById(event.getValorous().getConfigHandler().getString(ConfigOption.APPLICATIONSCATEGORY));
		if(category == null)
		{
			EmbedUtil.sendError(event, "There is no category setup for application channels, cannot continue.");
			return;
		}

		category.createTextChannel("apply-" + event.getAuthor().getName())
				.addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
				.addMemberPermissionOverride(event.getAuthor().getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_READ, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY), null)
				.addRolePermissionOverride(776525052472590346L, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_READ, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY), null)
				.queue(channel -> event.getValorous().getApplicationHandler().addApplication(new Application(event.getAuthor(), channel, event.getValorous())));

	}
}
