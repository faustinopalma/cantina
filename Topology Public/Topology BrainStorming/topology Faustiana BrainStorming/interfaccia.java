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
  Button button3 = new Button();
  Button button4 = new Button();
  Button button5 = new Button();
  static Label displayEnergia = new Label();
  Button button6 = new Button();
  TextField scaleR = new TextField();
  Label labelKappaGaussiana = new Label();
  Button button7 = new Button();
  Button button8 = new Button();

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
    this.setSize(new Dimension(800, 600));
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
    button2.setLabel("passa al metodo swap");
    button2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        button2_mousePressed(e);
      }
    });
    button3.setLabel("impedisci aumenti di energia");
    button3.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        button3_mousePressed(e);
      }
    });
    button4.setLabel("permetti aumenti di energia");
    button4.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        button4_mousePressed(e);
      }
    });
    button5.setLabel("Stop");
    button5.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        button5_mousePressed(e);
      }
    });
    displayEnergia.setText("ENERGY");
    button6.setLabel("Genera SVG");
    button6.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        button6_mousePressed(e);
      }
    });
    scaleR.setText("1");
    button7.setLabel("su");
    button7.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        button7_mousePressed(e);
      }
    });
    button8.setLabel("giu");
    button8.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        button8_mousePressed(e);
      }
    });
    labelKappaGaussiana.setText("0");
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuHelp);
    contentPane.add(CampoFileTopologiaXML,        new XYConstraints(20, 40, 350, 20));
    contentPane.add(label1,    new XYConstraints(20, 20, 200, 20));
    contentPane.add(messaggi,     new XYConstraints(20, 130, 350, 100));
    contentPane.add(button2,   new XYConstraints(398, 335, 217, 37));
    contentPane.add(button4,  new XYConstraints(399, 277, 214, 33));
    contentPane.add(button3,  new XYConstraints(399, 225, 213, 35));
    contentPane.add(button1,  new XYConstraints(417, 33, 192, 144));
    contentPane.add(button5,  new XYConstraints(402, 403, 217, 40));
    contentPane.add(displayEnergia,   new XYConstraints(201, 257, 157, 35));
    contentPane.add(button6,  new XYConstraints(401, 464, 225, 68));
    contentPane.add(scaleR,  new XYConstraints(246, 473, 120, 44));
    contentPane.add(labelKappaGaussiana,  new XYConstraints(38, 360, 97, 48));
    contentPane.add(button7,  new XYConstraints(144, 337, 93, 39));
    contentPane.add(button8,  new XYConstraints(146, 388, 93, 38));
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
    engine.minimizzaForze=false;
    engine.disattivaMetodoSwap=false;
    button2.setEnabled(false);
  }

  void button3_mousePressed(MouseEvent e) {
    engine.noAumenti=true;
  }

  void button4_mousePressed(MouseEvent e) {
    engine.noAumenti=false;
  }

  void button5_mousePressed(MouseEvent e) {
    engine.disattivaMetodoSwap=true;
  }

  void button6_mousePressed(MouseEvent e) {
    outputterSVG out = new outputterSVG(engine.graph, engine.nomeFileTopologiaXML.substring(0, engine.nomeFileTopologiaXML.lastIndexOf("\\"))+"\\grezzo.svg");
    out.drawPolar((new Double(scaleR.getText())).doubleValue() );
  }

  void button7_mousePressed(MouseEvent e) {
    engine.Kgaussian+=0.5;
    labelKappaGaussiana.setText(""+engine.Kgaussian);
    labelKappaGaussiana.repaint();
  }

  void button8_mousePressed(MouseEvent e) {
    engine.Kgaussian-=0.5;
    labelKappaGaussiana.setText(""+engine.Kgaussian);
    labelKappaGaussiana.repaint();
  }
}