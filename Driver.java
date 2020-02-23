
public class Driver 
{

	public static void main(String[] args) 
	{
		Thread dobT = new Thread(new RM(), "Dobson");
		Thread bobT = new Thread(new TM(), "Bob");
		
		Message m = Message.ABORT;
		
		System.out.println(m);
		
		dobT.start();
		bobT.start();
	}
}
