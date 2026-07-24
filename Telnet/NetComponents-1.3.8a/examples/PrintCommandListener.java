/***
 * PrintCommandListener.java,v 1.1 1998/03/15 21:00:22
 * Copyright 1997-1998 Original Reusable Objects, Inc.  All rights reserved.
 ***/

import java.io.*;

import com.oroinc.net.*;

/***
 * This is a support class for some of the example programs.  It is
 * a sample implementation of the ProtocolCommandListener interface
 * which just prints out to a specified stream all command/reply traffic.
 * <p>
 * Copyright &#169 1997-1998 Original Reusable Objects, Inc.
 * All rights reserved.
 ***/

public class PrintCommandListener implements ProtocolCommandListener {
  private PrintWriter __writer;

  public PrintCommandListener(PrintWriter writer) {
    __writer = writer;
  }

  public void protocolCommandSent(ProtocolCommandEvent event) {
    __writer.print(event.getMessage());
    __writer.flush();
  }

  public void protocolReplyReceived(ProtocolCommandEvent event) {
    __writer.print(event.getMessage());
    __writer.flush();
  }
}
