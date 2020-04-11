package models;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Simulation
{
	private ArrayList<RM> listofRMs;
	private TM theManager;
	private boolean locked;
	private int numRMs;
	
	public Simulation() throws IOException
	{
		listofRMs = new ArrayList<>();
		theManager = new TM(5055, 4);		//Port 5054; capacity 4--arbitrary!
		locked = false;
		
		Thread man = new Thread(theManager, "TM");
		man.start();
	}
	
	public void addRM() throws UnknownHostException
	{
		RM newRM = new RM(numRMs, theManager.getPort());
		listofRMs.add(newRM);
		numRMs++;
		Thread t = new Thread(newRM, String.format("%d", numRMs));
		t.start();
	}
	
	public RM getRM(int id)
	{
		return listofRMs.get(id);
	}
	
	public void prepare(int rmID) throws IOException
	{
		listofRMs.get(rmID).prepare();
	}
}
