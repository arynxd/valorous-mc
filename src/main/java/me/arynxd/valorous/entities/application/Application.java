package me.arynxd.valorous.entities.application;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import me.arynxd.valorous.Valorous;
import me.arynxd.valorous.entities.config.ConfigOption;
import me.arynxd.valorous.util.EmbedUtil;
import net.dv8tion.jda.api.entities.*;

public class Application
{
	private final User user;
	private final TextChannel channel;
	private final Map<ApplicationQuestion, String> answers = new LinkedHashMap<>();
	private final Valorous valorous;
	private boolean isActive = true;

	public Application(User user, TextChannel channel, Valorous valorous)
	{
		this.user = user;
		this.channel = channel;
		this.valorous = valorous;
	}

	public long getUserId()
	{
		return user.getIdLong();
	}

	public MessageChannel getChannel()
	{
		return channel;
	}

	public User getUser()
	{
		return user;
	}

	public void cancel()
	{
		channel.sendMessage(user.getAsMention() + " you application has been canceled due to inactivity. Please make a new application if you intend on applying.").queue();
		valorous.getApplicationHandler().removeApplicationByChannelId(channel.getIdLong());
		channel.delete().queueAfter(10, TimeUnit.SECONDS, null, error -> {});
	}

	public void addAnswer(ApplicationQuestion question, String answer)
	{
		synchronized(answers)
		{
			answers.put(question, answer);
		}
	}

	private String generateAnswers()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Application for user ").append(user.getAsMention()).append("\n\n");
		for(Map.Entry<ApplicationQuestion, String> entry : answers.entrySet())
		{
			sb.append("**")
					.append(entry.getKey().getShortQuestion())
					.append("**").append("\n")
					.append(entry.getValue())
					.append("\n\n");
		}
		return sb.toString();
	}

	public void finish()
	{
		isActive = false;
		MessageChannel applicationsChannel = valorous.getJDA().getTextChannelById(valorous.getConfigHandler().getString(ConfigOption.APPLICATIONSCHANNEL));
		if(applicationsChannel == null)
		{
			EmbedUtil.sendError(channel, valorous, "Applications channel was not configured correctly, cannot send my response there. Contact the admins to resolve this.");
			return;
		}

		Role managerRole = valorous.getJDA().getRoleById(valorous.getConfigHandler().getString(ConfigOption.MANAGERROLE));

		if(managerRole != null)
		{
			applicationsChannel.sendMessage(managerRole.getAsMention()).queue();
		}
		String answers = generateAnswers();

		while(true)
		{
			if(answers.length() >= Message.MAX_CONTENT_LENGTH)
			{
				applicationsChannel.sendMessage(answers.substring(0, Message.MAX_CONTENT_LENGTH))
						.allowedMentions(Collections.emptyList())
						.queue();

				answers = answers.substring(Message.MAX_CONTENT_LENGTH);
			}
			else
			{
				applicationsChannel.sendMessage(answers)
						.allowedMentions(Collections.emptyList())
						.queue();
				break;
			}
		}
	}

	public boolean isActive()
	{
		return isActive;
	}
}
