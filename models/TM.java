package models;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class TM implements Runnable 
{
	private PState st;
	private int port;
	private int capacity;
	private ServerSocket server;
	
	private ArrayList<ClientConnection> clients;
	private LinkedBlockingQueue<Message> msgQueue;
	
	/**
	 * Constructor for the transaction manager.
	 * @param port The port on which the TM is to listen.
	 * @param capacity The number of RMs that the TM should manage
	 */
	public TM(int port, int capacity) throws IOException
	{
		this.capacity = capacity;
		this.port = port;
		clients = new ArrayList<>(capacity);
		msgQueue = new LinkedBlockingQueue<>();
		this.server = new ServerSocket(port);
	}
	
	public int getPort()
	{
		return port;
	}
	
	/**
	 * Runs on its own thread; implements the bulk of the algorithm.
	 * The content of this method will later be split into more methods,
	 * which will be called here.
	 */
	@Override
	public void run() 
	{
		try 
		{
			openConnections();

			while (st == PState.WORKING)
			{
				Message m = null;

				try 
				{
					m = msgQueue.take();
				}
				catch (NullPointerException npe)
				{
					;
				}
				
				if (m == Message.PREPARED)
				{
					changeState(PState.PREPARED);
				}
			}
			
			// Phase 1 of the algorithm: Get all 
			if (st == PState.PREPARED)
			{
				int preparedRMs = 0;
				broadcast(Message.PREPARE);
				
				while (preparedRMs < capacity)
				{
					Message m = null;
					
					try 
					{
						m = msgQueue.take();
					}
					catch (NullPointerException npe)
					{
						;
					}
					
					if (m == Message.PREPARED)
					{
						preparedRMs++;
					}
					else if (m == Message.ABORTED)
					{
						changeState(PState.ABORTED);
						broadcast(Message.ABORT);
						break;
					}
				}
				
				// Phase 2
				if (preparedRMs == capacity)
				{
					System.out.println("(TM): All RMs prepared.  Committing...");
					changeState(PState.COMMITTED);
					broadcast(Message.COMMIT);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			// All connections are closed; all threads terminated.
			System.out.println("(TM): Closing all connections.");
			for (ClientConnection c : clients)
			{
				try
				{
					c.close();
				}
				catch (Exception e)
				{ }
			}
		}
	}
	
	/**
	 * Opens connections between the TM and the expected number of RMs.
	 * @throws IOException
	 */
	private synchronized void openConnections() throws IOException
	{
		changeState(PState.INITIALIZING);
		System.out.printf("(TM): Waiting for clients to connect.%n");
		
		while (clients.size() < capacity)
		{
			ClientConnection newCon = new ClientConnection(server.accept());
			clients.add(newCon);
			System.out.printf("(TM): Client (%d/%d) connected.%n", 
					clients.size(), capacity);
		}
		changeState(PState.WORKING);
	}
	
	// Changes the state of the TM.
	private synchronized void changeState(PState s)
	{
		this.st = s;
		System.out.println("(TM): State = " + this.st);
	}
	
	// Broadcasts a given message to all connected RMs.
	private synchronized void broadcast(Message m) throws IOException
	{
		System.out.printf("(TM): Broadcasting message %s to all RMs%n", m);
		for (ClientConnection c : clients)
		{
			c.send(m);
		}
	}
	
	// Inner class to represent a connection to a RM
	class ClientConnection
	{
		Socket sock;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		boolean isOpen;
		
		ClientConnection(Socket sock) throws IOException
		{
			this.sock = sock;
			ois = new ObjectInputStream(sock.getInputStream());
			oos = new ObjectOutputStream(sock.getOutputStream());
			isOpen = true;
			
			/* A new thread listens for messages from the RM; 
			 * directs them to the message queue
			 */
			Thread listener = new Thread()
			{
				public void run()
				{
					while(isOpen)
					{
						try
						{
							Message m = (Message) ois.readObject();
							msgQueue.put(m);
						}
						catch (EOFException eof) {;}
						catch (SocketException se)
						{
							return;
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			};
			
			// setDaemon(true): ensures that this thread doesn't keep the JVM running
			listener.setDaemon(true);
			listener.start();
		}
		
		// Sends a message to the connected RM
		void send(Message m) throws IOException
		{
			oos.writeObject(m);
		}
		
		// Closes this connection
		void close() throws IOException
		{
			isOpen = false;
			ois.close();
			oos.close();
			sock.close();
		}
	}
}
