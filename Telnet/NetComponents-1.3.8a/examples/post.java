/***
 * post.java,v 1.3 1998/03/15 21:00:22
 * Copyright 1997 Original Reusable Objects, Inc.  All rights reserved.
 ***/

import java.io.*;

import com.oroinc.net.nntp.*;
import com.oroinc.io.*;

/***
 * This is an example program using the NNTP package to post an article
 * to the specified newsgroup(s).  It prompts you for header information and
 * a filename to post.
 * <p>
 * Copyright &#169 1997 Original Reusable Objects, Inc.
 * All rights reserved.
 ***/

public final class post {

  public final static void main(String[] args) {
    String from, subject, newsgroup, filename, server;
    BufferedReader stdin;
    FileReader fileReader = null;
    SimpleNNTPHeader header;
    NNTPClient client;

    if(args.length < 1) {
      System.err.println("Usage: post newsserver");
      System.exit(1);
    }

    server = args[0];

    stdin = new BufferedReader(new InputStreamReader(System.in));

    try {
      System.out.print("From: ");
      System.out.flush();

      from = stdin.readLine();

      System.out.print("Subject: ");
      System.out.flush();

      subject = stdin.readLine();

      header = new SimpleNNTPHeader(from, subject);

      System.out.print("Newsgroup: ");
      System.out.flush();

      newsgroup = stdin.readLine();
      header.addNewsgroup(newsgroup);

      while(true) {
	System.out.print("Additional Newsgroup <Hit enter to end>: ");
	System.out.flush();

	// Of course you don't want to do this because readLine() may be null
	newsgroup = stdin.readLine().trim();

	if(newsgroup.length() == 0)
	  break;

	header.addNewsgroup(newsgroup);
      }

      System.out.print("Filename: ");
      System.out.flush();

      filename = stdin.readLine();

      try {
	fileReader = new FileReader(filename);
      } catch(FileNotFoundException e) {
	System.err.println("File not found. " + e.getMessage());
      }

      client = new NNTPClient();
      client.addProtocolCommandListener(new PrintCommandListener(
					 new PrintWriter(System.out)));

      client.connect(server);

      if(!NNTPReply.isPositiveCompletion(client.getReplyCode())) {
	client.disconnect();
	System.err.println("NNTP server refused connection.");
	System.exit(1);
      }

      if(client.isAllowedToPost()) {
	Writer writer = client.postArticle();

	if(writer != null) {
	  writer.write(header.toString());
	  Util.copyReader(fileReader, writer);
	  writer.close();
	  client.completePendingCommand();
	}
      }

      fileReader.close();

      client.logout();

      client.disconnect();
    } catch(IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}


