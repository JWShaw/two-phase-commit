import java.net.*;
import java.io.*;

public class TM implements Runnable 
{
	private ServerSocket ss;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	@Override
	public void run() 
	{
		try
		{
			ss = new ServerSocket(5050);
			System.out.println("(server) Waiting for the Client Request");
			Socket sock = ss.accept();
			System.out.println("(server) Client connected.");
			
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
			
			while(true)
			{
				String message = (String) ois.readObject();
				System.out.println("((server) Message received: " + message);
				
				if (message.equals("Exit"))
				{
					System.out.print("(server) Closing the server!");
					ss.close();
					break;
				}
				
				Thread.sleep(1000);
				
				oos.writeObject("(server) Hi client!" + message);
			}
			
			ois.close();
			oos.close();
			sock.close();
		}
		catch (Exception e)
		{
			System.out.print("Something's gone wrong!");
			System.exit(0);
		}
	}
}
