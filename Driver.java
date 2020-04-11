import models.RM;
import models.Simulation;
import models.TM;

/**
 * This is just a crappy driver for quick testing purposes
 * @author jonshaw
 *
 */
public class Driver 
{

	public static void main(String[] args) 
	{
		try
		{
			Simulation s1 = new Simulation();
			
			s1.addRM();
			waitASec();
			s1.addRM();
			waitASec();
			s1.addRM();
			waitASec();
			s1.addRM();
			waitASec();
			s1.prepare(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
