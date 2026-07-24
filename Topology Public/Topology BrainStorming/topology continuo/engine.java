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
  Graph graph;
  volatile boolean esciDaMinimize=false;
  double deltaDiscendente=0.8;
  double gammaSmorzante=0.6;

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
      if (media<0.0001) {
        graph.node(i).centrality=1000000;
      } else {
        graph.node(i).centrality=media;
      }
      if (media<graph.minCentrality) graph.minCentrality=media;
      if (media>graph.maxCentrality) graph.maxCentrality=media;
    }
    initialPosition(graph);
    minimizeEnergy(graph, deltaDiscendente, gammaSmorzante);
    outputterSVG out = new outputterSVG(graph, nomeFileTopologiaXML.substring(0, nomeFileTopologiaXML.lastIndexOf("\\"))+"\\grezzo.svg");
    out.drawGrezzo();
  }//end public void run()

  void initialPosition(Graph graph) {
    ArrayList posizioni = new ArrayList(graph.Nodes.size());
    for (int i=0; i<graph.Nodes.size(); i++) {
      Integer pos = new Integer(i);
      posizioni.add(pos);
    }
    for (int i=0; i<graph.Nodes.size(); i++) {
      int j=random.nextInt(posizioni.size());
      graph.node(i).position=((Integer)posizioni.get(j)).intValue();
      graph.node(i).angle=2*Math.PI*graph.node(i).position/graph.Nodes.size();
      posizioni.remove(j);
    }
  }

  double energy(Graph graph) {
    double energy=0;
    for (int i=0; i<graph.Links.size(); i++) {
      int s=graph.link(i).source;
      int t=graph.link(i).target;
      energy+=-graph.K_attractive/distanzaQuadrata(s, t);
    }
    for (int i=0; i<graph.Nodes.size(); i++) {
      for (int j=0; j<i; j++) {
        energy+=Math.exp(graph.R_repulsive/distanzaQuadrata(i, j));
      }
    }
    return energy;
  }//end long energy

  double distanzaQuadrata(int i, int j) {
    double pi=graph.node(i).angle;
    double pj=graph.node(j).angle;
    double ri=graph.scale_R*graph.node(i).centrality;
    double rj=graph.scale_R*graph.node(j).centrality;
    double distanceSquare=Math.pow(ri*Math.sin(pi)-rj*Math.sin(pj), 2)
                         +Math.pow(ri*Math.cos(pi)-rj*Math.cos(pj), 2);
    return distanceSquare;
  }

  double derivata_distanza_inversa(int i, int j) {
    double pi=graph.node(i).angle;
    double pj=graph.node(j).angle;
    double ri=graph.scale_R*graph.node(i).centrality;
    double rj=graph.scale_R*graph.node(j).centrality;
    double derivata=
          -2*Math.sin(pi-pj)
          /( ri*rj*( ri/rj+rj/ri-2*Math.cos(pi-pj) ) );
    return derivata;
  }

  double[] energyGradient(Graph graph) {
    double[] gradient = new double[graph.Nodes.size()+1];
    gradient[graph.Nodes.size()]=0;
    for (int i=0; i<graph.Nodes.size(); i++) {
      gradient[i]=0;
      for (int l=0; l<graph.Links.size(); l++) {
        if (graph.link(l).source==i) {
          gradient[i]+=-graph.K_attractive*derivata_distanza_inversa(i, graph.link(l).target);
          gradient[graph.Nodes.size()]+=2*graph.K_attractive/(graph.scale_R*distanzaQuadrata(i, graph.link(l).target));
        }
        if (graph.link(l).target==i) {
          gradient[i]+=graph.K_attractive*derivata_distanza_inversa(i, graph.link(l).source);
          gradient[graph.Nodes.size()]+=2*graph.K_attractive/(graph.scale_R*distanzaQuadrata(i, graph.link(l).source));
        }
      }
      for (int j=0; j<i; j++) {
        gradient[i]+= graph.R_repulsive
                      *Math.exp(graph.R_repulsive/distanzaQuadrata(i, j))
                      *derivata_distanza_inversa(i, j);
        gradient[graph.Nodes.size()]+=
                      2*graph.R_repulsive
                      *Math.exp(graph.R_repulsive/distanzaQuadrata(i, j))
                      /(graph.scale_R*distanzaQuadrata(i, j));
      }
      for (int j=i+1; j<graph.Nodes.size(); j++) {
        gradient[i]+= graph.R_repulsive
                      *Math.exp(graph.R_repulsive/distanzaQuadrata(i, j))
                      *derivata_distanza_inversa(j, i);
        gradient[graph.Nodes.size()]+=
                      2*graph.R_repulsive
                      *Math.exp(graph.R_repulsive/distanzaQuadrata(i, j))
                      /(graph.scale_R*distanzaQuadrata(i, j));
      }
    }
    return gradient;
  }

  void angleRegolize(Graph graph) {
    for (int i=0; i<graph.Nodes.size(); i++) {
      graph.node(i).angle=graph.node(i).angle-Math.round(graph.node(i).angle/(2*Math.PI));
    }
  }

  void temperatureEffect(Graph graph, double temperatureEnergy, int maxIterations) {

  }//end temperatureEffect


  void minimizeEnergy(Graph graph, double delta, double gamma) {
    graph.K_attractive=10000000;
    graph.R_repulsive=100;
    interfaccia.messaggi.append("\n");
    int freccette=1;
    double[] lastGradient = new double[graph.Nodes.size()+1];
    while (!esciDaMinimize) {
      interfaccia.messaggi.append("\n>"+energy(graph));
      //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        double[] gradient = energyGradient(graph);
        for (int i=0; i<gradient.length-1; i++) {
          graph.node(i).angle-=delta*(gradient[i]-gamma*lastGradient[i]);
        }
        graph.scale_R-=delta*gradient[gradient.length-1];
        lastGradient=gradient;
      //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      freccette++;
    }
  }//end void minimizeEnergy(Graph graph)
}