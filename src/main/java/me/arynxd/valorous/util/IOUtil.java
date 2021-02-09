package me.arynxd.valorous.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class IOUtil
{
	private IOUtil() { }

	public static InputStream getResourceFile(String fileName)
	{
		InputStream file;
		try
		{
			file = IOUtil.class.getClassLoader().getResourceAsStream(fileName);
		}
		catch(Exception exception)
		{
			return null;
		}
		return file;
	}

	public static InputStream getFromURL(String url)
	{
		try
		{
			return new URL(url).openStream();
		}
		catch(Exception exception)
		{
			return null;
		}
	}

	public static String convertToString(InputStream inputStream)
	{
		InputStreamReader isReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(isReader);

		StringBuilder stringBuilder = new StringBuilder();
		String str;
		try
		{
			while((str = reader.readLine()) != null)
			{
				stringBuilder.append(str);
			}
		}
		catch(Exception exception)
		{
			return "";
		}

		return stringBuilder.toString();
	}

	public static boolean isURL(String url)
	{
		try
		{
			URL obj = new URL(url);
			obj.toURI();
			return true;
		}
		catch(Exception exception)
		{
			return false;
		}
	}
}

