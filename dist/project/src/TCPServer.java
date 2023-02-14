import java.io.*;
import java.net.*;
import java.util.Base64;

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
      routerName = "172.23.0.5";
    }
    String address; // destination IP (Client). Thomas. This address is set up manually
    if(args.length>=2){
      address=args[1];
    }
    else{
      address = "172.23.0.7";
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

    // Communication process (initial sends/receives)
    out.println(address); // initial send (IP of the destination Client)
    fromClient = in.readLine(); // initial receive from router (verification of connection)

    System.out.println("ServerRouter: " + fromClient);

    String dummyThic = in.readLine();
    System.out.println(dummyThic);

    String fileName = in.readLine(); // NEW: added filename to header -Matthew
    Boolean isTxt = isTXT(fileName);

    System.out.println("fileName: " + fileName);
    System.out.println("isTxt: " + isTxt.toString());

    out.println("Ack");

    // Communication while loop
    while ((fromClient = in.readLine()) != null) {
      if (isTxt) { // txt mode

        System.out.println("Client said: " + fromClient);
        if (fromClient.trim().equals("Bye.")) {// exit statement
          break;
        }
        fromServer = fromClient.toUpperCase(); // converting received message to upper case
        System.out.println("Server said: " + fromServer);
        out.println(fromServer); // sending the converted message back to the Client via ServerRouter

      } else { // base64 payload mode

        if (fromClient.trim().equals("Bye.")) {
          System.out.println("Client said: Bye.");
          break;
        }
        String base64Payload = fromClient;
        byte[] fileByteArray = Base64.getDecoder().decode(base64Payload); // convert message from base64 text to original bytes
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
          fos.write(fileByteArray);
        } catch (IOException e) {
          System.err.println("IO error occured when trying to dump to file: " + e.getMessage());
          System.exit(1);
        }

        System.out.println("Server said: payload is A-OK kthx");
        // client expects something back for every line
        out.println("payload is A-OK kthx"); // kthx\nBye. -Matthew
      }
    }

    // closing connections
    out.close();
    in.close();
    Socket.close();
  }

  // returns true if fileName ends in .txt
  public static boolean isTXT(String fileName) {
    //Checks if file is a .txt file -Dillon
    String[] fileSplit = fileName.split("\\.");
    if (fileSplit.length <= 1 || !fileSplit[1].toLowerCase().equals("txt")) {
      return false;
    } else {
      return true;
    }
  }
}
