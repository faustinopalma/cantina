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
  volatile boolean minimizzaForze=true;
  volatile boolean disattivaMetodoSwap=false;
  volatile boolean noAumenti=false;
  Graph graph;

  public engine(String nomeFileTopologiaXML) {
    this.nomeFileTopologiaXML = nomeFileTopologiaXML;
    thread = new Thread(this);
    thread.start();
  }
  public void run() {
    graph = new Graph(nomeFileTopologiaXML);
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
    graph.meetNeighbors();
    graph.initialPosition(2500);
    //graph.initialPositionTrial();
    interfaccia.messaggi.append("\nEnergyStart="+energy(graph));
    //minimizeEnergyForce(graph);
    //dontMakeWarMakeSex(graph);
    //minimizza_a_pennellate_simple(graph);
    minimizza_a_pennellate(graph);
    //swapEffect(graph);
  }//end public void run()
//##############################################################################


  long energy(Graph graph) {
    long energy=0;
    for (int i=0; i<graph.Links.size(); i++) {
      int s=graph.link(i).source;
      int t=graph.link(i).target;
      int ps=graph.node(s).getPosition();
      int pt=graph.node(t).getPosition();
      energy+=Math.pow(graph.distance(ps, pt), 2);
    }
    return energy;
  }//end long energy

  long force(int id, Graph graph) {
    long force=0;
    int p=graph.node(id).getPosition();
    for (int i=0; i<graph.node(id).Neighbors.size(); i++) {
      int r=graph.node(id).neighbor(i);
      force+=graph.distance(r, p);
    }
    return force;
  }

  void evaluateForce(Graph graph) {
    for (int i=0; i<graph.Nodes.size(); i++) {
      graph.node(i).force=force(i, graph);
    }
  }


  void temperatureEffect(Graph graph, double temperatureEnergy, int maxIterations) {
    long energyOld =energy(graph);
    long energyNew =Long.MAX_VALUE;
    ArrayList posizioniOld = new ArrayList(graph.Nodes.size());
    for (int i=0; i<graph.Nodes.size(); i++) {
      posizioniOld.add(new Integer(graph.node(i).getPosition()));
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
        graph.node(i).setPosition( ((Integer)urna.get(j)).intValue() );
        urna.remove(j);
      }
      energyNew=energy(graph);
      iterations++;
    }
    if (iterations>=maxIterations) {
      for (int i=0; i<graph.Nodes.size(); i++) {
        graph.node(i).setPosition( ((Integer)posizioniOld.get(i)).intValue() );
      }
    }
  }//end temperatureEffect



  void swapEffect(Graph graph) {
    while(!disattivaMetodoSwap) {
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
          int posizione_scambio=graph.node(indice_i).getPosition();
          graph.node(indice_i).setPosition(graph.node(indice_j).getPosition());
          graph.node(indice_j).setPosition(posizione_scambio);
          long energia_dopo = energy(graph);
          if (energia_dopo<energia_prima) {
            scambio=true;
            interfaccia.messaggi.append("\n"+energia_dopo);
            interfaccia.displayEnergia.setText(""+energia_dopo);
          } else {
            graph.node(indice_j).setPosition(graph.node(indice_i).getPosition());
            graph.node(indice_i).setPosition(posizione_scambio);
          }
        }
      }
    }
  }//end boolean swapEffect(Graph graph)

  void simpleMinimizeDeltaForce(Graph graph) {
    interfaccia.messaggi.append("\n");
    int freccette=1;
    while (minimizzaForze/*D AMODIFICARE*/) {
      String E = ""+energy(graph);
      interfaccia.messaggi.append("\n"+E);
      interfaccia.displayEnergia.setText(E);
      evaluateForce(graph);
      for (int i=0; i<graph.Nodes.size(); i+=2) {
        if(graph.deltaForce(i)>0) {
          graph.swap(graph.node(graph.nodeIndexByPosition(i))
                    ,graph.node(graph.nodeIndexByPosition(i+1)));
        }
      }
      evaluateForce(graph);
      for (int i=1; i<graph.Nodes.size(); i+=2) {
        if(graph.deltaForce(i)>0) {
          graph.swap(graph.node(graph.nodeIndexByPosition(i))
                    ,graph.node(graph.nodeIndexByPosition(i+1)));
        }
      }
    }
  }


  volatile double Kgaussian=0;
  void minimizza_a_pennellate(Graph graph) {
    interfaccia.messaggi.append("\n");
    long E= energy(graph);
    long newE;
    int startPoint = 0;
    while (minimizzaForze/*D AMODIFICARE*/) {
      String stringE = ""+E;
      interfaccia.messaggi.append("\n"+stringE);
      interfaccia.displayEnergia.setText(stringE);
      for (int i=startPoint; i<graph.Nodes.size()-1+startPoint; i++) {
        graph.swap(graph.node(graph.nodeIndexByPosition(i))
                  ,graph.node(graph.nodeIndexByPosition(i+1)));
        newE= energy(graph);
        if (newE-E>Kgaussian*random.nextGaussian()) {
          graph.swap(graph.node(graph.nodeIndexByPosition(i))
                    ,graph.node(graph.nodeIndexByPosition(i+1)));
          newE=E;
        }
        E=newE;
      }
      for (int i=startPoint; i>startPoint-graph.Nodes.size()+1; i--) {
        graph.swap(graph.node(graph.nodeIndexByPosition(i))
                  ,graph.node(graph.nodeIndexByPosition(i+1)));
        newE= energy(graph);
        if (newE-E>Kgaussian*random.nextGaussian()) {
          graph.swap(graph.node(graph.nodeIndexByPosition(i))
                    ,graph.node(graph.nodeIndexByPosition(i+1)));
          newE=E;
        }
        E=newE;
      }
    }
  }

  void minimizza_a_pennellate_simple(Graph graph) {
    interfaccia.messaggi.append("\n");
    long E= energy(graph);
    long newE;
    while (minimizzaForze/*D AMODIFICARE*/) {
      String stringE = ""+E;
      interfaccia.messaggi.append("\n"+stringE);
      interfaccia.displayEnergia.setText(stringE);
      for (int i=0; i<graph.Nodes.size()-1; i++) {
        graph.swap(graph.node(graph.nodeIndexByPosition(i))
                  ,graph.node(graph.nodeIndexByPosition(i+1)));
        newE= energy(graph);
        if (newE>E) {
          graph.swap(graph.node(graph.nodeIndexByPosition(i))
                    ,graph.node(graph.nodeIndexByPosition(i+1)));
          newE=E;
        }
        E=newE;
      }
      for (int i=graph.Nodes.size()-1; i>0; i--) {
        graph.swap(graph.node(graph.nodeIndexByPosition(i))
                  ,graph.node(graph.nodeIndexByPosition(i+1)));
        newE= energy(graph);
        if (newE>E) {
          graph.swap(graph.node(graph.nodeIndexByPosition(i))
                    ,graph.node(graph.nodeIndexByPosition(i+1)));
          newE=E;
        }
        E=newE;
      }
    }
  }

  void minimizeDeltaForceOld(Graph graph) {
    interfaccia.messaggi.append("\n");
    int freccette=1;
    while (minimizzaForze/*D AMODIFICARE*/) {
      String E = ""+energy(graph);
      interfaccia.messaggi.append("\n"+E);
      interfaccia.displayEnergia.setText(E);
      evaluateForce(graph);
      graph.compareDeltaForce();
      int bestDeltaForceCoupleIndex=0;
      boolean swap=false;
      while ( !swap && bestDeltaForceCoupleIndex<graph.bestDeltaForce.size() ) {
        long energyBefore=energy(graph);
        int bestLoverCouple=graph.bestDeltaForce(bestDeltaForceCoupleIndex);
        graph.swap(graph.node(graph.nodeIndexByPosition(bestLoverCouple))
                  ,graph.node(graph.nodeIndexByPosition(bestLoverCouple+1)));
        if (energy(graph)<energyBefore) {
          swap=true;
        } else {
          graph.swap(graph.node(graph.nodeIndexByPosition(bestLoverCouple))
                    ,graph.node(graph.nodeIndexByPosition(bestLoverCouple+1)));
          bestDeltaForceCoupleIndex++;
        }
      }//end while ( !swap && couple<graph.bestLovers.size() )
    }
  }

  void minimizeEnergyForce(Graph graph) {
    interfaccia.messaggi.append("\n");
    int freccette=1;
    while (minimizzaForze) {
      String E = ""+energy(graph);
      interfaccia.messaggi.append("\n"+E);
      interfaccia.displayEnergia.setText(E);
      evaluateForce(graph);
      graph.competition();
      int campione=0;
      boolean swap=false;
      while ( !swap && campione<graph.campions.size() ) {
        long energyBefore=energy(graph);
        int byCampione;
        if (Math.abs(graph.campion(campione).force)>0) {
          if (graph.campion(campione).force>0) {
            byCampione=graph.byMe(graph.campion(campione).getPosition(), +1);
            graph.swap(graph.campion(campione), graph.node(byCampione));
          } else {
            byCampione=graph.byMe(graph.campion(campione).getPosition(), -1);
            graph.swap(graph.campion(campione), graph.node(byCampione));
          }
          if (noAumenti) {
            if (energy(graph)<energyBefore) {
              swap=true;
            } else {
              graph.swap(graph.campion(campione), graph.node(byCampione));
              campione++;
            }
          } else {
            swap=true;
          }//end if else (noAumenti)
        } else {
          interfaccia.messaggi.append("\nNon si spostano più");
          swap=true;
        }
      }//end ( !swap && campione<graph.campions.size() )
      freccette++;
    }// fine ciclo massime forze
  }//end void minimizeEnergy(Graph graph)
}