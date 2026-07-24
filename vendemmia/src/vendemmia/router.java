package vendemmia;
import java.util.*;

/**
 * <p>Title: vendemmia</p>
 * <p>Description: raccoglie file di testo contenenti l'output dei comandi sh eseguiti sui router cisco</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Lutech SPA</p>
 * @author Faustino Palma
 * @version 1.0
 */

public class router {
  public String id_name=null;
  public String id_address=null;
  public ArrayList neighbors = new ArrayList();
  public neighbor neighbor(int i) {return (neighbor) neighbors.get(i);}

  public ArrayList interfaces = new ArrayList();
  public ip_interface ip_interface(int i) {return (ip_interface) interfaces.get(i);}
}