package me.arynxd.valorous.entities.application;

import java.util.List;

public enum ApplicationQuestion
{
	ONE("What role are you applying for?\n\n" +
			"1. Junior Helper\n" +
			"2. Helper\n" +
			"3. Junior Moderator\n" +
			"4. Moderator\n" +
			"5. Senior Moderator\n" +
			"6. Administrator\n" +
			"7. Developer\n" +
			"8. Builder\n" +
			"9. Manager", "What role are you applying for?", 0),

	TWO("Why do you want to be staff?", "Why do you want to be staff?", 1),
	THREE("What is your timezone?", "What is your timezone?", 2),
	FOUR("You MUST NOT be staff on other Minecraft servers. Is this okay?", "Do you moderate any other servers?", 3),
	FIVE("Do you have a working microphone?", "Do you have a working microphone?", 4),
	SIX("How old are you?", "How old are you?", 5),
	SEVEN("What is your previous experience as a staff?", "What is your previous experience as a staff?", 6),
	EIGHT("If so, please provide evidence of this staffing ", "Other staffing evidence?", 7),
	NINE("Why do you think YOU would be the fit that our team needs?", "Why do you think YOU would be the fit that our team needs?", 8),
	TEN("How many times have you applied already?", "How many times have you applied already?", 9),
	ELEVEN("What is your current rank on the server?", "What is your current rank on the server?", 10),
	TWELVE("How long have you been playing on our server?", "For how long have you been playing on our server?", 11),
	THIRTEEN("Are you multilingual?", "Are you multilingual?", 12),
	FOURTEEN("What do you like to do in/outside Minecraft?", "What do you like to do in/outside Minecraft?", 13),
	FIFTEEN("Is there anything else we should know?", "Is there anything else we should know?", 14);


	private final String question;
	private final int indice;
	private final String shortQuestion;

	ApplicationQuestion(String question, String shortQuestion, int indice)
	{
		this.question = question;
		this.shortQuestion = shortQuestion;
		this.indice = indice;
	}

	public static List<ApplicationQuestion> getQuestions()
	{
		return List.of(values());
	}

	public static int getMaxValue()
	{
		return values().length;
	}

	public int getIndice()
	{
		return indice;
	}

	public String getShortQuestion()
	{
		return shortQuestion;
	}

	public String getQuestion()
	{
		return question;
	}
}
