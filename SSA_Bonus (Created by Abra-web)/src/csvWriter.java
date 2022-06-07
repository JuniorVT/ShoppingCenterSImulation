import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class csvWriter {

    public static void writeToCSV(Sink si) throws IOException
    {
        FileWriter fw = new FileWriter("csv/output.csv");
        PrintWriter out = new PrintWriter(fw);

        String[] events = si.getEvents();
        int[] numbers = si.getNumbers();
        String[] stations = si.getStations();
        boolean[] serviceDesk = si.getServiceDesk();
        double[] times = si.getTimes();

        out.print("events");
        out.print(",");
        out.print("numbers");
        out.print(",");
        out.print("stations");
        out.print(",");
        out.print("serviceCustomer");
        out.print(",");
        out.println("times");

        for (int i = 0; i < events.length; i++) {
            out.print(events[i]);
            out.print(",");
            out.print(numbers[i]);
            out.print(",");
            out.print(stations[i]);
            out.print(",");
            out.print(serviceDesk[i]);
            out.print(",");
            out.println(times[i]);
        }

        out.flush();
        out.close();
        fw.close();
    }

}
