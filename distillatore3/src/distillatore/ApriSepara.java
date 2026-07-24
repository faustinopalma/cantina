package distillatore;
import java.io.*;
import java.util.regex.*;
import java.awt.*;

/**
 * <p>Title: Distillatore</p>
 * <p>Description: Distilla le informazioni essenziali dall'output dei comandi show</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Lutech SPA</p>
 * @author Faustino Palma
 * @version 1.0
 */

public class ApriSepara {
  TextArea messaggi = new TextArea();
  StringBuffer testo = new StringBuffer();
  public ApriSepara (String testo_path, TextArea messaggi) {
    this.messaggi = messaggi;
    try {
      BufferedReader testo_file = new BufferedReader(new FileReader(testo_path));
      boolean uscita = false;
      while (!uscita) {
        String riga = testo_file.readLine();
        if (riga == null) {
          uscita = true;
        } else {
          testo.append(riga + "\n");
        }
      }
    testo_file.close();
    } catch (IOException e) {
      messaggi.append("\n" +"Classe apriSepara, Metodo apriSepara: Errore di IO");
    }
  }

  public String getTesto() {
    return testo.toString();
  }

  public String getIniziaFinisceCon(String inizia,String finisce) {
      Pattern PrimaRigaPattern = Pattern.compile(inizia);
      Matcher PrimaRiga = PrimaRigaPattern.matcher(testo.toString());
      if (PrimaRiga.find()) {
        Pattern UltimaRigaPattern = Pattern.compile(finisce);
        Matcher UltimaRiga = UltimaRigaPattern.matcher(testo.toString());
        if (UltimaRiga.find(PrimaRiga.end())) {
          return (String) testo.substring(PrimaRiga.start(),UltimaRiga.end());
        } else {
          messaggi.append("\n" +"Classe apriSepara, Metodo getIniziaFinisceCon: fine non trovato");
          return null;
        }
      } else {
        messaggi.append("\n" +"Classe apriSepara, Metodo getIniziaFinisceCon: inizio non trovata");
        return null;
      }
  }//fine separatore
}