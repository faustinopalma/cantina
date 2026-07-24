package topology;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;

/**
 * <p>Title: Topology</p>
 * <p>Description: Disegna un grafo della rete in formato SVG</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Exeleration</p>
 * @author Faustino Palma
 * @version 1.0
 */

public class interfaccia extends JFrame {
  JPanel contentPane;
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileExit = new JMenuItem();
  JMenu jMenuHelp = new JMenu();
  JMenuItem jMenuHelpAbout = new JMenuItem();
  XYLayout xYLayout1 = new XYLayout();
  TextField CampoFileTopologiaXML = new TextField();
  Label label1 = new Label();
  Button button1 = new Button();
  static TextArea messaggi = new TextArea();
  Button button2 = new Button();
  engine engine;

  //Construct the frame
  public interfaccia() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception  {
    //setIconImage(Toolkit.getDefaultToolkit().createImage(interfaccia.class.getResource("[Your Icon]")));
    contentPane = (JPanel) this.getContentPane();
    this.setSize(new Dimension(400, 300));
    this.setTitle("Topology");
    jMenuFile.setText("File");
    jMenuFileExit.setText("Exit");
    jMenuFileExit.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        jMenuFileExit_actionPerformed(e);
      }
    });
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        jMenuHelpAbout_actionPerformed(e);
      }
    });
    contentPane.setLayout(xYLayout1);
    label1.setText("File XML Topologia:");
    button1.setLabel("Start");
    button1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        button1_mousePressed(e);
      }
    });
    CampoFileTopologiaXML.setText("\\topologiaXML.xml");
    button2.setLabel("Stop minimize");
    button2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        button2_mousePressed(e);
      }
    });
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuHelp);
    contentPane.add(CampoFileTopologiaXML,        new XYConstraints(20, 40, 350, 20));
    contentPane.add(label1,    new XYConstraints(20, 20, 200, 20));
    contentPane.add(messaggi,     new XYConstraints(20, 130, 350, 100));
    contentPane.add(button1,           new XYConstraints(110, 80, 120, 30));
    contentPane.add(button2,          new XYConstraints(250, 80, 120, 30));
    this.setJMenuBar(jMenuBar1);
  }
  //File | Exit action performed
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    System.exit(0);
  }
  //Help | About action performed
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    interfaccia_AboutBox dlg = new interfaccia_AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.show();
  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }

  void button1_mousePressed(MouseEvent e) {
    button1.setEnabled(false);
    engine = new engine(CampoFileTopologiaXML.getText());
  }

  void button2_mousePressed(MouseEvent e) {
    engine.esciDaMinimize=true;
    button2.setEnabled(false);
  }
}