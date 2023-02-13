import java.io.*;
import java.net.*;

public class SThread extends Thread {
  private Object[][] RTable; // routing table Thomas. As each thread is created, the routing table is updated (shared memory?).
  private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
  private BufferedReader in; // reader (for reading from the machine connected to)
  private String inputLine, outputLine, destination, addr; // communication strings
  private Socket outSocket; // socket for communicating with a destination
  private int ind; // indext in the routing table
  public static SynchronizedRollingAverage lookupAverage = new SynchronizedRollingAverage();
  public static SynchronizedRollingAverage messageSizeAverage = new SynchronizedRollingAverage();

  // Constructor
  SThread(Object[][] Table, Socket toClient, int index) throws IOException {
    out = new PrintWriter(toClient.getOutputStream(), true); //A way to send data to a client/server
    in = new BufferedReader(new InputStreamReader(toClient.getInputStream())); //A way to receive data to client/server
    RTable = Table;
    addr = toClient.getInetAddress().getHostAddress();
    RTable[index][0] = addr; // IP addresses Thomas. Each connetion adding its info to the routing table
    RTable[index][1] = toClient; // sockets for communication
    ind = index;
  }

  // Run method (will run for each machine that connects to the ServerRouter)
  public void run() {
    try {
      // Initial sends/receives
      destination = in.readLine(); // initial read (the destination for writing)
      System.out.println("Forwarding to " + destination);
      out.println("Connected to the router."); // confirmation of connection

      // waits 10 seconds to let the routing table fill with all machines' information
      try {
        Thread.currentThread().sleep(10000);
      } catch (InterruptedException ie) {
        System.out.println("Thread interrupted");
      }
      
      long t0,t1; // variable for router lookup times
      double t;
      t0=System.nanoTime(); // Initial time
      // loops through the routing table to find the destination Thomas. In other words, its pairing a client to a server or vice versa
      for (int i = TCPServerRouter.TABLE_ENTRIES-1; i >= 0; i--) {
        if (destination.equals((String) RTable[i][0])) {
          outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
          System.out.println("Found destination: " + destination);
          outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer  Thomas. Forwarding all communication to the newly paired client or server
          break;
        }
      }
      t1=System.nanoTime();
      t=(t1-t0)/1000000.0d; // Router table lookup time
      lookupAverage.addValue(t);
      // Communication loop
      while ((inputLine = in.readLine()) != null) {
        System.out.println("Client/Server said: " + inputLine);
        outputLine =
            inputLine; // passes the input from the machine to the output string for the destination
        messageSizeAverage.addValue(inputLine.length());
        System.out.println("Average message size: " + messageSizeAverage.getAverage());
        System.out.println("Average router lookup time: "+SThread.lookupAverage.getAverage());

        if (outSocket != null) {
          outTo.println(outputLine); // writes to the destination
        }

        if (inputLine.equals("Bye.")) { // exit statement
          break; //Thomas. termination of connection
        }
      } // end while
    } // end try
    catch (IOException e) {
      System.err.println("Could not listen to socket.");
      System.exit(1);
    }
  }
}
