import statistics.LeftTruncatedNormalDistribution;

/**
 *	Machine in a factory
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class Machine implements CProcess,ProductAcceptor
{
	/** Product that is being handled  */
	private Product product;
	/** Eventlist that will manage events */
	private final CEventList eventlist;
	/** Queue from which the machine has to take products */
	private Queue queue;
	private Queue queue_sd;
	/** Sink to dump products */
	private ProductAcceptor sink;
	/** Status of the machine (b=busy, i=idle) */
	private char status;
	/** Machine name */
	private final String name;


	private LeftTruncatedNormalDistribution normal;
	private LeftTruncatedNormalDistribution normalServiceDesk;


	private final boolean serviceDesk;
	

	/**
	*	Constructor
	*        Service times are exponentially distributed with mean 30
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	 *
	*/
	public Machine(Queue q, ProductAcceptor s, CEventList e, String n)
	{
		status='i';
		queue=q;
		sink=s;
		eventlist=e;
		name=n;
		serviceDesk=false;
		normal = new LeftTruncatedNormalDistribution((2.6*60), (1.1*60), 1);
		normalServiceDesk = null;


		queue.askProduct(this);
	}

	public Machine(Queue q, ProductAcceptor s, CEventList e, String n, Queue sd)
	{
		status='i';
		queue=q;
		sink=s;
		eventlist=e;
		name=n;
		queue_sd = sd;
		serviceDesk = true;
		normal = new LeftTruncatedNormalDistribution((2.6*60), (1.1*60), 1);
		normalServiceDesk = new LeftTruncatedNormalDistribution((4.1*60), (1.1*60), 1);

		// first ask for product from service desk, if that does not work, ask for product from cash queue
		if (!queue_sd.askProduct(this)){
			queue.askProduct(this);
		}
	}


	/**
	*	Method to have this object execute an event
	*	@param type	The type of the event that has to be executed
	*	@param tme	The current time
	*/
	public void execute(int type, double tme)
	{
		// show arrival
		System.out.println("Product finished at time = " + tme);
		// Remove product from system
		product.stamp(tme,"Production complete",name);
		sink.giveProduct(product);
		product=null;
		// set machine status to idle
		status='i';

		if (serviceDesk) {
			// first ask for product from service desk, if that does not work, ask for product from cash queue
			if (!queue_sd.askProduct(this)){
				queue.askProduct(this);
			}
		} else {
			queue.askProduct(this);
		}
	}
	
	/**
	*	Let the machine accept a product and let it start handling it
	*	@param p	The product that is offered
	*	@return	true if the product is accepted and started, false in all other cases
	*/
        @Override
	public boolean giveProduct(Product p)
	{
		// Only accept something if the machine is idle
		if(status=='i')
		{
			// accept the product
			product=p;
			// mark starting time
			product.stamp(eventlist.getTime(),"Production started",name);
			// start production
			startProduction();
			// Flag that the product has arrived
			return true;
		}
		// Flag that the product has been rejected
		else return false;
	}
	
	/**
	*	Starting routine for the production
	*	Start the handling of the current product with an exponentionally distributed processingtime with average 30
	*	This time is placed in the eventlist
	*/
	private void startProduction()
	{
		double duration = 0;
		if (product.isServiceDeskClient()){
			 duration = normalServiceDesk.sample();
		} else {
			 duration = normal.sample();
		}
		// Create a new event in the eventlist
		double tme = eventlist.getTime();
		eventlist.add(this,0,tme+duration); //target,type,time
		// set status to busy
		status='b';
	}

}