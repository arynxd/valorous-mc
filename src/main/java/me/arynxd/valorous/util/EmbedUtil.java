package me.arynxd.valorous.util;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import me.arynxd.valorous.Valorous;
import me.arynxd.valorous.entities.command.CommandEvent;
import me.arynxd.valorous.entities.config.ConfigOption;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class EmbedUtil
{
	private EmbedUtil() { }
	private static long errorCount = 0;

	public static void sendError(CommandEvent event, String message)
	{
		event.getChannel().sendMessage("Computer says no. (Error Code " + errorCount + ")\n\n https://youtu.be/kMWW3iJeBu0").queue();

		MessageChannel channel = event.getValorous().getJDA().getTextChannelById(event.getValorous().getConfigHandler().getString(ConfigOption.ERRORCHANNEL));
		if(channel != null)
		{
			channel.sendMessage(new EmbedBuilder()
					.setDescription(message + " (**#" + errorCount + "**)")
					.setColor(Color.RED)
					.build()).queue();
			errorCount++;
		}
	}

	public static void sendError(MessageChannel channel, Valorous valorous, String message)
	{
		channel.sendMessage("Computer says no. (Error Code " + errorCount + ")\n\n https://youtu.be/kMWW3iJeBu0").queue();

		MessageChannel errorChannel = valorous.getJDA().getTextChannelById(valorous.getConfigHandler().getString(ConfigOption.ERRORCHANNEL));
		if(errorChannel != null)
		{
			errorChannel.sendMessage(new EmbedBuilder()
					.setDescription(message + " (#**" + errorCount + "**)")
					.setColor(Color.RED)
					.build()).queue();
			errorCount++;
		}
	}

	public static void sendSuccess(MessageChannel channel, String message)
	{
		channel.sendMessage(new EmbedBuilder()
				.setDescription(message)
				.setColor(Color.GREEN)
				.build()).delay(10, TimeUnit.SECONDS)
				.flatMap(Message::delete).queue(null, error -> {});
	}
}
