/***
 * daytime.java,v 1.3 1997/11/02 05:57:46
 * Copyright 1997 Original Reusable Objects, Inc.  All rights reserved.
 ***/

import java.io.*;
import java.net.*;

import com.oroinc.net.*;

/***
 * This is an example program demonstrating how to use the DaytimeTCP
 * and DaytimeUDP classes.
 * This program connects to the default daytime service port of a
 * specified server, retrieves the daytime, and prints it to standard output.
 * The default is to use the TCP port.  Use the -udp flag to use the UDP
 * port.
 * <p>
 * Usage: daytime [-udp] <hostname>
 * <p>
 * Copyright &#169 1997 Original Reusable Objects, Inc.
 * All rights reserved.
 ***/
public final class daytime {

  public static final void daytimeTCP(String host) throws IOException {
    DaytimeTCPClient client = new DaytimeTCPClient();

    // We want to timeout if a response takes longer than 60 seconds
    client.setDefaultTimeout(60000);
    client.connect(host);
    System.out.println(client.getTime().trim());
    client.disconnect();
  }

  public static final void daytimeUDP(String host) throws IOException {
    DaytimeUDPClient client = new DaytimeUDPClient();

    // We want to timeout if a response takes longer than 60 seconds
    client.setDefaultTimeout(60000);
    client.open();
    System.out.println(client.getTime(InetAddress.getByName(host)).trim());
    client.close();
  }


  public static final void main(String[] args) {

    if(args.length == 1) {
      try {
	daytimeTCP(args[0]);
      } catch(IOException e) {
	e.printStackTrace();
	System.exit(1);
      }
    } else if(args.length == 2 && args[0].equals("-udp")) {
      try {
	daytimeUDP(args[1]);
      } catch(IOException e) {
	e.printStackTrace();
	System.exit(1);
      }
    } else {
      System.err.println("Usage: daytime [-udp] <hostname>");
      System.exit(1);
    }

  }

}

