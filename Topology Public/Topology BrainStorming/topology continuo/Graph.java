package topology;
import java.util.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

/**
 * <p>Title: Topology</p>
 * <p>Description: Disegna un grafo della rete in formato SVG</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Exeleration</p>
 * @author Faustino Palma
 * @version 1.0
 */

public class Graph {
  ArrayList Nodes = new ArrayList();
  double minCentrality=Double.MAX_VALUE;
  double maxCentrality=0;
  double K_attractive;
  double R_repulsive;
  double scale_R=1;
  class node {
    int id;
    String name;
    int position;
    double angle;
    double centrality;
    ArrayList Vectors = new ArrayList();
    public class vector {
      int nextHop;
      double distance;
    }
    vector vector(int destination) {return (vector) Vectors.get(destination);}
    void addVector(int nextHop, double distance) {
      vector vector = new vector();
      vector.nextHop = nextHop;
      vector.distance = distance;
      Vectors.add(vector);
    }
  }//end class node
  node node(int indice) {return (node) Nodes.get(indice);}
  void addNode(int id, String name) {
    node node = new node();
    node.id = id;
    node.name= name;
    Nodes.add(node);
  }
  ArrayList Links = new ArrayList();
  public class link {
    int source;
    int target;
    double distance;
  }//end class Links
  void addLink(int source, int target, double distance) {
    link link = new link();
    link.source=source;
    link.target=target;
    link.distance=distance;
    Links.add(link);
  }
  public link link(int indice) {return (link) Links.get(indice);}



  //Costruttore del Graph
  public Graph(String nomeFileTopologiaXML) {
    Document topologiaXML=null;
    try {
      DocumentBuilderFactory fabbricaCostruttoriDocumenti = DocumentBuilderFactory.newInstance();
      DocumentBuilder costruttoreDocumenti = fabbricaCostruttoriDocumenti.newDocumentBuilder();
      topologiaXML = costruttoreDocumenti.parse(nomeFileTopologiaXML);
    } catch (Exception err) {
      interfaccia.messaggi.append("Errore nell'apertura del file XML topologia");
    }
    int numeroRouters = topologiaXML.getElementsByTagName("Nodes").item(0).getChildNodes().getLength();
    NodeList listaRouters = topologiaXML.getElementsByTagName("Nodes").item(0).getChildNodes();
    interfaccia.messaggi.append("\nNodes:\n");
    for (int source=0; source<numeroRouters; source++) {
      if (source%100==0&&source>0)
        interfaccia.messaggi.append("\n");
      interfaccia.messaggi.append("!");
      Element router = (Element) listaRouters.item(source);
      addNode(source, router.getAttribute("name"));
      router.setAttribute("internalID", String.valueOf(source));
      for (int target=0; target<numeroRouters; target++) {
        if (source==target) {
          node(source).addVector(source, 0);
        } else {
          node(source).addVector(target, Double.MAX_VALUE/2);
        }
      }
    }//end for (int i=0; i<numeroRouters; i++)

    int numeroLinks = topologiaXML.getElementsByTagName("Links").item(0).getChildNodes().getLength();
    NodeList listaLinks = topologiaXML.getElementsByTagName("Links").item(0).getChildNodes();
    interfaccia.messaggi.append("\nLinks:\n");
    for (int i=0; i<numeroLinks; i++) {
      if (i%100==0&&i>0)
        interfaccia.messaggi.append("\n");
      try {
        interfaccia.messaggi.append("!");
        Element link = (Element) listaLinks.item(i);
        Element sourceElement = topologiaXML.getElementById(link.getAttribute("source"));
        Element targetElement = topologiaXML.getElementById(link.getAttribute("target"));
        double distance = (new Long(link.getAttribute("distance"))).longValue();
        int source = (new Integer(sourceElement.getAttribute("internalID"))).intValue();
        int target = (new Integer(targetElement.getAttribute("internalID"))).intValue();
        addLink(source, target, distance);
        node(source).vector(target).nextHop=target;
        node(source).vector(target).distance=distance;
        node(target).vector(source).nextHop=source;
        node(target).vector(source).distance=distance;
      } catch (NullPointerException err) {}
    }//end for (int i=0; i<numeroLinks; i++)
  }
}//end class Graph