/**
 * This is just a crappy driver for quick testing purposes
 * @author jonshaw
 *
 */
public class Driver 
{

	public static void main(String[] args) 
	{
		TM tm;
		Thread T0;

		try {
			
			// TM runs on its own thread
			tm = new TM(5054, 3);
			T0 = new Thread(tm, "TM");
			T0.start();

			// Each RM runs on its own thread
			RM rm1 = new RM(1, 5054);
			RM rm2 = new RM(2, 5054);
			RM rm3 = new RM(3, 5054);

			Thread t1 = new Thread(rm1, "RM1");
			Thread t2 = new Thread(rm2, "RM2");
			Thread t3 = new Thread(rm3, "RM3");

			// The RM start times are staggered
			waitASec();
			t1.start();
			waitASec();
			t2.start();
			waitASec();
			t3.start();

			// After 5 seconds, RM1 enters a "prepared" state and the magic begins.
			waitMS(5000);

			rm1.prepare();

		}
		catch (Exception e)
		{
			System.err.println("oopsie");
		}
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
	
	public static void waitMS(long ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (Exception e)
		{
			;
		}
	}
}
