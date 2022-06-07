import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *	Example program for using eventlists
 *	@author Joel Karel
 *	@version %I%, %G%
 */

public class Simulation {

    public CEventList list;
    public Queue queue;
    public Source source;
    public Sink sink;
    public Machine mach;
	

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
    // Create an event list
	CEventList l = new CEventList();

    // A queue for the machine
    ArrayList<Queue> queues = new ArrayList<>();
    for (int i=0; i < 6; i++) {
        queues.add(new Queue());
    }
    Queue serviceDeskQueue = new Queue();

    // A source
	Source source_regular = new Source(queues,l,"Regular Customers Source", serviceDeskQueue);
    SourceServiceDesk source_service = new SourceServiceDesk(serviceDeskQueue,l,"Service Customers Source");

    // A sink
	Sink si = new Sink("Sink 1");

    // A machine
	Machine m1 = new Machine((Queue) queues.get(0),si,l,"Cash 1");
    Machine m2 = new Machine((Queue) queues.get(1),si,l,"Cash 2");
    Machine m3 = new Machine((Queue) queues.get(2),si,l,"Cash 3");
    Machine m4 = new Machine((Queue) queues.get(3),si,l,"Cash 4");
    Machine m5 = new Machine((Queue) queues.get(4),si,l,"Cash 5");
    Machine mServiceDesk = new Machine((Queue) queues.get(5),si,l,"Service Desk", serviceDeskQueue);

    // start the event list
	l.start(20000); // 2000 is maximum time

    csvWriter.writeToCSV(si);
    }


}
