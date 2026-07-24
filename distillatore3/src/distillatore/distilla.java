package distillatore;

import java.io.*;
import javax.swing.UIManager;
import java.awt.*;
import java.util.*;

/**
 * <p>Title: Distillatore</p>
 * <p>Description: Distilla le informazioni essenziali contenute nei comandi Show dell'IOS</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Lutech SPA</p>
 * @author Faustino Palma
 * @version 1.1
 */

public class distilla {
  boolean packFrame = false;

  //Construct the application
  public distilla() {
    Frame1 frame = new Frame1();
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame) {
      frame.pack();
    }
    else {
      frame.validate();
    }
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation( (screenSize.width - frameSize.width) / 2,
                      (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }

  //Main method
  public static void main(String[] args) {
/*    GregorianCalendar dataAttuale = new GregorianCalendar();
    int anno = 2003;
    int mese = 3 - 1;
    int giorno = 28;
    GregorianCalendar dataScadenza = new GregorianCalendar(anno, mese, giorno);
    //--------------------------------------------------------------------------
        BufferedReader bibi = null;
        try {
          bibi = new BufferedReader( new FileReader(".\\distillatore.jar.bibi.txt"));
        } catch (FileNotFoundException err) {
          System.err.println(err);
        }
        String bibiTest= "";
        try {
          bibiTest = bibi.readLine();
        } catch (IOException err) {
          System.err.println(err);
        }
        System.out.println(bibiTest);
        boolean bibiTestBool = bibiTest.compareTo("non ancora")==0;
    //--------------------------------------------------------------------------
    BufferedWriter bibiW = null;
    try {
      bibiW = new BufferedWriter(new FileWriter(".\\distillatore.jar"));
    }
    catch (FileNotFoundException err) {
      System.err.println(err);
    }
    catch (IOException err) {
      System.err.println(err);
    }
    //--------------------------------------------------------------------------
    if (!dataAttuale.after(dataScadenza)) {
*///##############################################################################
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      new distilla();
/*//##############################################################################
    }
    //--------------------------------------------------------------------------
    else {
      try {
        bibiW.write(" ...oops!");
        bibiW.flush();
        bibiW.close();
      }
      catch (IOException err) {
        System.err.println(err);
      }
    }*/
  }
}