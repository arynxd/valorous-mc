package me.arynxd.valorous.handlers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import me.arynxd.valorous.Valorous;
import me.arynxd.valorous.entities.application.Application;
import me.arynxd.valorous.entities.application.ApplicationQuestion;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ApplicationHandler
{
	private final Valorous valorous;
	private final Map<Long, Application> applications = new ConcurrentHashMap<>();

	public ApplicationHandler(Valorous valorous)
	{
		this.valorous = valorous;
	}

	public void removeApplicationByChannelId(long channelId)
	{
		applications.remove(channelId);
	}

	public void addApplication(Application application)
	{
		application.getChannel().sendMessage(application.getUser().getAsMention()).queue();
		sendQuestion(application, ApplicationQuestion.ONE);
		applications.put(application.getChannel().getIdLong(), application);
	}

	public Application getByChannelId(long channelId)
	{
		return applications.get(channelId);
	}

	private void sendQuestion(Application application, ApplicationQuestion question)
	{
		List<ApplicationQuestion> questions = ApplicationQuestion.getQuestions();
		application.getChannel().sendMessage(question.getQuestion()).queue();
		valorous.getEventWaiter().waitForEvent(GuildMessageReceivedEvent.class,
				event ->
				{
					if(event.getAuthor().equals(application.getUser()))
					{
						return event.getChannel().equals(application.getChannel());
					}
					return false;
				},
				event ->
				{
					application.addAnswer(question, event.getMessage().getContentRaw());
					if(question.getIndice() + 1 >= ApplicationQuestion.getMaxValue())
					{
						application.getChannel().sendMessage("Your application has been submitted!").queue();
						application.finish();
						return;
					}
					sendQuestion(application, questions.get(question.getIndice() + 1));
				}, 10, TimeUnit.MINUTES, application::cancel);
	}
}
