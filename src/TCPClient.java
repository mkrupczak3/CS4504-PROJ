import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.Enumeration;

public class TCPClient {
  public static void main(String[] args) throws IOException {

    // Comments by: Mark Walker
    // The TCP Client is responsible for recieving data from the Server
    // The Client also sends data to the Server.
    
    // Variables for setting up connection and communication
    Socket Socket = null; // Socket to connect with ServerRouter
    PrintWriter out = null; // Writer to ServerRouter
    BufferedReader in = null; // Reading from ServerRouter
    //Paramaterization by Dillon
    String routerName; 
    String fileName = "file.txt";
    if(args.length>=1) {
      fileName = args[0];
    }
    else{
      System.err.println("Invalid or missing arguments.");
      System.exit(1);
    }
    //Checks if file is a .txt file
    String[] fileSplit = fileName.split(".");
    boolean isTxt = fileSplit[1].toLowerCase().equals("txt");

    if(args.length>=2){
      routerName = args[1]; // ServerRouter host name
    }
    else{
      routerName = "172.24.0.5";
    }
    String address;
    if(args.length>=3){
      address=args[2];
    }
    else{
      address = "172.23.0.6"; // destination IP (Server) 
    }
    int SockNum;
    if(args.length>=4){
      SockNum = Integer.parseInt(args[3]);
    }
    else {
      SockNum = 5555; // port number
    }
    String host = getAdapterAddr();
    
    // In order to send and recieve data from the Server, the Client
    // has to be able to connect to the ServerRouter, so there is
    // try-catch block in place in order to do so.
    try {
      Socket = new Socket(routerName, SockNum); //Thomas. Broadcasting on port 5555 for routername, trying to make connection to 'routername.'
      out = new PrintWriter(Socket.getOutputStream(), true); //Thomas. A way to send data to the router.
      in = new BufferedReader(new InputStreamReader(Socket.getInputStream())); //Thomas. A way to read data from the router.
    } catch (UnknownHostException e) { //Thomas. There is no router on port 5555 that matches 'routername.'
      System.err.println("Don't know about router: " + routerName);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to: " + routerName);
      System.exit(1);
    }

    // Variables for message passing
    Reader reader;
    BufferedReader fromFile;
    if(isTxt){
      reader = new FileReader(fileName); 
      fromFile = new BufferedReader(reader); // reader for the string file
    }
    else{
      File inFile = new File(fileName);
      byte[] fileByteArry = new byte[(int) inFile.length()]; 
      try {
        FileInputStream fileBytes = new FileInputStream(inFile);
        fileBytes.read(fileByteArry);
      } catch (Exception e) {
        System.err.println("File was empty or could not be read");
        System.exit(1);
      }
  }
    String fromServer; // messages received from ServerRouter
    String fromUser; // messages sent to ServerRouter
   
    long t0, t1, t; //Thomas. Variables for time calculation

    // Communication process (initial sends and receives)
    out.println(address); // Initial send (IP of the destination Server)
    fromServer = in.readLine(); // Initial receive from router (verification of connection)
    System.out.println("ServerRouter: " + fromServer);
    out.println(host); // Client sends the IP of its machine as initial send
    t0 = System.currentTimeMillis(); //Thomas. Initial time.
    out.println(fileName);//sends file extension type to Server

    // While there is data to be read in from the server,
    // print out lines to the Server via PrintWriter labeled "out."
    // Time is recorded when a line is read (assigned value t1) and used to display time cycle.
    // If the exit phrase is read, end the loop.
    while ((fromServer = in.readLine()) != null) {
      System.out.println("Server: " + fromServer);
      t1 = System.currentTimeMillis();
      if (fromServer.equals("Bye.")) { // exit statement
        break;
      }
      t = t1 - t0; //Thomas. Cycle Time
      if(isTxt){
        System.out.println("Cycle time: " + t);
        fromUser = fromFile.readLine(); // reading strings from a file
        if (fromUser != null) {
            System.out.println("Client: " + fromUser);
            out.println(fromUser); // sending the strings to the Server via ServerRouter
            t0 = System.currentTimeMillis();
        }
      }
      else{

      }
      
      
    } // End While

    // closing connections
    out.close();
    in.close();
    Socket.close();
  }
  public static String getAdapterAddr(){
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces.hasMoreElements()) {
          NetworkInterface ni = interfaces.nextElement();
          Enumeration<InetAddress> addresses = ni.getInetAddresses();
          while (addresses.hasMoreElements()) {
              InetAddress address = addresses.nextElement();
              if (!address.isLinkLocalAddress() && !address.isLoopbackAddress() && address.getAddress().length == 4) {
                  return address.getHostAddress();
              }
          }
      }
  } catch (SocketException e) {
      System.out.println("An error occurred: " + e.getMessage());
      System.exit(1);
  }
  return "";
  }
}
