import java.net.*;
import java.io.*;

public class RM implements Runnable
{
	private InetAddress host;
	private int id;
	private State st;
	
	private int destinationPort;
	private Socket sock;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	/**
	 * 
	 * @param x
	 * @param id
	 */
	public RM(int id, int port)
	{
		this.id = id;
		this.destinationPort = port;
		
		try
		{
			host = InetAddress.getLocalHost();
		}
		catch(UnknownHostException uhe)
		{
			uhe.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void run() 
	{
		try
		{
			sock = new Socket(host.getHostName(), destinationPort);
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
			
			changeState(State.WORKING);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			try 
			{
				oos.close();
				ois.close();
				sock.close();
			}
			catch (Exception e)
			{ ; }
		}
	}
	
	private void changeState(State s)
	{
		this.st = s;
		System.out.printf("(RM%d): State = %s%n", id, st);
	}
}
