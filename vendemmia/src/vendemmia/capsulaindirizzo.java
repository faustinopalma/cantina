package vendemmia;
import java.util.*;

/**
 * <p>Title: Aladino</p>
 * <p>Description: Apre una serie di sessioni telnet verso una lista di indirizzi ed esegue un insieme prestailito di comandi</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Lutech</p>
 * @author Faustino Palma
 * @version 1.0
 */

public class capsulaIndirizzo {
  public String indirizzo;
  public int numeroTentativi;
  boolean inGioco;
  ArrayList argomenti = new ArrayList();
  public String argomento(int i) {
    return (String) argomenti.get(i);
  }
  public capsulaIndirizzo(String indirizzo, int numeroTentativi, boolean inGioco) {
    this.indirizzo = indirizzo;
    while (indirizzo.indexOf(" ")!=-1) {
      indirizzo = indirizzo.replaceAll(" ", "");
    }
    this.numeroTentativi = numeroTentativi;
    this.inGioco = true;
  }
}