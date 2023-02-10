import java.io.*;
import java.net.*;

public class TCPServer {
  public static void main(String[] args) throws IOException {

    // Variables for setting up connection and communication
    Socket Socket = null; // socket to connect with ServerRouter
    PrintWriter out = null; // for writing to ServerRouter
    BufferedReader in = null; // for reading form ServerRouter
    InetAddress addr = InetAddress.getLocalHost();
    String host = addr.getHostAddress(); // Server machine's IP
    //Paramaterization by Dillon
    String routerName; 
    if(args.length>=1) {
      routerName = args[0]; // ServerRouter host name
    }
    else{
      routerName = "172.20.0.5";
    }
    int SockNum;
    if(args.length>=3){
      SockNum = Integer.parseInt(args[2]);  
    }
    else {
      SockNum = 5555; // port number
    }

    // Tries to connect to the ServerRouter
    try {
      Socket = new Socket(routerName, SockNum); //Thomas. Broadcast on port 5555 for 'routername.'
      out = new PrintWriter(Socket.getOutputStream(), true); //Thomas. A way to send data to the router.
      in = new BufferedReader(new InputStreamReader(Socket.getInputStream())); //Thomas. A way to receive data from router.
    } catch (UnknownHostException e) { //Thomas. There is no router called, 'routername' on port 5555
      System.err.println("Don't know about router: " + routerName);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to: " + routerName);
      System.exit(1);
    }

    // Variables for message passing
    String fromServer; // messages sent to ServerRouter
    String fromClient; // messages received from ServerRouter
    String address;
    if(args.length>=2){
      address=args[1];
    }
    else{
      address = "172.20.0.7"; // destination IP (Client). Thomas. This address is set up manually
    }

    // Communication process (initial sends/receives)
    out.println(address); // initial send (IP of the destination Client)
    fromClient = in.readLine(); // initial receive from router (verification of connection)
    System.out.println("ServerRouter: " + fromClient);

    // Communication while loop
    while ((fromClient = in.readLine()) != null) {
      System.out.println("Client said: " + fromClient);
      if (fromClient.equals("Bye.")) {// exit statement
        break;
      }
      fromServer = fromClient.toUpperCase(); // converting received message to upper case
      System.out.println("Server said: " + fromServer);
      out.println(fromServer); // sending the converted message back to the Client via ServerRouter
    }

    // closing connections
    out.close();
    in.close();
    Socket.close();
  }
}
