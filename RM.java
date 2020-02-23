import java.net.*;
import java.io.*;

public class RM implements Runnable
{
	private Socket sock;
	private InetAddress host;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	@Override
	public void run() 
	{
		try
		{
			host = InetAddress.getLocalHost();
			sock = new Socket(host.getHostName(), 5050);
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
			
			for (int i = 0; i < 5; i++)
			{
				System.out.println("(client) Sending request to Socket Server");
				oos.writeObject("" + i);
				
				Thread.sleep(500);
				
				String message = (String) ois.readObject();
				System.out.println("(client) Server says: " + message);
			}
			
			oos.writeObject("Exit");
			ois.close();
			oos.close();
		}
		catch (Exception e)
		{
			System.out.print("(client) Something's gone wrong! (RM)");
			System.exit(0);
		}
	}
}
