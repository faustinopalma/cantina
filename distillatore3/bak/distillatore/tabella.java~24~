package distillatore;
import java.util.*;
import java.io.*;
import java.awt.*;

/**
 * <p>Title: Distillatore</p>
 * <p>Description: Distilla le informazioni essenziali dall'output dei comandi show</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Lutech SPA</p>
 * @author Faustino Palma
 * @version 1.0
 */

public class tabella {
  TextArea messaggi = new TextArea();
  ArrayList intestazione = new ArrayList();
  int numRuotatori = 0;
  int giaScritti = 0;
  int numColonne;
  ArrayList matrice = new ArrayList();

  tabella(TextArea messaggi) {

    //*******************************************************************************
    // per ogni campo inserire una riga come le seguenti
    intestazione.add("IndirizzoDiscovery");
    intestazione.add("IndirizzoLoopBack");
    intestazione.add("Riferimento");
    intestazione.add("EnablePassword");
    intestazione.add("User");
    intestazione.add("UserPassword");
    intestazione.add("ConfReg");
    intestazione.add("Tacacs");
    intestazione.add("SNMPcommunity");


    //intestazione.add("NomeFile");


    //intestazione.add("BannerSI/NO");
    //intestazione.add("TD");
    //intestazione.add("NumeroISDN");
    //intestazione.add("DLCI");
    intestazione.add("VersioneIOS");
    intestazione.add("TipoIOS");
    intestazione.add("FileIOS");
    intestazione.add("RAM e Shared");
    intestazione.add("Flash");
    intestazione.add("SerialNumber");
    //intestazione.add("HostName");
    //intestazione.add("ip_p2p");
    //intestazione.add("ip_dial");

    //intestazione.add("bandwidthBassa");
    intestazione.add("ModelloRouter");
    intestazione.add("TipoRouter");
    intestazione.add("TipoConfDLSW");
    intestazione.add("ISDNremoti");
    intestazione.add("TipiStatiche");
    intestazione.add("TipiDistribuiteList");
    intestazione.add("configurazioneRedistribute");
    intestazione.add("configurazioneNTP");

    intestazione.add("numInterfacceSerialiNonWAN");

    //intestazione.add("Banner");
    //intestazione.add("ParolaEnd");

    numColonne = intestazione.size();
    for (int i = 0; i < numColonne; i++) {
      matrice.add(new ArrayList()); // aggiunge le colonne
    }
  }

  public void addRuotatore() {
    numRuotatori++;
    for (int i = 0; i < numColonne; i++) {
      ((ArrayList) matrice.get(i)).add("");
    }
  }

  public void inserire(String nomeColonna, String contenuto) {
    int colonna = intestazione.indexOf(nomeColonna);
    ((ArrayList) matrice.get(colonna)).remove(numRuotatori - 1);
    ((ArrayList) matrice.get(colonna)).add(contenuto);
  }

  public void scriviFile(String nomeFile) {
    try {
      BufferedWriter uscita = new BufferedWriter(new FileWriter(nomeFile));

      StringBuffer rigaIntestazione = new StringBuffer();
      for (int i = 0; i < numColonne; i++) {
        rigaIntestazione.append(intestazione.get(i));
        rigaIntestazione.append("\t");
      }
      uscita.write(rigaIntestazione.toString());
      uscita.newLine();

      while (giaScritti < numRuotatori) {
        StringBuffer riga = new StringBuffer();
        for (int i = 0; i < numColonne; i++) {
          riga.append(((ArrayList) matrice.get(i)).get(giaScritti));
          riga.append("\t");
        }
        uscita.write(riga.toString());
        uscita.newLine();
        giaScritti++;
      }
      uscita.close();
    } catch (IOException e) {messaggi.append("\n" +"errore in scriviFile");}
  }

  public void scriviFileHTML(String nomeFile) {
    try {
      BufferedWriter uscita = new BufferedWriter(new FileWriter(nomeFile));
      uscita.write("<html>");
      uscita.newLine();
      uscita.write("<head><TITLE>Network Analysis (by Faustino Palma CCIE#8959)</TITLE></head>");
      uscita.newLine();
      uscita.write("<body>");
      uscita.write("<table border=\"1\">");
      StringBuffer rigaIntestazione = new StringBuffer();
      rigaIntestazione.append("<tr>");
      for (int i = 0; i < numColonne; i++) {
        rigaIntestazione.append("<td>"+intestazione.get(i)+"</td>");
      }
      rigaIntestazione.append("</tr>");
      uscita.write(rigaIntestazione.toString());
      uscita.newLine();
      giaScritti=0;//xxxxxxxxxxxxxxxxx NON COSI' SCALABILE xxxxxxxxxxxxxxxxxxxxx
      while (giaScritti < numRuotatori) {
        StringBuffer riga = new StringBuffer();
        riga.append("<tr>");
        for (int i = 0; i < numColonne; i++) {
          riga.append("<td>"+((ArrayList) matrice.get(i)).get(giaScritti)+"</td>");
        }
        riga.append("</tr>");
        uscita.write(riga.toString());
        uscita.newLine();
        giaScritti++;
      }
      uscita.write("</table>");
      uscita.write("</html>\n</body>");
      uscita.close();
    } catch (IOException e) {messaggi.append("\n" +"errore in scriviFile");}
  }

} // fine tabella