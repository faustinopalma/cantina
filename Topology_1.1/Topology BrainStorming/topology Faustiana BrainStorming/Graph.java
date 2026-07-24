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
  Random random = new Random(System.currentTimeMillis()/2);
  ArrayList Nodes = new ArrayList();
  double minCentrality=Double.MAX_VALUE;
  double maxCentrality=0;
  double scale_R=1;
  class node {
    int id;
    String name;
    private int position;
    int indice;
    void setPosition(int p) {
      position=p%grid.size();
      if (p<0) p+=grid.size();
      setGrid(id, p);
    }
    int getPosition() {
      return position;
    }
    double centrality;
    long force;
    boolean initialPositioned=false;
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
    ArrayList Neighbors = new ArrayList();
    void addNeighbor(int id) {
      Integer neighbor = new Integer(id);
      Neighbors.add(neighbor);
    }
    int neighbor(int indice) {
      return ((Integer)Neighbors.get(indice)).intValue();
    }
  }//end class node
  node node(int indice) {return (node) Nodes.get(indice);}
  void addNode(int id, String name) {
    node node = new node();
    node.id = id;
    node.name= name;
    node.indice=Nodes.size();
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

  void meetNeighbors() {
    for (int l=0; l<Links.size(); l++) {
      int s=link(l).source;
      int t=link(l).target;
      node(s).addNeighbor(t);
      node(t).addNeighbor(s);
    }
  }

  public int distance(int p1, int p2) {
    int dist_direct = p1-p2;
    int dist_inverse = p1+grid.size()-p2;
    if (Math.abs(dist_direct)>Math.abs(dist_inverse)) {
      return dist_inverse;
    } else {
      return dist_direct;
    }
  }

  public ArrayList grid;
  int nodeIndexByPosition(int pos) {
    pos=pos%grid.size();
    if (pos<0) pos+=grid.size();
    return ((Integer)grid.get(pos)).intValue();
  }
  void setGrid(int id, int pos) {
    grid.set(pos, new Integer(id));
  }

  int byMe(int myPosition, int offset) {
    boolean trovato=false;
    int indice=0;
    int soulMate=myPosition;
    soulMate=nodeIndexByPosition(myPosition+offset);
    return soulMate;
  }

  ArrayList campions = new ArrayList();
  void competition() {
    campions.clear();
    campions.add(node(0));
    for (int i=1; i<Nodes.size(); i++) {
      boolean classificato=false;
      int indice_inferiore=0;
      int indice_superiore=campions.size();
      int indice_medio=(indice_superiore+indice_inferiore)/2;
      while (!classificato) {
        if (indice_superiore-indice_inferiore<=1) {
          if ( Math.abs(node(i).force)>Math.abs(campion(indice_inferiore).force) ) {
            campions.add(indice_inferiore, node(i));
            classificato=true;
          } else {
            campions.add(indice_superiore, node(i));
            classificato=true;
          }
        } else {
          indice_medio=(indice_superiore+indice_inferiore)/2;
          if (Math.abs(node(i).force)>Math.abs(campion(indice_medio).force) ) {
            indice_superiore=indice_medio;
          } else {
            indice_inferiore=indice_medio;
          }
        }
      }//end while (!classificato)
    }//end (int i=0; i<Nodes.size(); i++)
  }// end void competition()
  node campion(int i) {
    return (node) campions.get(i);
  }

//##############################################################################
  ArrayList bestDeltaForce = new ArrayList();
  void compareDeltaForce() {
    bestDeltaForce.clear();
    bestDeltaForce.add(new Integer(0));
    for (int i=1; i<Nodes.size(); i++) {
      boolean classificato=false;
      int indice_inferiore=0;
      int indice_superiore=bestDeltaForce.size();
      int indice_medio=(indice_superiore+indice_inferiore)/2;
      while (!classificato) {
        if (indice_superiore-indice_inferiore<=1) {
          if (deltaForce(i)>deltaForce(bestDeltaForce(indice_inferiore))) {
            bestDeltaForce.add(indice_inferiore, new Integer(i));
            classificato=true;
          } else {
            bestDeltaForce.add(indice_superiore, new Integer(i));
            classificato=true;
          }
        } else {
          indice_medio=(indice_superiore+indice_inferiore)/2;
          if (deltaForce(i)>deltaForce(bestDeltaForce(indice_medio))) {
            indice_superiore= indice_medio;
          } else {
            indice_inferiore= indice_medio;
          }
        }
      }
    }
  }

  long deltaForce(int couple) {
    node nodeSX = node(nodeIndexByPosition(couple));
    node nodeDX = node(nodeIndexByPosition(couple+1));
    return nodeSX.force-nodeDX.force;
  }
  int bestDeltaForce(int grade) {
    return ((Integer)bestDeltaForce.get(grade)).intValue();
  }

//##############################################################################

  void swap(node node_a, node node_b) {
    int posizione_intermedia=node_a.position;

    node_a.setPosition(node_b.position);
    node_b.setPosition(posizione_intermedia);
  }

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

  void initialPositionTrial() {
    ArrayList posizioni = new ArrayList(Nodes.size());
    grid = new ArrayList(Nodes.size());
    for (int i=0; i<Nodes.size(); i++) {
      grid.add(i, null);
    }
    for (int i=0; i<Nodes.size(); i++) {
      Integer pos = new Integer(i);
      posizioni.add(pos);
    }
    for (int i=0; i<Nodes.size(); i++) {
      int j=random.nextInt(posizioni.size());
      node(i).setPosition( ((Integer)posizioni.get(j)).intValue() );
      posizioni.remove(j);
    }
  }
  //############################################################################

  ArrayList mostCentral = new ArrayList();
  void classifyCentrality() {
    mostCentral.clear();
    mostCentral.add(node(0));
    for (int i=1; i<Nodes.size(); i++) {
      boolean classificato=false;
      int indice_inferiore=0;
      int indice_superiore=mostCentral.size();
      int indice_medio=(indice_superiore+indice_inferiore)/2;
      while (!classificato) {
        if (indice_superiore-indice_inferiore<=1) {
          if ( node(i).centrality<mostCentral(indice_inferiore).centrality ) {
            mostCentral.add(indice_inferiore, node(i));
            classificato=true;
          } else {
            mostCentral.add(indice_superiore, node(i));
            classificato=true;
          }
        } else {
          indice_medio=(indice_superiore+indice_inferiore)/2;
          if (node(i).centrality<mostCentral(indice_medio).centrality ) {
            indice_superiore=indice_medio;
          } else {
            indice_inferiore=indice_medio;
          }
        }
      }//end while (!classificato)
    }//end (int i=0; i<Nodes.size(); i++)
  }
  node mostCentral(int i) {
    return (node) mostCentral.get(i);
  }

  void initialPosition(double deltaCentrality) {
    grid = new ArrayList(Nodes.size());
    for (int i=0; i<Nodes.size(); i++) {
      grid.add(i, null);
    }
    classifyCentrality();
    ArrayList daPosizionareArray = new ArrayList(mostCentral);
    int posizione=0;
    for (int i=0; i<daPosizionareArray.size(); i++) {
      node daPosizionare = (node) daPosizionareArray.get(i);
      if (!daPosizionare.initialPositioned) {
        daPosizionare.setPosition(posizione);
        daPosizionare.initialPositioned=true;
        posizione++;
      }
      for (int j=0; j<daPosizionare.Neighbors.size(); j++) {
        node neighborJ = node(daPosizionare.neighbor(j));
        if (!neighborJ.initialPositioned &&
            neighborJ.centrality-daPosizionare.centrality>deltaCentrality)
        {
          neighborJ.setPosition(posizione);
          neighborJ.initialPositioned=true;
          posizione++;
        }
      }//end for (int j=0; j<daPosizionare.Neighbors.size(); j++)
    }//end for (int i=0; i<daPosizionareArray.size(); i++)
  }
}//end class Graph