package models;
import java.net.*;
import java.io.*;

public class RM implements Runnable
{
	private InetAddress host;
	private int id;
	private PState st;
	
	private int destinationPort;
	private Socket sock;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	/**
	 * Constructor for the RM
	 * @param id Identification number
	 * @param The port on which the TM is listening
	 */
	public RM(int id, int port) throws UnknownHostException
	{
		this.id = id;
		this.destinationPort = port;
		
		host = InetAddress.getLocalHost();
	}
	
	/**
	 * The magic happens here: listens for RM messages and responds accordingly.
	 */
	public void run() 
	{
		try
		{
			sock = new Socket(host.getHostName(), destinationPort);
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
			
			changeState(PState.WORKING);
			
			while (true)
			{
				Message m = null;
				try
				{
					m = (Message) ois.readObject();
				}
				catch (EOFException eofe) {;}
				//catch (NullPointerException npe) {;}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				if (m == Message.PREPARE)
					prepare();
				else if (m == Message.COMMIT)
				{
					commit();
					return;
				}
				else if (m == Message.ABORT)
					abort();
			}
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
	
	// Internal method to change RM state.
	private synchronized void changeState(PState s)
	{
		this.st = s;
		System.out.printf("(RM%d): State = %s%n", id, st);
	}
	
	// Sends a message to the TM.
	private synchronized void sendMessage(Message m) throws IOException
	{
		System.out.printf("(RM%d): Sending message %s to the TM%n", id, m);
		oos.writeObject(m);
	}
	
	// Puts the RM into a prepared state; notifies TM if necessary.
	public synchronized void prepare() throws IOException
	{
		if (st == PState.WORKING || st == PState.PREPARED)
		{
			changeState(PState.PREPARED);
			sendMessage(Message.PREPARED);
		}
		else
		{
			System.err.println("Error: RM" + id + " tried to prepare, "
					+ "but was already committed/aborted!");
		}
	}
	
	// Puts the RM into a committed state.
	private synchronized void commit() throws IOException
	{
		if (st == PState.PREPARED)
		{
			changeState(PState.COMMITTED);
		}
		else
		{
			System.err.println("Error: RM" + id + " tried to commit, "
					+ "but was not prepared!");
		}
	}
	
	// Puts the RM into an aborted state; notifies TM if necessary.
	public synchronized void abort() throws IOException
	{
		if (st == PState.WORKING)
		{
			changeState(PState.ABORTED);
			sendMessage(Message.ABORTED);
		}
		else if (st == PState.PREPARED)
		{
			changeState(PState.ABORTED);
		}
	}
}
