package topology;
import java.util.*;


/**
 * <p>Title: Topology</p>
 * <p>Description: Disegna un grafo della rete in formato SVG</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Exeleration</p>
 * @author Faustino Palma
 * @version 1.0
 */

public class engine implements Runnable {
  Thread thread;
  String nomeFileTopologiaXML;
  Random random = new Random(System.currentTimeMillis());

  public engine(String nomeFileTopologiaXML) {
    this.nomeFileTopologiaXML = nomeFileTopologiaXML;
    thread = new Thread(this);
    thread.start();
  }
  public void run() {
    Graph graph = new Graph(nomeFileTopologiaXML);
    boolean updateTaken=true;
    interfaccia.messaggi.append("\n");
    int stelline=1;

    while (updateTaken) {
      interfaccia.messaggi.append("*");
      if (stelline%10==0)
        interfaccia.messaggi.append("\n");
      updateTaken=false;
      for (int i=0; i<graph.Links.size(); i++) {
        for (int j=0;
             j<graph.node(graph.link(i).source).Vectors.size()&&
             j<graph.node(graph.link(i).target).Vectors.size();
             j++)
        {
          if (graph.node(graph.link(i).source).vector(j).distance + graph.link(i).distance<
              graph.node(graph.link(i).target).vector(j).distance )
          {
            graph.node(graph.link(i).target).vector(j).distance=
              graph.node(graph.link(i).source).vector(j).distance + graph.link(i).distance;
            graph.node(graph.link(i).target).vector(j).nextHop=graph.link(i).source;
            updateTaken=true;
          }//end if --> update da source a target
          if (graph.node(graph.link(i).target).vector(j).distance + graph.link(i).distance<
              graph.node(graph.link(i).source).vector(j).distance )
          {
            graph.node(graph.link(i).source).vector(j).distance=
              graph.node(graph.link(i).target).vector(j).distance + graph.link(i).distance;
            graph.node(graph.link(i).source).vector(j).nextHop=graph.link(i).target;
            updateTaken=true;
          }//end if <-- update da target a source
        }//end for
      }//end for (int i=0; i<graph.Links.size(); i++)
    }//end while (updateTaken)
    interfaccia.messaggi.append("\ntabelle completate");

    for (int i=0; i<graph.Nodes.size(); i++) {
      double totale=0;
      int denominatore=0;
      for (int j=0; j<graph.node(i).Vectors.size(); j++) {
        if (graph.node(i).vector(j).distance<Double.MAX_VALUE/2) {
          totale+=graph.node(i).vector(j).distance;
          denominatore++;
        }
      }
      double media = totale/denominatore;
      graph.node(i).centrality=media;
      if (media<graph.minCentrality) graph.minCentrality=media;
      if (media>graph.maxCentrality) graph.maxCentrality=media;
    }
    initialPosition(graph);
    minimizeEnergy(graph);
    outputterSVG out = new outputterSVG(graph, nomeFileTopologiaXML.substring(0, nomeFileTopologiaXML.lastIndexOf("\\"))+"\\grezzo.svg");
    out.drawGrezzo();
  }//end public void run()

  long energy(Graph graph) {
    long energy=0;
    for (int i=0; i<graph.Links.size(); i++) {
      int s=graph.link(i).source;
      int t=graph.link(i).target;
      int ps=graph.node(s).position;
      int pt=graph.node(t).position;
      energy+=Math.abs(ps-pt);
    }
    return energy;
  }//end long energy


  void initialPosition(Graph graph) {
    ArrayList posizioni = new ArrayList(graph.Nodes.size());
    for (int i=0; i<graph.Nodes.size(); i++) {
      Integer pos = new Integer(i);
      posizioni.add(pos);
    }
    for (int i=0; i<graph.Nodes.size(); i++) {
      int j=random.nextInt(posizioni.size());
      graph.node(i).position=((Integer)posizioni.get(j)).intValue();
      posizioni.remove(j);
    }
  }


  void temperatureEffect(Graph graph, double temperatureEnergy, int maxIterations) {
    long energyOld =energy(graph);
    long energyNew =Long.MAX_VALUE;
    ArrayList posizioniOld = new ArrayList(graph.Nodes.size());
    for (int i=0; i<graph.Nodes.size(); i++) {
      posizioniOld.add(new Integer(graph.node(i).position));
    }
    int iterations=0;
    while (energyNew-energyOld>temperatureEnergy&&iterations<maxIterations) {
      ArrayList urna = new ArrayList(graph.Nodes.size());
      for (int i=0; i<graph.Nodes.size(); i++) {
        Integer pos = new Integer(i);
        urna.add(pos);
      }
      for (int i=0; i<graph.Nodes.size(); i++) {
        int j=random.nextInt(urna.size());
        graph.node(i).position=((Integer)urna.get(j)).intValue();
        urna.remove(j);
      }
      energyNew=energy(graph);
      iterations++;
    }
    if (iterations>=maxIterations) {
      for (int i=0; i<graph.Nodes.size(); i++) {
        graph.node(i).position=((Integer)posizioniOld.get(i)).intValue();
      }
    }
  }//end temperatureEffect



  boolean swapEffect(Graph graph) {
    ArrayList urna = new ArrayList(graph.Nodes.size());
    ArrayList posizioni = new ArrayList(graph.Nodes.size());
    for (int i=0; i<graph.Nodes.size(); i++) {
      Integer pos = new Integer(i);
      urna.add(pos);
    }
    for (int i=0; i<graph.Nodes.size(); i++) {
      int j=random.nextInt(urna.size());
      posizioni.add((Integer)urna.get(j));
      urna.remove(j);
    }
    boolean scambio=false;
    for (int i=0; i<posizioni.size(); i++) {
      for (int j=0; j<i; j++) {
        int indice_i = ((Integer)posizioni.get(i)).intValue();
        int indice_j = ((Integer)posizioni.get(j)).intValue();
        long energia_prima = energy(graph);
        int posizione_scambio=graph.node(indice_i).position;
        graph.node(indice_i).position=graph.node(indice_j).position;
        graph.node(indice_j).position=posizione_scambio;
        long energia_dopo = energy(graph);
        if (energia_dopo<energia_prima) {
          scambio=true;
        } else {
          graph.node(indice_j).position=graph.node(indice_i).position;
          graph.node(indice_i).position=posizione_scambio;
        }
      }
    }
    return scambio;
  }//end boolean swapEffect(Graph graph)

  void minimizeEnergy(Graph graph) {
    boolean scambio=true;
    interfaccia.messaggi.append("\n");
    int freccette=1;
    while (scambio) {
      interfaccia.messaggi.append(">");
      if (freccette%10==0)
        interfaccia.messaggi.append("\n");
      scambio=swapEffect(graph);
      freccette++;
    }
    interfaccia.messaggi.append("\nminimo trovato");
  }//end void minimizeEnergy(Graph graph)
}