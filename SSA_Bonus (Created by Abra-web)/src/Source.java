import java.util.ArrayList;
import java.util.Arrays;

import statistics.PoissonDistribution;

/**
 *	A source of products
 *	This class implements CProcess so that it can execute events.
 *	By continuously creating new events, the source keeps busy.
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class Source implements CProcess
{
	/** Eventlist that will be requested to construct events */
	private CEventList list;
	/** Queue that buffers products for the machine */
	private ArrayList<Queue> queues;
	private Queue serviceDeskQueue;
	/** Name of the source */
	private String name;

	private PoissonDistribution poisson;

	/**
	 *	Custom Constructor including "Service Desk Queue"
	 *
	 *
	 *	@param qs	Array of receiverw of the products
	 *	@param l	The eventlist that is requested to construct events
	 *	@param n	Name of object
	 */
	public Source(ArrayList<Queue> qs, CEventList l, String n, Queue sd_queue)
	{
		list = l;
		queues = qs;
		serviceDeskQueue = sd_queue;
		name = n;
		poisson = new PoissonDistribution(60);
		list.add(this,0,poisson.sample());
	}


	/**
	*	Constructor, creates objects
	*        Interarrival times are exponentially distributed with mean 33
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*/
	public Source(Queue q,CEventList l,String n)
	{
		list = l;
		queues.add(q);
		name = n;
		poisson = new PoissonDistribution(60);
		// put first event in list for initialization
		list.add(this,0,poisson.sample()); //target,type,time
	}
	
        @Override
	public void execute(int type, double tme)
	{
		// show arrival
		System.out.println("Normal Customer arrival at time = " + tme);
		// give arrived product to queue
		Product p = new Product();
		p.stamp(tme,"Creation",name);
		if (queues.size() == 1) {
			// only 1 queue, give it the product
			queues.get(0).giveProduct(p);
			// generate duration
			double duration = poisson.sample();
			// Create a new event in the eventlist
			list.add(this, 0, tme + duration); //target,type,time
		} else {
			// there are many queues, need to find where is it best to put the product
			int shortest_queue = findShortestOpenQueue();
			queues.get(shortest_queue).giveProduct(p);
			// generate duration
			double duration = poisson.sample();
			// Create a new event in the eventlist
			list.add(this, 0, tme + duration); //target,type,time
		}
	}

	/**
	 * @return
	 */
	private int findShortestOpenQueue()
	{
		ArrayList<Integer> openQueues = getOpenQueues();
		int minLength = 99999;
		int minLengthID = 99999;
		for (int i=0; i < openQueues.size(); i++) {
			int currentLength = queues.get(openQueues.get(i)).getLegnth();
			// since this is the service desk cash queue, take into account service only customers
			if (openQueues.get(i) == 5){
				currentLength = currentLength + serviceDeskQueue.getLegnth();
			}
			if (currentLength < minLength) {
				minLength = currentLength;
				minLengthID = openQueues.get(i);
			}
		}
		System.out.format("Shortest queue is %d of length %d %n", minLengthID, minLength);
		return minLengthID;
	}

	/**
	 * @return
	 */

	private ArrayList<Integer> getOpenQueues() {
		ArrayList<Integer> openQueues = new ArrayList<>();
		openQueues.add(0);	// always open
		openQueues.add(1);	// always open
		openQueues.add(5);	// always open

		// if there is a costumer in a queue then its open, starting from the second queue,
		// and ignoring the last one because its the service desk regular customers, its always open
		for (int i=2; i < queues.size()-1; i++) {
			if (queues.get(i).getLegnth() > 0){
				System.out.format("%d is open because costumer in it %n", i);
				openQueues.add(i);
			}
		}

		// if all open queues have at least 4 costumers, open a new queue, if available
		boolean allOpenHaveMoreThan4 = true;

		for (int i=0; i < openQueues.size(); i++) {
			int queueLength = queues.get(openQueues.get(i)).getLegnth();
			// if it's the service desk queue, take into account service customers
			if (openQueues.get(i) == 5) {
				queueLength = queueLength + serviceDeskQueue.getLegnth();
			}
			if (queueLength < 4) {
				allOpenHaveMoreThan4 = false;
			}
		}

		boolean	openedNewOne = false;
		if (allOpenHaveMoreThan4) {
			System.out.println("All open queues are full");

			if (!openQueues.contains(2) && !openedNewOne){
				openQueues.add(2);
				openedNewOne = true;
			}
			if (!openQueues.contains(3) && !openedNewOne){
				openQueues.add(3);
				openedNewOne = true;
			}
			if (!openQueues.contains(4) && !openedNewOne){
				openQueues.add(4);
			}
		}

		// reorder queue 5 (which is the cash queue of the service desk) to the end so its least priority
		openQueues.removeAll(Arrays.asList(5));
		openQueues.add(5);

		return openQueues;
	}


}