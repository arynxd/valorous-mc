package me.arynxd.valorous.commands;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import me.arynxd.valorous.entities.command.Command;
import me.arynxd.valorous.entities.command.CommandEvent;
import me.arynxd.valorous.entities.config.ConfigOption;
import me.arynxd.valorous.entities.database.Ticket;
import me.arynxd.valorous.util.EmbedUtil;
import net.dv8tion.jda.api.entities.MessageChannel;

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

		EmbedUtil.sendSuccess(event.getChannel(), "Ticket closed! Generating transcripts.");

		event.getTextChannel().getIterableHistory().takeUntilAsync(Objects::isNull).thenAccept(messages ->
		{
			event.getTextChannel().delete().queueAfter(10, TimeUnit.SECONDS, null, error -> {});
			StringBuilder transcript = new StringBuilder();
			messages.stream()
					.filter(message -> !message.getAuthor().isBot())
					.forEach(message -> transcript
							.append("{ ")
							.append(message.getAuthor().getAsTag())
							.append(" / ")
							.append(message.getAuthor().getId())
							.append(" }")
							.append("  ->  ")
							.append(message.getContentDisplay())
							.append("\n"));

			InputStream file = new ByteArrayInputStream(transcript.toString().getBytes(StandardCharsets.UTF_8));
			MessageChannel errorChannel = event.getValorous().getJDA().getTextChannelById(event.getValorous().getConfigHandler().getString(ConfigOption.ERRORCHANNEL));
			if(errorChannel != null)
			{
				errorChannel
						.sendMessage("Transcripts for ticket " + ticket.getChannelId() + " started by user " + "<@" + ticket.getUserId() + ">")
						.allowedMentions(Collections.emptyList())
						.addFile(file, "transcript--" + ticket.getUserId() + ".txt").queue();
			}
		});
	}
}
