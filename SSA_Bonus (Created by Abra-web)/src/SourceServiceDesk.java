import statistics.PoissonDistribution;

import java.util.ArrayList;

/**
 *	A source of products
 *	This class implements CProcess so that it can execute events.
 *	By continuously creating new events, the source keeps busy.
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class SourceServiceDesk implements CProcess
{
	/** Eventlist that will be requested to construct events */
	private CEventList list;
	/** Queue that buffers products for the machine */
	private Queue queue;
	/** Name of the source */
	private String name;

	private PoissonDistribution poisson;


	/**
	*	Constructor, creates objects
	*        Interarrival times are exponentially distributed with mean 33
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*/
	public SourceServiceDesk(Queue q, CEventList l, String n)
	{
		list = l;
		queue = q;
		name = n;
		poisson = new PoissonDistribution(60*5);
		// put first event in list for initialization
		list.add(this,0,poisson.sample()); //target,type,time
	}
	
        @Override
	public void execute(int type, double tme)
	{
		// show arrival
		System.out.println("Service Customer Arrival at time = " + tme);
		// give arrived product to queue
		Product p = new Product(true);
		p.stamp(tme,"Creation",name);
		// only 1 queue, give it the product
		queue.giveProduct(p);
		// generate duration
		double duration = poisson.sample();
		// Create a new event in the eventlist
		list.add(this, 0, tme + duration); //target,type,time

	}


}