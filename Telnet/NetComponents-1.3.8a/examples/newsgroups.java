/***
 * newsgroups.java,v 1.1 1997/11/24 18:20:14
 * Copyright 1997 Original Reusable Objects, Inc.  All rights reserved.
 ***/

import java.io.*;

import com.oroinc.net.nntp.*;
import com.oroinc.io.*;

/***
 * This is a trivial example using the NNTP package to approximate the
 * Unix newsgroups command.  It merely connects to the specified news
 * server and issues fetches the list of newsgroups stored by the server.
 * On servers that store a lot of newsgroups, this command can take a very
 * long time (listing upwards of 30,000 groups).
 * <p>
 * Copyright &#169 1997 Original Reusable Objects, Inc.
 * All rights reserved.
 ***/

public final class newsgroups {

  public final static void main(String[] args) {
    NNTPClient client;
    NewsgroupInfo[] list;

    if(args.length < 1) {
      System.err.println("Usage: newsgroups newsserver");
      System.exit(1);
    }

    client = new NNTPClient();

    try {
      client.connect(args[0]);

      list = client.listNewsgroups();

      if(list != null) {
	for(int i=0; i < list.length; i++)
	  System.out.println(list[i].getNewsgroup());
      } else {
	System.err.println("LIST command failed.");
	System.err.println("Server reply: " + client.getReplyString());
      }
    } catch(IOException e) {
      e.printStackTrace();
    } finally {
      try {
	if(client.isConnected())
	  client.disconnect();
      } catch(IOException e) {
	System.err.println("Error disconnecting from server.");
	e.printStackTrace();
	System.exit(1);
      }
    }

  }

}


