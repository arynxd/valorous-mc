package me.arynxd.valorous;

import javax.security.auth.login.LoginException;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			Valorous valorous = new Valorous();
			valorous.build();
		}
		catch(LoginException exception)
		{
			System.out.println("Invalid token provided, cannot continue.");
		}
		catch(InterruptedException exception)
		{
			System.out.println("Bot was interrupted during boot, please try again.");
		}
	}
}
