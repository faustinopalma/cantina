package vendemmia;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import java.io.*;

/**
 * <p>Title: vendemmia</p>
 * <p>Description: raccoglie file di testo contenenti l'output dei comandi sh eseguiti sui router cisco</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Lutech SPA</p>
 * @author Faustino Palma
 * @version 1.0
 */

public class frameVendemmiatore extends JFrame {
  JPanel contentPane;
  TextArea messaggi = new TextArea();
  BorderLayout borderLayout1 = new BorderLayout();
  vendemmiatore nuovo_vendemmiatore;
  //variabili da passare al vendemmiatore
  lotteria lotteria_mia;
  configurazione conf;
  Checkbox checkbox_visualizza = new Checkbox();
  Panel panel1 = new Panel();
  TextField textField_comando = new TextField();
  Button button_invio = new Button();
  Button button_bobina = new Button();
  public int contatore;
  GridLayout gridLayout1 = new GridLayout();
  JButton jButton1 = new JButton();

  //Construct the frame
  public frameVendemmiatore(lotteria lotteria_mia, configurazione conf, int contatore) {
    this.contatore=contatore;
    this.lotteria_mia = lotteria_mia;
    this.conf = conf;
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
    contentPane.setLayout(borderLayout1);
    this.setSize(new Dimension(460, 430));
    this.setTitle("vendemmiatore_"+contatore);
    checkbox_visualizza.setLabel("visualizza");
    checkbox_visualizza.setState(false);
    checkbox_visualizza.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        checkbox_visualizza_mousePressed(e);
      }
    });
    panel1.setLayout(gridLayout1);
    button_invio.setLabel("invio");
    button_invio.setVisible(false);
    button_invio.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        button_invio_mousePressed(e);
      }
    });
    textField_comando.setColumns(30);
    textField_comando.setVisible(false);
    button_bobina.setLabel("file bobina");
    button_bobina.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_bobina_actionPerformed(e);
      }
    });
    jButton1.setText("bobina");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    messaggi.setBackground(SystemColor.text);
    messaggi.setEditable(false);
    panel1.add(checkbox_visualizza, null);
    panel1.add(jButton1, null);
    panel1.add(button_bobina, null);
    panel1.add(textField_comando, null);
    panel1.add(button_invio, null);
    contentPane.add(messaggi, BorderLayout.CENTER);
    contentPane.add(panel1, BorderLayout.SOUTH);
    enter();
  }

  void enter() {
    token nuovo_token = new token(conf, messaggi, checkbox_visualizza, new tokenDispencer(conf));
    nuovo_vendemmiatore = new vendemmiatore(nuovo_token, lotteria_mia, this);
  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      //System.exit(0);
      nuovo_vendemmiatore.interrotto = true;
    }
  }

  void checkbox_visualizza_mousePressed(MouseEvent e) {

  }

  void button_invio_mousePressed(MouseEvent e) {
    try {
      nuovo_vendemmiatore.tok.reg.scrivi(textField_comando.getText()+"\n");
    } catch (IOException err) {}
    textField_comando.setText("");
  }



  void button_bobina_actionPerformed(ActionEvent e) {
      printer printer = new printer();
    }
    public class printer  implements Runnable{
      public printer() {
        Thread thread = new Thread(this);
        thread.start();
      }
      public void run() {
        try {
          BufferedWriter escribe = new BufferedWriter(new FileWriter(conf.directoryBase+"\\bobina_"+contatore+".txt"));
          escribe.write(nuovo_vendemmiatore.tok.reg.bobina.toString());
          escribe.flush();
          escribe.close();
        } catch (IOException err) {
          System.err.println(err);
        }
      }
    }

  void jButton1_actionPerformed(ActionEvent e) {
    messaggi.append("\n---------------------------------------------------------------------------------------------\n");
    messaggi.append(nuovo_vendemmiatore.tok.reg.bobina.toString());
    messaggi.append("\n---------------------------------------------------------------------------------------------\n");
  }



}