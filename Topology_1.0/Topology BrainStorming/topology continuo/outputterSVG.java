package topology;
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

public class outputterSVG {
  String SVGfileName;
  Graph graph;
  public outputterSVG(Graph graph, String SVGfileName) {
    this.SVGfileName=SVGfileName;
    this.graph=graph;
  }

  public void drawGrezzo() {
    Document SVGdoc=null;
    try {
      DocumentBuilderFactory fabbrica = DocumentBuilderFactory.newInstance();
      DocumentBuilder costruttore = fabbrica.newDocumentBuilder();
      SVGdoc = costruttore.newDocument();
    } catch (ParserConfigurationException err) {}
    //Inizio xml per Svg
    //==========================================================================
    double scaleX=50;
    double scaleY=0.03;
    int raggio=10;
    Element radice = SVGdoc.createElement("svg");
    long width = graph.Nodes.size()*((long)scaleX);
    if (width<1000) width=1000;
    radice.setAttribute("width",""+width);
    radice.setAttribute("height","800");
    SVGdoc.appendChild(radice);
    double minC=graph.minCentrality;
    double maxC=graph.maxCentrality;
    for (int i=0; i<graph.Links.size(); i++) {
      int s=graph.link(i).source;
      int t=graph.link(i).target;
      int ps=graph.node(s).position;
      int pt=graph.node(t).position;
      double cs=graph.node(s).centrality;
      double ct=graph.node(t).centrality;
      Element line = SVGdoc.createElement("line");
      line.setAttribute("id", "line"+i);
      line.setAttribute("y1", ""+(scaleY*(cs-minC)+2*raggio));
      line.setAttribute("x1", ""+(scaleX*ps+2*raggio));
      line.setAttribute("y2", ""+(scaleY*(ct-minC)+2*raggio));
      line.setAttribute("x2", ""+(scaleX*pt+2*raggio));
      line.setAttribute("style", "stroke:blue;");
      radice.appendChild(line);
    }

    for(int i=0;i<graph.Nodes.size();i++){
      Element circle=SVGdoc.createElement("circle");
      double c=graph.node(i).centrality;
      int p=graph.node(i).position;
      circle.setAttribute("id", ""+graph.node(i).id);
      circle.setAttribute("cy", ""+(scaleY*(c-minC)+2*raggio));
      circle.setAttribute("cx", ""+(scaleX*p+2*raggio));
      circle.setAttribute("r", ""+raggio);
      radice.appendChild(circle);
    }
    //==========================================================================
    // Fine xml per Svg

    try {
      TransformerFactory fabbricaTrasformer = TransformerFactory.newInstance();
      Transformer trasformer = fabbricaTrasformer.newTransformer();
      DOMSource source = new DOMSource(SVGdoc);
      StreamResult result = new StreamResult(new FileWriter(SVGfileName));
      trasformer.transform(source, result);
    } catch (TransformerConfigurationException err) {}
      catch (IOException err) {}
      catch (TransformerException err) {}
    intesta();
  }

  void intesta() {
    StringBuffer testo = new StringBuffer();
    try {
      BufferedReader testo_file = new BufferedReader(new FileReader(SVGfileName));
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
      interfaccia.messaggi.append("\n" +"Classe apriSepara, Metodo apriSepara: Errore di IO");
    }
  String intestazione=
      "<?xml version=\"1.0\" standalone=\"no\"?>"+
      "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\""+"\n"+
      "\"http://www.w3.org/TR/2001/REC-SVG-20020904/DTD/svg10.dtd\">";
    int posizioneDTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length();
    String SVGcompleto = testo.substring(posizioneDTD).toString();
    testo.delete(0,posizioneDTD);
    SVGcompleto = testo.insert(0, intestazione).toString();
    try {
      BufferedWriter scriviFileXML_DTD = new BufferedWriter(new FileWriter(SVGfileName) );
      scriviFileXML_DTD.write(SVGcompleto);
      scriviFileXML_DTD.close();
    } catch (IOException err) {}
  }
}