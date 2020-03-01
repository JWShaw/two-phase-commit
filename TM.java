import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class TM implements Runnable 
{
	private State st;
	private final int port;
	private int capacity;
	private ServerSocket server;
	
	private ArrayList<Socket> clientSocks;
	private ArrayList<ObjectInputStream> clientIn;
	private ArrayList<ObjectOutputStream> clientOut;
	
	/**
	 * Constructor for the transaction manager.
	 * @param port The port on which the TM is to listen.
	 * @param capacity The number of RMs that the TM should manage
	 */
	public TM(int port, int capacity)
	{
		this.port = port;
		this.capacity = capacity;
		
		this.clientSocks = new ArrayList<>(capacity);
		this.clientIn = new ArrayList<>(capacity);
		this.clientOut = new ArrayList<>(capacity);
		
		try
		{
			this.server = new ServerSocket(port);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{
		try 
		{
			changeState(State.INITIALIZING);
			openConnections();
			changeState(State.WORKING);
			
			// Listen for "Committed" message.
			while (st == State.WORKING)
			{
				for (ObjectInputStream ois : clientIn)
				{
					Message msg = (Message) ois.readObject();
					if (msg == Message.PREPARED)
					{
						changeState(State.PREPARING);
					};
				}
			}
			broadcast(Message.PREPARE);		
		}
		catch (Exception e) // Gotta catch 'em all! (Better exception handling to be added later.)
		{
			System.err.println("(TM): Something's gone wrong!");
			e.printStackTrace();
		}
		finally		// Connections are closed.
		{
			System.out.println("(TM): closing connections");
			closeConnections();
		}

	}
	
	/**
	 * Opens connections between the TM and the expected number of RMs.
	 * @throws IOException
	 */
	private void openConnections() throws IOException
	{
		System.out.println("(TM): Waiting for clients to connect...");
		while(clientSocks.size() < capacity)
		{
			Socket client = server.accept();
			clientSocks.add(client);
			clientIn.add(new ObjectInputStream(client.getInputStream()));
			clientOut.add(new ObjectOutputStream(client.getOutputStream()));
			System.out.printf("(TM): Client [%d/%d] connected.%n", clientSocks.size(), capacity);
		}
	}
	
	private void changeState(State s)
	{
		this.st = s;
		System.out.println("(TM): State = " + this.st);
	}
	
	private void broadcast(Message m) throws IOException
	{
		for (ObjectOutputStream oos : clientOut)
		{
			oos.writeObject(m);
		}
	}
	
	private void closeConnections()
	{
		try
		{
			for (ObjectInputStream ois : clientIn)
				ois.close();
			for (ObjectOutputStream oos : clientOut)
				oos.close();
			for (Socket s : clientSocks)
				s.close();
		}
		catch (Exception e)
		{
			
		}
	}
}
