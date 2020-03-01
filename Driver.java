
public class Driver 
{

	public static void main(String[] args) 
	{
		Thread TM = new Thread(new TM(5054, 3), "TM");
		
		Thread RM1 = new Thread(new RM(1, 5054), "RM1");
		Thread RM2 = new Thread(new RM(2, 5054), "RM2");
		Thread RM3 = new Thread(new RM(3, 5054), "RM3");
		
		TM.start();
		
		waitASec();
		
		RM1.start();
		
		waitASec();
		
		RM2.start();
		
		waitASec();
		
		RM3.start();
		
		waitASec();
	}
	
	public static void waitASec()
	{
		try
		{
			Thread.sleep(1000);
		}
		catch (Exception e)
		{
			;
		}
	}
}
