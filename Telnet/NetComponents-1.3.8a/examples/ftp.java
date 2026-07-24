/***
 * ftp.java,v 1.12 1998/04/11 01:42:34
 * Copyright 1997-1998 Original Reusable Objects, Inc.  All rights reserved.
 ***/

import java.io.*;

import com.oroinc.net.ftp.*;

/***
 * This is an example program demonstrating how to use the FTPClient class.
 * This program connects to an FTP server and retrieves the specified
 * file.  If the -s flag is used, it stores the local file at the FTP server.
 * Just so you can see what's happening, all reply strings are printed.
 * If the -b flag is used, a binary transfer is assumed (default is ASCII).
 * <p>
 * Usage: ftp [-s] [-b] <hostname> <username> <password> <remote file> <local file>
 * <p>
 * Copyright &#169 1997-1998 Original Reusable Objects, Inc.
 * All rights reserved.
 ***/
public final class ftp {

  public static final String USAGE =
"Usage: ftp [-s] [-b] <hostname> <username> <password> <remote file> <local file>\n" +
"\nDefault behavior is to download a file and use ASCII transfer mode.\n" +
"\t-s store file on server (upload)\n" +
"\t-b use binary transfer mode\n";

  public static final void main(String[] args) {
    int base = 0;
    boolean storeFile = false, binaryTransfer = false, error = false;
    String server, username, password, remote, local;
    FTPClient ftp;

    for(base = 0; base < args.length; base++) {
      if(args[base].startsWith("-s"))
	storeFile = true;
      else if(args[base].startsWith("-b"))
	binaryTransfer = true;
      else
	break;
    }

    if((args.length - base) != 5) {
      System.err.println(USAGE);
      System.exit(1);
    }

    server   = args[base++];
    username = args[base++];
    password = args[base++];
    remote   = args[base++];
    local    = args[base];

    ftp = new FTPClient();
    ftp.addProtocolCommandListener(new PrintCommandListener(
				    new PrintWriter(System.out)));

    try {
      int reply;
      ftp.connect(server);
      System.out.println("Connected to " + server + ".");

      // After connection attempt, you should check the reply code to verify
      // success.
      reply = ftp.getReplyCode();

      if(!FTPReply.isPositiveCompletion(reply)) {
	ftp.disconnect();
	System.err.println("FTP server refused connection.");
	System.exit(1);
      }
    } catch(IOException e) {
      if(ftp.isConnected()) {
	try {
	  ftp.disconnect();
	} catch(IOException f) {
	  // do nothing
	}
      }
      System.err.println("Could not connect to server.");
      e.printStackTrace();
      System.exit(1);
    }

  __main:
    try {
      if(!ftp.login(username, password)) {
	ftp.logout();
	error = true;
	break __main;
      }

      System.out.println("Remote system is " + ftp.getSystemName());

      if(binaryTransfer)
	ftp.setFileType(FTP.BINARY_FILE_TYPE);

      if(storeFile) {
	InputStream input;

	input = new FileInputStream(local);
	ftp.storeFile(remote, input);
      } else {
	OutputStream output;

	output = new FileOutputStream(local);
	ftp.retrieveFile(remote, output);
      }

      ftp.logout();
    } catch(FTPConnectionClosedException e) {
      error = true;
      System.err.println("Server closed connection.");
      e.printStackTrace();
    } catch(IOException e) {
      error = true;
      e.printStackTrace();
    } finally {
      if(ftp.isConnected()) {
	try {
	  ftp.disconnect();
	} catch(IOException f) {
	  // do nothing
	}
      } 
    }

    System.exit(error ? 1 : 0);
  } // end main

}

