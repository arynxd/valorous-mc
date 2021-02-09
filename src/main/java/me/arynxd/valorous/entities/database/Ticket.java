package me.arynxd.valorous.entities.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import me.arynxd.valorous.Valorous;

public class Ticket
{
	private final long userId;
	private final long channelId;

	private Ticket(long userId, long channelId)
	{
		this.userId = userId;
		this.channelId = channelId;
	}

	public static Ticket getByChannelId(long channelId, Valorous valorous)
	{
		try(Connection connection = valorous.getDatabaseHandler().getConnection())
		{
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM tickets WHERE channel_id = ?");
			ps.setLong(1, channelId);

			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				return new Ticket(rs.getLong(2), rs.getLong(1));
			}
			return null;
		}
		catch(Exception exception)
		{
			System.out.println("An SQL error occurred");
			exception.printStackTrace();
			return null;
		}
	}

	public static boolean hasTicket(long userId, Valorous valorous)
	{
		try(Connection connection = valorous.getDatabaseHandler().getConnection())
		{
			PreparedStatement check = connection.prepareStatement("SELECT COUNT(*) FROM tickets WHERE user_id = ?");
			check.setLong(1, userId);
			ResultSet rs = check.executeQuery();
			return rs.next() && rs.getInt(1) > 0;
		}
		catch(Exception exception)
		{
			System.out.println("An SQL error occurred");
			exception.printStackTrace();
			return false;
		}
	}

	public static void createTicket(long userId, long channelId, Valorous valorous)
	{
		try(Connection connection = valorous.getDatabaseHandler().getConnection())
		{
			if(hasTicket(userId, valorous))
			{
				return;
			}

			PreparedStatement ps = connection.prepareStatement("INSERT INTO tickets (user_id, channel_id) VALUES (?, ?);");
			ps.setLong(1, userId);
			ps.setLong(2, channelId);
			ps.executeUpdate();
		}
		catch(Exception exception)
		{
			System.out.println("An SQL error occurred");
			exception.printStackTrace();
		}
	}

	public long getUserId()
	{
		return userId;
	}

	public long getChannelId()
	{
		return channelId;
	}

	public void close(Valorous valorous)
	{
		try(Connection connection = valorous.getDatabaseHandler().getConnection())
		{
			PreparedStatement ps = connection.prepareStatement("DELETE FROM tickets WHERE channel_id = ?;");
			ps.setLong(1, channelId);
			ps.execute();
		}
		catch(Exception exception)
		{
			System.out.println("An SQL error occurred");
			exception.printStackTrace();
		}
	}
}
